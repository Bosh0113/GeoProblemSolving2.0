package cn.edu.njnu.geoproblemsolving.Controller;

import cn.edu.njnu.geoproblemsolving.Dao.JupyterhubUsers.JupyterhubUsersDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.JupyterhubUsersEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin(origins = "*", allowCredentials = "true")
@RestController
@RequestMapping("/jupyter")
public class JupyterhubUsersController {
    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/create", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public Object createJupyterUser(@RequestBody JupyterhubUsersEntity jupyterUser) {
        JupyterhubUsersDaoImpl jupyterhubUsersDao = new JupyterhubUsersDaoImpl(mongoTemplate);
        try {
            return jupyterhubUsersDao.createUser(jupyterUser);
        } catch (Exception e) {
            return "Fail";
        }
    }

    @RequestMapping(value = "/inquiry", method = RequestMethod.GET)
    public Object inquiryJupyterUser( @RequestParam("projectId") String projectId) {
        JupyterhubUsersDaoImpl jupyterhubUsersDao = new JupyterhubUsersDaoImpl(mongoTemplate);
        try {
            return jupyterhubUsersDao.inquiryUser(projectId);
        } catch (Exception e) {
            return "Fail";
        }
    }
}
