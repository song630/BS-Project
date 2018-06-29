package example.controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import example.CookieController;
import example.dao.DailyJDBCTemplate;
import example.dao.UserJDBCTemplate;
import example.dao.WordEntryJDBCTemplate;
import example.pojo.Daily;
import example.pojo.WordEntry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

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
            result.put("id", entry.getId() + "");
            result.put("info", "success");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("info", "error");
            return result;
        }
    }

    @ResponseBody
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/submit_list", method = RequestMethod.GET)
    public Map<String, String> submitList(HttpServletRequest request, HttpServletResponse response, @RequestParam String obj) {
        if(request.getHeader("Origin").contains("localhost")) {
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        }
        response.setHeader("Access-Control-Allow-Credentials", "true");
        CookieController cc = new CookieController(request, response);
        Map<String, String> resultMap = new HashMap<String, String>();
        String user = cc.getValue("username");
        System.out.println("submitList, user: " + user);
        System.out.println("submitList, obj: " + obj);
        try {
            JSONObject json = JSON.parseObject(obj);
            JSONArray yes = json.getJSONArray("yes"), no = json.getJSONArray("no");
            List<Daily> yesList = new ArrayList<Daily>(), noList = new ArrayList<Daily>();
            drawList(yes, yesList);
            drawList(no, noList);
            int total = yes.size() + no.size(); // 学习的单词的数量
            // 当前日期:
            Calendar now = Calendar.getInstance();
            String date = now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DAY_OF_MONTH);

            ApplicationContext context = new ClassPathXmlApplicationContext("file:D://courses/3.2/BS/BS-Project/web/Demo/src/main/webapp/WEB-INF/applicationContext.xml");
            UserJDBCTemplate userTemp = (UserJDBCTemplate) context.getBean("userJDBCTemplate");
            String lastTime = userTemp.getLastDate(user); // 获取最后登录日期
            if (!date.equals(lastTime)) { // 最后一次登录的日期不是当前日期
                DailyJDBCTemplate dailyTemp = (DailyJDBCTemplate) context.getBean("dailyJDBCTemplate");
                dailyTemp.batchDelete(user); // 删除这个用户的所有单词项
                dailyTemp.batchCreate(yesList);
                dailyTemp.batchCreate(noList);
            } else { // 否则直接加入新的单词清单
                DailyJDBCTemplate dailyTemp = (DailyJDBCTemplate) context.getBean("dailyJDBCTemplate");
                dailyTemp.batchCreate(yesList);
                dailyTemp.batchCreate(noList);
            }
            String[] date1 = lastTime.split("-");
            String[] date2 = date.split("-");
            int day1 = Integer.parseInt(date1[2]);
            int day2 = Integer.parseInt(date2[2]);
            if (!date1[0].equals(date2[0]) || !date1[1].equals(date2[1]) || day2 - day1 >= 7) {
                // 直接清空day1-day7
                userTemp.clearDays(user);
            } else if (day1 != day2) {
                // 整体向左平移 同时更新day7
                userTemp.leftShift(user, day2 - day1, total);
            } else { // day1 == day2
                // 更新day7
                userTemp.updateDay7(user, total);
                System.out.println("submitList, day7 updated.");
            }
            userTemp.updateDate(user, date); // 更新最后登录的日期
            // 更新用户的day1-day7:
            resultMap.put("info", "success");
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("info", "error");
            return resultMap;
        }
    }

    private void drawList(JSONArray no, List<Daily> noList) {
        for (int i = 0; i < no.size(); i++) {
            JSONObject entry = no.getJSONObject(i);
            Daily d = new Daily();
            d.setUsername(entry.getString("username"));
            d.setStatus(entry.getString("status"));
            d.setWord(entry.getString("word"));
            d.setId(entry.getIntValue("id"));
            noList.add(d);
        }
    }
}
