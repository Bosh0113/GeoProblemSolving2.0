package cn.edu.njnu.geoproblemsolving.Dao.Step;

import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
import cn.edu.njnu.geoproblemsolving.Entity.Folder.FolderEntity;
import cn.edu.njnu.geoproblemsolving.Entity.StepEntity;
import cn.edu.njnu.geoproblemsolving.Entity.SubProjectEntity;
import cn.edu.njnu.geoproblemsolving.Entity.ToolsetEntity;
import cn.edu.njnu.geoproblemsolving.domain.tool.ToolEntity;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;


@Component
public class StepDaoImpl implements IStepDao {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public StepDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public String createStep(StepEntity step) {

        // 基本信息+工具模块+特有内容
        String stepId = UUID.randomUUID().toString();
        step.setStepId(stepId);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        step.setCreateTime(dateFormat.format(date));
//        step.setToolList(new ArrayList<>());
//        step.setToolsetList(new ArrayList<>());
        mongoTemplate.save(step);

        // 资源
        FolderEntity folderEntity = new FolderEntity();
        folderEntity.setScopeId(stepId);
        folderEntity.setFolders(new ArrayList<>());
        folderEntity.setFiles(new ArrayList<>());
        folderEntity.setFolderName(step.getName());
        folderEntity.setParentId("");
        folderEntity.setFolderId(step.getStepId());
        mongoTemplate.save(folderEntity);

        return stepId;
    }

    public String createStepOfType(StepEntity step) {

        // 基本信息+工具模块+特有内容
        String stepId = UUID.randomUUID().toString();
        step.setStepId(stepId);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        step.setCreateTime(dateFormat.format(date));
        Query queryPublic = new Query(Criteria.where("privacy").is("Public"));
        List<ToolEntity> toolEntities = mongoTemplate.find(queryPublic, ToolEntity.class);
        ArrayList<String> toolStrs = new ArrayList<>();
        for (ToolEntity toolEntity:toolEntities){
            toolStrs.add(toolEntity.getTid());
        }
        step.setToolList(toolStrs);
        List<ToolsetEntity> toolsetEntities = mongoTemplate.find(queryPublic,ToolsetEntity.class);
        ArrayList<String> toolsetStrs = new ArrayList<>();
        for (ToolsetEntity toolsetEntity: toolsetEntities){
            toolsetStrs.add(toolsetEntity.getTsId());
        }
        step.setToolsetList(toolsetStrs);
        mongoTemplate.save(step);

        // 资源
        FolderEntity folderEntity = new FolderEntity();
        folderEntity.setScopeId(stepId);
        folderEntity.setFolders(new ArrayList<>());
        folderEntity.setFiles(new ArrayList<>());
        folderEntity.setFolderName(step.getName());
        folderEntity.setParentId("");
        folderEntity.setFolderId(step.getStepId());
        mongoTemplate.save(folderEntity);

        return stepId;
    }

    @Override
    public Object readStep(String key, String value) {
        Query query = Query.query(Criteria.where(key).is(value));
        if (mongoTemplate.find(query, StepEntity.class).isEmpty()) {
            return "None";
        } else {
            List<StepEntity> StepEntities = mongoTemplate.find(query, StepEntity.class);
            return StepEntities;
        }
    }

    @Override
    public void deleteStep(String key, String value) {
        Query query = Query.query(Criteria.where(key).is(value));
        mongoTemplate.remove(query, StepEntity.class);
    }

    @Override
    public String updateStep(HttpServletRequest request) {
        try {
            Query query = new Query(Criteria.where("stepId").is(request.getParameter("stepId")));
            CommonMethod method = new CommonMethod();
            Update update = method.setUpdate(request);
            StepEntity stepEntity = mongoTemplate.findOne(query,StepEntity.class);

            if(request.getParameter("toolList")!=null){
                String newToolList = request.getParameter("toolList");
                ArrayList<String> toolList =new ArrayList<>();
                Collections.addAll(toolList, newToolList.split(","));
                update.set("toolList",toolList);
            }
            if(request.getParameter("toolsetList")!=null){
                String newToolsetList = request.getParameter("toolsetList");
                ArrayList<String> toolsetList =new ArrayList<>();
                Collections.addAll(toolsetList, newToolsetList.split(","));
                update.set("toolsetList",toolsetList);
            }
            //更新子项目中有向图中的标签
            if(request.getParameter("name")!=null&&!stepEntity.getName().equals(request.getParameter("name"))){
                String newName = request.getParameter("name");
                String subProjectId = stepEntity.getSubProjectId();
                String projectId = stepEntity.getProjectId();
                String solvingProcess = "";
                Query query2 = new Query();
                String level = "";
                // project 下的 step
                if(subProjectId.equals("") && !projectId.equals("")){
                    level = "project";
                    query2 = new Query(Criteria.where("projectId").is(projectId));
                    ProjectEntity projectEntity = mongoTemplate.findOne(query2,ProjectEntity.class);
                    solvingProcess = projectEntity.getSolvingProcess();
                }
                // subproject 下的 step
                else if(!subProjectId.equals("") && projectId.equals("")){
                    level = "subproject";
                    query2 = new Query(Criteria.where("subProjectId").is(subProjectId));
                    SubProjectEntity subProjectEntity = mongoTemplate.findOne(query2,SubProjectEntity.class);
                    solvingProcess = subProjectEntity.getSolvingProcess();
                }
                JSONArray processEntities = JSONArray.parseArray(solvingProcess);
                int nodeId = 0;
                for(int i=0;i<processEntities.size();i++){
                    JSONObject processEntity =  processEntities.getJSONObject(i);
                    if(processEntity.get("stepID").equals(request.getParameter("stepId"))){
                        processEntity.put("name",newName);
                        processEntities.set(i,processEntity);
                        nodeId = Integer.valueOf(processEntity.get("id").toString());
                        break;
                    }
                }
                for (int i=0;i<processEntities.size();i++){
                    JSONObject processEntity = processEntities.getJSONObject(i);
                    JSONArray last = processEntity.getJSONArray("last");
                    JSONArray next = processEntity.getJSONArray("next");
                    for(int j=0;j<last.size();j++){
                        JSONObject lastNode = last.getJSONObject(j);
                        if(Integer.valueOf(lastNode.get("id").toString())==nodeId){
                            lastNode.put("name",newName);
                            last.set(j,lastNode);
                        }
                    }
                    for(int j=0;j<next.size();j++){
                        JSONObject nextNode = next.getJSONObject(j);
                        if(Integer.valueOf(nextNode.get("id").toString())==nodeId){
                            nextNode.put("name",newName);
                            next.set(j,nextNode);
                        }
                    }
                    processEntity.put("last",last);
                    processEntity.put("next",next);
                    processEntities.set(i,processEntity);
                }
                solvingProcess = processEntities.toString();

                if(level.equals("project")){
                    Update updateProject = new Update();
                    updateProject.set("solvingProcess",solvingProcess);
                    mongoTemplate.updateFirst(query2,updateProject,ProjectEntity.class);
                }
                else if(level.equals("subproject")) {
                    Update updateSubProject = new Update();
                    updateSubProject.set("solvingProcess", solvingProcess);
                    mongoTemplate.updateFirst(query2, updateSubProject, SubProjectEntity.class);
                }
            }

            mongoTemplate.updateFirst(query, update, StepEntity.class);
            return "Success";
        } catch (Exception e) {
            return "Fail";
        }
    }

}
