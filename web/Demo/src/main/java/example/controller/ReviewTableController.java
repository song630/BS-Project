package example.controller;
import example.dao.DailyJDBCTemplate;
import example.dao.UserJDBCTemplate;
import example.dao.WordEntryJDBCTemplate;
import example.pojo.Daily;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/Hello")
public class ReviewTableController {
    public class TableEntry {
        private String word;
        private String poses;
        private String status;

        public void setWord(String word) { this.word = word; }

        public String getWord() { return word; }

        private void setPoses(String poses) { this.poses = poses; }

        public String getPoses() { return poses; }

        private void setStatus(String status) { this.status = status; }

        public String getStatus() { return status; }
    }

    @ResponseBody
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/get_daily/{user}", method = RequestMethod.GET)
    public List<TableEntry> getDaily(@PathVariable String user) {
        System.out.println("getDaily, user: " + user);
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("file:D://courses/3.2/BS/BS-Project/web/Demo/src/main/webapp/WEB-INF/applicationContext.xml");
            UserJDBCTemplate userTemp = (UserJDBCTemplate) context.getBean("userJDBCTemplate");
            DailyJDBCTemplate dailyTemp = (DailyJDBCTemplate) context.getBean("dailyJDBCTemplate");
            String studying = userTemp.getStudying(user);
            if (studying.equals("none")) { // 未选择单词书
                return null;
            }
            List<Daily> list = dailyTemp.getAll(user); // 取出daily中所有属于某用户的单词项
            List<TableEntry> result = new ArrayList<TableEntry>();
            WordEntryJDBCTemplate wordTemp = (WordEntryJDBCTemplate) context.getBean("wordEntryJDBCTemplate");
            for (Daily d : list) {
                String poses = wordTemp.getPoses(d.getId(), studying); // 获取所有释义
                if (poses.length() > 40) // ===== 释义的长度超过40时 截断
                    poses = poses.substring(0, 39) + "...";
                TableEntry e = new TableEntry();
                e.setWord(d.getWord());
                e.setPoses(poses);
                e.setStatus(d.getStatus());
                result.add(e);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
