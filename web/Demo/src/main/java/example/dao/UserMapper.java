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
        user.setLastDate(rs.getString("lastDate"));
        user.setDay1(rs.getInt("day1"));
        user.setDay2(rs.getInt("day2"));
        user.setDay3(rs.getInt("day3"));
        user.setDay4(rs.getInt("day4"));
        user.setDay5(rs.getInt("day5"));
        user.setDay6(rs.getInt("day6"));
        user.setDay7(rs.getInt("day7"));
        return user;
    }
}
