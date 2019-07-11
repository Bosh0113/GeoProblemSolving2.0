package cn.edu.njnu.geoproblemsolving.Dao.Bulletin;

import cn.edu.njnu.geoproblemsolving.Entity.BulletinEntity;

import javax.servlet.http.HttpServletRequest;

public interface IBulletinDaoImpl {

    Object saveBulletin(BulletinEntity bulletinEntity);

    Object inquiryBulletin(String key, String value);

    Object updateBulletin(HttpServletRequest request);

    String deleteBulletin(String bulletinId);
}
