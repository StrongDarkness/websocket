package cn.qxl.Controller;

import cn.qxl.Bean.Token;
import cn.qxl.Bean.User;
import cn.qxl.Chat.MyWebSocket;
import cn.qxl.Service.UserService;
import cn.qxl.Utils.JwtUtil;
import cn.qxl.Utils.LogUtils;
import com.alibaba.fastjson.JSON;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.Local;
import javax.resource.spi.AuthenticationMechanism;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qiu on 2018/10/13.
 */
@Controller
@RequestMapping("/")
public class ViewController {
    @Autowired
    private MyWebSocket socket;

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public String loginpage() {
        return "login";
    }

    @RequestMapping("/index")
    @RequiresAuthentication
    public String index() {
        return "index";
    }

    @RequestMapping("/add")
    @RequiresPermissions(logical = Logical.AND, value = {"add", "view"})
    public String add() {
        return "index";
    }

    @RequestMapping("/get")
    @RequiresPermissions(logical = Logical.AND, value = {"get", "view"})
    public String get() {
        return "index";
    }

    @RequestMapping("/delete")
    @RequiresRoles("admin")
    @RequiresPermissions("delete")
    public String delete() {
        return "index";
    }

    @RequestMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        Subject user = SecurityUtils.getSubject();
        // AuthenticationToken token=new UsernamePasswordToken(username,password);
        Token tk = new Token(JwtUtil.sign(username, password));
        try {
            user.login(tk);
            return "index";
        }catch (UnknownAccountException e){
            LogUtils.getLogger(getClass()).info(e.getMessage());
            System.out.println(e.getMessage());
            model.addAttribute("msg", e.getMessage());
        } catch (AuthenticationException e) {
            LogUtils.getLogger(getClass()).info(e.getMessage());
            model.addAttribute("msg", e.getMessage());
        }
        return "login";
    }

    @RequestMapping("/dologin")
    public String dologin(){
        return "login";
    }

    @RequestMapping("/loginOut")
    public String loginOut() {
        SecurityUtils.getSubject().logout();
        return "login";
    }

    @RequestMapping("/401")
    @ExceptionHandler(ShiroException.class)
    public ModelAndView unauth(ShiroException e) {
        return new ModelAndView("401", "msg", "你无权访问" + e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ModelAndView unauth(AuthenticationException e) {
        return new ModelAndView("401", "msg", "你无权访问" + e.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public String unauth(Exception e){
        return JSON.toJSONString(e.getMessage());
    }

}
