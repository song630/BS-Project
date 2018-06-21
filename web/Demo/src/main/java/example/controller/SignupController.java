package example.controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
// import example.pojo.User;
import example.dao.UserJDBCTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/Hello")
public class SignupController {
    @ResponseBody // 此批注是ajax获取返回值使用
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/submit_signup", method = RequestMethod.GET)
    public Map<String, String> SignupUser(@RequestParam String obj) {
        Map<String, String> resultMap = new HashMap<String, String>();
        try {
            System.out.println("received: " + obj);
            JSONObject json = JSON.parseObject(obj);
            ApplicationContext context = new ClassPathXmlApplicationContext("file:D://courses/3.2/BS/BS-Project/web/Demo/src/main/webapp/WEB-INF/applicationContext.xml");
            UserJDBCTemplate temp = (UserJDBCTemplate) context.getBean("userJDBCTemplate");
            String username, password, email, phone, education;
            username = json.getString("username");
            password = json.getString("password");
            email = json.getString("email");
            phone = json.getString("phone");
            education = json.getString("education");
            if (temp.isUsernameExist(username)) { // 用户名已经存在
                resultMap.put("info", "existed");
                return resultMap;
            }
            temp.create(username, password, email, phone, education);
            resultMap.put("info", "success");
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("info", "error");
            return resultMap;
        }
    }
}
