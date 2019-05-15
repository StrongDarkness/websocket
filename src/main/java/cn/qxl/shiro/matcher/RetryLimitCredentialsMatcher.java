package cn.qxl.shiro.matcher;

import cn.qxl.utils.EhcacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 密码错误次数锁定验证器
 * Created by qiu on 2019/2/15.
 */
@Slf4j
public class RetryLimitCredentialsMatcher extends HashedCredentialsMatcher {
    /**
     * 密码错误锁定次数
     */
    private static final int MAX_LOGIN_TIMES = 5;
//    private Cache<String, AtomicInteger> passwordRetryCache;

//    public RetryLimitCredentialsMatcher(CacheManager manager){
//        passwordRetryCache=manager.getCache("passwordRetryCache");
//    }
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String) token.getPrincipal();
        log.error(username);
        AtomicInteger count = (AtomicInteger) EhcacheUtil.getInstance().get("passwordRetryCache", username);
//        AtomicInteger count = passwordRetryCache.get(username);
        System.out.println(count);
        if (count == null) {
            count = new AtomicInteger(0);
//            passwordRetryCache.put(username,count);
            EhcacheUtil.getInstance().put("passwordRetryCache", username, count);
        }else{

        }
        if (count.incrementAndGet() > MAX_LOGIN_TIMES) {
            System.out.println("密码错误次数过多，账号锁定5分钟");
            throw new ExcessiveAttemptsException("密码错误次数过多，账号锁定5分钟");
        }
        boolean match = super.doCredentialsMatch(token, info);
        if (match) {
            EhcacheUtil.getInstance().remove("passwordRetryCache", username);
//            passwordRetryCache.remove(username);
        }
        return match;
    }
}
