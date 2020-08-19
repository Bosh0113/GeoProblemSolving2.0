package cn.edu.njnu.geoproblemsolving.Entity.Resources;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Data
@Document(collection = "Folder")
public class FolderEntity {
    private String scopeId;
    private String folderId;
    private String folderName;
    private String parentId;
    private String creatorId;
    private ArrayList<FolderItem> folders;
    private ArrayList<ResourceEntity> files;
}
