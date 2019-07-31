package cn.edu.njnu.geoproblemsolving.ChangeDB;
import com.alibaba.fastjson.JSONArray;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "SubProject")
public class OldSubProjectEntity {

    private String projectId;
    private String subProjectId;
    private String title;
    private String description;
    private String managerId;
    private String managerName;
    private JSONArray members;
    private String createTime;
    private String fileStruct;
    private String solvingProcess;

    public String getManagerName() {
        return managerName;
    }

    public String getDescription() {
        return description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public JSONArray getMembers() {
        return members;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getTitle() {
        return title;
    }

    public String getManagerId() {
        return managerId;
    }

    public String getSubProjectId() {
        return subProjectId;
    }

    public String getSolvingProcess() {
        return solvingProcess;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMembers(JSONArray members) {
        this.members = members;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public void setSubProjectId(String subProjectId) {
        this.subProjectId = subProjectId;
    }

    public String getFileStruct() {
        return fileStruct;
    }

    public void setFileStruct(String fileStruct) {
        this.fileStruct = fileStruct;
    }

    public void setSolvingProcess(String solvingProcess) {
        this.solvingProcess = solvingProcess;
    }
}
