package cn.edu.njnu.geoproblemsolving.business.user.entity;

import com.alibaba.fastjson.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
//GeoProblemSolving platform user
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "userDto")
public class UserDto {
    private String userId;
    private String userName;
    private String email;
    private String password;
    private String mobilePhone;
    private String gender;
    private String jobTitle;
    private String country;
    private String city;
    private String organization;
    private String introduction;
    private String direction;
    private String homePage;
    private String avatar;


    private JSONArray joinedProjects;
    private JSONArray manageProjects;
}
