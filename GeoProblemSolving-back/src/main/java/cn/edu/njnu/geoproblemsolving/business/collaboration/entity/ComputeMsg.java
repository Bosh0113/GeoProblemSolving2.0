package cn.edu.njnu.geoproblemsolving.business.collaboration.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author mzy
 * @Date 2021/4
 * @Description 计算记录
 */
@Data
public class ComputeMsg {

    private String toolId;

    private String aid;

    private ArrayList<CollaborationUser> receivers;

    private String time;

    public void setReceivers(HashMap<String,CollaborationUser> users){
        receivers = new ArrayList<>();
        for(Map.Entry<String, CollaborationUser> receiver: users.entrySet()){
            receivers.add(receiver.getValue());
        }
    }

    public HashMap<String,CollaborationUser> getReceivers(){
        HashMap<String,CollaborationUser> users = new HashMap<>();
        for(CollaborationUser receiver: receivers){
            users.put(receiver.getUserId(), receiver);
        }
        return users;
    }

}
