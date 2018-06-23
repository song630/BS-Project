package example.controller;
import example.dao.UserJDBCTemplate;
import example.pojo.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/Hello")
public class UserInfoController {
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/get_userInfo/{user}", method = RequestMethod.GET)
    public @ResponseBody User getUserInfo(@PathVariable String user) {
        System.out.println("getUserInfo, user: " + user);
        User u = new User();
        u.setUsername(user);
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("file:D://courses/3.2/BS/BS-Project/web/Demo/src/main/webapp/WEB-INF/applicationContext.xml");
            UserJDBCTemplate temp = (UserJDBCTemplate) context.getBean("userJDBCTemplate");
            User res = temp.getUser(user);
            u.setPhone(res.getPhone());
            u.setEmail(res.getEmail());
            u.setEducation(res.getEducation());
            System.out.println("getUserInfo: done.");
            return u; // 把这个用户相关的信息都发回去
        } catch (Exception e) {
            e.printStackTrace();
            u.setUsername("null");
            return u;
        }
    }
}
