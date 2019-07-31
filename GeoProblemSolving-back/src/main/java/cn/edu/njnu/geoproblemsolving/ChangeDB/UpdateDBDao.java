package cn.edu.njnu.geoproblemsolving.ChangeDB;

import cn.edu.njnu.geoproblemsolving.Entity.Folder.FolderEntity;
import cn.edu.njnu.geoproblemsolving.Entity.Folder.FolderItem;
import cn.edu.njnu.geoproblemsolving.Entity.ProjectEntity;
import cn.edu.njnu.geoproblemsolving.Entity.ResourceEntity;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

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
        List<ProjectEntity> projects = mongoTemplate.findAll(ProjectEntity.class);
        for(ProjectEntity project:projects){
            String projectId = project.getProjectId();
            Query query = new Query(Criteria.where("scope.projectId").is(projectId));
            FolderEntity folderEntity = new FolderEntity();
            folderEntity.setParentId("");
            folderEntity.setFolderName(project.getTitle());
            folderEntity.setFolderId(projectId);
            folderEntity.setFolders(new ArrayList<>());
            List<ResourceEntity> resources = mongoTemplate.find(query,ResourceEntity.class);
            ArrayList<ResourceEntity> files = new ArrayList<>(resources);
            folderEntity.setFiles(files);
            mongoTemplate.save(folderEntity);
        }
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
}
