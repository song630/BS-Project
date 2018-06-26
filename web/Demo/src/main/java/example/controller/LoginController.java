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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import example.CookieController;

@Controller
@RequestMapping("/Hello")
public class LoginController {
    @ResponseBody
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/submit_login", method = RequestMethod.GET)
    public Map<String, String> LoginUser(HttpServletRequest request, HttpServletResponse response, @RequestParam String obj) {
        if(request.getHeader("Origin").contains("localhost")) {
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        }
        response.setHeader("Access-Control-Allow-Credentials", "true");
        Map<String, String> resultMap = new HashMap<String, String>();
        CookieController cc = new CookieController(request, response);
        System.out.print("LoginUser, ");
        cc.showCookies();
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
                if (user.getPassword().equals(password)) { // 登录成功
                    resultMap.put("info", "success");
                    cc.addCookie("isLogin", "true", "/", "localhost");
                    cc.addCookie("username", username, "/", "localhost");
                    // 新的cookie已经add到了response中
                    System.out.print("LoginUser, ");
                    cc.showCookies();
                    return resultMap;
                } else {
                    resultMap.put("info", "wrong_pwd"); // 密码错误
                    return resultMap;
                }
            } catch (EmptyResultDataAccessException e) { // 用户不存在
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
