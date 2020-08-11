package cn.edu.njnu.geoproblemsolving.Entity;

import cn.edu.njnu.geoproblemsolving.domain.user.enums.UserTitle;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Data
@Document(collection = "User")
public class User {

    /**
     * Necessary
     */
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
    private String province;    // County / State / Province
    private String city;
    private String homepage;
    private String introduction;
    private String createdTime;
    private ArrayList<String> organizations;
    private ArrayList<String> domain;
    private ArrayList<String> loginIp;
    //private ArrayList<String> resource;

    /**
     * Unique
     * adi
     */
    private ArrayList<String> joinedProjects;
    private ArrayList<String> manageProjects;
}
