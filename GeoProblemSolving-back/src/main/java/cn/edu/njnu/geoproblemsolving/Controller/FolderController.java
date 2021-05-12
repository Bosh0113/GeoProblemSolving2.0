package cn.edu.njnu.geoproblemsolving.Controller;

import cn.edu.njnu.geoproblemsolving.Dao.Folder.FolderDaoImpl;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*",allowCredentials = "true")
@RestController
@RequestMapping("/folder")
public class FolderController {

    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/new", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public Object createFolder(@RequestParam("folderName") String folderName, @RequestParam("parentId") String parentId, @RequestParam("scopeId") String scopeId){
        FolderDaoImpl folderDao = new FolderDaoImpl(mongoTemplate);
        return folderDao.createFolder(folderName,parentId,scopeId);
    }

    @RequestMapping(value = "/inquiry", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.GET)
    public Object inquiryFolder(@RequestParam("folderId") String folderId){
        FolderDaoImpl folderDao = new FolderDaoImpl(mongoTemplate);
        return folderDao.inquireFolder(folderId);
    }

    @RequestMapping(value = "/renameFolder", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.GET)
    public Object renameFolder(@RequestParam("newName") String newName, @RequestParam("folderId") String folderId, @RequestParam("parentId") String parentId){
        FolderDaoImpl folderDao = new FolderDaoImpl(mongoTemplate);
        return folderDao.renameFolder(newName,folderId,parentId);
    }

    @RequestMapping(value = "/removeFolder", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.GET)
    public Object removeFolder(@RequestParam("folderId") String folderId, @RequestParam("parentId") String parentId){
        FolderDaoImpl folderDao = new FolderDaoImpl(mongoTemplate);
        return folderDao.removeFolder(folderId,parentId);
    }

    @RequestMapping(value = "/uploadToFolder", method = RequestMethod.POST)
    public Object uploadToFolder(HttpServletRequest request){
        FolderDaoImpl folderDao = new FolderDaoImpl(mongoTemplate);
        return folderDao.uploadToFolder(request);
    }

    @RequestMapping(value = "/shareToFolder", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.GET)
    public Object shareToFolder(@RequestParam("addFileList") String addFileList, @RequestParam("folderId") String folderId){
        FolderDaoImpl folderDao = new FolderDaoImpl(mongoTemplate);
        return folderDao.shareToFolder(addFileList,folderId);
    }

    @RequestMapping(value = "/editFile", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.GET)
    public Object renameFile(@RequestParam("fileId") String fileId,
                             @RequestParam("folderId") String folderId,
                             @RequestParam("type") String type,
                             @RequestParam("name") String name,
                             @RequestParam("description") String description){
        FolderDaoImpl folderDao = new FolderDaoImpl(mongoTemplate);
        return folderDao.editFile(fileId,folderId,type,name,description);
    }

    @RequestMapping(value = "/removeFile", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.GET)
    public Object removeFile(@RequestParam("fileId") String fileId, @RequestParam("folderId") String folderId){
        FolderDaoImpl folderDao = new FolderDaoImpl(mongoTemplate);
        return folderDao.removeFile(fileId,folderId);
    }

    @RequestMapping(value = "/copyToCenter",method = RequestMethod.GET)
    public String copyToCenter(@RequestParam("resourceId") String resourceId,
                               @RequestParam("userId") String userId,
                               @RequestParam("privacy") String privacy,
                               @RequestParam("type") String type,
                               @RequestParam("name") String name,
                               @RequestParam("description") String description){
        FolderDaoImpl folderDao = new FolderDaoImpl(mongoTemplate);
        return folderDao.copyFileToPersonalCenter(resourceId,userId,privacy,type,name,description);
    }

    @RequestMapping(value = "/findByFileType", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.GET)
    public Object findByFileType(@RequestParam("scopeId") String scopeId, @RequestParam("type") String type){
        FolderDaoImpl folderDao = new FolderDaoImpl(mongoTemplate);
        return folderDao.findByFileType(scopeId,type);
    }
}
