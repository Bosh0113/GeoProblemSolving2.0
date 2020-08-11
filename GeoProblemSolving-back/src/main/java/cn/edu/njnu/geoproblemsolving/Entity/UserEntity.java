package cn.edu.njnu.geoproblemsolving.Entity;

import com.alibaba.fastjson.JSONArray;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "User")
public class UserEntity {

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
    private JSONArray joinedProjects;
    private JSONArray manageProjects;
    private String introduction;
    private String direction;
    private String homePage;
    private String avatar;

    public JSONArray getManageProjects() {
        return manageProjects;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getCountry() {
        return country;
    }

    public String getOrganization() {
        return organization;
    }

    public JSONArray getJoinedProjects() {
        return joinedProjects;
    }

    public String getCity() {
        return city;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getHomePage() {
        return homePage;
    }

    public String getDirection() {
        return direction;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setJoinedProjects(JSONArray joinedProjects) {
        this.joinedProjects = joinedProjects;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setManageProjects(JSONArray manageProjects) {
        this.manageProjects = manageProjects;
    }
}