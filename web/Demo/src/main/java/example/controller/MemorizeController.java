package example.controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import example.dao.UserJDBCTemplate;
import example.dao.WordEntryJDBCTemplate;
import example.pojo.WordEntry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/Hello")
public class MemorizeController {
    @ResponseBody
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/memorize", method = RequestMethod.GET)
    public Map<String, String> memorizeWord(@RequestParam String obj) {
        System.out.println("memorizeWord, obj: " + obj);
        Map<String, String> result = new HashMap<String, String>();
        JSONObject json = JSON.parseObject(obj);
        try {
            // 先取出4项信息:
            String user = json.getString("user");
            String studying = json.getString("studying");
            int num = json.getIntValue("num");
            int indexOfTotal = json.getIntValue("indexOfTotal"); // 当前的单词是这20个中的第几个(0-19)

            ApplicationContext context = new ClassPathXmlApplicationContext("file:D://courses/3.2/BS/BS-Project/web/Demo/src/main/webapp/WEB-INF/applicationContext.xml");
            UserJDBCTemplate userTemp = (UserJDBCTemplate) context.getBean("userJDBCTemplate");
            WordEntryJDBCTemplate wordTemp = (WordEntryJDBCTemplate) context.getBean("wordEntryJDBCTemplate");
            int plan = userTemp.getPlan(user);
            int studied = userTemp.getStudied(user);
            int totalNum = (studied + plan > num) ? (num - studied) : plan; // 实际在这个list中要遍历显示的单词数量
            if (indexOfTotal == totalNum) {  // 已经完成了这个list的学习
                userTemp.updateStudied(user, totalNum); // 记录学习进度
                result.put("info", "end");
                return result;
            }
            WordEntry entry = wordTemp.getWordEntry(studied + indexOfTotal, studying);
            result.put("totalNum", totalNum + "");
            result.put("studying", studying);
            result.put("word", entry.getWord());
            result.put("phonetic", entry.getPhonetic());
            result.put("poses", entry.getPoses());
            result.put("info", "success");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("info", "error");
            return result;
        }
    }
}
