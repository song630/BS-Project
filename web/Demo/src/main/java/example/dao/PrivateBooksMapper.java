package example.dao;
import example.pojo.PrivateBooks;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PrivateBooksMapper implements RowMapper<PrivateBooks> {
    public PrivateBooks mapRow(ResultSet rs, int rowNum) throws SQLException {
        PrivateBooks pb = new PrivateBooks();
        pb.setUsername(rs.getString("username"));
        pb.setOrigin(rs.getString("origin"));
        pb.setId(rs.getInt("id"));
        pb.setWord(rs.getString("word"));
        return pb;
    }
}
