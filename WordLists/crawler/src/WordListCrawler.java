import java.io.*;
import java.util.Vector;
import java.util.concurrent.*;
import basic.WordEntry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
// import static java.lang.Thread.sleep; // 加上sleep以免请求太频繁

public class WordListCrawler {
    private final static String word_books[] = {
        "GRE词汇.txt",
        "托福词汇.txt",
        "六级词汇.txt"
    };
    private final static String word_books_path[] = {"GRE.xml", "TOEFL.xml", "CET-6.xml"};
    // e.g. 要搜abandon这个词 网址就是http://www.youdao.com/w/eng/abandon/
    private final static String website = "http://dict.youdao.com/w/eng/";
    private static Vector<String> words = new Vector<>(); // 一本单词书的所有单词集合
    private static int num_words; // 一本单词书的单词数量
    private final static int max_threads = 5;
    private static Vector<WordEntry> WordEntries = new Vector<>(); // 一本单词书收集所有单词信息的集合

    private void drawWords(int index) { // 提取出一本单词书的所有单词
        if (index >= word_books.length || index < 0) {
            System.out.println("invalid parameter in drawWords().");
            return;
        }
        try {
            System.out.println(word_books[index]);
            BufferedReader br = new BufferedReader(new FileReader(word_books[index]));
            String str;
            while ((str = br.readLine()) != null)
                words.add(str);
            br.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        num_words = words.size();
    }

    public static class Task implements Runnable {
        private WordEntry object;
        private int num;
        private Task(WordEntry obj, int i) {
            object = obj;
            num = i;
        }
        public void run() {
            System.out.println("running thread " + num + " of " + num_words);
            object.FetchWordInfo();
            /*
            try {
                sleep(300);
            } catch (Exception e) {
                e.printStackTrace();
            } */
        }
    }

    private static void DOMCreate(File file) {
        try {
            DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
            DocumentBuilder db=dbf.newDocumentBuilder();
            Document document=db.newDocument();
            document.setXmlStandalone(true);
            Element root = document.createElement("wordbook");
            int i = 0;
            int total = WordEntries.size();
            for (WordEntry w: WordEntries) {
                System.out.println("Generating xml: " + (i + 1) + " of " + total);
                Element wordEntry = document.createElement("wordEntry");
                wordEntry.setAttribute("id", i + "");
                Element word = document.createElement("word");
                word.setTextContent(w.word);
                Element phonetic = document.createElement("phonetic"); // 音标
                phonetic.setTextContent(w.symbols);
                Element pron = document.createElement("pron"); // 发音源
                pron.setTextContent(w.pronunciation);
                Element syn = document.createElement("synonyms");
                syn.setTextContent(w.synonyms);
                Element ant = document.createElement("antonyms");
                ant.setTextContent(w.antonyms);

                // fill in poses:
                Element poses = document.createElement("poses");
                StringBuilder poses_content = new StringBuilder();
                int num_poses = w.poses.size();
                for (int j = 0; j < num_poses; j++) {
                    poses_content.append(w.poses.get(j));
                    for (String exp : w.explanations.get(j)) {
                        poses_content.append(exp);
                    }
                }
                poses.setTextContent(poses_content.toString());

                // fill in sentences:
                Element sentenses = document.createElement("sentences");
                StringBuilder s_content = new StringBuilder();
                for (String sen : w.sentences) {
                    s_content.append(sen);
                }
                sentenses.setTextContent(s_content.toString());

                wordEntry.appendChild(word);
                wordEntry.appendChild(phonetic);
                wordEntry.appendChild(pron);
                wordEntry.appendChild(poses);
                wordEntry.appendChild(sentenses);
                root.appendChild(wordEntry);
                i++;
            }
            document.appendChild(root);
            TransformerFactory tff=TransformerFactory.newInstance();
            Transformer tf=tff.newTransformer();
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.transform(new DOMSource(document), new StreamResult(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WordListCrawler WC = new WordListCrawler();
        for (int index = 0; index < word_books.length; index++) { // 遍历所有单词书
            System.out.println("Book " + (index + 1) + " of " + word_books.length + ":\n===================\n");
            WC.drawWords(index);
            for (String s: words) {
                WordEntries.add(new WordEntry(s, website));
            }
            int i = 0;
            ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(max_threads);
            for (WordEntry w: WordEntries) {
                executor.execute(new Task(w, i));
                i++;
            }
            executor.shutdown();
            while (!executor.isTerminated()); // 等待所有线程结束
            // 生成xml文件
            words.removeAllElements();
            File file = new File(word_books_path[index]);
            DOMCreate(file);
            WordEntries.removeAllElements();
        }
    }
}
