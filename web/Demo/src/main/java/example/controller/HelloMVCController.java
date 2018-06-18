package example.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import example.pojo.Person;

@Controller
@RequestMapping("/Hello")
public class HelloMVCController {
    @RequestMapping("/home")
    public String homeHandler(){
        return "home";
    }

    @RequestMapping("/test")
    public String testHandler(){
        return "test";
    }

    @CrossOrigin(origins="*", maxAge=3600)
    @RequestMapping(value="/getperson/{personID}", method=RequestMethod.GET)
    public @ResponseBody Person getPerson(@PathVariable int personID) {
        Person p = new Person();
        p.setName("Eric");
        p.setSex("male");
        p.setId(personID);
        return p;
    }
}
