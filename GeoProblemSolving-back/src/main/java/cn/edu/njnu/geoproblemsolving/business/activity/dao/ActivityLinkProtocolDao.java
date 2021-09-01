package cn.edu.njnu.geoproblemsolving.business.activity.dao;

import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.entity.ActivityLinkProtocol;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.query.Update;

public interface ActivityLinkProtocolDao {
    ActivityLinkProtocol saveProtocol(ActivityLinkProtocol protocol);

    ActivityLinkProtocol findProtocolById(String id);

    UpdateResult updateLinkProtocol(String id, Update update);

    DeleteResult delLinkProtocol(String id);
}
