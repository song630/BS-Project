package example.controller;
import example.dao.UserJDBCTemplate;
import example.dao.WordBookJDBCTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/Hello")
public class StartController {
    @ResponseBody
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/start_info/{user}", method = RequestMethod.GET)
    public Map<String, String> startInfo(@PathVariable String user) {
        System.out.println("startInfo, user: " + user);
        Map<String, String> resultMap = new HashMap<String, String>();
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("file:D://courses/3.2/BS/BS-Project/web/Demo/src/main/webapp/WEB-INF/applicationContext.xml");
            UserJDBCTemplate userTemp = (UserJDBCTemplate) context.getBean("userJDBCTemplate");
            WordBookJDBCTemplate bookTemp = (WordBookJDBCTemplate) context.getBean("wordBookJDBCTemplate");
            String studying = userTemp.getStudying(user);
            if (studying.equals("none")) {
                resultMap.put("info", "no_book"); // 没有选择学习的单词书
                return resultMap;
            }
            int num = bookTemp.getNum(studying);
            int plan = userTemp.getPlan(user);
            resultMap.put("num", num + "");
            resultMap.put("plan", plan + "");
            resultMap.put("studying", studying);
            resultMap.put("info", "success");
            return resultMap; // 有4个属性 都是字符串的形式
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("info", "error");
            return resultMap;
        }
    }
}
