package cn.edu.njnu.geoproblemsolving.Service;

import cn.edu.njnu.geoproblemsolving.Entity.User;

public interface UserService {

    public User findUser(String userId);

    public Object updataUserInfo(User user);

    public Object register(User user);

    public String changePassword(String email, String password);

    public Object login(String email, String password);
}
