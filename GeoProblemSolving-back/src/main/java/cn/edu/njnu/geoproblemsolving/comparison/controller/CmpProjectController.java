package cn.edu.njnu.geoproblemsolving.comparison.controller;

import cn.edu.njnu.geoproblemsolving.comparison.bean.JsonResult;
import cn.edu.njnu.geoproblemsolving.comparison.dao.modelresource.ModelResourceDaoImpl;
import cn.edu.njnu.geoproblemsolving.comparison.dao.project.CmpProjectDaoImpl;
import cn.edu.njnu.geoproblemsolving.comparison.dao.solution.SolutionImpl;
import cn.edu.njnu.geoproblemsolving.comparison.dao.subproject.CmpSubprojectDaoImpl;
import cn.edu.njnu.geoproblemsolving.comparison.entity.CmpProject;
import cn.edu.njnu.geoproblemsolving.comparison.entity.CmpSolution;
import cn.edu.njnu.geoproblemsolving.comparison.entity.CmpSubproject;
import cn.edu.njnu.geoproblemsolving.comparison.entity.ModelResource;
import cn.edu.njnu.geoproblemsolving.comparison.enums.ProjectType;
import cn.edu.njnu.geoproblemsolving.comparison.enums.ResultEnum;
import cn.edu.njnu.geoproblemsolving.comparison.utils.ResultUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
        try{
            List<CmpProject> allProject = cmpProjectDao.getAllProject();
            return ResultUtils.success(allProject);
        }catch (Exception e){
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @RequestMapping(value = "/getSubprojectList", method = RequestMethod.GET)
    public JsonResult getSubprojectList(@RequestParam("projectId") String projectId){
        CmpProjectDaoImpl cmpProjectDao = new CmpProjectDaoImpl(mongoTemplate);
        try{
            CmpProject project = cmpProjectDao.findByProjectId(projectId);
            if(project.getSubprojects()!=null){
                List<CmpProject> projectList = cmpProjectDao.findByProjectIdList(project.getSubprojects());
                return ResultUtils.success(projectList);
            }else{
                return ResultUtils.error(ResultEnum.NO_OBJECT);
            }
        }catch (Exception e){
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @RequestMapping(value = "/getProject",method = RequestMethod.GET)
    public JsonResult getProject(@RequestParam("key") String key, @RequestParam("value") String value){
        CmpProjectDaoImpl cmpProjectDao = new CmpProjectDaoImpl(mongoTemplate);
        try{
            List<CmpProject> projects = cmpProjectDao.getProjects(key, value);
            return ResultUtils.success(projects);
        }catch (Exception e){
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @RequestMapping(value = "/getProjectAllInfo",method = RequestMethod.GET)
    public JsonResult getProjectAllInfo(@RequestParam("projectId") String projectId){
        CmpProjectDaoImpl cmpProjectDao = new CmpProjectDaoImpl(mongoTemplate);
        CmpProject project = cmpProjectDao.findByProjectId(projectId);

        ProjectType projectType = project.getProjectType();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("project",project);
        if(projectType.equals(ProjectType.COMPREHENSIVE)){
            // 如果是 comprehensive 类型，只查询子项目信息;
            List<String> subprojects = project.getSubprojects();
            List<CmpProject> subprojectList = cmpProjectDao.findByProjectIdList(subprojects);
            jsonObject.put("subproject",subprojectList);
        }else{

            //modelList
            List<String> modelIdList = project.getModelList();
            ModelResourceDaoImpl modelResourceDao = new ModelResourceDaoImpl(mongoTemplate);
            List<ModelResource> modelList = modelResourceDao.findModelByIdList(modelIdList);

            jsonObject.put("model",modelList);
        }
        return ResultUtils.success(jsonObject);
    }
}
