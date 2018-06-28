package example.dao;
import example.pojo.WordBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import java.util.List;

@Configuration
public class WordBookJDBCTemplate implements WordBookDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemp;

    @Override
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemp = new JdbcTemplate(dataSource);
    }

    @Override
    public WordBook getWordBook(String title) {
        String sql = "select * from wordbooks where title = ?;";
        return jdbcTemp.queryForObject(sql, new Object[]{title}, new WordBookMapper());
    }

    @Override
    public List<WordBook> listWordBooks() {
        String sql = "select * from wordbooks;";
        return jdbcTemp.query(sql, new WordBookMapper());
    }

    @Override
    public int getNum(String title) {
        String sql = "select num from wordbooks where title = ?;";
        return jdbcTemp.queryForObject(sql, new Object[]{title}, java.lang.Integer.class);
    }
}
