package example.dao;
import example.pojo.User;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserJDBCTemplate implements UserDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemp;

    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemp = new JdbcTemplate(dataSource);
    }

    @Override
    public void create(String username, String password, String email, String phone, String education) {
        String sql = "insert into user(username, password, email, phone, education)" +
                " values(?, ?, ?, ?, ?)";
        jdbcTemp.update(sql, username, password, email, phone, education);
        System.out.println("Created record username = " + username);
    }

    @Override
    public User getUser(String username) {
        String sql = "select * from user where username = ?;";
        return jdbcTemp.queryForObject(sql, new Object[]{username}, new UserMapper());
    }

    @Override
    public List<User> listUsers() {
        String sql = "select * from user;";
        return jdbcTemp.query(sql, new UserMapper());
    }

    @Override
    public void delete(String username) {
        String sql = "delete from user where username = ?;";
        jdbcTemp.update(sql, username);
        System.out.println("Deleted record with username = " + username);
    }

    @Override
    public void resetPassword(String username, String newPassword) {
        String sql = "update user set password = ? where username = ?;";
        jdbcTemp.update(sql, username, newPassword);
        System.out.println("Updated record with username = " + username);
    }
}
