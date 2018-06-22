package basic;
import java.net.*;
import java.io.*;
import java.util.Vector;

public class WordEntry {
    public final String word;
    private final String main_url;
    public String pronunciation; // 发音源
    public String symbols; // 音标
    public Vector<String> poses = new Vector<>(); // 词性
    public Vector<Vector<String>> explanations = new Vector<>(); // 词性与释义对应
    public Vector<String> sentences = new Vector<>(); // 例句 中英文对应
    public String antonyms; // 反义词
    public String synonyms; // 近义词

    public WordEntry(final String the_word, final String root_url) {
        word = the_word;
        main_url = root_url + word + "/#keyfrom=dict2.index";
        pronunciation = "http://dict.youdao.com/dictvoice?audio=" + word;
    }

    public void FetchWordInfo() {
        try {
            URL url = new URL(main_url);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String buf;
            while ((buf = in.readLine()) != null) {
                if (buf.contains("\"pronounce\">美")) { // 美式发音
                    buf = in.readLine();
                    if (buf.indexOf('[') == -1)
                        symbols = "[]";
                    else
                        symbols = buf.substring(buf.indexOf("["), buf.indexOf("]") + 1); // 保存音标
                }
                if (buf.contains("pos wordGroup")) { // 词性
                    // 得到词性 用{}标记
                    String pos = "{" + buf.substring(buf.indexOf(">") + 1, buf.indexOf("</")) + "}";
                    poses.add(pos);
                    Vector<String> exp_of_pos = new Vector<>();
                    while (!(buf = in.readLine()).contains("</ul>")) { // 没有读到下一个词性
                        if (buf.contains("class=\"def\"")) { // 这个词性的某个释义 用++ **标记
                            exp_of_pos.add("++" + buf.substring(buf.indexOf("class=\"def\"") + 12, buf.indexOf("</span>")) + "**");
                        }
                    }
                    explanations.add(exp_of_pos); // 加入这个词性的所有释义
                }
                if (buf.contains("近义词：")) {
                    in.readLine();
                    buf = in.readLine();
                    synonyms = buf.substring(buf.indexOf("hy") + 4, buf.indexOf("</a>"));
                    // flag1 = false;
                }
                if (buf.contains("反义词：")) {
                    in.readLine();
                    buf = in.readLine();
                    antonyms = buf.substring(buf.indexOf("hy") + 4, buf.indexOf("</a>"));
                    // flag2 = false;
                }
                if (buf.contains("<p><span")) { // 一个例句 用++ **标记
                    String text = buf.replaceAll("</?[^>]+>", ""); // 提取纯文本
                    sentences.add("++" + text.trim() + "**");
                }
                if (buf.contains("更多双语例句")) break;
            }
            in.close();
        } catch (Exception e) {
            System.out.println("error: inside FetchWordInfo(), " + main_url);
            e.printStackTrace();
        }
    }
}
