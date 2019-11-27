package cn.edu.njnu.geoproblemsolving.ChangeDB;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@CrossOrigin(origins = "*",allowCredentials = "true")
@RestController
@RequestMapping("/changeDB")
public class UpdateDBController {

    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/createProjectFolder", method = RequestMethod.GET)
    public String createProjectFolder(){
        UpdateDBDao updateDBDao = new UpdateDBDao(mongoTemplate);
        return updateDBDao.createProjectFolder();
    }

    @RequestMapping(value = "/fileStrucToFolder", method = RequestMethod.GET)
    public String fileStrucToFolder(){
        UpdateDBDao updateDBDao = new UpdateDBDao(mongoTemplate);
        return updateDBDao.fileStrucToFolder();
    }

    @RequestMapping(value = "/folderAddScopeId", method = RequestMethod.GET)
    public String foldersAddScopeId(){
        UpdateDBDao updateDBDao = new UpdateDBDao(mongoTemplate);
        return updateDBDao.foldersAddScopeId();
    }

    @RequestMapping(value = "/moduleToStepTree", method = RequestMethod.GET)
    public String moduleToStepTree(){
        UpdateDBDao updateDBDao = new UpdateDBDao(mongoTemplate);
        return updateDBDao.moduleToStepTree();
    }
}
