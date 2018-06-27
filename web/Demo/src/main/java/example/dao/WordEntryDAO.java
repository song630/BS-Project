package example.dao;
import example.pojo.WordEntry;
import javax.sql.DataSource;
import java.util.List;

public interface WordEntryDAO {
    public void setDataSource(DataSource ds);
    public WordEntry getWordEntry(String word, String title);
    public WordEntry getWordEntry(int id, String title);
    public List<WordEntry> getWordEntryList(int start, int end, String title);
}
