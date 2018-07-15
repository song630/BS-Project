package example.service;
import example.pojo.MailModel;
import org.apache.log4j.Logger;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
// 下面2个需要在pom.xml中引入spring-context-support后才能使用
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// 标注业务层组件
@Service
public class RealEmailService implements EmailService {
    private static Logger logger = Logger.getLogger(RealEmailService.class);
    private String attachFilePath = "d://";

    // @Resource默认按照名称方式进行bean匹配
    // @Autowired默认按照类型方式进行bean匹配
    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private SimpleMailMessage simpleMailMessage;

    @Override
    public void emailManage(String toMail) {
        MailModel mail = new MailModel();
        mail.setSubject("恢复登录"); // 邮件主题
        // 不设置附件
        mail.setAttachFilenames(null);
        mail.setToEmails(toMail); // 若同时发给多人 在sendMail函数中再处理
        String content = "<html><body>您好<br>" +
                "&nbsp&nbsp&nbsp&nbsp用户密码已经重置为123456<br>" +
                "&nbsp&nbsp&nbsp&nbsp现在可以登录<br>" +
                "</body></html>";
        mail.setContent(content);
        // ===== 要设置MailModel中的一些参数 =====
        sendEmail(mail);
    }

    @Override
    public void sendEmail(MailModel mail) {
        MimeMessage message = javaMailSender.createMimeMessage(); // 建立邮件消息
        MimeMessageHelper messageHelper;
        try {
            messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            // 设置发件人邮箱
            if (mail.getFromEmail() != null) {
                messageHelper.setFrom(mail.getFromEmail());
            } else { // 用默认的
                messageHelper.setFrom(simpleMailMessage.getFrom());
            }

            // 设置收件人邮箱
            if (mail.getToEmails() != null) {
                String[] toEmailArray = mail.getToEmails().split(";");
                List<String> toEmailList = new ArrayList<String>();
                if (toEmailArray.length <= 0) {
                    throw new Exception("收件人邮箱不得为空");
                } else {
                    for (String s : toEmailArray) {
                        if (s != null && !s.equals("")) {
                            toEmailList.add(s);
                        }
                    }
                    if (toEmailList.size() <= 0) {
                        throw new Exception("收件人邮箱不得为空");
                    } else {
                        toEmailArray = new String[toEmailList.size()];
                        for (int i = 0; i < toEmailList.size(); i++) {
                            toEmailArray[i] = toEmailList.get(i);
                        }
                    }
                }
                messageHelper.setTo(toEmailArray);
            } else { // 用默认的
                messageHelper.setTo(simpleMailMessage.getTo());
            }

            // 邮件主题
            if (mail.getSubject() != null) {
                messageHelper.setSubject(mail.getSubject());
            } else { // 用默认的
                messageHelper.setSubject(simpleMailMessage.getSubject());
            }
            // === 不添加图片和附件
            // true表示启动HTML格式的邮件
            messageHelper.setText(mail.getContent(), true);
            messageHelper.setSentDate(new Date());
            // 发送邮件
            javaMailSender.send(message);
            logger.info("-----------发送邮件完成----------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
