package cn.edu.njnu.geoproblemsolving.Dao.Notice;

import cn.edu.njnu.geoproblemsolving.Entity.NoticeEntity;

import javax.servlet.http.HttpServletRequest;

public interface INoticeDao {

    String saveNotice(NoticeEntity notice);

    Object inquiryNotice(String key,String value);

    String deleteNotice(String noticeId);

    String updateRead(String noticeId);

    String updateNotice(HttpServletRequest request);
}
