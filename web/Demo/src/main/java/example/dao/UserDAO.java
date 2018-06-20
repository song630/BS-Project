package example.dao;
import example.pojo.User;
import java.util.List;
import javax.sql.DataSource;

public interface UserDAO {
    // init database connection
    public void setDataSource(DataSource ds);
    // create a record
    public void create(String username, String password, String email, String phone, String education);
    // list one record
    public User getUser(String username);
    public boolean isUsernameExist(String username);
    // list down all records
    public List<User> listUsers();
    public void delete(String username);
    public void resetPassword(String username, String newPassword);
}
