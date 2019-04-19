package cn.qxl.shiro.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import cn.qxl.shiro.filter.JwtFilter;
import cn.qxl.shiro.matcher.RetryLimitCredentialsMatcher;
import cn.qxl.shiro.realm.AuthRealm;
import cn.qxl.shiro.realm.CustomModularRealmAuthenticator;
import cn.qxl.shiro.realm.FormRealm;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.*;

/**
 * Created by qiu on 2018/10/29.
 */
@ComponentScan
@Configuration
public class ShiroConfig {

    @Bean("securityManager")
    public DefaultWebSecurityManager getManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setAuthenticator(modularRealmAuthenticator());//设置支持多realm
        List<Realm> realms = new ArrayList<>();
        realms.add(authRealm());
        realms.add(formRealm());
        manager.setSessionManager(getSessionManager());
        manager.setRealms(realms);
//        manager.setCacheManager(ehcacheManager(EhcacheUtil.getInstance().getManager()));

        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator evaluator = new DefaultSessionStorageEvaluator();
        evaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(evaluator);
        manager.setSubjectDAO(subjectDAO);

        return manager;
    }

    @Bean("sessionManager")
    public DefaultWebSessionManager getSessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionIdCookie(sessionIdCookie());
        return sessionManager;
    }

    @Bean
    public SimpleCookie sessionIdCookie() {
        //DefaultSecurityManager
        SimpleCookie simpleCookie = new SimpleCookie();
        //sessionManager.setCacheManager(ehCacheManager());
        //如果在Cookie中设置了"HttpOnly"属性，那么通过程序(JS脚本、Applet等)将无法读取到Cookie信息，这样能有效的防止XSS攻击。
        simpleCookie.setHttpOnly(true);
//		simpleCookie.setName("SHRIOSESSIONID");
        simpleCookie.setName("shiro.sesssion");
        //单位秒
        simpleCookie.setMaxAge(86400);
        return simpleCookie;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean factory(DefaultWebSecurityManager manager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt", new JwtFilter());//jwt过滤器
        bean.setFilters(filterMap);
//        bean.setSecurityManager(manager);
        bean.setSecurityManager(getManager());
        bean.setUnauthorizedUrl("/401");
        bean.setLoginUrl("/");
        bean.setSuccessUrl("/index");//登录成功的返回
        /*
         * 自定义url规则
         * http://shiro.apache.org/web.html#urls-
         */
        Map<String, String> filterRuleMap = new LinkedHashMap<>();
        // 所有请求通过我们自己的JWT Filter
        filterRuleMap.put("/User/login", "anon");
        filterRuleMap.put("/User/register", "anon");
        filterRuleMap.put("/adminlogin", "anon");
        filterRuleMap.put("/admin/getCodeImg", "anon");
        filterRuleMap.put("/login", "anon");
        filterRuleMap.put("/websocket/**", "anon");//websocket路径
        filterRuleMap.put("/*.ico", "anon");
        filterRuleMap.put("/admin/loginOut", "anon");
        filterRuleMap.put("/admin/formLogin", "anon");
        // 访问401和404页面不通过我们的Filter
        filterRuleMap.put("/401", "anon");
        filterRuleMap.put("/response401", "anon");
        filterRuleMap.put("/404", "anon");
        filterRuleMap.put("/images/**", "anon");//img
        filterRuleMap.put("/img/**", "anon");//img
        filterRuleMap.put("/css/**", "anon");//css
        filterRuleMap.put("/js/**", "anon");//js
        filterRuleMap.put("/layer/**", "anon");//js
        filterRuleMap.put("/laydate/**", "anon");//js
        filterRuleMap.put("/static/**", "anon");
        filterRuleMap.put("/html/**", "anon");
        filterRuleMap.put("/jsp/**", "anon");
        filterRuleMap.put("/druid/**", "anon");
        filterRuleMap.put("/ureport/**", "anon");
        filterRuleMap.put("/upload/**", "anon");
        filterRuleMap.put("/WEB-INF/**", "anon");
        filterRuleMap.put("/api/**", "jwt");//api下用户jwt拦截
//        filterRuleMap.put("/**", "authc");//默认拦截所有url
        bean.setFilterChainDefinitionMap(filterRuleMap);

        return bean;
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        creator.setProxyTargetClass(true);
        return creator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions)
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager manager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(manager);
        return advisor;
    }

    @Bean(name = "realm")
    public AuthRealm authRealm() {
        return new AuthRealm();
    }

    @Bean(name = "formRealm")
    public FormRealm formRealm() {
        FormRealm realm = new FormRealm();
//        realm.setCacheManager(ehcacheManager());
//        realm.setCredentialsMatcher(retryLimitCredentialsMatcher());
        return realm;
    }

    /**
     * 系统自带的Realm管理，主要针对多realm
     */
    @Bean
    public ModularRealmAuthenticator modularRealmAuthenticator() {
        //自己重写的ModularRealmAuthenticator
        CustomModularRealmAuthenticator modularRealmAuthenticator = new CustomModularRealmAuthenticator();
        modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return modularRealmAuthenticator;
    }

    /**
     * 密码错误次数过多锁定
     *
     * @return
     */
    @Bean("credentialsMatcher")
    public CredentialsMatcher retryLimitCredentialsMatcher() {
//        System.out.println("retryLimitCredentialsMatcher");
        RetryLimitCredentialsMatcher matcher = new RetryLimitCredentialsMatcher();
        matcher.setHashAlgorithmName("MD5");
        matcher.setHashIterations(1);
//        RetryLimitCredentialsMatcher matcher = new RetryLimitCredentialsMatcher(this.ehcacheManager(EhcacheUtil.getInstance().getManager()));
        return matcher;
    }

//    @Bean
//    public EhCacheManager ehcacheManager(CacheManager cacheManager) {
//        EhCacheManager manager=new EhCacheManager();
////        manager.setCacheManager(EhcacheUtil.getInstance().getManager());
////        manager.setCacheManagerConfigFile("classpath:ehcache.xml");
//        manager.setCacheManager(cacheManager);
//        return manager;
//    }

    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }


}
