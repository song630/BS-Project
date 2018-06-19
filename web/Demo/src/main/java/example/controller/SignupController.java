package example.controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
// import example.pojo.User;
import example.dao.UserJDBCTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;

@Controller
public class SignupController {
    @ResponseBody
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "main/submit_signup", method = RequestMethod.GET)
    public String SignupUser(@RequestParam String obj) {
        try {
            Connection conn;
            String driver = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/bs?useSSL=false&serverTimezone=UTC";
            String user = "root";
            String pwd = "123456";
            try {
                Class.forName(driver);
                conn = DriverManager.getConnection(url,user,pwd);
                if(!conn.isClosed())
                    System.out.println("Succeeded connecting to the Database!");
                Statement statement = conn.createStatement();
                String sql = "select * from user";
                ResultSet rs = statement.executeQuery(sql);
                System.out.println("done!");
                rs.close();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("received: " + obj);
            JSONObject json = JSON.parseObject(obj);
            ApplicationContext context = new ClassPathXmlApplicationContext("file:D://courses/3.2/BS/BS-Project/web/Demo/src/main/webapp/WEB-INF/applicationContext.xml");
            UserJDBCTemplate temp = (UserJDBCTemplate) context.getBean("userJDBCTemplate");
            String username, password, email, phone, education;
            username = json.getString("username");
            password = json.getString("password");
            email = json.getString("email");
            phone = json.getString("phone");
            education = json.getString("education");
            temp.create(username, password, email, phone, education);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
