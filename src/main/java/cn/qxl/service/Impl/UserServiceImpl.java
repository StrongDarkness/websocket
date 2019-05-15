package cn.qxl.service.Impl;

import cn.qxl.bean.UserInfo;
import cn.qxl.mapper.UserInfoMapper;
import cn.qxl.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by qiu on 2019/1/11.
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public int deleteUserInfoById(Long id) {
        return userInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int saveUserInfo(UserInfo record) {
        return userInfoMapper.insert(record);
    }

    @Override
    public int saveUserInfoSelective(UserInfo record) {
        return userInfoMapper.insertSelective(record);
    }

    @Override
    public UserInfo selectUserInfoById(Long id) {
        return userInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateUserInfoSelective(UserInfo record) {
        return userInfoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateUserInfo(UserInfo record) {
        return userInfoMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<UserInfo> getAllUser(UserInfo ui) {
        return userInfoMapper.getAllUserInfo(ui);
    }

    @Override
    public UserInfo getUserByUserName(String username) {
        return userInfoMapper.getUserByUserName(username);
    }

    @Override
    public UserInfo getUserById(String userId) {
        return userInfoMapper.getUserById(userId);
    }

    @Override
    @Transactional
    public boolean register(UserInfo info) throws Exception {

        return false;
    }
}
