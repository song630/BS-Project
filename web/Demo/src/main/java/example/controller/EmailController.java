package example.controller;
import example.dao.UserJDBCTemplate;
import example.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/Hello")
public class EmailController {
    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    @Resource
    private EmailService emailService;

    @ResponseBody
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/send_email/{user}", method = RequestMethod.GET)
    public Map<String, String> sendEmail(@PathVariable String user) {
        Map<String, String> result = new HashMap<String, String>();
        System.out.println("sendEmail(), user: " + user);
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("file:D://courses/3.2/BS/BS-Project/web/Demo/src/main/webapp/WEB-INF/applicationContext.xml");
            UserJDBCTemplate temp = (UserJDBCTemplate) context.getBean("userJDBCTemplate");
            String toEmail = temp.getEmail(user);
            logger.info("----- SEND EMAIL: START -----");
            emailService.emailManage(toEmail);
            // 会发生AuthenticationFailedException
            // 解决方法 在邮箱中打开设置 开启POP3/SMTP/IMAP服务
            logger.info("----- SEND EMAIL: END -----");
            result.put("info", "success");
            // reset password to 123456:
            temp.resetPassword(user, "123456");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("info", "error");
            return result;
        }
    }
}
