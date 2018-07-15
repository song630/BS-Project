package example.pojo;
import org.springframework.context.annotation.Configuration;
import java.util.Map;

@Configuration
public class MailModel {
    private String fromHost; // 发件人邮箱服务器
    private String fromEmail; // 发件人邮箱
    private String fromUsername; // 发件人用户名
    private String fromPwd; // 发件人密码
    private String toEmails; // 收件人邮箱 多个邮箱以;分隔
    private String subject; // 邮件主题
    private String content; // 邮件内容
    private Map<String, String> pictures; // 邮件中的图片 为空时无图片 map中的key为图片ID value为图片地址
    private Map<String, String> attachments; // 邮件中的附件 为空时无附件 map中的key为附件ID value为附件地址
    private String fromAddr; // 发送人地址
    private String toAddrs; // 接收人地址 可以为很多个 每个地址之间用;分隔
    private String[] attachFilenames; // 附件

    public void setFromHost(String fromHost) {
        this.fromHost = fromHost;
    }

    public String getFromHost() {
        return fromHost;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromPwd(String fromPwd) {
        this.fromPwd = fromPwd;
    }

    public String getFromPwd() {
        return fromPwd;
    }

    public void setToEmails(String toEmails) {
        this.toEmails = toEmails;
    }

    public String getToEmails() {
        return toEmails;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setPictures(Map<String, String> pictures) {
        this.pictures = pictures;
    }

    public Map<String, String> getPictures() {
        return pictures;
    }

    public void setAttachments(Map<String, String> attachments) {
        this.attachments = attachments;
    }

    public Map<String, String> getAttachments() {
        return attachments;
    }

    public void setFromAddr(String fromAddr) {
        this.fromAddr = fromAddr;
    }

    public String getFromAddr() {
        return fromAddr;
    }

    public void setToAddrs(String toAddrs) {
        this.toAddrs = toAddrs;
    }

    public String getToAddrs() {
        return toAddrs;
    }

    public void setAttachFilenames(String[] attachFilenames) {
        this.attachFilenames = attachFilenames;
    }

    public String[] getAttachFilenames() {
        return attachFilenames;
    }
}
