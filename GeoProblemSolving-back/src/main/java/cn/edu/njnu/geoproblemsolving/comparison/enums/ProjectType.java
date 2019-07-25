package cn.edu.njnu.geoproblemsolving.comparison.enums;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 20:37 2019/7/24
 * @Modified By:
 **/
public enum ProjectType {
    COMPREHENSIVE(1,"comprehensive"),
    SPECIFIC(2,"specific");


    private int code;
    private String type;

    ProjectType(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }
}
