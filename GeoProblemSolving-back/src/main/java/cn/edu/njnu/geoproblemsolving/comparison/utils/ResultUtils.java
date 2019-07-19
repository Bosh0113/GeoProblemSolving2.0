package cn.edu.njnu.geoproblemsolving.comparison.utils;


import cn.edu.njnu.geoproblemsolving.comparison.bean.JsonResult;
import cn.edu.njnu.geoproblemsolving.comparison.enums.ResultEnum;

/**
 * @ClassName ResultUtils
 * @Description todo
 * @Author sun_liber
 * @Date 2018/11/28
 * @Version 1.0.0
 */
public class ResultUtils {
    public static JsonResult success() {
        return success(null);
    }

    public static JsonResult success(Object obj) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setMsg(ResultEnum.SUCCESS.getMsg());
        jsonResult.setCode(ResultEnum.SUCCESS.getCode());
        jsonResult.setData(obj);
        return jsonResult;
    }

    public static JsonResult error(ResultEnum resultEnum){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(resultEnum.getCode());
        jsonResult.setMsg(resultEnum.getMsg());
        jsonResult.setData(null);
        return jsonResult;
    }

    public static JsonResult error(Integer code, String msg) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(code);
        jsonResult.setMsg(msg);
        jsonResult.setData(null);
        return jsonResult;
    }
}
