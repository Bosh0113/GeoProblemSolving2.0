package cn.edu.njnu.geoproblemsolving.domain.support;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName BaseEntity
 * @Description 结构化数据库的BaseEntity
 * @Author sun_liber
 * @Date 2019/7/9
 * @Version 1.0.0
 */
@Data
public class BaseEntity {
    @JsonFormat (pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    @JsonFormat (pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;


    @BeforeInsert
    public void beforeInsert(){
        Date date=new Date();
        if(createTime==null){
            createTime=date;
        }
        if(updateTime==null){
            updateTime=date;
        }
    }

    @BeforeUpdate
    public void beforeUpdate(){
        updateTime=new Date();
    }
}
