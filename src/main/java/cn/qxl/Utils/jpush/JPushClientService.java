package cn.qxl.Utils.jpush;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.qxl.Utils.jpush.config.JpushConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 极光推送
 *
 * @author qiu
 */
@Slf4j
@Service("jpushService")
public class JPushClientService {

    @Autowired
    JpushConfig config;
    /**
     * appkey
     */
//    private String flag = config.getIsDebug();
 //   private String APPKEY = config.getAppkey();
    /**
     * mastersecret
     */
//	private static final String MASTERSECRET = "";
//    private String MASTERSECRET = config.getMasterSecret();
    // 保存离线的时长，最多支持10天 （Ps：不填写时，默认是保存一天的离线消息 0：代表不保存离线消息）
    private static int timeToLive = 60 * 60 * 24;
    private static JPushClient client = null;

    public void sendAllsetNotification(String alias, String alert, String id) {
        if (config.getIsDebug().equals("true")) {
            log.info("测试环境，关闭极光推送");
            return;
        }
//        JPushClient jpushClient = new JPushClient(MASTERSECRET, APPKEY, null, ClientConfig.getInstance());
        JPushClient jpushClient = new JPushClient(config.getMasterSecret(), config.getAppkey(), null, ClientConfig.getInstance());

        PushPayload payload1 = null;
        PushPayload payload = null;
        try {
            payload1 = buildPushObject_all_alias_alert_Android(alias, alert, id);
        } catch (Exception e) {
            System.out.println("Android发送错误！");
            log.error("Android发送错误！", e);
        }

        try {
            payload = buildPushObject_all_alias_alert_Ios(alias, alert, id);
        } catch (Exception e) {
            System.out.println("Ios发送错误！");
            log.error("Ios发送错误！", e);
        }
        try {
            PushResult result = jpushClient.sendPush(payload);
        } catch (Exception e) {
            System.out.println("Ios发送错误！");
            log.error("Ios发送错误！", e);
        }

        try {
            PushResult result1 = jpushClient.sendPush(payload1);
        } catch (Exception e) {
            System.out.println("Android发送错误！");
            log.error("Android发送错误！", e);
        }
    }

