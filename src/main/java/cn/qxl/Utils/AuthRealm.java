package cn.qxl.Utils;

import cn.qxl.Bean.Token;
import cn.qxl.Bean.User;
import cn.qxl.Service.UserService;
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by qiu on 2018/10/29.
 */
@Service
public class AuthRealm  extends AuthorizingRealm {

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
        String username=JwtUtil.getUsername(principalCollection.toString());
        User u=userService.getUser(username);
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        info.addRole(u.getRole());
        Set<String> permission=new HashSet<>(Arrays.asList(u.getPermission().split(",")));
//        System.out.println(permission.toString());
//        info.addStringPermission(permission.toString());
        info.addStringPermissions(permission);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token= (String) authenticationToken.getCredentials();
        String username=JwtUtil.getUsername(token);
        if (username==null)
        {
            System.out.println("token invalid");
            throw new AuthenticationException("token invalid");
        }
        User user=userService.getUser(username);
        if (user==null){
            System.out.println("user didn't existed!");
            throw new AuthenticationException("user didn't existed!");
        }
        if (!JwtUtil.verify(token,username,user.getPassword())){
            System.out.println("username or password error");
            throw new AuthenticationException("username or password error");
        }
        return new SimpleAuthenticationInfo(token,token,getName());
    }
}
