package cn.edu.njnu.geoproblemsolving.comparison.enums;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 15:36 2019/7/30
 * @Modified By:
 **/
public enum ExtentsType {
    TEXT(1,"text"),
    ENVELOPE(2,"envelope"),
    POLYGON(3,"polygon");


    private int code;
    private String type;

    ExtentsType(int code, String type) {
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
