package cn.edu.njnu.geoproblemsolving.business.modeltask.modelContainer;

import lombok.Data;
import org.json.JSONObject;

import java.util.Set;

/**
 * @ClassName InvokeInfo
 * @Description 记录工具调用相关信息
 * 将此内容绑定到 activity 上
 * 一切模型调用相关内容都同 activity 绑定
 * @Author zhngzhng
 * @Date 2021/4/12
 **/
@Data
public class InvokeInfo {
    //调用者id
    String invokeUserId;
    //上传的文件信息，包含 event&dataId 两个部分
    Set<JSONObject> uploadDataInfo;
}
