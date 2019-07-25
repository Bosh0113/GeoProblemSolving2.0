package cn.edu.njnu.geoproblemsolving.comparison.controller;

import cn.edu.njnu.geoproblemsolving.comparison.bean.JsonResult;
import cn.edu.njnu.geoproblemsolving.comparison.dao.project.CmpProjectDaoImpl;
import cn.edu.njnu.geoproblemsolving.comparison.dao.subproject.CmpSubprojectDaoImpl;
import cn.edu.njnu.geoproblemsolving.comparison.entity.CmpProject;
import cn.edu.njnu.geoproblemsolving.comparison.entity.CmpSubproject;
import cn.edu.njnu.geoproblemsolving.comparison.enums.ResultEnum;
import cn.edu.njnu.geoproblemsolving.comparison.utils.ResultUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 11:28 2019/7/10
 * @Modified By:
 **/
@CrossOrigin
@RestController
@RequestMapping("/cmp_project")
public class CmpProjectController {
    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/createProject", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public JsonResult createProject(@RequestBody CmpProject project) {
        CmpProjectDaoImpl cmpProjectDao = new CmpProjectDaoImpl(mongoTemplate);
        try {
            CmpProject newProject = cmpProjectDao.addProject(project);
            return ResultUtils.success(newProject);
        } catch (Exception e) {
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @RequestMapping(value="/getAllProjects", method = RequestMethod.GET)
    public JsonResult getProjectList(){
        CmpProjectDaoImpl cmpProjectDao = new CmpProjectDaoImpl(mongoTemplate);
        List<CmpProject> allProject = cmpProjectDao.getAllProject();
        return ResultUtils.success(allProject);
    }

    @RequestMapping(value = "/getProject",method = RequestMethod.GET)
    public JsonResult getProject(@RequestParam("key") String key, @RequestParam("value") String value){
        CmpProjectDaoImpl cmpProjectDao = new CmpProjectDaoImpl(mongoTemplate);
        List<CmpProject> projects = cmpProjectDao.getProjects(key, value);
        return ResultUtils.success(projects);
    }
}
