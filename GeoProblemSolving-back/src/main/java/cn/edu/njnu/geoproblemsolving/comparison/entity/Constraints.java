package cn.edu.njnu.geoproblemsolving.comparison.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 19:57 2019/7/8
 * @Modified By:
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Constraints {
    @Id
    private String id;
    private Date startTime;
    private Date endTime;
    private String location;
    private String scale;
    private String resolution;
    private String timeInterval;
    private List<String> modelsId; //todo 模型信息 此处不应该是 String 类型
    private List<String> requiredInputsId; //   必要输入数据
    private List<String> requiredOutputsId;//   必要输出数据
}
