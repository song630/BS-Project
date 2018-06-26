package example.controller;
import example.CookieController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/Hello")
public class PlanController {
    @ResponseBody
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/submit_plan/{numWords}", method = RequestMethod.GET)
    public Map<String, String> submitPlan(HttpServletRequest request, HttpServletResponse response, @PathVariable int numWords) { // 还需要user参数
        if(request.getHeader("Origin").contains("localhost")) {
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        }
        response.setHeader("Access-Control-Allow-Credentials", "true");
        CookieController cc = new CookieController(request, response);
        Map<String, String> resultMap = new HashMap<String, String>();
        String user = cc.getValue("username");
        try {
            // ===== 补上操作 =====
            resultMap.put("info", "success");
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("info", "error");
            return resultMap;
        }
    }
}
