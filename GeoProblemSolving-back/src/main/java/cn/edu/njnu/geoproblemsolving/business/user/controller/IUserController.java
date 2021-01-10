package cn.edu.njnu.geoproblemsolving.business.user.controller;

import cn.edu.njnu.geoproblemsolving.business.user.StaticParams;
import cn.edu.njnu.geoproblemsolving.business.user.dao.IUserImpl;
import cn.edu.njnu.geoproblemsolving.business.user.entity.User;
import cn.edu.njnu.geoproblemsolving.business.user.service.TokenTask;
import cn.edu.njnu.geoproblemsolving.business.user.util.ICommonUtil;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/user")
public class IUserController {
    @Autowired
    TokenTask tokenTask;
    @Autowired
    IUserImpl userDao;
    @Value("$AuthUrl")
    String redirectUri;

    //存储-1页面
    // @RequestMapping(value = "/login", method = RequestMethod.GET)
    // public String login(@RequestParam("pageUrl") String pageUrl, HttpServletRequest req, HttpServletResponse resp) {
    //     ICommonUtil iCommonUtil = new ICommonUtil();
    //     try {
    //         StaticParams.referPageUrl = pageUrl;
    //         // String contextPath = req.getRequestURI().toString();
    //         //发送邮件
    //         return "http://106.14.78.235/AuthServer/oauth/authorize?grant_type=authorization_code&response_type=code&client_id=zhengzhong&scope=all";
    //     } catch (Exception e) {
    //         return null;
    //     }
    //
    // }

   @RequestMapping(value = "/login", method = RequestMethod.GET)
   public Object login(@RequestParam("email") String email, @RequestParam("password") String password, HttpServletRequest request) {

       JSONObject token = tokenTask.getTokenUsePwd(email, password);
       if (token == null){
           return "Fail";
       }
       String access_token = (String)token.get("access_token");

       //获取到用户服务器对象
       JSONObject userBaseJson = tokenTask.getUserFromResServer(access_token);
       User userBase =  JSONObject.toJavaObject(userBaseJson, User.class);

       //如果本地数据库中无此对象，则将其存入本地数据库中
       User localUser = userDao.findUserById(userBase.getUserId());
       if (localUser == null){
           userDao.saveLocalUser(userBase);
       }
        HttpSession session = request.getSession();
       session.setAttribute("userId", userBase.getUserId());
       session.setAttribute("name", userBase.getName());
       session.setAttribute("avatar", userBase.getAvatar());
       session.setAttribute("email", userBase.getEmail());
       session.setMaxInactiveInterval(60 * 60 * 2);
       System.out.println("User login. User name: " + userBase.getName());
       return userBase;
   }

    /**
     * 授权码模式 废弃
     * @param code
     * @param req
     * @param resp
     */
    // @RequestMapping(value = "/getToken", method = RequestMethod.GET)
    // public void getUserInfo(@RequestParam("code") String code, HttpServletRequest req, HttpServletResponse resp) {
    //     try {
    //         String userBaseStr = (String) tokenTask.getData(code);
    //         JSONObject userBase = JSONObject.parseObject(userBaseStr);
    //         User gsmUser = JSONObject.toJavaObject(userBase, User.class);
    //         String userId = gsmUser.getUserId();
    //         //查询如果无的话，则将此用户存入到本地数据库中
    //         StaticParams.loginUser = userDao.findUserById(userId);
    //         if (StaticParams.loginUser == null) {
    //             StaticParams.loginUser = (User) userDao.saveUser(userBase);
    //             StaticParams.loginUser = (User) userDao.saveLocalUser(gsmUser);
    //         }
    //         HttpSession session = req.getSession();
    //         session.setAttribute("userId", userId);
    //         session.setAttribute("userName", gsmUser.getName());
    //         System.out.println("User login. UserName: " + StaticParams.loginUser.getName());
    //         resp.sendRedirect(StaticParams.referPageUrl);
    //     } catch (Exception e) {
    //         System.out.println("login error: " + e);
    //         return;
    //     }
    // }

    @Autowired
    MongoTemplate mongoTemplate;

    @RequestMapping(value = "/state", method = RequestMethod.POST)
    public Object userState(HttpServletRequest req) {
        HttpSession session = req.getSession();
        String userId = (String)session.getAttribute("userId");
        if (userId == null){
            return false;
        }else {
            //从本地数据库
            User user = userDao.findUserById(userId);
            return user;
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        HttpSession session = request.getSession();
        System.out.println("User logout. UserName: " + session.getAttribute("userName"));
        session.invalidate();
    }

    /**
     * 更新用户基础信息，需要更新本地数据库，也需要更新用户服务器的数据库。
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/update", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public Object updateUser(HttpServletRequest req) {
        ICommonUtil commonUtil = new ICommonUtil();
        //update local userInfo and set userBase Update
        User userInfo = (User) userDao.updateUserInfo(req);
        userInfo.setPassword("");
        commonUtil.gsm2BaseUser(StaticParams.paramsMap);
        return userInfo;
    }

    /**
     * 查询项目
     * @param joinedProjects
     * @return
     */
    @RequestMapping(value = "/JoinedProjects", method = RequestMethod.POST)
    public JsonResult getManagerProjectList(@RequestBody String[] joinedProjects) {
        System.out.println(joinedProjects);
        return userDao.getJoinedProjectList(joinedProjects);
    }

    /**
     * @Author mzy
     * @Date 2020.12.18
     */

    /**
     * Inquiry information of one user
     * @param key：userId, email
     * @param value
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public JsonResult getUserInfo(@RequestParam String key, @RequestParam String value){
        return userDao.getUserInfo(key, value);
    }

    /**
     * Store user information to database
     * @param user
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public JsonResult addUserInfo(@RequestBody JSONObject user){
        return userDao.addUserInfo(user);
    }


}
