package example.dao;
import example.pojo.PrivateBooks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import java.util.List;

@Configuration
public class PrivateBooksJDBCTemplate implements PrivateBooksDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemp;

    @Override
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemp = new JdbcTemplate(dataSource);
    }

    @Override
    public boolean create(String username, String title, String origin, int id, String word) {
        try {
            String sql = "select id from privatebooks where username = ? and title = ? and word = ?;";
            jdbcTemp.queryForObject(sql, new Object[]{username, title, word}, java.lang.Integer.class);
            return false;
        } catch (EmptyResultDataAccessException e) {
            String sql = "insert into privatebooks(username, title, origin, id, word) values(?, ?, ?, ?, ?);";
            jdbcTemp.update(sql, username, title, origin, id, word);
            return true;
        }
    }

    @Override
    public void delete(String username, String title, String word) {
        String sql = "delete from privatebooks where username = ? and title = ? and word = ?;";
        jdbcTemp.update(sql, username, title, word);
    }

    @Override
    public List<PrivateBooks> getAllWords(String username, String title) {
        String sql = "select * from privatebooks where username = ? and title = ?;";
        return jdbcTemp.query(sql, new PrivateBooksMapper());
    }

    @Override
    public List<String> getAllTitles(String username) { // ===== 待验证
        String sql = "select title from privatebooks where username = ?;";
        return jdbcTemp.query(sql, new TitleMapper());
    }

    @Override
    public void deleteAllOfUser(String username) {
        String sql = "delete from privatebooks where username = ?;";
        jdbcTemp.update(sql, username);
    }

    @Override
    public void deleteAllOfTitle(String username, String title) {
        String sql = "delete from privatebooks where username = ? and title = ?;";
        jdbcTemp.update(sql, username, title);
    }

    @Override
    public PrivateBooks getEntry(String username, String title, String word) {
        String sql = "select * from privatebooks where username = ? and title = ? and word = ?;";
        return jdbcTemp.queryForObject(sql, new Object[]{username, title, word}, new PrivateBooksMapper());
    }
}
