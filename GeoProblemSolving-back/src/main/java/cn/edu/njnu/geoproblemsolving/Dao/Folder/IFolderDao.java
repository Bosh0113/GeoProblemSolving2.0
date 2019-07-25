package cn.edu.njnu.geoproblemsolving.Dao.Folder;

import javax.servlet.http.HttpServletRequest;

public interface IFolderDao {
    Object newFolder(String folderName, String parentId);
    Object inquireFolder(String folderId);
    Object renameFolder(String newName, String folderId, String parentId);
    Object removeFolder(String folderId, String parentId);
    Object uploadToFolder(HttpServletRequest request);
    Object shareToFolder(String addFileList, String folderId);
    Object renameFile(String newName, String fileId, String folderId);
    Object removeFile(String fileId, String folderId);
}