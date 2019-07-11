package cn.edu.njnu.geoproblemsolving.Dao.HistoryEvent;

import cn.edu.njnu.geoproblemsolving.Entity.HistoryEventEntity;

public interface IHistoryEventDao {

    String saveHistoryEvent(HistoryEventEntity historyEvent);

    Object inquiryHistoryEvent(String eventType, String key, String value);
}
