package cn.edu.njnu.geoproblemsolving.business.user.service;

import cn.edu.njnu.geoproblemsolving.business.user.entity.User;

import java.util.ArrayList;

public interface UserService {

    public User findUser(String userId);

    public Object updataUserInfo(User user);

    public Object register(User user);

    public String changePassword(String email, String password);

    public Object login(String email, String password);

}
