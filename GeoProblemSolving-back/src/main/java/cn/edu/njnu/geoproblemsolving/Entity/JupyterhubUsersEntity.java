package cn.edu.njnu.geoproblemsolving.Entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "JupterhubUsers")
public class JupyterhubUsersEntity {

    private String projectId;
    private String jupyterUserId;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getJupyterUserId() {
        return jupyterUserId;
    }

    public void setJupyterUserId(String jupyterUserId) {
        this.jupyterUserId = jupyterUserId;
    }
}
