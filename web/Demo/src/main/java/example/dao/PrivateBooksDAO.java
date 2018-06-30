package example.dao;
import example.pojo.PrivateBooks;
import javax.sql.DataSource;
import java.util.List;

public interface PrivateBooksDAO {
    public void setDataSource(DataSource ds);
    // 即使来自不同单词书 单词也不能重复
    public boolean create(String username, String origin, int id, String word);
    public void delete(String username, String word);
    // 获取一个用户的所有单词
    public List<PrivateBooks> getAllWords(String username);
    // 删除一个用户的所有单词(注销用户后)
    public void deleteAllOfUser(String username);
    // 获取一个单词记录(用于查看单词详细信息)
    public PrivateBooks getEntry(String username, String word);
    public int count(String username);
}
