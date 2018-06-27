package example.pojo;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class WordEntry {
    private int id;
    private String word;
    private String phonetic;
    private String pron;
    private String poses; // 词性和释义
    private String sentences;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPron(String pron) {
        this.pron = pron;
    }

    public String getPron() {
        return pron;
    }

    public void setPoses(String poses) {
        this.poses = poses;
    }

    public String getPoses() {
        return poses;
    }

    public void setSentences(String sentences) {
        this.sentences = sentences;
    }

    public String getSentences() {
        return sentences;
    }
}
