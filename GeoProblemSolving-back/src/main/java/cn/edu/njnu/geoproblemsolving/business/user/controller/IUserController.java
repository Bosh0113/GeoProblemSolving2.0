package cn.edu.njnu.geoproblemsolving.business.user.controller;

import cn.edu.njnu.geoproblemsolving.business.user.dao.IUserImpl;
import cn.edu.njnu.geoproblemsolving.business.user.entity.User;
import cn.edu.njnu.geoproblemsolving.business.user.entity.UserVo;
import cn.edu.njnu.geoproblemsolving.business.user.service.Impl.UserServiceImpl;
import cn.edu.njnu.geoproblemsolving.business.user.service.TokenTask;
import cn.edu.njnu.geoproblemsolving.business.user.util.ICommonUtil;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping(value = "/user")
public class IUserController {
    @Autowired
    TokenTask tokenTask;
    @Autowired
    IUserImpl userDao;

    @Autowired
    UserServiceImpl userService;


    @RequestMapping(method = RequestMethod.PUT, value = "/quitProject")
    public JsonResult updateUserProject(HttpServletRequest req) {
        return userService.deleteUserProjectService(req);
    }



    /**
     * @Author mzy
     * @Date 2020.12.18
     */

    /**
     * Inquiry information of one user
     *
     * @param key：userId, email
     * @param value
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public JsonResult getUserInfo(@RequestParam String key, @RequestParam String value) {
        return userDao.getUserInfo(key, value);
    }




    /**
     * @Author zhngzhng
     * @Date 2021/4/4
     * @Description 我想的话，common Response 仅在 controller 层使用即可
     * 我这里用得似乎并不规范
     */

    /**
     * 用户注册
     * 两个步骤
     * 1.用户服务器进行注册
     * 2.注册成功则在参与式本地进行保存，失败则返回对应信息回前端
     *
     * @param user
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public JsonResult register(@RequestBody User user) {
        return userService.registerService(user);
    }

    @RequestMapping(value = "/sendEmail/{email}", method = RequestMethod.GET)
    public JsonResult sendCode(@PathVariable String email) {
        return userService.sendResetPwdEmail(email);
    }

    @RequestMapping(value = "/changePwd/{email}/{code}/{newPwd}", method = RequestMethod.GET)
    public JsonResult changePassword(@PathVariable("email") String email,
                                     @PathVariable("code") String code,
                                     @PathVariable("newPwd") String newPwd) {
        return userService.resetPwdByCode(email, code, newPwd);
    }

    @RequestMapping(value = "/resetPwd/{email}/{oldPwd}/{newPwd}", method = RequestMethod.GET)
    public JsonResult resetPassword(@PathVariable("email") String email,
                                    @PathVariable("oldPwd") String oldPwd,
                                    @PathVariable("newPwd") String newPwd) {
        return userService.resetPwdByOldPwd(email, oldPwd, newPwd);
    }

    /**
     * 需要修改，增加 httpRequest 获取用户登录ip
     * @param email
     * @param password
     * @param request
     * @return
     */
    @RequestMapping(value = "/login/{email}/{password}", method = RequestMethod.GET)
    public JsonResult login(@PathVariable("email") String email,
                            @PathVariable("password") String password,
                            HttpServletRequest request) {
        String ipAddr = userService.getIpAddr(request);
        JsonResult loginResult = userService.loginAndAcquireInfo(email, password, ipAddr);
        if (loginResult.getCode() != 0) {
            return loginResult;
        }
        UserVo localUser = (UserVo)loginResult.getData();
        HttpSession session = request.getSession();
        session.setAttribute("userId", localUser.getUserId());
        session.setAttribute("name", localUser.getName());
        session.setAttribute("avatar", localUser.getAvatar());
        session.setAttribute("email", localUser.getEmail());
        session.setMaxInactiveInterval(60 * 60 * 2);
        System.out.println("User login. User name: " + localUser.getName());
        return loginResult;
    }

    /**
     * 用户登录状态判定
     * @param req
     * @return
     */
    @RequestMapping(value = "/state", method = RequestMethod.POST)
    public Object userState(HttpServletRequest req) {
        HttpSession session = req.getSession();
        String userId = (String) session.getAttribute("userId");
        if (userId == null) { return false; }
        return userService.getUserVo(userId);

    }

    /**
     * 涉及到到 resource 的需要将所有内容搞定
     * @param user
     * @return
     */
    @RequestMapping(produces = "application/json;charset=UTF-8", method = RequestMethod.PUT)
    public JsonResult updateUserInfo(@RequestBody JSONObject user){
        //此处 user 必须将其的userId 带过来
        return userService.updateUserInfo(user);
    }

    /**
     * 登出，将用户tokenInfo字段置空
     * @param request
     * @throws IOException
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout(HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        System.out.println("User logout. UserName: " + session.getAttribute("userName"));
        String userId = (String)session.getAttribute("userId");
        userService.logout(userId);
        session.invalidate();
    }



}
