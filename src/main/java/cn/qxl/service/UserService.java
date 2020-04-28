package cn.qxl.service;

import cn.qxl.bean.UserInfo;

import java.util.List;

/**
 * 用户信息
 * Created by qiu on 2019/1/11.
 */
public interface UserService {
    int deleteUserInfoById(Long id);

    int saveUserInfo(UserInfo record);

    int saveUserInfoSelective(UserInfo record);

    UserInfo selectUserInfoById(Long id);

    int updateUserInfoSelective(UserInfo record);

    int updateUserInfo(UserInfo record);

    List<UserInfo> getAllUser(UserInfo ui);

    UserInfo getUserByUserName(String username);
    //根据user_id查询用户
    UserInfo getUserById(String userId);

    UserInfo getUserByUserId(String userId);

    boolean register(UserInfo info) throws Exception;
}
