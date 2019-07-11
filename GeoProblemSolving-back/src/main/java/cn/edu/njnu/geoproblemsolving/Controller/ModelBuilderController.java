package cn.edu.njnu.geoproblemsolving.Controller;

import cn.edu.njnu.geoproblemsolving.Dao.ModelBuilder.*;
import cn.edu.njnu.geoproblemsolving.Entity.ComputationalTaskEntity;
import cn.edu.njnu.geoproblemsolving.Entity.ConceptualTaskEntity;
import cn.edu.njnu.geoproblemsolving.Entity.LogicalTaskEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@CrossOrigin(origins = "*",allowCredentials = "true")
@RestController
@RequestMapping("/modelBuilder")
public class ModelBuilderController {

    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/conceptual/save", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public String saveConceptualTask(ConceptualTaskEntity conceptualTask){
        ConceptualTaskDaoImpl conceptualTaskDao=new ConceptualTaskDaoImpl(mongoTemplate);
        Date date=new Date();
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        conceptualTask.setTaskId(UUID.randomUUID().toString());
        conceptualTask.setCreateTime(dateFormat.format(date));
        return conceptualTaskDao.saveConceptualTask(conceptualTask);
    }

    @RequestMapping(value = "/conceptual/read",method = RequestMethod.POST)
    public Object readConceptualTask(@RequestParam("collaborativeId") String collaborativeId){
        ConceptualTaskDaoImpl conceptualTaskDao=new ConceptualTaskDaoImpl(mongoTemplate);
        return conceptualTaskDao.readConceptualTask(collaborativeId);
    }

    @RequestMapping(value = "/conceptual/delete",method = RequestMethod.POST)
    public String deleteConceptualTask(@RequestParam("taskId") String taskId){
        ConceptualTaskDaoImpl conceptualTaskDao=new ConceptualTaskDaoImpl(mongoTemplate);
        return conceptualTaskDao.deleteConceptualTask(taskId);
    }

    @RequestMapping(value = "/conceptual/update", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public String updateConceptualTask(HttpServletRequest request){
        ConceptualTaskDaoImpl conceptualTaskDao=new ConceptualTaskDaoImpl(mongoTemplate);
        return conceptualTaskDao.updateConceptualTask(request);
    }

    @RequestMapping(value = "/logical/save", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public String saveLogicalTask(LogicalTaskEntity logicalTask){
        LogicalTaskDaoImpl logicalTaskDao=new LogicalTaskDaoImpl(mongoTemplate);
        Date date=new Date();
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        logicalTask.setTaskId(UUID.randomUUID().toString());
        logicalTask.setCreateTime(dateFormat.format(date));
        return logicalTaskDao.saveLogicalTask(logicalTask);
    }

    @RequestMapping(value = "/logical/read",method = RequestMethod.POST)
    public Object readLogicalTask(@RequestParam("collaborativeId") String collaborativeId){
        LogicalTaskDaoImpl logicalTaskDao=new LogicalTaskDaoImpl(mongoTemplate);
        return logicalTaskDao.readLogicalTask(collaborativeId);
    }

    @RequestMapping(value = "/logical/delete",method = RequestMethod.POST)
    public String deleteLogicalTask(@RequestParam("taskId") String taskId){
        LogicalTaskDaoImpl logicalTaskDao=new LogicalTaskDaoImpl(mongoTemplate);
        return logicalTaskDao.deleteLogicalTask(taskId);
    }

    @RequestMapping(value = "/logical/update", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public String updateLogicalTask(HttpServletRequest request){
        LogicalTaskDaoImpl logicalTaskDao=new LogicalTaskDaoImpl(mongoTemplate);
        return logicalTaskDao.updateLogicalTask(request);
    }

    @RequestMapping(value = "/computational/save", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public String saveComputationalTask(ComputationalTaskEntity computationalTask){
        ComputationalTaskDaoImpl computationalTaskDao=new ComputationalTaskDaoImpl(mongoTemplate);
        Date date=new Date();
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        computationalTask.setTaskId(UUID.randomUUID().toString());
        computationalTask.setCreateTime(dateFormat.format(date));
        return computationalTaskDao.saveComputationalTask(computationalTask);
    }

    @RequestMapping(value = "/computational/read",method = RequestMethod.POST)
    public Object readComputationalTask(@RequestParam("collaborativeId") String collaborativeId){
        ComputationalTaskDaoImpl computationalTaskDao=new ComputationalTaskDaoImpl(mongoTemplate);
        return computationalTaskDao.readComputationalTask(collaborativeId);
    }

    @RequestMapping(value = "/computational/delete",method = RequestMethod.POST)
    public String deleteComputationalTask(@RequestParam("taskId") String taskId){
        ComputationalTaskDaoImpl computationalTaskDao=new ComputationalTaskDaoImpl(mongoTemplate);
        return computationalTaskDao.deleteComputationTask(taskId);
    }

    @RequestMapping(value = "/computational/update", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public String updateComputationalTask(HttpServletRequest request){
        ComputationalTaskDaoImpl computationalTaskDao=new ComputationalTaskDaoImpl(mongoTemplate);
        return computationalTaskDao.updateComputationalTask(request);
    }
}
