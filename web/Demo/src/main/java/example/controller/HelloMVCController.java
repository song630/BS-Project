package example.controller;
import example.CookieController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import example.pojo.Person;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/Hello")
public class HelloMVCController {
    @RequestMapping("/home")
    public String homeHandler(){
        System.out.println("home");
        return "home";
    }

    @RequestMapping("/test")
    public String testHandler(){
        System.out.println("test");
        return "test";
    }

    @CrossOrigin(origins="*", maxAge=3600)
    @RequestMapping(value="/getperson/{personID}", method=RequestMethod.GET)
    public @ResponseBody Person getPerson(HttpServletRequest request, HttpServletResponse response, @PathVariable int personID) {
        if(request.getHeader("Origin").contains("localhost")) {
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        }
        response.setHeader("Access-Control-Allow-Credentials", "true");
        CookieController cc = new CookieController(request, response);
        cc.showCookies();
        Person p = new Person();
        p.setName("Eric");
        p.setSex("male");
        p.setId(personID);
        return p;
    }
}
