package example.controller;
import example.CookieController;
import example.dao.UserJDBCTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/Hello")
public class ChangeController {
    @ResponseBody
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/change_book/{newTitle}", method = RequestMethod.GET)
    public Map<String, String> changeStudying(HttpServletRequest request, HttpServletResponse response, @PathVariable String newTitle) {
        if(request.getHeader("Origin").contains("localhost")) {
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        }
        response.setHeader("Access-Control-Allow-Credentials", "true");
        CookieController cc = new CookieController(request, response);
        System.out.println("changeStudying, newTitle: " + newTitle);
        Map<String, String> resultMap = new HashMap<String, String>();
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("file:D://courses/3.2/BS/BS-Project/web/Demo/src/main/webapp/WEB-INF/applicationContext.xml");
            UserJDBCTemplate temp = (UserJDBCTemplate) context.getBean("userJDBCTemplate");
            // ===== 后期更改 清除旧单词书的一些信息 (2018.6.27已经更改) =====
            String oldTitle = cc.getValue("studying");
            if (oldTitle.equals(newTitle)) {
                resultMap.put("info", "error");
                return resultMap;
            }
            temp.setStudying(cc.getValue("username"), newTitle);
            cc.addCookie("studying", newTitle, "/", "localhost"); // 更改cookie
            resultMap.put("info", "success");
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("info", "error");
            return resultMap;
        }
    }
}
