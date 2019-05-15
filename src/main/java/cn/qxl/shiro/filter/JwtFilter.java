package cn.qxl.shiro.filter;

import cn.qxl.bean.Token;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qiu on 2018/10/29
 */
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

    /**
     * 判断用户是否想要登入。
     * 检测header里面是否包含token字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("token");
        return token != null;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("token");
        Token tk = new Token(token);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        if (token==null||"".equals(token.trim())) {
            return false;
        }
        try {
            getSubject(request, response).login(tk);
        } catch (AuthenticationException e) {
//            e.printStackTrace();
//            onLoginFailure(tk, e, request, response);
            response401(request, response, e.getMessage());
            return false;
        }
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问
     * 例如我们提供一个地址 GET /article
     * 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws AuthenticationException {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("token");
        Token tk = new Token(token);
        if (isLoginAttempt(request, response)) {
            try {
                executeLogin(request, response);
            } catch (Exception e) {
                response401(request, response, e.getMessage());
//              onLoginFailure(tk,new AuthenticationException("You don't have permission to access this URL."),request,response);
//                onLoginFailure(tk, new AuthenticationException(e.getMessage()), request, response);
                return false;
            }
        } else {
            response401(request, response, "未登录或登录已失效！");
//            onLoginFailure(tk, new AuthenticationException("未登录或登录已失效！"), request, response);
            return false;
        }
        return true;
    }


    /**
     * 对跨域提供支持
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setHeader("Access-control-Allow-Origin", req.getHeader("Origin"));
        resp.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        resp.setHeader("Access-Control-Allow-Headers", req.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
            resp.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
//        System.out.println(e.getMessage());
        request.setAttribute("msg", e.getMessage());
        PrintWriter out = null;
        try {
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=utf-8");
            out = response.getWriter();
            Map<String, Object> map = new HashMap<>();
            map.put("status", "401");
            map.put("result", e.getMessage());
            out.println(JSON.toJSONString(map));
            out.flush();
            out.close();
        } catch (IOException e1) {
            // e1.printStackTrace();
            log.error(e1.getMessage());
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return super.onAccessDenied(request, response);
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
//        return super.onLoginSuccess(token, subject, request, response);
        issueSuccessRedirect(request, response);
        return false;
    }

    @Override
    protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
//        super.issueSuccessRedirect(request, response);
//        System.out.println(getSuccessUrl());
        WebUtils.redirectToSavedRequest(request, response, getSuccessUrl());
    }

    /**
     * 将非法请求跳转到 /response401并提示
     */
    private void response401(ServletRequest req, ServletResponse resp, String msg) {
        try {
//            System.out.println(msg);
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) resp;
            resp.setContentType("text/html;charset=utf-8");
            request.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
//            response.sendRedirect("/response401?msg=" + msg);
            request.getRequestDispatcher("/response401?msg=" + msg).forward(request, response);

        } catch (Exception e) {
//            System.err.println(e.getMessage());
            log.error(e.getMessage());
        }
    }
}
