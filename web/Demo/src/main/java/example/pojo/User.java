package example.pojo;
import org.springframework.context.annotation.Configuration;

@Configuration  // 注册为Bean
public class User {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String education;
    private String studying;
    private int studied;
    private int plan;
    private int finished;
    private String lastDate;
    private int day1;
    private int day2;
    private int day3;
    private int day4;
    private int day5;
    private int day6;
    private int day7;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getEducation() {
        return education;
    }

    public void setStudying(String studying) {
        this.studying = studying;
    }

    public String getStudying() {
        return studying;
    }

    public void setStudied(int studied) {
        this.studied = studied;
    }

    public int getStudied() {
        return studied;
    }

    public void setPlan(int plan) {
        this.plan = plan;
    }

    public int getPlan() {
        return plan;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setDay1(int day1) {
        this.day1 = day1;
    }

    public int getDay1() {
        return day1;
    }

    public void setDay2(int day2) {
        this.day2 = day2;
    }

    public int getDay2() {
        return day2;
    }

    public void setDay3(int day3) {
        this.day3 = day3;
    }

    public int getDay3() {
        return day3;
    }

    public void setDay4(int day4) {
        this.day4 = day4;
    }

    public int getDay4() {
        return day4;
    }

    public void setDay5(int day5) {
        this.day5 = day5;
    }

    public int getDay5() {
        return day5;
    }

    public void setDay6(int day6) {
        this.day6 = day6;
    }

    public int getDay6() {
        return day6;
    }

    public void setDay7(int day7) {
        this.day7 = day7;
    }

    public int getDay7() {
        return day7;
    }
}
