package cn.edu.njnu.geoproblemsolving.business.collaboration.compute.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @ClassName ToolTestData
 * @Description 测试数据
 * @Author zhngzhng
 * @Date 2021/5/13
 **/
@Data
@Document
public class ToolTestData {
    @Id
    String testDataId;
    String fileName;
    String url;
    String suffix;
    String stateId;
}
