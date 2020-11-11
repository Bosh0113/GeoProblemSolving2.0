package cn.edu.njnu.geoproblemsolving.business.user.controller;

import cn.edu.njnu.geoproblemsolving.business.StaticParams;
import cn.edu.njnu.geoproblemsolving.business.user.dao.IUserDtoImpl;
import cn.edu.njnu.geoproblemsolving.business.user.entity.UserDto;
import cn.edu.njnu.geoproblemsolving.business.user.service.TokenTask;
import cn.edu.njnu.geoproblemsolving.business.user.util.ICommonUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequestMapping(value = "/user")
public class IUserController {
    @Autowired
    TokenTask tokenTask;
    @Autowired
    IUserDtoImpl userDao;

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
            String userId = (String) userBase.get("userId");
            StaticParams.loginUser = userDao.findUserById(userId);
            System.out.println("User login. UserName: " + StaticParams.loginUser.getUserName());
            resp.sendRedirect(StaticParams.referPageUrl);
        } catch (Exception e) {
            return;
        }
    }

    @RequestMapping(value = "/state", method = RequestMethod.POST)
    public Object userState(HttpServletRequest req) {
        HttpSession session = req.getSession();
        if (StaticParams.loginUser != null) {
            UserDto userInfo = StaticParams.loginUser;
            session.setMaxInactiveInterval(60 * 60 * 2);
            session.setAttribute("userId", StaticParams.loginUser.getUserId());
            session.setAttribute("userName", StaticParams.loginUser.getUserName());
            userInfo.setPassword("");
            return userInfo;
        }
        return false;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        System.out.println("User logout. UserName: " + StaticParams.loginUser.getUserName());
        session.invalidate();
        StaticParams.loginUser = null;
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
        UserDto userInfo = (UserDto) userDao.updateUserInfo(req);
        MultiValueMap<String, Object> map = commonUtil.gsm2BaseUser();
        return userInfo.getUserId();
    }
}
