package cn.qxl.Chat;

import cn.qxl.Bean.User;
import cn.qxl.Bean.chatBean;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import javax.jws.soap.SOAPBinding;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by qiu on 2018/10/13.
 */
@ServerEndpoint(value = "/websocket/{username}")
@Component
@EnableWebSocket
public class MyWebSocket {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //connect key为session的ID，value为此对象this
    private static final HashMap<String, Object> connect = new HashMap<String, Object>();
    //userMap key为session的ID，value为用户名
    private static final HashMap<String, String> userMap = new HashMap<String, String>();
    private static final HashMap<String, Session> sessionMap = new HashMap<String, Session>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //判断是否是第一次接收的消息
    private boolean isfirst = true;

    private String username;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Map<String, String> getOnline() {
        return userMap;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        this.session = session;
        this.username = username;
        connect.put(session.getId(), this);
        sessionMap.put(username,session);
        userMap.put(session.getId(), username);
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        sendInfo(format.format(new Date()) + " 有新连接加入！当前在线人数为" + getOnlineCount());
        sendInfo(JSON.toJSONString(userMap));
        for (MyWebSocket item : webSocketSet) {
            try {
                item.sendMessage(format.format(new Date()) + " 系统：欢迎 " + username + " 连接");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        String user = userMap.get(session.getId());
        connect.remove(session.getId());
        userMap.remove(session.getId());
        sessionMap.remove(user);
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("用户 " + user + " 断开了连接");
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
        for (MyWebSocket item : webSocketSet) {
            try {
                item.sendMessage(format.format(new Date()) + " 系统：用户 " + username + " 断开连接");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        if (isfirst) {
            this.username = userMap.get(session.getId());
            System.out.println("用户" + username + "上线,在线人数：" + connect.size());
            //userMap.put(session.getId(), username);
            isfirst = false;
        } else {
            String[] msg = message.split("@", 2);//以@为分隔符把字符串分为xxx和xxxxx两部分,msg[0]表示发送至的用户名，all则表示发给所有人
            if (msg[0].equals("all")) {
                chatBean bean = new chatBean();
                bean.setTime(new Date());
                bean.setToUser("all");
                bean.setUsername(username);
                bean.setMsg(msg[1]);
                sendToAll(JSON.toJSONString(bean));
            } else {
                sendToUser(msg[0], msg[1], session);
            }
        }
    }

    /**
     * 发生错误时调用
     *
     * @OnError
     */
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 给所有人发送消息
     *
     * @param msg     发送的消息
     */
    public void sendToAll(String msg) {
        String who = "";
        //群发消息
        for (String key : connect.keySet()) {
            MyWebSocket client = (MyWebSocket) connect.get(key);
            if (key.equalsIgnoreCase(userMap.get(key))) {
                who = "自己对大家说 : ";
            } else {
////                who = format.format(new Date()) + " " + userMap.get(session.getId()) + "对大家说 :";
            }
            synchronized (client) {
                try {
                    client.session.getBasicRemote().sendText(who + msg);
                } catch (IOException e) {
                    connect.remove(client);
                    e.printStackTrace();
                    try {
                        client.session.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }

        }
    }

    /**
     * 发送给指定用户
     *
     * @param user    用户名
     * @param msg     发送的消息
     * @param session
     */
    public void sendToUser(String user, String msg, Session session) {
        boolean you = false;//标记是否找到发送的用户
        for (String key : userMap.keySet()) {
            if (user.equalsIgnoreCase(userMap.get(key))) {
                MyWebSocket client = (MyWebSocket) connect.get(key);
                synchronized (client) {
                    try {
                        client.session.getBasicRemote().sendText(userMap.get(session.getId()) + "对你说：" + msg);
                    } catch (IOException e) {
                        connect.remove(client);
                        try {
                            client.session.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                you = true;//找到指定用户标记为true
                break;
            }

        }
        //you为true则在自己页面显示自己对xxx说xxxxx,否则显示系统：无此用户
        if (you) {
            try {
                session.getBasicRemote().sendText("自己对" + user + "说:" + msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                session.getBasicRemote().sendText(format.format(new Date()) + " 系统：无此用户或用户已下线");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }


    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message) {
        for (MyWebSocket item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        MyWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        MyWebSocket.onlineCount--;
    }
}