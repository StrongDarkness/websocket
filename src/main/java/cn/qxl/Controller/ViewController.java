package cn.qxl.Controller;

import cn.qxl.Bean.R;
import cn.qxl.Bean.Token;
import cn.qxl.Bean.UserInfo;
import cn.qxl.Chat.MyWebSocket;
import cn.qxl.Service.UserService;
import cn.qxl.Utils.LogUtils;
import cn.qxl.shiro.jwt.JwtUtil;
import com.alibaba.fastjson.JSON;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView loginpage() {
        return new ModelAndView("login");
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

    @RequestMapping("/adminlogin")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        Subject user = SecurityUtils.getSubject();
        UserInfo ui = userService.getUserByUserName(username);
        // AuthenticationToken token=new UsernamePasswordToken(username,password);
        Token tk = new Token(JwtUtil.sign(username, password,ui.getUserId()));
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


    @RequestMapping("response401")
    @ResponseBody
    public R response401(String msg) {
//        System.out.println(msg);
        return R.error(401, msg);
    }

}
