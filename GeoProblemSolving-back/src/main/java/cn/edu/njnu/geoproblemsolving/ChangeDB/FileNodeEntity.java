package cn.edu.njnu.geoproblemsolving.ChangeDB;

public class FileNodeEntity {
    String name;
    String uid;

    public FileNodeEntity() {
    }

    public FileNodeEntity(String name, String uid) {
        this.name = name;
        this.uid = uid;
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

    @Override
    public String toString() {
        return "FileNode{" +
                "name='" + name + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
