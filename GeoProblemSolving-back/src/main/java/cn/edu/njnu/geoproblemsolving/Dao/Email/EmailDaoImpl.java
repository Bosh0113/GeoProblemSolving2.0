package cn.edu.njnu.geoproblemsolving.Dao.Email;

import cn.edu.njnu.geoproblemsolving.Entity.EmailEntity;

import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class EmailDaoImpl implements IEmailDao{

    private static final long serialVersionUID = 1L;
    private static String myEmailAccount = "geoproblemsolving@163.com";
    //这里设置为授权码，而不是邮箱密码
    private static String myEmailPassword = "LYC123456";
    private static String myEmailSMTPHost = "smtp.163.com";

    @Override
    public String sendEmail(EmailEntity emailEntity){
        try {

            String recipient=emailEntity.getRecipient();//收件人
            String mailTitle=emailEntity.getMailTitle();//邮件标题
            String mailContent=emailEntity.getMailContent();// 邮件正文

            // 1、创建参数的配置，用于连接邮件服务器的参数配置
            Properties props=new Properties();
            props.setProperty("mail.transport.protocol", "smtp");  // 使用的协议，javaMail规范要求
            props.setProperty("mail.smtp.host", myEmailSMTPHost);  // 发件人邮箱的SMTP服务器的地址
            props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.socketFactory.port", "587");
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.auth", "true");           // 需要认证的请求

            // 2、根据配置创建会话，用于邮件和服务器交互
            Session session=Session.getInstance(props);
            session.setDebug(true);       // 设置为debug，可以查看详细的log

            try {
                // 3、创建一封邮件对象
                MimeMessage message=createMimeMessage(session,myEmailAccount,recipient,mailContent,mailTitle);

                // 4、 根据session 获取邮件传输对象
                Transport transport =session.getTransport();

                // 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
                //
                //    PS_01: 成败的判断关键在此一句, 如果连接服务器失败, 都会在控制台输出相应失败原因的 log,
                //           仔细查看失败原因, 有些邮箱服务器会返回错误码或查看错误类型的链接, 根据给出的错误
                //           类型到对应邮件服务器的帮助网站上查看具体失败原因。
                //
                //    PS_02: 连接失败的原因通常为以下几点, 仔细检查代码:
                //           (1) 邮箱没有开启 SMTP 服务;
                //           (2) 邮箱密码错误, 例如某些邮箱开启了独立密码;
                //           (3) 邮箱服务器要求必须要使用 SSL 安全连接;
                //           (4) 请求过于频繁或其他原因, 被邮件服务器拒绝服务;
                //           (5) 如果以上几点都确定无误, 到邮件服务器网站查找帮助。
                //
                //    PS_03: 仔细看log, 认真看log, 看懂log, 错误原因都在log已说明。

                transport.connect(myEmailAccount, myEmailPassword);

                // 6、 发送邮件，发到所有的收件地址 ，message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
                transport.sendMessage(message, message.getAllRecipients());

                // 7 关闭连接
                transport.close();

                return "Success";
            }catch (NoSuchProviderException e){
                e.printStackTrace();
                return "Fail";
            }
        } catch (Exception e){
            e.printStackTrace();
            return "Fail";
        }
    }

    private static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail,
                                                 String mailContent, String mailTitle) throws Exception{

        // 1  创建一封邮件
        MimeMessage message = new MimeMessage(session);

        // 2  发件人
        message.setFrom(new InternetAddress(sendMail,"OpenGMS","utf-8"));

        // 3  收件人，可以增加多个收件人，抄送，密送
        String[] recipients=receiveMail.split(",");
        for (String recipient : recipients) {
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipient,"Recipient","utf-8"));
        }
//        // 增加收件人
//        message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress("xuheng_z@126.com", "USER_DD", "UTF-8"));
//        // Cc: 抄送（可选）
//        message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress("1095201979@qq.com", "USER_EE", "UTF-8"));
//        // Bcc: 密送（可选）
//        message.setRecipient(MimeMessage.RecipientType.BCC, new InternetAddress("1432952955@qq.com", "USER_FF", "UTF-8"));

        // 4 Subject,邮件主题
        message.setSubject(mailTitle,"utf-8");

        // 5  邮件正文
        message.setContent(mailContent, "text/html;charset=UTF-8");

        // 6  设置邮件发送的时间
        message.setSentDate(new Date());

        // 7 保存设置
        message.saveChanges();
        return message;
    }
}
