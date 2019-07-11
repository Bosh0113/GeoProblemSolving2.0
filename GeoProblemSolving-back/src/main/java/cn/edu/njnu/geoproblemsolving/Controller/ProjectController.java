package cn.edu.njnu.geoproblemsolving.Controller;

import cn.edu.njnu.geoproblemsolving.Dao.Project.ProjectDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.EmailEntity;
import cn.edu.njnu.geoproblemsolving.Entity.ProjectEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", allowCredentials = "true")
@RestController
@RequestMapping("/project")
public class ProjectController {
    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/create", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public String createProject(@RequestBody ProjectEntity project) {
        ProjectDaoImpl projectDao = new ProjectDaoImpl(mongoTemplate);
        try {
            return projectDao.createProject(project);
        } catch (Exception e) {
            return "Fail";
        }
    }

    @RequestMapping(value = "/inquiry", method = RequestMethod.GET)
    public Object readProject(@RequestParam("key") String key, @RequestParam("value") String value) {
        ProjectDaoImpl projectDao = new ProjectDaoImpl(mongoTemplate);
        try {
            return projectDao.readProject(key, value);
        } catch (Exception e) {
            return "Fail";
        }
    }

    @RequestMapping(value = "/inquiryAll", method = RequestMethod.GET)
    public Object readAllProject() {
        ProjectDaoImpl projectDao = new ProjectDaoImpl(mongoTemplate);
        return projectDao.readAllProjects();
    }

    @RequestMapping(value = "/inquiryByType", method = RequestMethod.GET)
    public Object readByType(@RequestParam("key") String key, @RequestParam("value") String value) {
        ProjectDaoImpl projectDao = new ProjectDaoImpl(mongoTemplate);
        return projectDao.readByType(key, value);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteProject(@RequestParam("projectId") String projectId) {
        ProjectDaoImpl projectDao = new ProjectDaoImpl(mongoTemplate);
        try {
            projectDao.deleteProject("projectId", projectId);
            return "Success";
        } catch (Exception e) {
            return "Fail";
        }
    }

    @RequestMapping(value = "/update", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public Object updateProject(HttpServletRequest request) {
        ProjectDaoImpl projectDao = new ProjectDaoImpl(mongoTemplate);
        try {
            return projectDao.updateProject(request);
        } catch (Exception e) {
            return "Fail";
        }
    }

    @RequestMapping(value = "/join", method = RequestMethod.GET)
    public Object joinProject(@RequestParam("projectId") String projectId, @RequestParam("userId") String userId) {
        ProjectDaoImpl projectDao = new ProjectDaoImpl(mongoTemplate);
        try {
            return projectDao.joinProject(projectId, userId);
        } catch (Exception e) {
            return "Fail";
        }
    }

    @RequestMapping(value = "/quit", method = RequestMethod.GET)
    public Object quitProject(@RequestParam("projectId") String projectId, @RequestParam("userId") String userId) {
        ProjectDaoImpl projectDao = new ProjectDaoImpl(mongoTemplate);
        try {
            return projectDao.quitProject(projectId, userId);
        } catch (Exception e) {
            return "Fail";
        }
    }

    @RequestMapping(value = "/manager", method = RequestMethod.GET)
    public Object changeManager(@RequestParam("projectId") String projectId, @RequestParam("newManagerId") String userId) {
        ProjectDaoImpl projectDao = new ProjectDaoImpl(mongoTemplate);
        try {
            return projectDao.changeManager(projectId, userId);
        } catch (Exception e) {
            return "Fail";
        }
    }

    @RequestMapping(value = "/joinByMail", method = RequestMethod.GET)
    public String joinByMail(@RequestParam("projectId") String projectId, @RequestParam("email") String email, @RequestParam("password") String password) {
        ProjectDaoImpl projectDao = new ProjectDaoImpl(mongoTemplate);
        return projectDao.joinByMail(projectId, email, password);
    }

    @RequestMapping(value = "/applyByMail", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public String sendEmail(@RequestBody EmailEntity emailEntity) {
        ProjectDaoImpl projectDao = new ProjectDaoImpl(mongoTemplate);
        return projectDao.applyByEmail(emailEntity);
    }

    @RequestMapping(value = "/picture", method = RequestMethod.POST)
    public String uploadPicture(HttpServletRequest request) {
        ProjectDaoImpl projectDao = new ProjectDaoImpl(mongoTemplate);
        return projectDao.uploadPicture(request);
    }
}
