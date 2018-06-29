package example.dao;
import example.pojo.Daily;
import javax.sql.DataSource;
import java.util.List;

public interface DailyDAO {
    public void setDataSource(DataSource ds);
    public void create(String username, String status, String word, int id);
    // 获取一个用户在最后一次登录的一天学习的所有单词
    public List<Daily> getAll(String username);
    // 获取一个用户所有未掌握的单词
    public List<Daily> getYes(String username);
    // 获取一个用户所有掌握的单词
    public List<Daily> getNo(String username);
    // 完成一个list后批量将单词加进去
    public void batchCreate(final List<Daily> batch);
    // 删除表中一个用户的所有单词记录
    public void batchDelete(String username);
}
