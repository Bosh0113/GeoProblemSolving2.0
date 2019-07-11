package cn.edu.njnu.geoproblemsolving.Dao.Folder;

import cn.edu.njnu.geoproblemsolving.Dao.Resource.ResourceDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.Folder.FolderEntity;
import cn.edu.njnu.geoproblemsolving.Entity.Folder.FolderItem;
import cn.edu.njnu.geoproblemsolving.Entity.Folder.UploadResult;
import cn.edu.njnu.geoproblemsolving.Entity.ResourceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.UUID;

@Component
public class FolderDaoImpl implements IFolderDao{

    private final MongoTemplate mongoTemplate;

    @Autowired
    public FolderDaoImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Object newFolder(String folderName, String parentId) {
        try {
            // decode
            parentId = decode(parentId);
            FolderItem newFolderInfo = new FolderItem();
            newFolderInfo.setName(folderName);
            newFolderInfo.setUid(UUID.randomUUID().toString());

            Query queryParent = new Query(Criteria.where("folderId").is(parentId));
            FolderEntity parentFolder = mongoTemplate.findOne(queryParent,FolderEntity.class);
            ArrayList<FolderItem> folderItems = parentFolder.getFolders();
            folderItems.add(newFolderInfo);
            Update updateParent = new Update();
            updateParent.set("folders",folderItems);
            mongoTemplate.updateFirst(queryParent,updateParent,FolderEntity.class);

            FolderEntity newFolder = new FolderEntity();
            newFolder.setFiles(new ArrayList<>());
            newFolder.setFolders(new ArrayList<>());
            newFolder.setFolderId(newFolderInfo.getUid());
            newFolder.setParentId(parentId);
            newFolder.setFolderName(folderName);
            mongoTemplate.save(newFolder);
            return newFolderInfo;
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object inquireFolder(String folderId) {
        try{
            folderId = decode(folderId);
            Query queryFolder = new Query(Criteria.where("folderId").is(folderId));
            return mongoTemplate.findOne(queryFolder,FolderEntity.class);
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object renameFolder(String newName, String folderId, String parentId) {
        try{
            parentId = decode(parentId);
            Query queryParent = new Query(Criteria.where("folderId").is(parentId));
            FolderEntity parentFolder = mongoTemplate.findOne(queryParent,FolderEntity.class);
            ArrayList<FolderItem> folders = parentFolder.getFolders();
            for (FolderItem folderItem : folders) {
                if (folderItem.getUid().equals(folderId)) {
                    folderItem.setName(newName);
                    break;
                }
            }
            Update updateParent = new Update();
            updateParent.set("folders",folders);
            mongoTemplate.updateFirst(queryParent,updateParent,FolderEntity.class);

            Query queryFolder = new Query(Criteria.where("folderId").is(folderId));
            Update updateName = new Update();
            updateName.set("folderName",newName);
            mongoTemplate.updateFirst(queryFolder,updateName,FolderEntity.class);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object removeFolder(String folderId, String parentId) {
        try{
            parentId = decode(parentId);
            Query queryParent = new Query(Criteria.where("folderId").is(parentId));
            FolderEntity parentFolder = mongoTemplate.findOne(queryParent,FolderEntity.class);
            ArrayList<FolderItem> folders = parentFolder.getFolders();
            for(int i=0;i<folders.size();i++){
                FolderItem folder = folders.get(i);
                if(folder.getUid().equals(folderId)){
                    folders.remove(i);
                    break;
                }
            }
            Update updateParent = new Update();
            updateParent.set("folders",folders);
            mongoTemplate.updateFirst(queryParent,updateParent,FolderEntity.class);

            removeFolderIteration(folderId);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object uploadToFolder(HttpServletRequest request) {
        try{
            ResourceDaoImpl resourceDao = new ResourceDaoImpl(mongoTemplate);
            UploadResult uploadResult = (UploadResult) resourceDao.saveResource(request);
            ArrayList<ResourceEntity> uploaded = uploadResult.getUploaded();

            String folderId = request.getParameter("folderId");
            folderId = decode(folderId);
            Query queryFolder = new Query(Criteria.where("folderId").is(folderId));
            FolderEntity folderEntity = mongoTemplate.findOne(queryFolder,FolderEntity.class);
            ArrayList<ResourceEntity> files = folderEntity.getFiles();
            files.addAll(uploaded);
            Update updateFolder = new Update();
            updateFolder.set("files",files);
            mongoTemplate.updateFirst(queryFolder,updateFolder,FolderEntity.class);
            return uploadResult;
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object shareToFolder(String resourceId, String folderId) {
        try{
            folderId = decode(folderId);
            Query queryResource = new Query(Criteria.where("resourceId").is(resourceId));
            ResourceEntity resourceEntity = mongoTemplate.findOne(queryResource,ResourceEntity.class);
            Query queryFolder = new Query(Criteria.where("folderId").is(folderId));
            FolderEntity folderEntity = mongoTemplate.findOne(queryFolder,FolderEntity.class);
            ArrayList<ResourceEntity> files = folderEntity.getFiles();
            files.add(resourceEntity);
            Update updateFolder = new Update();
            updateFolder.set("files",files);
            mongoTemplate.updateFirst(queryFolder,updateFolder,FolderEntity.class);
            return resourceEntity;
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object renameFile(String newName, String fileId, String folderId) {
        try{
            folderId = decode(folderId);
            Query queryFolder = new Query(Criteria.where("folderId").is(folderId));
            FolderEntity folderEntity = mongoTemplate.findOne(queryFolder,FolderEntity.class);
            ArrayList<ResourceEntity> files = folderEntity.getFiles();
            for (ResourceEntity file:files){
                if (file.getResourceId().equals(fileId)){
                    file.setName(newName);
                    break;
                }
            }
            Update updateFolder = new Update();
            updateFolder.set("files",files);
            mongoTemplate.updateFirst(queryFolder,updateFolder,FolderEntity.class);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object removeFile(String fileId,String folderId) {
        try{
            folderId = decode(folderId);
            Query queryFolder = new Query(Criteria.where("folderId").is(folderId));
            FolderEntity folderEntity = mongoTemplate.findOne(queryFolder,FolderEntity.class);
            ArrayList<ResourceEntity> files = folderEntity.getFiles();
            for (int i=0;i<files.size();i++){
                if(files.get(i).getResourceId().equals(fileId)){
                    files.remove(i);
                    break;
                }
            }

            Update updateFolder = new Update();
            updateFolder.set("files",files);
            mongoTemplate.updateFirst(queryFolder,updateFolder,FolderEntity.class);
            return null;
        }catch (Exception e){
            return null;
        }
    }

    private String decode(String id){
        return id;
    }

    private void removeFolderIteration(String folderId){
        try {
            Query queryFolder = new Query(Criteria.where("folderId").is(folderId));
            FolderEntity folderEntity = mongoTemplate.findOne(queryFolder,FolderEntity.class);

            ArrayList<FolderItem> folderItems = folderEntity.getFolders();
            for (FolderItem folderItem:folderItems){
                removeFolderIteration(folderItem.getUid());
            }
            mongoTemplate.remove(queryFolder,FolderEntity.class);
        }catch (Exception ignored){}
    }
}
