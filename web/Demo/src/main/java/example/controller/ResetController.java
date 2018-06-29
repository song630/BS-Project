package example.controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import example.dao.UserJDBCTemplate;
import example.pojo.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/Hello")
public class ResetController {
    @ResponseBody
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/submit_reset", method = RequestMethod.GET)
    public Map<String, String> ResetUser(@RequestParam String obj) {
        Map<String, String> resultMap = new HashMap<String, String>();
        try {
            System.out.println("ResetUser, received: " + obj);
            JSONObject json = JSON.parseObject(obj);
            ApplicationContext context = new ClassPathXmlApplicationContext("file:D://courses/3.2/BS/BS-Project/web/Demo/src/main/webapp/WEB-INF/applicationContext.xml");
            UserJDBCTemplate temp = (UserJDBCTemplate) context.getBean("userJDBCTemplate");
            String username, old_password, new_password, email, phone, education;
            username = json.getString("username");
            old_password = json.getString("old_pwd");
            new_password = json.getString("new_pwd");
            email = json.getString("email");
            phone = json.getString("phone");
            education = json.getString("education");
            User old_user = temp.getUser(username);
            if (!old_user.getPassword().equals(old_password)) { // 密码错误 不能更改
                resultMap.put("info", "wrong_pwd");
                return resultMap;
            }
            temp.delete(username); // 删除旧用户所有信息
            if (temp.isEmailExist(email)) { // 邮箱已经存在
                resultMap.put("info", "email_existed");
                return resultMap;
            }
            // ===== 2018.6.29更新: 加入日期操作
            Calendar now = Calendar.getInstance();
            String date = now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DAY_OF_MONTH);
            int plan = 20;
            temp.create(username, new_password, email, phone, education, plan, date);
            resultMap.put("info", "success");
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("info", "error");
            return resultMap;
        }
    }
}
