package cn.edu.njnu.geoproblemsolving.Dao.MessageRecords;

import cn.edu.njnu.geoproblemsolving.Entity.MessageRecordsEntity;

public interface IMessageRecordsDao {

    String saveMessageRecords(MessageRecordsEntity messageRecords);

    Object inquiryMessageRecords(String type, String key, String value);
}
