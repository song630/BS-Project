package example.dao;
import example.pojo.WordBook;
import javax.sql.DataSource;
import java.util.List;

public interface WordBookDAO {
    public void setDataSource(DataSource ds);
    public WordBook getWordBook(String title);
    public List<WordBook> listWordBooks();
}
