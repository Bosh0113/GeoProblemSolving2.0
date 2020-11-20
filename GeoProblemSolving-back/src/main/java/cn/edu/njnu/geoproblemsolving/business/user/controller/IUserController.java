package cn.edu.njnu.geoproblemsolving.business.user.controller;

import cn.edu.njnu.geoproblemsolving.business.StaticParams;
import cn.edu.njnu.geoproblemsolving.business.user.dao.IUserImpl;
import cn.edu.njnu.geoproblemsolving.business.user.entity.User;
import cn.edu.njnu.geoproblemsolving.business.user.entity.UserDto;
import cn.edu.njnu.geoproblemsolving.business.user.service.TokenTask;
import cn.edu.njnu.geoproblemsolving.business.user.util.ICommonUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/user")
public class IUserController {
    @Autowired
    TokenTask tokenTask;
    @Autowired
    IUserImpl userDao;

    //存储-1页面
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam("pageUrl") String pageUrl, HttpServletRequest req, HttpServletResponse resp) {
        ICommonUtil iCommonUtil = new ICommonUtil();
        try {
            StaticParams.referPageUrl = pageUrl;
            // String contextPath = req.getRequestURI().toString();
            //发送邮件
            return "http://106.14.78.235/AuthServer/oauth/authorize?grant_type=authorization_code&response_type=code&client_id=GSM&scope=all";
        } catch (Exception e) {
            return null;
        }

    }

    @RequestMapping(value = "/getToken", method = RequestMethod.GET)
    public void getUserInfo(@RequestParam("code") String code, HttpServletRequest req, HttpServletResponse resp) {
        try {
            String userBaseStr = (String) tokenTask.getData(code);
            JSONObject userBase = JSONObject.parseObject(userBaseStr);
            User gsmUser = JSONObject.toJavaObject(userBase, User.class);
            String userId = gsmUser.getUserId();
            //查询如果无的话，则将此用户存入到本地数据库中
            StaticParams.loginUser = userDao.findUserById(userId);
            if (StaticParams.loginUser == null){
                // StaticParams.loginUser = (User) userDao.saveUser(userBase);
                StaticParams.loginUser = (User)userDao.saveLocalUser(gsmUser);
            }
            System.out.println("User login. UserName: " + StaticParams.loginUser.getName());
            resp.sendRedirect(StaticParams.referPageUrl);
        } catch (Exception e) {
            System.out.println("login error: " + e);
            return;
        }
    }

    @Autowired
    MongoTemplate mongoTemplate;
    @RequestMapping(value = "/state", method = RequestMethod.POST)
    public Object userState(HttpServletRequest req) {
        HttpSession session = req.getSession();
        if (StaticParams.loginUser != null) {
            User userInfo = StaticParams.loginUser;
            User user = userDao.findUserById(StaticParams.loginUser.getUserId());
            session.setMaxInactiveInterval(60 * 60 * 2);
            session.setAttribute("userId", userInfo.getUserId());
            session.setAttribute("userName", userInfo.getName());
            userInfo.setPassword("");
            return user;
        }
        return false;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        System.out.println("User logout. UserName: " + StaticParams.loginUser.getName());
        session.invalidate();
        StaticParams.loginUser = null;
        StaticParams.access_token = "";
        StaticParams.refresh_token = "";
    }

    /**
     * 更新用户基础信息，需要更新本地数据库，也需要更新用户服务器的数据库。
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
}
