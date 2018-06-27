package example.dao;
import example.pojo.WordEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import java.util.List;

@Configuration
public class WordEntryJDBCTemplate implements WordEntryDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemp;

    @Override
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemp = new JdbcTemplate(dataSource);
    }

    @Override
    public WordEntry getWordEntry(String word, String title) {
        String sql = "select * from " + title + " where word = ?;";
        return jdbcTemp.queryForObject(sql, new Object[]{word}, new WordEntryMapper());
    }

    @Override
    public WordEntry getWordEntry(int id, String title) {
        String sql = "select * from " + title + " where id = ?;";
        return jdbcTemp.queryForObject(sql, new Object[]{id}, new WordEntryMapper());
    }

    @Override
    public List<WordEntry> getWordEntryList(int start, int end, String title) {
        String sql = "select * from " + title + " where id >= ? and id < ?;";
        return jdbcTemp.query(sql, new Object[]{start, end}, new WordEntryMapper());
    }
}
