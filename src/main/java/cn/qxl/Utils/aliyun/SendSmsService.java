package cn.qxl.Utils.aliyun;

import cn.qxl.Utils.aliyun.config.aliyunConfig;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 短信发送
 *
 * @author qiu
 */
@Slf4j
@Service("SendSmsService")
public class SendSmsService {

    @Autowired
    private aliyunConfig config;
    // 产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    // 产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    // static final String accessKeyId = "";
//    private String accessKeyId = config.getAccessKeyID();
    // static final String accessKeySecret = "";
//    private String accessKeySecret = config.getAccessKeySecret();

    public static enum Type {
        /* 注册 */
        REGISTER,
        /* 忘记密码 */
        FORGETPWD,
        /* 实名认证失败 */
        REALNAMEFALIED,
        /* 更改设备 */
        CHANGEDEVICE,
        /*管理员登录验证*/
        ADMINLOGIN
    }

    /**
     * 发送短信
     *
     * @param phoneNum
     * @param code
     * @return
     * @throws ClientException
     */
    public SendSmsResponse sendSms(String phoneNum, String code, Type t) throws ClientException {

        // 可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        // 初始化acsClient,暂不支持region化
//        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", config.getAccessKeyID(), config.getAccessKeySecret());
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        // 组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        // 必填:待发送手机号
        request.setPhoneNumbers(phoneNum);
        // System.out.println(phoneNum);
        // 必填:短信签名-可在短信控制台中找到
        request.setSignName("米仓伙伴");// 阿里云短信测试专用
        // 必填:短信模板-可在短信控制台中找到
        if (t == Type.REGISTER) {// 注册
            request.setTemplateCode("SMS_129755922");
            // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam("{\"name\":\"" + phoneNum + "\", \"code\":\"" + code + "\"}");
        } else if (t == Type.FORGETPWD) {// 忘记密码
            request.setTemplateCode("SMS_134250184");
            // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam("{\"name\":\"" + phoneNum + "\", \"code\":\"" + code + "\"}");
        } else if (t == Type.REALNAMEFALIED) {// 实名认证失败
            request.setTemplateCode("SMS_142010095");// SMS_130919841
            request.setTemplateParam("{\"errmsg\":\"" + code + "\"}");
        } else if (t == Type.CHANGEDEVICE) {// SMS_144146135//更换设备登录
            request.setTemplateCode("SMS_144146135");// 您的账号正尝试在其他设备登录，登录验证码：${code}，如果这不是您本人操作，您的密码可能已经泄露，请及时修改密码！
            request.setTemplateParam("{\"code\":\"" + code + "\"}");
        }else if(t== Type.ADMINLOGIN){//管理员登录模板
            request.setTemplateCode("SMS_153880064");// 您的管理后台账号正尝试在其他电脑登录，登录验证码：${code}，如果这不是您本人操作，您的密码可能已经泄露，请及时修改密码！
            request.setTemplateParam("{\"code\":\"" + code + "\"}");
        } else {
            request.setTemplateCode("SMS_129755922");
            // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam("{\"name\":\"" + phoneNum + "\", \"code\":\"" + code + "\"}");
        }

        // System.out.println("{\"name\":\"15970446645\",
        // \"code\":\""+code+"\"}");
        // 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        // request.setSmsUpExtendCode("90997");

        // 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("yourOutId");

        // hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        return sendSmsResponse;
    }

    /**
     * 获取短信信息
     *
     * @param bizId
     * @param phoneNum
     * @return
     * @throws ClientException
     */
    public QuerySendDetailsResponse querySendDetails(String bizId, String phoneNum) throws ClientException {

        // 可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        // 初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", config.getAccessKeyID(), config.getAccessKeySecret());
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        // 组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        // 必填-号码
        request.setPhoneNumber(phoneNum);
        // 可选-流水号
        request.setBizId(bizId);
        // 必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(new Date()));
        // 必填-页大小
        request.setPageSize(10L);
        // 必填-当前页码从1开始计数
        request.setCurrentPage(1L);

        // hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);

        return querySendDetailsResponse;
    }
}
