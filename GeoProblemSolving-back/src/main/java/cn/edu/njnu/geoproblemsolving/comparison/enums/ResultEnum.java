package cn.edu.njnu.geoproblemsolving.comparison.enums;

/**
 * 自定义的异常code，请勿覆盖常见的HttpStatus.*.value()
 */
public enum ResultEnum {
    SUCCESS(0, "成功"),
    FAILED(-16,"network problem"),

    DEFAULT_EXCEPTION(-1, "默认的服务器内部异常，我并不想进行处理！！"),

    NO_OBJECT(-2, "Not Found"),
    EXIST_OBJECT(-3, "同名对象已存在，请更换名称"),

    NO_TOKEN(-4, "Missing Token"),
    TOKEN_NOT_MATCH(-5, "Token not match"),
    TOKEN_WRONG(-6, "Token Wrong"),
    USER_PASSWORD_NOT_MATCH(-7, "账户名和密码不匹配"),

    UPLOAD_TYPE_ERROR(-8, "上传Type存在问题"),
    TRANSFER_UDX_ERROR(-9, "UDX转换时，支持的文件类型仅仅有json与xml"),
    NOTSUPPORT_GEOSERVER_ERROR(-10, "Geoserver服务发布不支持当前类型"),
    NOTSUPPORT_GETMETA_ERROR(-11, "获取meta不支持当前类型"),
    REMOTE_SERVICE_ERROR(-12, "远程服务调用出错"),
    FILE_NOT_FOUND(-13,"file not found"),
    FILE_TRANSLATE_FAILED(-14,"Failed to translate file"),
    NO_GDAL_LIB(-15,"No gdal lib"),
    DEPLOY_MODEL_ERROR(-16,"Failed to deploy model."),
    NO_COMPUTABLE_MODEL(-17,"No computable model"),
    FAILED_TO_GET_INFO(-18,"Failed to get info"),
    FAILED_TO_UPLOAD_DATA(-19,"Failed to upload data"),
    FAILED_TO_INVOKE_MODEL(-20,"Failed to invoke model");

    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
