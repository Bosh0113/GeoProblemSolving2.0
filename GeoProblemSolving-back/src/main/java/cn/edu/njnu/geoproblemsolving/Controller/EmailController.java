package cn.edu.njnu.geoproblemsolving.Controller;

import cn.edu.njnu.geoproblemsolving.Dao.Email.EmailDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.EmailEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*",allowCredentials = "true")
@RestController
@RequestMapping("/email")
public class EmailController {

    @RequestMapping(value = "/invite", produces = {"application/json;charset=UTF-8"},method = RequestMethod.POST)
    public String inviteEmail(@RequestBody EmailEntity emailEntity){
        EmailDaoImpl emailDao=new EmailDaoImpl();
        String emailContent=emailEntity.getMailContent();
        emailContent=emailContent+emailEntity.getRecipient();
        emailEntity.setMailContent(emailContent);
        return emailDao.sendEmail(emailEntity);
    }

    @RequestMapping(value = "/send", produces = {"application/json;charset=UTF-8"},method = RequestMethod.POST)
    public String sendEmail(@RequestBody EmailEntity emailEntity){
        EmailDaoImpl emailDao=new EmailDaoImpl();
        return emailDao.sendEmail(emailEntity);
    }
}
