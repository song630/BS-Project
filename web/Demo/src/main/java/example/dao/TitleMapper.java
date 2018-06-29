package example.dao;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TitleMapper implements RowMapper<String> {
    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("title");
    }
}
