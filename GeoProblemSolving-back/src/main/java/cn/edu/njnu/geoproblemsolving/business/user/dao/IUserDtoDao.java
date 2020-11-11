package cn.edu.njnu.geoproblemsolving.business.user.dao;

import cn.edu.njnu.geoproblemsolving.business.user.entity.UserDto;

import javax.servlet.http.HttpServletRequest;

public interface IUserDtoDao {
    Object findUserById(String userId);
    Object updateUserInfo(HttpServletRequest req);
    //前端传输用户信息
    Object updateUserInfo(UserDto userDto);
}
