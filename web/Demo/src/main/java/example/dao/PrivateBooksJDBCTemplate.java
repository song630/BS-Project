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
    public boolean create(String username, String origin, int id, String word) {
        try {
            String sql = "select id from privatebooks where username = ? and word = ?;";
            jdbcTemp.queryForObject(sql, new Object[]{username, word}, java.lang.Integer.class);
            return false;
        } catch (EmptyResultDataAccessException e) {
            String sql = "insert into privatebooks(username, origin, id, word) values(?, ?, ?, ?);";
            jdbcTemp.update(sql, username, origin, id, word);
            return true;
        }
    }

    @Override
    public void delete(String username, String word) {
        String sql = "delete from privatebooks where username = ? and word = ?;";
        jdbcTemp.update(sql, username, word);
    }

    @Override
    public List<PrivateBooks> getAllWords(String username) {
        String sql = "select * from privatebooks where username = ?;";
        return jdbcTemp.query(sql, new Object[]{username}, new PrivateBooksMapper());
    }

    @Override
    public void deleteAllOfUser(String username) {
        String sql = "delete from privatebooks where username = ?;";
        jdbcTemp.update(sql, username);
    }

    @Override
    public PrivateBooks getEntry(String username, String word) {
        String sql = "select * from privatebooks where username = ? and word = ?;";
        try {
            return jdbcTemp.queryForObject(sql, new Object[]{username, word}, new PrivateBooksMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int count(String username) { // === 待验证
        String sql = "select count(username = ? or null) from privatebooks;";
        return jdbcTemp.queryForObject(sql, new Object[]{username}, java.lang.Integer.class);
    }
}
