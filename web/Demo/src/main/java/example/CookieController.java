package example;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieController {
    private HttpServletRequest request;
    private HttpServletResponse response;

    public CookieController(HttpServletRequest req, HttpServletResponse resp) {
        this.request = req;
        this.response = resp;
    }

    public void showCookies() {
        Cookie[] cookies = this.request.getCookies();
        if (null == cookies) { // 若没有cookie数组
            System.out.println("no cookie.");
        } else {
            System.out.println("show cookies: ");
            for(Cookie cookie : cookies){
                System.out.println("cookieName: " + cookie.getName() + ", cookieValue: " + cookie.getValue());
            }
        }
    }

    public void addCookie(final String name, final String value, final String path, final String domain){
        Cookie cookie = new Cookie(name, value); // 创建新cookie
        cookie.setMaxAge(5 * 60); // 设置存在时间
        cookie.setPath(path); // 设置作用域 如"/"
        cookie.setDomain(domain);
        this.response.addCookie(cookie); // 将cookie添加到response的cookie数组中返回
        System.out.println("addCookie: done.");
    }

    public void addCookie(final String name, final String value, final String path, final String domain, final int age){
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(age);
        cookie.setPath(path);
        cookie.setDomain(domain);
        this.response.addCookie(cookie);
    }

    public String getValue(final String name) {
        Cookie[] cookies = this.request.getCookies();
        if (null == cookies) {
            System.out.println("no cookies.");
            return null;
        } else {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name))
                    return cookie.getValue();
            }
            return null;
        }
    }

    public void updateCookie(final String name, final String new_value, final String path, final int age) {
        Cookie[] cookies = this.request.getCookies();
        if (null == cookies) {
            System.out.println("no cookies.");
        } else {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setValue(new_value);
                    cookie.setPath(path);
                    cookie.setMaxAge(age);
                    response.addCookie(cookie);
                    break;
                }
            }
        }
    }

    public void delCookie(final String name, final String path){
        Cookie[] cookies = this.request.getCookies();
        if (null == cookies) {
            System.out.println("no cookie.");
        } else {
            for (Cookie cookie : cookies){
                // 找到同名cookie 将value设置为null 将存活时间设置为0 再替换原cookie
                if (cookie.getName().equals(name)) {
                    cookie.setValue(null);
                    cookie.setMaxAge(0);
                    cookie.setPath(path);
                    response.addCookie(cookie);
                    break;
                }
            }
        }
    }
}
