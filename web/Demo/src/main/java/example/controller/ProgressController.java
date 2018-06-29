package example.controller;
import example.dao.UserJDBCTemplate;
import example.dao.WordBookJDBCTemplate;
import example.pojo.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/Hello")
public class ProgressController {
    @ResponseBody
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/get_progress/{user}", method = RequestMethod.GET)
    public Map<String, String> getProgress(@PathVariable String user) {
        System.out.println("getProgress, user: " + user);
        Map<String, String> result = new HashMap<String, String>();
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("file:D://courses/3.2/BS/BS-Project/web/Demo/src/main/webapp/WEB-INF/applicationContext.xml");
            UserJDBCTemplate userTemp = (UserJDBCTemplate) context.getBean("userJDBCTemplate");
            String studying = userTemp.getStudying(user);
            int studied = userTemp.getStudied(user); // 当前单词书中已经学习的单词数
            WordBookJDBCTemplate bookTemp = (WordBookJDBCTemplate) context.getBean("wordBookJDBCTemplate");
            int total = bookTemp.getNum(studying); // 获取正在学习的单词书有多少单词
            int percent = studied * 100 / total;
            result.put("percent", percent + "");
            User u = userTemp.getUser(user);
            result.put("day1", u.getDay1() + "");
            result.put("day2", u.getDay2() + "");
            result.put("day3", u.getDay3() + "");
            result.put("day4", u.getDay4() + "");
            result.put("day5", u.getDay5() + "");
            result.put("day6", u.getDay6() + "");
            result.put("day7", u.getDay7() + "");
            result.put("info", "success");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("info", "error");
            return result;
        }
    }
}
