package cn.edu.njnu.geoproblemsolving.business.user.dto;

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
public class InquiryUserDto {
    /**
     * Necessary
     */
    @Id
    private String userId;  //UUID
    private String name;
    private String email;   //used for registration

    /**
     * UserEntity server
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

    /**
     * Unique
     * adi
     */
    private ArrayList<String> joinedProjects;
    private ArrayList<String> createdProjects;
}
