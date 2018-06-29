package example.dao;
import example.pojo.Daily;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Configuration
public class DailyJDBCTemplate implements DailyDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemp;

    @Override
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemp = new JdbcTemplate(dataSource);
    }

    @Override
    public void create(String username, String status, String word, int id) {
        String sql = "insert into daily(username, status, word, id) values(?, ?, ?, ?);";
        jdbcTemp.update(sql, username, status, word, id);
    }

    @Override
    public List<Daily> getAll(String username) {
        String sql = "select * from daily where username = ?;";
        return jdbcTemp.query(sql, new DailyMapper());
    }

    @Override
    public List<Daily> getYes(String username) {
        String sql = "select * from daily where username = ? and status = 'yes'";
        return jdbcTemp.query(sql, new DailyMapper());
    }

    @Override
    public List<Daily> getNo(String username) {
        String sql = "select * from daily where username = ? and status = 'no'";
        return jdbcTemp.query(sql, new DailyMapper());
    }

    @Override
    public void batchCreate(List<Daily> batch) {
        String sql = "insert into daily(username, status, word, id) values(?, ?, ?, ?);";
        jdbcTemp.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Daily d = batch.get(i);
                preparedStatement.setString(1, d.getUsername());
                preparedStatement.setString(2, d.getStatus());
                preparedStatement.setString(3, d.getWord());
                preparedStatement.setInt(4, d.getId());
            }

            @Override
            public int getBatchSize() {
                return batch.size();
            }
        });
    }

    @Override
    public void batchDelete(String username) {
        String sql = "delete from daily where username = ?;";
        jdbcTemp.update(sql, username);
    }
}
