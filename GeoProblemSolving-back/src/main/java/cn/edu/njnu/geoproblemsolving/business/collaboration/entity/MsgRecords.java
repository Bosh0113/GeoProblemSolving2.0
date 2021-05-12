package cn.edu.njnu.geoproblemsolving.business.collaboration.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Data
@Document(collection = "MsgRecords")
public class MsgRecords {

    @Id
    private String messageId;

    private String aid; //查询条件Id

    private ArrayList<ChatMsg> records;

    private String createdTime;
}
