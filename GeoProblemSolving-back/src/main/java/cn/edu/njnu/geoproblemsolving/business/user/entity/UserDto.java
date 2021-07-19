package cn.edu.njnu.geoproblemsolving.business.user.entity;

import cn.edu.njnu.geoproblemsolving.Entity.Resources.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.business.user.enums.UserTitle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Date;

/**
 * @ClassName UserDto
 * @Description 用于与用户服务器进行数据交换
 * @Author zhngzhng
 * @Date 2021/4/4
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String userId;
    private String email;
    private String password;
    private String name;

    private String phone;
    private UserTitle title;
    private String country;
    private String state;
    private String city;
    private String homepage;
    private ArrayList<String> organizations;
    private String avatar;
    private String introduction;
    private Date createdTime;
    private ArrayList<String> loginIp;
    private ArrayList<String> domain;
    private ArrayList<ResourceEntity> resource;
}
