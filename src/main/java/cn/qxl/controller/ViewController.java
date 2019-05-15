package cn.qxl.controller;

import cn.qxl.bean.R;
import cn.qxl.bean.Token;
import cn.qxl.bean.UserInfo;
import cn.qxl.chat.MyWebSocket;
import cn.qxl.service.UserService;
import cn.qxl.utils.LogUtils;
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
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by qiu on 2018/10/13.
 */
@Controller
@RequestMapping("/")
public class ViewController {
    @Autowired
    private MyWebSocket socket;

    private UserService userService;

    private static String commodityCount = "commodityCount";//商品key
    private static String lockKey = "testRedisson";//分布式锁的key

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    Redisson redisson;

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
        try {
            UserInfo ui = userService.getUserByUserName(username);
            if (ui==null){
                model.addAttribute("msg","用户不存在");
                return "login";
            }
            // AuthenticationToken token=new UsernamePasswordToken(username,password);
            Token tk = new Token(JwtUtil.sign(username, password,ui.getUserId()));
            user.login(tk);
            model.addAttribute("username",username);
            model.addAttribute("nickname",ui.getNickName());
            return "index";
        }catch (UnknownAccountException e){
            LogUtils.getLogger(getClass()).info(e.getMessage());
            System.out.println(e.getMessage());
            model.addAttribute("msg", e.getMessage());
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
            LogUtils.getLogger(getClass()).info(e.getMessage());
            model.addAttribute("msg", e.getMessage());
        }catch (Exception e){
            System.out.println(e);
            model.addAttribute("msg","未知异常："+e.getMessage());
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
    @RequestMapping("/test")
    @ResponseBody
    public String test() throws Exception {
        //设置一个key，aaa商品的库存数量为100
        stringRedisTemplate.opsForValue().set(commodityCount,"5");
//        Assert.assertEquals("100", stringRedisTemplate.opsForValue().get("aaa"));
        return "success";
    }


    /**
     * 模拟秒杀抢购，并发200个请求过来，查看是否出现超卖
     * @return
     */
    @RequestMapping("/spike")
    @ResponseBody
    public String spike() throws Exception{
        String flag = "success";
        RLock lock = redisson.getLock(lockKey);
        try{
            //lock.lockAsync(5 , TimeUnit.SECONDS);
            //lock.lock(5, TimeUnit.SECONDS); //设置60秒自动释放锁  （默认是30秒自动过期）
            Future<Boolean> res = lock.tryLockAsync(100, 5, TimeUnit.SECONDS);
            boolean result = res.get();
            if(result){
                int stock = Integer.parseInt(redisTemplate.opsForValue().get(commodityCount).toString());
                System.out.println("stock-->"+stock);
                if(stock > 0){
                    redisTemplate.opsForValue().set(commodityCount,(stock-1)+"");
                }else{
                    flag = "fail";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            lock.unlock(); //释放锁
        }
        System.out.println(Thread.currentThread().getName()+"-result:" + flag);
        return flag;
    }
}
