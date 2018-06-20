package example.controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import example.dao.UserJDBCTemplate;
import example.pojo.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {
    @ResponseBody
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "main/submit_login", method = RequestMethod.GET)
    public Map<String, String> LoginUser(@RequestParam String obj) {
        Map<String, String> resultMap = new HashMap<String, String>();
        try {
            System.out.println("received: " + obj);
            JSONObject json = JSON.parseObject(obj);
            ApplicationContext context = new ClassPathXmlApplicationContext("file:D://courses/3.2/BS/BS-Project/web/Demo/src/main/webapp/WEB-INF/applicationContext.xml");
            UserJDBCTemplate temp = (UserJDBCTemplate) context.getBean("userJDBCTemplate");
            String username, password;
            username = json.getString("username");
            password = json.getString("password");
            try {
                User user = temp.getUser(username);
                if (user.getPassword().equals(password)) {
                    resultMap.put("info", "success");
                    return resultMap;
                } else {
                    resultMap.put("info", "wrong_pwd");
                    return resultMap;
                }
            } catch (EmptyResultDataAccessException e) {
                resultMap.put("info", "not_found");
                return resultMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("info", "error");
            return resultMap;
        }
    }
}
