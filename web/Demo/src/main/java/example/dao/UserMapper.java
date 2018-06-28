package example.dao;
import example.pojo.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class UserMapper implements RowMapper<User> {
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setEducation(rs.getString("education"));
        user.setStudying(rs.getString("studying"));
        user.setStudied(rs.getInt("studied"));
        user.setPlan(rs.getInt("plan"));
        user.setFinished(rs.getInt("finished"));
        return user;
    }
}
