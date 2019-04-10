package cn.qxl.shiro.realm;

import cn.qxl.Bean.Token;
import cn.qxl.Bean.UserInfo;
import cn.qxl.Service.UserService;
import cn.qxl.Utils.EhcacheUtil;
import cn.qxl.shiro.jwt.JwtUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * app端token校验器
 * Created by qiu on 2018/10/29.
 */
@Service
public class AuthRealm extends AuthorizingRealm {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof Token;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = JwtUtil.getUsername(principalCollection.toString());
        UserInfo u = userService.getUserByUserName(username);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (u != null) {
            info.addRole(u.getRoles());
            //        Set<String> permission = new HashSet<>(Arrays.asList(u.getPermission().split(",")));
            Set<String> permission = new HashSet<>();
//        System.out.println(permission.toString());
//        info.addStringPermission(permission.toString());
            info.addStringPermissions(permission);
        }
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        String username = JwtUtil.getUsername(token);//解密token信息
        if (username == null) {
//            System.out.println("token invalid");
            throw new AuthenticationException("token invalid!");
        }
        UserInfo user = userService.getUserByUserName(username);
        //处理token(单人同一时间内只能在线一人)
        String tk = (String) EhcacheUtil.getInstance().get(username);
        if (tk!=null&&!"".equals(tk)) {
            if (!tk.equals(token)) {
                throw new AuthenticationException("this user is logined at other place!");
            }
        }

        if (user == null) {
//            System.out.println("user didn't existed!");
            throw new AuthenticationException("user didn't existed!");
        }
        if (!JwtUtil.verify(token, username, user.getPassword(), user.getUserId())) {
//            System.out.println("username or password error");
            throw new AuthenticationException("username or password error!中文");
        }
        return new SimpleAuthenticationInfo(token, token, getName());
    }
}
