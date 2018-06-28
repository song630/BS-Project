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

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public int getFinished() {
        return finished;
    }
}
