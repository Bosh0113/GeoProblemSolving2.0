package cn.edu.njnu.geoproblemsolving.ChangeDB;

import java.util.ArrayList;

public class FileStructEntity {
    private String name;
    private String uid;
    private ArrayList<FileStructEntity> folders;
    private ArrayList<FileNodeEntity> files;

    public FileStructEntity() {
    }

    public FileStructEntity(String name, String uid, ArrayList<FileStructEntity> folders, ArrayList<FileNodeEntity> files) {
        this.name = name;
        this.uid = uid;
        this.folders = folders;
        this.files = files;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ArrayList<FileStructEntity> getFolders() {
        return folders;
    }

    public void setFolders(ArrayList<FileStructEntity> folders) {
        this.folders = folders;
    }

    public ArrayList<FileNodeEntity> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<FileNodeEntity> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", uid='" + uid + '\'' +
                ", folders=" + folders +
                ", files=" + files +
                '}';
    }
}
