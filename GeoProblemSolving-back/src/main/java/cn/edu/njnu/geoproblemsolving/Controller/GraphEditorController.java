package cn.edu.njnu.geoproblemsolving.Controller;

import cn.edu.njnu.geoproblemsolving.Dao.GraphEditor.GraphEditorDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.GraphEditorEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*",allowCredentials = "true")
@RestController
@RequestMapping("/graphEditor")
public class GraphEditorController {

    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/save", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public Object saveTask(@RequestBody GraphEditorEntity graphEditorEntity) {
        GraphEditorDaoImpl graphEditorDao = new GraphEditorDaoImpl(mongoTemplate);
        return graphEditorDao.saveTask(graphEditorEntity);
    }

    @RequestMapping(value = "/inquiry", method = RequestMethod.GET)
    public Object inquiryTask(@RequestParam("scopeId") String scopeId) {
        GraphEditorDaoImpl graphEditorDao = new GraphEditorDaoImpl(mongoTemplate);
        return graphEditorDao.loadTaskList(scopeId);
    }

    @RequestMapping(value = "/update", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public Object updateTask(HttpServletRequest request) {
        GraphEditorDaoImpl graphEditorDao = new GraphEditorDaoImpl(mongoTemplate);
        return graphEditorDao.updateTask(request);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteBulletin(@RequestParam("taskId") String taskId) {
        GraphEditorDaoImpl graphEditorDao = new GraphEditorDaoImpl(mongoTemplate);
        return graphEditorDao.deleteTask(taskId);
    }
}
