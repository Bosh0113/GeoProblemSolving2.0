package cn.edu.njnu.geoproblemsolving.business.user.entity;

import cn.edu.njnu.geoproblemsolving.business.user.enums.UserTitle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Data
@Document(collection = "User")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * Necessary
     */
    @Id
    private String userId;  //UUID
    private String name;
    private String password;    //MD5
    private String email;   //used for registration

    /**
     * User server
     */
    private String avatar;
    private String phone;
    private UserTitle title;
    private String country;
    private String state;    // County / State / Province
    private String city;
    private String homePage;
    private String introduction;
    private String createdTime;
    private ArrayList<String> organizations;
    private ArrayList<String> domain;
    private ArrayList<String> loginIp;
    private ArrayList<Object> resource;

    /**
     * Unique
     * adi
     */
    private ArrayList<String> joinedProjects;
    private ArrayList<String> createdProjects;
}
