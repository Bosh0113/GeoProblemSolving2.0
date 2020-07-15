package cn.edu.njnu.geoproblemsolving.Entity.Folder;

import cn.edu.njnu.geoproblemsolving.Entity.ResourceEntity;

import java.util.ArrayList;

public class UploadResult {
    public ArrayList<ResourceEntity> uploaded;
    public ArrayList<String> failed;
    public ArrayList<String> sizeOver;

    public ArrayList<ResourceEntity> getUploaded() {
        return uploaded;
    }

    public ArrayList<String> getFailed() {
        return failed;
    }

    public ArrayList<String> getSizeOver() {
        return sizeOver;
    }

    public void setFailed(ArrayList<String> failed) {
        this.failed = failed;
    }

    public void setSizeOver(ArrayList<String> sizeOver) {
        this.sizeOver = sizeOver;
    }

    public void setUploaded(ArrayList<ResourceEntity> uploaded) {
        this.uploaded = uploaded;
    }
}
