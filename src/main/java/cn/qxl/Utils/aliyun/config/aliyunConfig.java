package cn.qxl.Utils.aliyun.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by qiu on 2019/1/17.
 */
@Component
@Data
@ConfigurationProperties(prefix = "aliyun")
public class aliyunConfig {
    @Value("${aliyun.AccessKeyID}")
    private String AccessKeyID;
    @Value("${aliyun.AccessKeySecret}")
    private String AccessKeySecret;
}
