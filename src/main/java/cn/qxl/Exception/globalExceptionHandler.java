package cn.qxl.Exception;

import com.alibaba.fastjson.JSON;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


/**
 * Created by qiu on 2018/10/30.
 */
@ControllerAdvice
public class globalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public String AuthenticationException(AuthenticationException e) throws Exception {
        System.out.println(e.getMessage());
        return JSON.toJSONString(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView globalException(Exception e) {
        System.out.println("错误信息:"+e.getMessage());
        return new ModelAndView("401","msg",e);
    }
}
