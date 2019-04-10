package cn.qxl.shiro.realm;

import cn.qxl.Bean.AdminUser;
import cn.qxl.Service.AdminRolesService;
import cn.qxl.Service.AdminUserService;
import cn.qxl.Utils.EhcacheUtil;
import cn.qxl.shiro.token.PasswordToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * web端token校验
 * Created by qiu on 2018/10/29.
 */
@Service
@Slf4j
public class FormRealm extends AuthorizingRealm {

    public AdminUserService adminUserService;
    public AdminRolesService adminRolesService;

    @Autowired
    public void setUserService(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @Autowired
    public void setAdminRolesService(AdminRolesService adminRolesService) {
        this.adminRolesService = adminRolesService;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof PasswordToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = principalCollection.getPrimaryPrincipal().toString();
        AdminUser au = adminUserService.getAdminUserByUserName(username);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (au != null) {
//            System.out.println(au.getRoleId());
            info.addRole(au.getRoleId());
//            Set<String> roles = new HashSet<>(Arrays.asList(au.getRoleId().split(",")));
            Set<String> permission2 = new HashSet<>();
//        log.info(permission2.toString());
            permission2.add("admin");
            info.addStringPermissions(permission2);
        }
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        PasswordToken token = (PasswordToken) authenticationToken;
        String username = token.getUsername();
        String password = String.valueOf(token.getPassword());
        //处理session(单人同一时间内只能在线一人)
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        DefaultWebSessionManager sessionManager = (DefaultWebSessionManager) securityManager.getSessionManager();
        Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();//获取当前已登录的用户session列表
        for (Session session : sessions) {
            //清除该用户以前登录时保存的session
            if (username.equals(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)))) {
                sessionManager.getSessionDAO().delete(session);
//                log.info("踢出前者"+username);
            }
        }

//        System.out.println(username);
        if (username == null) {
//            System.out.println("token invalid");
            throw new UnknownAccountException("token invalid!");
        }
        AdminUser au = adminUserService.getAdminUserByUserName(username);
        if (au == null) {
//            System.out.println("user didn't existed!");
            throw new UnknownAccountException("user didn't existed!");
        }

        if (!au.getAdminPassword().equals(password)) {
//            System.out.println("username or password error");
            //保存密码错误次数
            EhcacheUtil util = EhcacheUtil.getInstance();
            String scount = (String) util.get("passwordRetryCache", username);
            if (scount == null) {
                scount = "0";
            }
            int count = Integer.parseInt(scount) + 1;//每次加1
            if (count > 5) {//密码错误超过5次，锁定账号5分钟
                throw new ExcessiveAttemptsException("user locked 5 mins!");
            } else {
                util.update("passwordRetryCache", username, count + "");
            }
            throw new IncorrectCredentialsException("username or password error!");
        }
        return new SimpleAuthenticationInfo(username, au.getAdminPassword(), getName());
    }

    /**
     * 清理权限缓存
     */
    public void clearCachedAuthorization() {
        //清空权限缓存
        clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }

}
