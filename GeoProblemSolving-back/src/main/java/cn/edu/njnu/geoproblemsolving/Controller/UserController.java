package cn.edu.njnu.geoproblemsolving.Controller;

import cn.edu.njnu.geoproblemsolving.Entity.User;
import cn.edu.njnu.geoproblemsolving.Service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@CrossOrigin(origins = "http://localhost:8080",allowCredentials = "true")
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Register
     * @param user
     * @return
     */
    @RequestMapping(produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public Object saveUser(@RequestBody User user) {
        return userService.register(user);
    }

    @RequestMapping(produces = {"application/json;charset=UTF-8"}, method = RequestMethod.PUT)
    public Object updateUser(@RequestBody User user) {
        return userService.updataUserInfo(user);
    }

    /**
     * Get status
     * @param request
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Object userState(HttpServletRequest request){
        try {
            HttpSession session=request.getSession();
            if (session.getAttribute("userId") != null){
                User user = userService.findUser(session.getAttribute("userId").toString());
                if(user == null) return "Fail";
                user.setPassword("");
                return user;
            }
            else {
                return false;
            }
        }catch (Exception e){
            return "Fail";
        }
    }

    /**
     * change password
     * @param email
     * @param password
     * @return
     */
    @RequestMapping(value = "/password", method = RequestMethod.POST)
    public String updatePassword(@RequestParam("email") String email, @RequestParam("password") String password) {
        return userService.changePassword(email, password);
    }

    /**
     * login
     * @param email
     * @param password
     * @param request
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public Object login(@RequestParam("email") String email, @RequestParam("password") String password, HttpServletRequest request) {
        Object result = userService.login(email, password);
        if (result instanceof String) {
            return result;
        } else {
            User user = (User) result;
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(30 * 60);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("name", user.getName());
            session.setAttribute("avatar", user.getAvatar());
            session.setAttribute("email", user.getEmail());
            session.setMaxInactiveInterval(30 * 60);
            System.out.println("User login. User name: " + user.getName());
            return user;
        }
    }

    /**
     * logout
     * @param request
     */
    @RequestMapping(value = "/logout", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.GET)
    public void logout(HttpServletRequest request){
        HttpSession session=request.getSession();
        System.out.println("User logout. UserName: "+session.getAttribute("userName"));
        session.invalidate();
    }
}
