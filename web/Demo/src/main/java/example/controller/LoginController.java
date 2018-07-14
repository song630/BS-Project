package example.controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import example.RSAUtil;
import example.dao.UserJDBCTemplate;
import example.pojo.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import example.CookieController;

@Controller
@RequestMapping("/Hello")
public class LoginController {
    // === 2018.7.14修改
    // 第一个函数是修改加密功能之前的 实际使用后两个
    /*
    @ResponseBody
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/submit_login", method = RequestMethod.GET)
    public Map<String, String> LoginUser(HttpServletRequest request, HttpServletResponse response, @RequestParam String obj) {
        if(request.getHeader("Origin").contains("localhost")) {
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        }
        response.setHeader("Access-Control-Allow-Credentials", "true");
        Map<String, String> resultMap = new HashMap<String, String>();
        CookieController cc = new CookieController(request, response);
        System.out.print("LoginUser, ");
        cc.showCookies();
        try {
            System.out.println("received: " + obj);
            JSONObject json = JSON.parseObject(obj);
            ApplicationContext context = new ClassPathXmlApplicationContext("file:D://courses/3.2/BS/BS-Project/web/Demo/src/main/webapp/WEB-INF/applicationContext.xml");
            UserJDBCTemplate temp = (UserJDBCTemplate) context.getBean("userJDBCTemplate");
            String username, password;
            username = json.getString("username");
            password = json.getString("password");
            try {
                User user = temp.getUser(username);
                if (user.getPassword().equals(password)) { // 登录成功
                    resultMap.put("info", "success");
                    cc.addCookie("isLogin", "true", "/", "localhost");
                    cc.addCookie("username", username, "/", "localhost");
                    // 新的cookie已经add到了response中
                    System.out.print("LoginUser, ");
                    cc.showCookies();
                    return resultMap;
                } else {
                    resultMap.put("info", "wrong_pwd"); // 密码错误
                    return resultMap;
                }
            } catch (EmptyResultDataAccessException e) { // 用户不存在
                resultMap.put("info", "not_found");
                return resultMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("info", "error");
            return resultMap;
        }
    }
    */

    @ResponseBody
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/login_request", method = RequestMethod.GET)
    public Map<String, String> loginRequest(HttpServletRequest request) {
        Map<String, String> result = new HashMap<String, String>();
        try {
            System.out.println("in loginRequest()");
            KeyPair kp = RSAUtil.generateKeyPair(); // 生成公钥私钥
            // 分离公钥私钥 公钥发给前端 私钥存在服务端的session中
            RSAPublicKey pub_key = (RSAPublicKey) kp.getPublic(); // generate public key
            RSAPrivateKey pri_key = (RSAPrivateKey) kp.getPrivate(); // generate private key
            String publicKeyExponent = pub_key.getPublicExponent().toString(16); // hexadecimal
            String publicKeyModulus = pub_key.getModulus().toString(16); // hexadecimal
            request.getSession().setAttribute("pri_key", pri_key);
            result.put("pub_exp", publicKeyExponent);
            result.put("pub_mod", publicKeyModulus);
            result.put("info", "success");
            return result; // 公钥的两个参数送回前端
        } catch (Exception e) {
            e.printStackTrace();
            result.put("info", "error");
            return result;
        }
    }

    @ResponseBody
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/login_check", method = RequestMethod.POST)
    public Map<String, String> loginCheck(HttpServletRequest request, HttpServletResponse response, @RequestParam String obj) {
        if(request.getHeader("Origin").contains("localhost")) {
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        }
        response.setHeader("Access-Control-Allow-Credentials", "true");
        Map<String, String> result = new HashMap<String, String>();
        CookieController cc = new CookieController(request, response);
        System.out.println("in loginCheck(), obj = " + obj);
        try {
            JSONObject json = JSON.parseObject(obj);
            ApplicationContext context = new ClassPathXmlApplicationContext("file:D://courses/3.2/BS/BS-Project/web/Demo/src/main/webapp/WEB-INF/applicationContext.xml");
            UserJDBCTemplate temp = (UserJDBCTemplate) context.getBean("userJDBCTemplate");
            String username, encrypted_pwd;
            username = json.getString("username");
            encrypted_pwd = json.getString("encrypted"); // 加密过的密码 要用私钥解密

            RSAPrivateKey pri_key = (RSAPrivateKey)request.getSession().getAttribute("pri_key");
            // byte[] byte_encrypted = new BigInteger(encrypted_pwd, 16).toByteArray();
            // 用上面删掉的一行RSAdecrypt函数会报错Bad arguments
            // 解决方法见网址 https://blog.csdn.net/greenqingqingws/article/details/51745571
            byte[] byte_encrypted = LoginController.hexStringToBytes(encrypted_pwd);
            StringBuilder pwd = new StringBuilder();
            pwd.append(new String(RSAUtil.decrypt(pri_key, byte_encrypted))).reverse();
            String real_pwd = pwd.toString(); // 获得用私钥解密后的实际密码
            System.out.println("real password: " + real_pwd);

            try {
                User user = temp.getUser(username);
                if (user.getPassword().equals(real_pwd)) { // 登录成功
                    result.put("info", "success");
                    cc.addCookie("isLogin", "true", "/", "localhost");
                    cc.addCookie("username", username, "/", "localhost");
                    // 新的cookie已经add到了response中
                    System.out.print("LoginUser, ");
                    cc.showCookies();
                    return result;
                } else {
                    result.put("info", "wrong_pwd"); // 密码错误
                    return result;
                }
            } catch (EmptyResultDataAccessException e) { // 用户不存在
                result.put("info", "not_found");
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("info", "error");
            return result;
        }
    }

    private static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
