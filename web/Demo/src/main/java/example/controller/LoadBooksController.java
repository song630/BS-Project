package example.controller;
import example.CookieController;
import example.dao.PrivateBooksJDBCTemplate;
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
            // 添加正在学习的单词书的cookie
            cc.addCookie("studying", studying, "/", "localhost");
            System.out.print("getWordBooks, ");
            cc.showCookies();
            // ===== 以下为2018.6.30添加
            List<WordBook> list = bookTemp.listWordBooks();
            WordBook w = new WordBook();
            w.setTitle("Private");
            PrivateBooksJDBCTemplate pTemp = (PrivateBooksJDBCTemplate) context.getBean("privateBooksJDBCTemplate");
            w.setNum(pTemp.count(user));
            list.add(w); // 把自定义单词书加进去
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/get_book/{user}", method = RequestMethod.GET)
    public WordBook getBook(@PathVariable String user) { // 进入制定计划页面时调用
        System.out.println("getBook, user: " + user);
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("file:D://courses/3.2/BS/BS-Project/web/Demo/src/main/webapp/WEB-INF/applicationContext.xml");
            WordBookJDBCTemplate bookTemp = (WordBookJDBCTemplate) context.getBean("wordBookJDBCTemplate");
            UserJDBCTemplate userTemp = (UserJDBCTemplate) context.getBean("userJDBCTemplate");
            String title = userTemp.getStudying(user);
            if (title.equals("none")) // 没有正在学习的单词书
                return null;
            return bookTemp.getWordBook(title);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
