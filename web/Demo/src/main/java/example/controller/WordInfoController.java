package example.controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import example.dao.WordEntryJDBCTemplate;
import example.pojo.WordEntry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/Hello")
public class WordInfoController {
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/get_wordInfo", method = RequestMethod.GET)
    public @ResponseBody WordEntry getWordInfo(@RequestParam String obj) {
        System.out.println("getWordInfo, received: " + obj);
        JSONObject json = JSON.parseObject(obj);
        String title = json.getString("title");
        int id = json.getIntValue("id");
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("file:D://courses/3.2/BS/BS-Project/web/Demo/src/main/webapp/WEB-INF/applicationContext.xml");
            WordEntryJDBCTemplate temp = (WordEntryJDBCTemplate) context.getBean("wordEntryJDBCTemplate");
            return temp.getWordEntry(id, title);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
