package example.service;
import example.pojo.MailModel;

public interface EmailService {
    public void emailManage(String toMail); // email配置

    public void sendEmail(MailModel mail); // 发送邮件
}
