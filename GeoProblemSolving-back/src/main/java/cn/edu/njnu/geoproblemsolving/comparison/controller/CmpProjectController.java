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
            String projectId = cmpProjectDao.addProject(project);
            return ResultUtils.success(projectId);
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


    @RequestMapping(value = "/createSubproject", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public JsonResult createSubproject(@RequestBody CmpSubproject project){
        CmpSubprojectDaoImpl cmpSubprojectDao = new CmpSubprojectDaoImpl(mongoTemplate);
        try{
            String subprojectId = cmpSubprojectDao.addProject(project);
            //更新上级项目
            CmpProjectDaoImpl cmpProjectDao = new CmpProjectDaoImpl(mongoTemplate);
            List<CmpProject> projects = cmpProjectDao.getProjects("projectId", project.getProjectId());
            if(projects!=null){
                CmpProject parentProject = projects.get(0);
                List<String> subprojectIds = parentProject.getSubprojectIds();
                if(subprojectIds==null){
                    subprojectIds = new ArrayList<>();
                    subprojectIds.add(subprojectId);
                }else{
                    subprojectIds.add(subprojectId);
                }
                parentProject.setSubprojectIds(subprojectIds);
                cmpProjectDao.updateProject(parentProject);
            }
            return ResultUtils.success(subprojectId);
        } catch (Exception e) {
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }
}
