package cn.edu.njnu.geoproblemsolving.Entity.Folder;

import cn.edu.njnu.geoproblemsolving.Entity.ResourceEntity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "Folder")
public class FolderEntity {
    private String folderId;
    private String folderName;
    private String parentId;
    private ArrayList<FolderItem> folders;
    private ArrayList<ResourceEntity> files;

    public ArrayList<FolderItem> getFolders() {
        return folders;
    }

    public ArrayList<ResourceEntity> getFiles() {
        return files;
    }

    public String getFolderName() {
        return folderName;
    }

    public String getFolderId() {
        return folderId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setFiles(ArrayList<ResourceEntity> files) {
        this.files = files;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public void setFolders(ArrayList<FolderItem> folders) {
        this.folders = folders;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
