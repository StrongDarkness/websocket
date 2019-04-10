package cn.qxl.shiro.realm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.realm.Realm;

import java.util.Collection;

/**
 * 重写该方法支持多realm登录方式
 * Created by qiu on 2019/1/14.
 */
@Slf4j
public class CustomModularRealmAuthenticator extends ModularRealmAuthenticator {


    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 判断getRealms()是否返回为空
        assertRealmsConfigured();
        //匹配realm类型
        Realm realm = getUniqueRealm(getRealms(), authenticationToken);
        if (null == realm) {
            throw new UnsupportedTokenException("没有匹配类型的realm");
        }
        return realm.getAuthenticationInfo(authenticationToken);
    }

    /**
     * 判断realms是否匹配,并返回唯一的可用的realm,否则返回空
     *
     * @param realms realm集合
     * @param token  登陆信息
     * @return 返回唯一的可用的realm
     */
    private Realm getUniqueRealm(Collection<Realm> realms, AuthenticationToken token) {
        for (Realm realm : realms) {
            if (realm.supports(token)) {
                return realm;
            }
        }
        log.error("一个可用的realm都没有找到......");
        return null;
    }
}
