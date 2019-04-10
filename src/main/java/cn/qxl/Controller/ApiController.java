package cn.qxl.Controller;

import cn.qxl.Bean.UserInfo;
import cn.qxl.Chat.MyWebSocket;
import cn.qxl.Service.UserService;
import cn.qxl.shiro.jwt.JwtUtil;
import com.alibaba.fastjson.JSON;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qiu on 2018/10/29.
 */
@Controller
@RequestMapping("/api")
public class ApiController {
    private UserService userService;
    @Autowired
    private MyWebSocket webSocket;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletResponse response) {
        UserInfo user = userService.getUserByUserName(username);
        if (user.getPassword().equals(password)) {
            Map<String, Object> result = new HashMap<>();
            result.put("result", "login success");
            String token = JwtUtil.sign(username, password,user.getUserId());
            result.put("token", token);
            return JSON.toJSONString(result);
        } else {
            throw new UnauthorizedException();
        }
    }

    @ResponseBody
    @GetMapping("/getuser")
//    @RequiresAuthentication
    public String test() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return JSON.toJSONString("已登录有权限访问");
        } else {
            return JSON.toJSONString("未登录没有权限访问");
        }
    }


    @ResponseBody
    @GetMapping("/getUserOnline")
    public String getUserOnline() {
        Map<String,String> umap=  MyWebSocket.getOnline();
        return JSON.toJSONString(umap);
    }

    @ResponseBody
    @GetMapping("/sendMsg")
    public String sendMsg(String msg) {
        webSocket.sendToAll(msg);
        return "发送成功";
    }

}
