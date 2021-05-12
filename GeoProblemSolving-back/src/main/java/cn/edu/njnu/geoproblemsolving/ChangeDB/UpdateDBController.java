//package cn.edu.njnu.geoproblemsolving.ChangeDB;
//
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//
//@CrossOrigin(origins = "*",allowCredentials = "true")
//@RestController
//@RequestMapping("/mapping")
//public class UpdateDBController {
//
//    @Resource
//    private MongoTemplate mongoTemplate;
//
//    @RequestMapping(value = "/project", method = RequestMethod.GET)
//    public String createProjectFolder(){
//        UpdateDBDao updateDBDao = new UpdateDBDao(mongoTemplate);
//        return updateDBDao.projectUpdate();
//    }
//
//    @RequestMapping(value = "/subproject", method = RequestMethod.GET)
//    public String fileStrucToFolder(){
//        UpdateDBDao updateDBDao = new UpdateDBDao(mongoTemplate);
//        return updateDBDao.subprojectUpdate();
//    }
//
//    @RequestMapping(value = "/activity", method = RequestMethod.GET)
//    public String foldersAddScopeId(){
//        UpdateDBDao updateDBDao = new UpdateDBDao(mongoTemplate);
//        return updateDBDao.activityUpdate();
//    }
//
//    @RequestMapping(value = "/user", method = RequestMethod.GET)
//    public String moduleToStepTree(){
//        UpdateDBDao updateDBDao = new UpdateDBDao(mongoTemplate);
//        return updateDBDao.userUpdate();
//    }
//}
