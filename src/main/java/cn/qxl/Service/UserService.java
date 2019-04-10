package cn.qxl.Service;

import cn.qxl.Bean.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by qiu on 2018/10/29.
 */
@Component
@Service
public class UserService {

    public User getUser(String username){
        User u=new User();
        u.setUsername("user");
        u.setPassword("123456");
        u.setPermission("add,delete,view");
        u.setRole("user");
        return u;
    }
}
