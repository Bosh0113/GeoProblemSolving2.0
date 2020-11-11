package cn.edu.njnu.geoproblemsolving.business.resource.entity;

import lombok.Data;

import java.util.ArrayList;

@Data
public class IUploadResult {
    public ArrayList<IResourceEntity> uploaded;
    public ArrayList<String> failed;
    public ArrayList<String> sizeOver;
}
