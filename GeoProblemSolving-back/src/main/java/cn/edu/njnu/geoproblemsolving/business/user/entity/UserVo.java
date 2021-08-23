package cn.edu.njnu.geoproblemsolving.business.user.entity;

import cn.edu.njnu.geoproblemsolving.business.user.enums.UserTitle;
import com.alibaba.fastjson.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Date;

/**
 * @ClassName UserVo
 * @Description viewObject 前端使用
 * @Author zhngzhng
 * @Date 2021/4/6
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserVo {
    private String userId;
    private String name;
    private String email;

    /**
     * UserEntity server
     */
    private String avatar;
    private String phone;
    private UserTitle title;
    private String country;
    private String province;
    private String city;
    private String homepage;
    private String introduction;
    private ArrayList<String> organizations;
    private ArrayList<String> domain;
    //资源不需要放在 session 里面
    // private ArrayList<ResourcePojo> resource;

    /**
     * Unique
     * adi
     */
    private ArrayList<String> joinedProjects;
    private ArrayList<String> createdProjects;
}
