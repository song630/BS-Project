package example.dao;
import example.pojo.Daily;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DailyMapper implements RowMapper<Daily> {
    public Daily mapRow(ResultSet rs, int rowNum) throws SQLException {
        Daily d = new Daily();
        d.setUsername(rs.getString("username"));
        d.setStatus(rs.getString("status"));
        d.setWord(rs.getString("word"));
        d.setId(rs.getInt("id"));
        return d;
    }
}