    public PushPayload buildPushObject_all_alias_alert_Ios(String alias, String alert, String id) {
        if (id!=null&&!"".equals(id)) {
            id = "";
        }
        return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder().setAlert(alert).setBadge(+1)
                                // .addExtras(map)
                                .addExtra("id", id).build())
                        .build())
                .build();
    }

    public PushPayload buildPushObject_all_alias_alert_Android(String alias, String alert, String id) {
        if (id!=null&&!"".equals(id)) {
            id = "";
        }
        return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder().setAlert(alert)
                                // .addExtras(map)
                                .addExtra("id", id).build())
                        .build())
                .build();
    }

    /**
     * 设置通知
     *
     * @param alias
     * @param alert
     * @param isRealName
     * @return
     */
    public PushPayload buildPushObject_alias_followUser(String alias, String alert, String isRealName,
                                                        String name) {
        return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder().addExtra("isRealName", isRealName)
                                .addExtra("nickname", name).setAlert(alert).build())
                        .addPlatformNotification(IosNotification.newBuilder().addExtra("isRealName", isRealName)
                                .addExtra("nickname", name).setAlert(alert).setBadge(+1).build())
                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(true)// true-推送生产环境
                        .setTimeToLive(timeToLive).build())
                .build();
    }

    /**
     * 极光通用信息发送
     *
     * @param alias 推送范围
     * @param alert 内容
     * @param data  附加内容
     * @return
     * @see #
     * <li>{@link #buildPushObjectComm}</li>
     */
    public PushPayload buildPushObjectComm(String alias, String alert, Map<String, String> data) {
        AndroidNotification.Builder andBuilder = AndroidNotification.newBuilder();
        IosNotification.Builder iosBuilder = IosNotification.newBuilder();
        if (data != null) {// 遍历数据
            Set<Entry<String, String>> set = data.entrySet();
            for (Entry<String, String> entry : set) {
                andBuilder.addExtra(entry.getKey(), entry.getValue());
                iosBuilder.addExtra(entry.getKey(), entry.getValue());
                System.out.println(entry.getKey() + ":" + entry.getValue());
            }
        }
        return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder().addPlatformNotification(andBuilder.setAlert(alert).build())
                        .addPlatformNotification(iosBuilder.setAlert(alert).setBadge(+1).build()).build())
                .setOptions(Options.newBuilder().setApnsProduction(true)// true-推送生产环境
                        .setTimeToLive(timeToLive).build())
                .build();
    }

    /**
     * 发送通知(通用)
     *
     * @param alias 通知范围
     * @param alert 通知内容
     * @param data  附加数据
     * @see #
     * <li>{@link #sendPushComm}</li>
     */
    public void sendPushComm(String alias, String alert, Map<String, String> data) {
        if (config.getIsDebug().equals("true")) {
            log.info("测试环境，关闭极光推送");
            return;
        }
        try {
            client = new JPushClient(config.getMasterSecret(), config.getAppkey(), null, ClientConfig.getInstance());
            // 生成推送的内容
            PushPayload payload = buildPushObjectComm(alias, alert, data);
            payload.resetOptionsTimeToLive(timeToLive);
            PushResult result = client.sendPush(payload);
            log.info("Got result - " + result);
            System.out.println("jpush result:" + result);
        } catch (APIConnectionException e) {
            // Connection error, should retry later
            log.error("Connection error, should retry later", e);
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
            // Should review the error, and fix the request
            log.error("Should review the error, and fix the request", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
        }

    }

    /**
     * 发送推送消息
     *
     * @param alias      推送范围
     * @param alert      消息
     * @param isRealName 附加内容
     */
    // send message after followUser
    public void sendPushAfterFollow(String alias, String alert, String isRealName, String name) {
        if (config.getIsDebug().equals("true")) {
            log.info("测试环境，关闭极光推送");
            return;
        }
        try {
            client = new JPushClient(config.getMasterSecret(), config.getAppkey(), null, ClientConfig.getInstance());
            // 生成推送的内容
            PushPayload payload = buildPushObject_alias_followUser(alias, alert, isRealName, name);
            payload.resetOptionsTimeToLive(timeToLive);
            PushResult result = client.sendPush(payload);
            log.info("Got result - " + result);
            System.out.println("jpush result:" + result);
        } catch (APIConnectionException e) {
            // Connection error, should retry later
            log.error("Connection error, should retry later", e);
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
            // Should review the error, and fix the request
            log.error("Should review the error, and fix the request", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
        }
    }

    /**
     * 发送推送消息 积分订单
     *
     * @param alias      推送范围
     * @param alert      消息
     * @param isRealName 附加内容
     */
    // send message after followUser
    public void sendPushAfterFollowjf(String alias, String alert, String isRealName) {
        if (config.getIsDebug().equals("true")) {
            log.info("测试环境，关闭极光推送");
            return;
        }
        try {
            client = new JPushClient(config.getMasterSecret(), config.getAppkey(), null, ClientConfig.getInstance());
            // 生成推送的内容
            PushPayload payload = buildPushObject_alias_followUser_jf(alias, alert, isRealName);
            payload.resetOptionsTimeToLive(timeToLive);
            PushResult result = client.sendPush(payload);
            log.info("Got result - " + result);
            System.out.println("jpush result:" + result);
        } catch (APIConnectionException e) {
            // Connection error, should retry later
            log.error("Connection error, should retry later", e);
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
            // Should review the error, and fix the request
            log.error("Should review the error, and fix the request", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
        }

    }

    /**
     * 设置通知
     *
     * @param alias
     * @param alert
     * @param isRealName
     * @return
     */
    public PushPayload buildPushObject_alias_followUser_jf(String alias, String alert, String isRealName) {
        return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder().addExtra("integral", isRealName)
                                .setAlert(alert).build())
                        .addPlatformNotification(
                                IosNotification.newBuilder().addExtra("integral", isRealName).setAlert(alert).setBadge(+1).build())
                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(true)// true-推送生产环境
                        .setTimeToLive(timeToLive).build())
                .build();
    }

    /**
     * 发送推送消息 米币更新
     *
     * @param alias  推送范围
     * @param alert  消息
     * @param micion 附加内容
     */
    // send message after followUser
    public void sendPushMiCoin(String alias, String alert, String micion) {
        if (config.getIsDebug().equals("true")) {
            log.info("测试环境，关闭极光推送");
            return;
        }
        try {
            client = new JPushClient(config.getMasterSecret(), config.getAppkey(), null, ClientConfig.getInstance());
            // 生成推送的内容
            PushPayload payload = buildPushObjectMiCoin(alias, alert, micion);
            payload.resetOptionsTimeToLive(timeToLive);
            PushResult result = client.sendPush(payload);
            log.info("Got result - " + result);
            System.out.println("jpush result:" + result);
        } catch (APIConnectionException e) {
            // Connection error, should retry later
            log.error("Connection error, should retry later", e);
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
            // Should review the error, and fix the request
            log.error("Should review the error, and fix the request", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
        }

    }

    /**
     * 设置通知 米币
     *
     * @param alias
     * @param alert
     * @param micion
     * @return
     */
    public PushPayload buildPushObjectMiCoin(String alias, String alert, String micion) {
        return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(
                                AndroidNotification.newBuilder().addExtra("micion", micion).setAlert(alert).build())
                        .addPlatformNotification(
                                IosNotification.newBuilder().addExtra("micion", micion).setAlert(alert).setBadge(+1).build())
                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(true)// true-推送生产环境
                        .setTimeToLive(timeToLive).build())
                .build();
    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("cece", "1000");
        map.put("aa", "13213465");
        map.put("bb", "456");
        map.put("cc", "1dsdd");
        map.put("dd", "15555");

    }

}
