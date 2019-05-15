package cn.qxl.utils.jpush.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by qiu on 2019/1/17.
 */
@Component
@Data
@ConfigurationProperties(prefix = "jpush")
public class JpushConfig {
    @Value("${jpush.isDebug}")
    private String isDebug;
    // 读取极光配置信息中的用户名密码
    @Value("${jpush.appKey}")
    private String appkey;
    @Value("${jpush.masterSecret}")
    private String masterSecret;
    @Value("${jpush.liveTime}")
    private String liveTime;
}
