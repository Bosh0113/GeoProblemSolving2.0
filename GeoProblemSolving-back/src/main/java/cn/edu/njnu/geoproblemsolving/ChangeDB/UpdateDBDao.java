package cn.edu.njnu.geoproblemsolving.ChangeDB;

import cn.edu.njnu.geoproblemsolving.Entity.*;
import cn.edu.njnu.geoproblemsolving.Entity.Resources.FolderEntity;
import cn.edu.njnu.geoproblemsolving.Entity.Resources.FolderItem;
import cn.edu.njnu.geoproblemsolving.Entity.Resources.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.business.user.entity.User;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class UpdateDBDao {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public UpdateDBDao(MongoTemplate mongoTemplate){
        this.mongoTemplate=mongoTemplate;
    }

    public String createProjectFolder(){
//        List<ProjectEntity> projects = mongoTemplate.findAll(ProjectEntity.class);
//        for(ProjectEntity project:projects){
//            String projectId = project.getProjectId();
//            Query query = new Query(Criteria.where("scope.projectId").is(projectId));
//            FolderEntity folderEntity = new FolderEntity();
//            folderEntity.setParentId("");
//            folderEntity.setFolderName(project.getTitle());
//            folderEntity.setFolderId(projectId);
//            folderEntity.setFolders(new ArrayList<>());
//            List<ResourceEntity> resources = mongoTemplate.find(query,ResourceEntity.class);
//            ArrayList<ResourceEntity> files = new ArrayList<>(resources);
//            folderEntity.setFiles(files);
//            mongoTemplate.save(folderEntity);
//        }
        return "Success";
    }

    public String fileStrucToFolder(){
        List<OldSubProjectEntity> subProjects = mongoTemplate.findAll(OldSubProjectEntity.class);
        for(OldSubProjectEntity subProject : subProjects){
            try {
                String fileStruc = subProject.getFileStruct();
                if(fileStruc != null){
                    FileStructEntity fileStructEntity = JSONObject.parseObject(fileStruc,FileStructEntity.class);
                    transToFolder(fileStructEntity,"");
                }else {
                    FolderEntity folderEntity = new FolderEntity();
                    folderEntity.setFiles(new ArrayList<>());
                    folderEntity.setFolders(new ArrayList<>());
                    folderEntity.setFolderId(subProject.getSubProjectId());
                    folderEntity.setFolderName(subProject.getTitle());
                    folderEntity.setParentId("");
                    mongoTemplate.save(folderEntity);
                }
            }catch (Exception e){
                continue;
            }
        }
        return "Success";
    }

    private void transToFolder(FileStructEntity fileStruct, String parentId){
        String name = fileStruct.getName();
        String uid = fileStruct.getUid();
        ArrayList<FileStructEntity> folders = fileStruct.getFolders();
        ArrayList<FileNodeEntity> files = fileStruct.getFiles();

        FolderEntity folderEntity = new FolderEntity();
        folderEntity.setParentId(parentId);
        folderEntity.setFolderId(uid);
        folderEntity.setFolderName(name);

        ArrayList<FolderItem> ffolders = new ArrayList();
        for (FileStructEntity folder:folders){
            FolderItem folderItem = new FolderItem();
            folderItem.setName(folder.getName());
            folderItem.setUid(folder.getUid());
            ffolders.add(folderItem);
        }
        folderEntity.setFolders(ffolders);

        ArrayList<ResourceEntity> ffiles = new ArrayList<>();
        for(FileNodeEntity file:files){
            String resourceId = file.getUid();
            Query query = new Query(Criteria.where("resourceId").is(resourceId));
            ResourceEntity resourceEntity = mongoTemplate.findOne(query,ResourceEntity.class);
            ffiles.add(resourceEntity);
        }
        folderEntity.setFiles(ffiles);

        mongoTemplate.save(folderEntity);

        for (FileStructEntity folder:folders){
            transToFolder(folder,fileStruct.getUid());
        }
    }

    public String foldersAddScopeId(){
        try{
//            List<ProjectEntity> projectEntities = mongoTemplate.findAll(ProjectEntity.class);
//            List<SubProjectEntity> subProjectEntities = mongoTemplate.findAll(SubProjectEntity.class);
//            List<ModuleEntity> moduleEntities = mongoTemplate.findAll(ModuleEntity.class);
//            for (ProjectEntity projectEntity:projectEntities){
//                Query query = new Query(Criteria.where("folderId").is(projectEntity.getProjectId()));
//                FolderEntity folderEntity = mongoTemplate.findOne(query,FolderEntity.class);
//                folderAddScopeId(folderEntity,projectEntity.getProjectId());
//            }
//            for (SubProjectEntity subProjectEntity:subProjectEntities){
//                Query query = new Query(Criteria.where("folderId").is(subProjectEntity.getSubProjectId()));
//                FolderEntity folderEntity = mongoTemplate.findOne(query,FolderEntity.class);
//                folderAddScopeId(folderEntity,subProjectEntity.getSubProjectId());
//            }
//            for (ModuleEntity moduleEntity:moduleEntities){
//                Query query = new Query(Criteria.where("folderId").is(moduleEntity.getSubProjectId()));
//                FolderEntity folderEntity = mongoTemplate.findOne(query,FolderEntity.class);
//                folderAddScopeId(folderEntity,moduleEntity.getModuleId());
//            }
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    private void folderAddScopeId(FolderEntity folderEntity, String scopeId){
        Query query = new Query(Criteria.where("folderId").is(folderEntity.getFolderId()));
        Update update = new Update();
        update.set("scopeId",scopeId);
        mongoTemplate.updateFirst(query,update,FolderEntity.class);
        ArrayList<FolderItem> folderItems = folderEntity.getFolders();
        for(FolderItem folderItem : folderItems){
            Query query1 = new Query(Criteria.where("folderId").is(folderItem.getUid()));
            FolderEntity folderEntity1 = mongoTemplate.findOne(query1,FolderEntity.class);
            folderAddScopeId(folderEntity1,scopeId);
        }
    }

    public String moduleToStepTree(){
        try{
//            List<SubProjectEntity> subProjectEntities = mongoTemplate.findAll(SubProjectEntity.class);
//            for(SubProjectEntity subProject : subProjectEntities){
//                Query query = new Query(Criteria.where("subProjectId").is(subProject.getSubProjectId()));
//                List<ModuleEntity> moduleEntities = mongoTemplate.find(query,ModuleEntity.class);
//                if(moduleEntities.size()>0){
//                    ArrayList<StepNodeEntity> stepNodes = new ArrayList<>();
//                    for(int i=0;i<moduleEntities.size();i++){
//                        ModuleEntity moduleInfo = moduleEntities.get(i);
//                        StepEntity stepEntity = new StepEntity();
//                        stepEntity.setType(transStepType(moduleInfo.getType()));
//                        stepEntity.setName(moduleInfo.getTitle());
//                        stepEntity.setDescription(moduleInfo.getDescription());
//                        stepEntity.setSubProjectId(moduleInfo.getSubProjectId());
//                        stepEntity.setCreator(moduleInfo.getCreator());
//                        stepEntity.setContent(new JSONObject());
//
//                        StepNodeEntity stepNode = new StepNodeEntity();
//                        stepNode.setId(i);
//                        StepDaoImpl  stepDao = new StepDaoImpl(mongoTemplate);
//                        stepNode.setStepID(stepDao.createStep(stepEntity));
//                        stepNode.setName(stepEntity.getName());
//                        stepNode.setCategory(transStepCategory(stepEntity.getType()));
//                        ArrayList<StepListNodeEntity> last = new ArrayList<>();
//                        if (i!=0){
//                            StepListNodeEntity stepListNodeEntity = new StepListNodeEntity();
//                            stepListNodeEntity.setId(i-1);
//                            stepListNodeEntity.setName(moduleEntities.get(i-1).getTitle());
//                            last.add(stepListNodeEntity);
//                        }
//                        stepNode.setLast(last);
//                        ArrayList<StepListNodeEntity> next = new ArrayList<>();
//                        if(i!=moduleEntities.size()-1){
//                            StepListNodeEntity stepListNodeEntity = new StepListNodeEntity();
//                            stepListNodeEntity.setId(i+1);
//                            stepListNodeEntity.setName(moduleEntities.get(i+1).getTitle());
//                            next.add(stepListNodeEntity);
//                        }
//                        stepNode.setNext(next);
//                        if(i==0){
//                            stepNode.setX(0);
//                        }
//                        else {
//                            stepNode.setX(i*((float)800/(float)moduleEntities.size()));
//                        }
//                        stepNode.setY(200);
//                        stepNode.setLevel(i);
//                        if(i!=moduleEntities.size()-1){
//                            stepNode.setEnd(false);
//                            stepNode.setActiveStatus(false);
//                        }
//                        else {
//                            stepNode.setEnd(true);
//                            stepNode.setActiveStatus(true);
//                        }
//                        stepNodes.add(stepNode);
//                    }
//                    Update update = new Update();
//                    update.set("solvingProcess",JSONObject.toJSONString(stepNodes));
//                    mongoTemplate.updateFirst(query,update,SubProjectEntity.class);
//                }
//            }
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    private String transStepType(String oldType){
        switch (oldType){
            case "Preparation":
                return "Context definition & resource collection";
            case "Analysis":
                return "Data processing";
            case "Modeling":
                return "Modeling for geographic process";
            case "Simulation":
                return "Simulation/Prediction";
            case "Validation":
                return "Visualization & representation";
            case "Comparison":
                return "Model evaluation";
            default:
                return "Decision-making & management";
        }
    }

    private int transStepCategory(String type){
        switch (type){
            case "Context definition & resource collection":
                return 0;
            case "Data processing":
                return 1;
            case "Modeling for geographic process":
                return 2;
            case "Model evaluation":
                return 3;
            case "Quantitative and qualitative analysis":
                return 4;
            case "Simulation/Prediction":
                return 5;
            case "Visualization & representation":
                return 6;
            default:
                return 7;
        }
    }

    public String md5Password(){
        try{
            List<User> userEntities = mongoTemplate.findAll(User.class);
            for(User user :userEntities){
                String passWordMD5 = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
                Query query = new Query(Criteria.where("userId").is(user.getUserId()));
                Update update = new Update();
                update.set("password",passWordMD5);
                mongoTemplate.updateFirst(query,update, User.class);
            }
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    public String setPermission(){
        try{
//            List<ProjectEntity> projectEntities = mongoTemplate.findAll(ProjectEntity.class);
//            for(ProjectEntity projectEntity:projectEntities){
//
//                if(projectEntity.getPermissionManager() == null){
//
//                    String privacy = projectEntity.getPrivacy();
//
//                    // permission
//                    JSONObject permission = new JSONObject();
//                    String strPublicPermission = "{\"observe\":{\"project_manager\":null,\"subproject_manager\":null,\"member\":null,\"visitor\":\"At project level\"},\"auto_join\":{\"project_manager\":null,\"subproject_manager\":null,\"member\":null,\"visitor\":false},\"project_edit_info\":{\"project_manager\":true,\"subproject_manager\":null,\"member\":false,\"visitor\":null},\"project_invite_member\":{\"project_manager\":true,\"subproject_manager\":null,\"member\":true,\"visitor\":null},\"project_remove_member\":{\"project_manager\":true,\"subproject_manager\":null,\"member\":false,\"visitor\":null},\"project_task_create\":{\"project_manager\":true,\"subproject_manager\":null,\"member\":true,\"visitor\":null},\"project_task_manage\":{\"project_manager\":true,\"subproject_manager\":null,\"member\":\"Yes, partly\",\"visitor\":null},\"project_resource_manage\":{\"project_manager\":true,\"subproject_manager\":null,\"member\":\"Yes, partly\",\"visitor\":null},\"project_workspace_type_manage\":{\"project_manager\":true,\"subproject_manager\":null,\"member\":false,\"visitor\":null},\"subprojects_manage\":{\"project_manager\":true,\"subproject_manager\":null,\"member\":\"Yes, partly\",\"visitor\":null},\"subproject_edit_info\":{\"project_manager\":true,\"subproject_manager\":true,\"member\":false,\"visitor\":null},\"subproject_invite_member\":{\"project_manager\":true,\"subproject_manager\":true,\"member\":true,\"visitor\":null},\"subproject_remove_member\":{\"project_manager\":true,\"subproject_manager\":true,\"member\":false,\"visitor\":null},\"subproject_task_create\":{\"project_manager\":true,\"subproject_manager\":true,\"member\":true,\"visitor\":null},\"subproject_task_manage\":{\"project_manager\":true,\"subproject_manager\":true,\"member\":\"Yes, partly\",\"visitor\":null},\"subproject_resource_manage\":{\"project_manager\":\"Yes, partly\",\"subproject_manager\":true,\"member\":\"Yes, partly\",\"visitor\":null},\"subproject_workspace_type_manage\":{\"project_manager\":true,\"subproject_manager\":true,\"member\":false,\"visitor\":null},\"activity_manage\":{\"project_manager\":true,\"subproject_manager\":true,\"member\":true,\"visitor\":null},\"workspace_resource\":{\"project_manager\":\"Yes\",\"subproject_manager\":true,\"member\":\"Yes, partly\",\"visitor\":null},\"workspace_tool\":{\"project_manager\":true,\"subproject_manager\":true,\"member\":true,\"visitor\":null},\"workspace_edit\":{\"project_manager\":true,\"subproject_manager\":true,\"member\":true,\"visitor\":null}}";
//                    String strDiscoverablePermission = "{\"observe\":{\"project_manager\":null,\"subproject_manager\":null,\"member\":null,\"visitor\":\"No\"},\"auto_join\":{\"project_manager\":null,\"subproject_manager\":null,\"member\":null,\"visitor\":null},\"project_edit_info\":{\"project_manager\":true,\"subproject_manager\":null,\"member\":false,\"visitor\":null},\"project_invite_member\":{\"project_manager\":true,\"subproject_manager\":null,\"member\":false,\"visitor\":null},\"project_remove_member\":{\"project_manager\":true,\"subproject_manager\":null,\"member\":false,\"visitor\":null},\"project_task_create\":{\"project_manager\":true,\"subproject_manager\":null,\"member\":true,\"visitor\":null},\"project_task_manage\":{\"project_manager\":true,\"subproject_manager\":null,\"member\":\"Yes, partly\",\"visitor\":null},\"project_resource_manage\":{\"project_manager\":true,\"subproject_manager\":null,\"member\":\"Yes, partly\",\"visitor\":null},\"project_workspace_type_manage\":{\"project_manager\":true,\"subproject_manager\":null,\"member\":false,\"visitor\":null},\"subprojects_manage\":{\"project_manager\":true,\"subproject_manager\":null,\"member\":\"Yes, partly\",\"visitor\":null},\"subproject_edit_info\":{\"project_manager\":false,\"subproject_manager\":true,\"member\":false,\"visitor\":null},\"subproject_invite_member\":{\"project_manager\":false,\"subproject_manager\":true,\"member\":false,\"visitor\":null},\"subproject_remove_member\":{\"project_manager\":false,\"subproject_manager\":true,\"member\":false,\"visitor\":null},\"subproject_task_create\":{\"project_manager\":true,\"subproject_manager\":true,\"member\":true,\"visitor\":null},\"subproject_task_manage\":{\"project_manager\":\"Yes, partly\",\"subproject_manager\":true,\"member\":\"Yes, partly\",\"visitor\":null},\"subproject_resource_manage\":{\"project_manager\":\"Yes, partly\",\"subproject_manager\":true,\"member\":\"Yes, partly\",\"visitor\":null},\"subproject_workspace_type_manage\":{\"project_manager\":false,\"subproject_manager\":true,\"member\":false,\"visitor\":null},\"activity_manage\":{\"project_manager\":false,\"subproject_manager\":true,\"member\":false,\"visitor\":null},\"workspace_resource\":{\"project_manager\":\"Yes, partly\",\"subproject_manager\":true,\"member\":\"Yes, partly\",\"visitor\":null},\"workspace_tool\":{\"project_manager\":true,\"subproject_manager\":true,\"member\":true,\"visitor\":null},\"workspace_edit\":{\"project_manager\":false,\"subproject_manager\":true,\"member\":false,\"visitor\":null}}";
//                    String strPrivatePermission = "{\"observe\":{\"project_manager\":null,\"subproject_manager\":null,\"member\":null,\"visitor\":\"No\"},\"auto_join\":{\"project_manager\":null,\"subproject_manager\":null,\"member\":null,\"visitor\":null},\"project_edit_info\":{\"project_manager\":true,\"subproject_manager\":null,\"member\":false,\"visitor\":null},\"project_invite_member\":{\"project_manager\":true,\"subproject_manager\":null,\"member\":false,\"visitor\":null},\"project_remove_member\":{\"project_manager\":true,\"subproject_manager\":null,\"member\":false,\"visitor\":null},\"project_task_create\":{\"project_manager\":true,\"subproject_manager\":null,\"member\":true,\"visitor\":null},\"project_task_manage\":{\"project_manager\":true,\"subproject_manager\":null,\"member\":\"Yes, partly\",\"visitor\":null},\"project_resource_manage\":{\"project_manager\":true,\"subproject_manager\":null,\"member\":\"Yes, partly\",\"visitor\":null},\"project_workspace_type_manage\":{\"project_manager\":true,\"subproject_manager\":null,\"member\":false,\"visitor\":null},\"subprojects_manage\":{\"project_manager\":true,\"subproject_manager\":null,\"member\":\"Yes, partly\",\"visitor\":null},\"subproject_edit_info\":{\"project_manager\":false,\"subproject_manager\":true,\"member\":false,\"visitor\":null},\"subproject_invite_member\":{\"project_manager\":false,\"subproject_manager\":true,\"member\":false,\"visitor\":null},\"subproject_remove_member\":{\"project_manager\":false,\"subproject_manager\":true,\"member\":false,\"visitor\":null},\"subproject_task_create\":{\"project_manager\":true,\"subproject_manager\":true,\"member\":true,\"visitor\":null},\"subproject_task_manage\":{\"project_manager\":false,\"subproject_manager\":true,\"member\":\"Yes, partly\",\"visitor\":null},\"subproject_resource_manage\":{\"project_manager\":\"Yes, partly\",\"subproject_manager\":true,\"member\":\"Yes, partly\",\"visitor\":null},\"subproject_workspace_type_manage\":{\"project_manager\":true,\"subproject_manager\":true,\"member\":false,\"visitor\":null},\"activity_manage\":{\"project_manager\":false,\"subproject_manager\":true,\"member\":false,\"visitor\":null},\"workspace_resource\":{\"project_manager\":\"Yes, partly\",\"subproject_manager\":true,\"member\":\"Yes, partly\",\"visitor\":null},\"workspace_tool\":{\"project_manager\":true,\"subproject_manager\":true,\"member\":true,\"visitor\":null},\"workspace_edit\":{\"project_manager\":false,\"subproject_manager\":true,\"member\":false,\"visitor\":null}}";
//
//                    if(privacy.equals("Public")) {
//                        permission = (JSONObject) JSONObject.parse(strPublicPermission);
//                    } else if (privacy.equals("Discoverable")) {
//                        permission = (JSONObject) JSONObject.parse(strDiscoverablePermission);
//                    } else if (privacy.equals("Private")) {
//                        permission = (JSONObject) JSONObject.parse(strPrivatePermission);
//                    }
//
//                    Query query = new Query(Criteria.where("projectId").is(projectEntity.getProjectId()));
//                    Update update = new Update();
//                    update.set("permissionManager",permission);
//
//                    mongoTemplate.updateFirst(query,update,ProjectEntity.class);
//                }
//            }
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }
}
