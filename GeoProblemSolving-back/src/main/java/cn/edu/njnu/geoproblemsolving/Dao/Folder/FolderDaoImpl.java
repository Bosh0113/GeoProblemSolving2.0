package cn.edu.njnu.geoproblemsolving.Dao.Folder;

import cn.edu.njnu.geoproblemsolving.Dao.Resource.ResourceDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.Folder.FolderEntity;
import cn.edu.njnu.geoproblemsolving.Entity.Folder.FolderItem;
import cn.edu.njnu.geoproblemsolving.Entity.Folder.UploadResult;
import cn.edu.njnu.geoproblemsolving.Entity.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.Entity.UserEntity;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public Object shareToFolder(String addFileList, String folderId) {
        try{
            folderId = decode(folderId);
            Query queryFolder = new Query(Criteria.where("folderId").is(folderId));
            FolderEntity folderEntity = mongoTemplate.findOne(queryFolder,FolderEntity.class);
            ArrayList<ResourceEntity> files = folderEntity.getFiles();
            String[] addFileIds = addFileList.split(",");
            for(String addFileId : addFileIds){
                Query queryResource = new Query(Criteria.where("resourceId").is(addFileId));
                ResourceEntity resourceEntity = mongoTemplate.findOne(queryResource,ResourceEntity.class);
                files.add(resourceEntity);
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

    @Override
    public String copyFileToPersonalCenter(String resourceId, String userId,String privacy,String type,String name,String description){
        try {
            Query queryFile = new Query(Criteria.where("resourceId").is(resourceId));
            ResourceEntity resourceFile = mongoTemplate.findOne(queryFile,ResourceEntity.class);
            String fileUrl = resourceFile.getPathURL();
            String regex = "GeoProblemSolving(\\S*)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(fileUrl);
            String filePath = "";
            while (matcher.find()){
                filePath = matcher.group(1);
            }
            String localPath = System.getProperty("user.dir")+"/src/main/webapp"+filePath;
            String fileLocalPath = localPath.replaceAll("\\\\","/");
            File oldFile = new File(fileLocalPath);
            if(oldFile.exists()){
                String fileNames = resourceFile.getName();
                String fileName = fileNames.substring(0, fileNames.lastIndexOf("."));
                String suffix = fileNames.substring(fileNames.lastIndexOf(".") + 1);
                String newFilePath = System.getProperty("user.dir")+"/src/main/webapp/resource/"+userId;
                String newFileLocalPath = newFilePath.replaceAll("\\\\","/");
                File folder = new File(newFileLocalPath);
                if(!folder.exists()){
                    folder.mkdirs();
                }

                int randomNum = (int) (Math.random() * 10 + 1);
                for (int i = 0; i < 5; i++) {
                    randomNum = randomNum * 10 + (int) (Math.random() * 10 + 1);
                }
                String newFileName=fileName + randomNum + "." + suffix;
                String fileSavePath = newFilePath+"/"+newFileName;
                File newFile = new File(fileSavePath);

                FileUtils.copyFile(oldFile,newFile);//优化点，异步执行

                String newFileUrl = "/GeoProblemSolving/resource/"+userId+"/"+newFileName;
                resourceFile.setResourceId(UUID.randomUUID().toString());
                resourceFile.setUploaderId(userId);
                resourceFile.setPathURL(newFileUrl);
                resourceFile.setPrivacy(privacy);
                resourceFile.setType(type);
                resourceFile.setName(name);
                resourceFile.setDescription(description);
                Query queryUser = new Query(Criteria.where("userId").is(userId));
                UserEntity userEntity = mongoTemplate.findOne(queryUser, UserEntity.class);
                resourceFile.setUploaderName(userEntity.getUserName());
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String uploadTime = dateFormat.format(date);
                resourceFile.setUploadTime(uploadTime);
                mongoTemplate.save(resourceFile);
                return "Success";
            }
            else {
                return "None";
            }
        }catch (Exception e){
            return "Fail";
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
