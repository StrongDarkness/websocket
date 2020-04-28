package cn.qxl.chat;

import cn.qxl.bean.UserInfo;
import cn.qxl.bean.chatBean;
import cn.qxl.config.WebSocketConfig;
import cn.qxl.service.UserService;
import com.alibaba.fastjson.JSON;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * websocket消息处理
 * Created by qiu on 2018/10/13.
 */
@ServerEndpoint(value = "/websocket/{userId}", configurator = WebSocketConfig.class)
@Component
@EnableWebSocket
public class MyWebSocket {
    private static ApplicationContext applicationContext;
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //connect key为session的ID，value为此对象this
    private static final Map<String, Object> connect = new HashMap<String, Object>();
    //userMap key为session的ID，value为用户名
    private static final Map<String, UserInfo> userMap = new HashMap<String, UserInfo>();
    private static final Map<String, Session> sessionMap = new HashMap<String, Session>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //当前用户
    private String username;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();

    //在线用户
    public static Map<String, UserInfo> getOnline() {
        return userMap;
    }

    private static UserService userService;

    public static void setApplicationContext(ApplicationContext context) {
        MyWebSocket.applicationContext = context;
        userService=applicationContext.getBean(UserService.class);
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        UserInfo ui = userService.getUserByUserId(userId);
        if (ui==null){
            sendToAll("","用户不存在");
            return;
        }
        this.username = ui.getNickName();
//        String uname= (String) session.getUserProperties().get("username");
//        System.out.println(uname);
        connect.put(session.getId(), this);
        sessionMap.put(username, session);
        userMap.put(session.getId(), ui);
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        System.out.println();
        sendToAll("系统管理员", "有新连接加入！当前在线人数为" + getOnlineCount());
        sendToAll("系统管理员", "欢迎 " + username + " 连接");

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        UserInfo user = userMap.get(session.getId());
        connect.remove(session.getId());
        userMap.remove(session.getId());
        sessionMap.remove(user);
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("用户 " + user.getNickName() + " 断开了连接");
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
        sendToAll("系统管理员", "用户 " + username + " 断开连接");
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        this.username = userMap.get(session.getId()).getNickName();
        System.out.println("用户" + username + "上线,在线人数：" + connect.size());
        String[] msg = message.split("@", 2);//以@为分隔符把字符串分为xxx和xxxxx两部分,msg[0]表示发送至的用户名，all则表示发给所有人
        if (msg[0].equals("all")) {
            sendToAll(username, msg[1]);
        } else {
            sendToUser(msg[0], msg[1]);
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
     * @param msg 发送的消息
     */
    public void sendToAll(String username, String msg) {
        chatBean bean = new chatBean();
        bean.setTime(new Date());
        bean.setToUser("all");
        bean.setMsg(msg);
        //群发消息
        for (String key : connect.keySet()) {
            MyWebSocket client = (MyWebSocket) connect.get(key);
            if (key.equalsIgnoreCase(userMap.get(key).getNickName())) {
                bean.setUsername("自己");
                bean.setSelf(true);
            } else {
                bean.setUsername(username);
                bean.setSelf(false);
            }
            synchronized (client) {
                try {
                    client.session.getBasicRemote().sendText(JSON.toJSONString(bean));
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
     * @param user 用户名
     * @param msg  发送的消息
     */
    public void sendToUser(String user, String msg) {
        chatBean bean = new chatBean();
        bean.setMsg(msg);
        bean.setToUser(user);
        bean.setTime(new Date());
        if (!isOnline(user)) {
            try {
                bean.setUsername("系统消息");
                bean.setMsg("该用户不存在或已下线");
                sessionMap.get(username).getBasicRemote().sendText(JSON.toJSONString(bean));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        if (!user.equalsIgnoreCase(username)) {
            bean.setUsername(username);
            bean.setSelf(false);
            try {
                sessionMap.get(user).getBasicRemote().sendText(JSON.toJSONString(bean));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bean.setUsername("自己");
        bean.setSelf(true);
        try {
            sessionMap.get(username).getBasicRemote().sendText(JSON.toJSONString(bean));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 判断用户是否在线
     *
     * @param username
     * @return
     */
    private boolean isOnline(String username) {
        if (sessionMap.get(username) != null) {
            return true;
        }
        return false;
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