package example.dao;
import example.pojo.WordEntry;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WordEntryMapper implements RowMapper<WordEntry> {
    public WordEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
        WordEntry entry = new WordEntry();
        entry.setId(rs.getInt("id"));
        entry.setWord(rs.getString("word"));
        entry.setPhonetic(rs.getString("phonetic"));
        entry.setPron(rs.getString("pron"));
        entry.setPoses(rs.getString("poses"));
        entry.setSentences(rs.getString("sentences"));
        return entry;
    }
}
