package example.dao;
import example.pojo.WordBook;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WordBookMapper implements RowMapper<WordBook> {
    public WordBook mapRow(ResultSet rs, int rowNum) throws SQLException {
        WordBook book = new WordBook();
        book.setTitle(rs.getString("title"));
        book.setNum(rs.getInt("num"));
        return book;
    }
}
