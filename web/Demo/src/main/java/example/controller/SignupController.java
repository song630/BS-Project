package example.controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
// import example.pojo.User;
import example.dao.UserJDBCTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Calendar;
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
            System.out.println("SignupUser, received: " + obj);
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
                resultMap.put("info", "user_existed");
                return resultMap;
            }
            if (temp.isEmailExist(email)) { // 邮箱已存在
                resultMap.put("info", "email_existed");
                return resultMap;
            }
            // ===== 2018.6.28更改: 注册后更新最后一次登录的日期
            // ===== 2018.6.29更新: 更新日期的操作合并到create中
            Calendar now = Calendar.getInstance();
            String date = now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DAY_OF_MONTH);
            int plan = 20;
            temp.create(username, password, email, phone, education, plan, date);
            resultMap.put("info", "success");
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("info", "error");
            return resultMap;
        }
    }
}
