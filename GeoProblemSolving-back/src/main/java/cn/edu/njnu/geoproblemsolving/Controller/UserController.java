package cn.edu.njnu.geoproblemsolving.Controller;

import cn.edu.njnu.geoproblemsolving.Dao.User.UserDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.UserEntity;
import com.alibaba.fastjson.JSONArray;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8080",allowCredentials = "true")
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/register", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public String saveUser(@RequestBody UserEntity user) {
        UserDaoImpl userDao = new UserDaoImpl(mongoTemplate);
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);
        user.setJoinedProjects(new JSONArray());
        user.setManageProjects(new JSONArray());
        try {
            return userDao.saveUser(user);
        } catch (Exception e) {
            return "Fail";
        }
    }

    @RequestMapping(value = "/inquiry", method = RequestMethod.GET)
    public Object readUser(@RequestParam("key") String key,@RequestParam("value") String value){
        UserDaoImpl userDao=new UserDaoImpl(mongoTemplate);
        try {
            return userDao.readUser(key,value);
        }catch (Exception e){
            return "Fail";
        }
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public String removeUser(@RequestParam("userId") String userId) {
        UserDaoImpl userDao = new UserDaoImpl(mongoTemplate);
        try {
            userDao.removeUser("userId", userId);
            return "Success";
        } catch (Exception e) {
            return "Fail";
        }
    }

    @RequestMapping(value = "/update", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public Object updateUser(HttpServletRequest request) {
        UserDaoImpl userDao = new UserDaoImpl(mongoTemplate);
        try {
            return userDao.updateUser(request);
        }catch (Exception e){
            return "Fail";
        }
    }

    @RequestMapping(value = "/newPassword", method = RequestMethod.GET)
    public Object updateNewPassword(@RequestParam("email") String email,@RequestParam("password") String password) {
        UserDaoImpl userDao = new UserDaoImpl(mongoTemplate);
        return userDao.updatePassword(email,password);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public Object login(@RequestParam("email") String email, @RequestParam("password") String password, HttpServletRequest request) {
        UserDaoImpl userDao = new UserDaoImpl(mongoTemplate);
        try {
            Object object=userDao.login(email,password);
            if(!object.equals("Fail")){
                UserEntity user=(UserEntity)object;
                HttpSession session=request.getSession();
                session.setMaxInactiveInterval(30*60);
                session.setAttribute("userId",user.getUserId());
                session.setAttribute("userName",user.getUserName());
                session.setAttribute("avatar",user.getAvatar());
                session.setAttribute("email",user.getEmail());
                session.setMaxInactiveInterval(30*60);
                System.out.println("User login. UserName: "+user.getUserName());
            }
            return object;
        } catch (Exception e) {
            return "Fail";
        }
    }

    @RequestMapping(value = "/logout", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.GET)
    public void logout(HttpServletRequest request){
        HttpSession session=request.getSession();
        System.out.println("User logout. UserName: "+session.getAttribute("userName"));
        session.invalidate();
    }

    @RequestMapping(value = "/state", method = RequestMethod.POST)
    public Object userState(HttpServletRequest request){
        try {
            HttpSession session=request.getSession();
            if (session.getAttribute("userId")!=null){
                UserDaoImpl userDao=new UserDaoImpl(mongoTemplate);
                UserEntity userInfo=(UserEntity) userDao.readUser("userId",session.getAttribute("userId").toString());
                userInfo.setPassword("");
                return userInfo;
            }
            else {
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }

    @RequestMapping(value = "/isRegistered", method = RequestMethod.GET)
    public Boolean isRegistered(@RequestParam("email") String email){
        UserDaoImpl userDao = new UserDaoImpl(mongoTemplate);
        return userDao.isRegistered(email);
    }

}