package cn.edu.njnu.geoproblemsolving.Dao.Email;

import cn.edu.njnu.geoproblemsolving.Entity.EmailEntity;

public interface IEmailDao {
    String sendEmail(EmailEntity emailEntity);
}
