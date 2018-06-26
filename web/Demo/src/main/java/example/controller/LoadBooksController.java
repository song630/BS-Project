package example.controller;
import example.CookieController;
import example.dao.UserJDBCTemplate;
import example.dao.WordBookJDBCTemplate;
import example.pojo.WordBook;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/Hello")
public class LoadBooksController { // ===== 重要 后期要加上自定义单词书 =====
    @ResponseBody
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/get_books/{user}", method = RequestMethod.GET)
    public List<WordBook> getWordBooks(HttpServletRequest request, HttpServletResponse response, @PathVariable String user) {
        if(request.getHeader("Origin").contains("localhost")) {
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        }
        response.setHeader("Access-Control-Allow-Credentials", "true");
        CookieController cc = new CookieController(request, response);
        System.out.println("getWordBooks, user: " + user);
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("file:D://courses/3.2/BS/BS-Project/web/Demo/src/main/webapp/WEB-INF/applicationContext.xml");
            UserJDBCTemplate userTemp = (UserJDBCTemplate) context.getBean("userJDBCTemplate");
            WordBookJDBCTemplate bookTemp = (WordBookJDBCTemplate) context.getBean("wordBookJDBCTemplate");
            String studying = userTemp.getStudying(user); // could be none
            cc.addCookie("studying", studying, "/", "localhost");
            System.out.print("getWordBooks, ");
            cc.showCookies();
            return bookTemp.listWordBooks();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/get_book/{title}", method = RequestMethod.GET)
    public WordBook getBook(@PathVariable String title) {
        System.out.println("getBook, title: " + title);
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("file:D://courses/3.2/BS/BS-Project/web/Demo/src/main/webapp/WEB-INF/applicationContext.xml");
            WordBookJDBCTemplate bookTemp = (WordBookJDBCTemplate) context.getBean("wordBookJDBCTemplate");
            return bookTemp.getWordBook(title);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
