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
// import static java.lang.Thread.sleep;

public class WordListCrawler {
    private final static String word_books[] = {
        "六级词汇.txt",
        "托福词汇.txt",
        "GRE词汇.xml"
    };
    // e.g. 要搜abandon这个词 网址就是http://www.youdao.com/w/eng/abandon/
    private final static String website = "http://dict.youdao.com/w/eng/";
    private static Vector<String> words = new Vector<>();
    private static int num_words;
    private final static int max_threads = 5;
    private static Vector<WordEntry> WordEntries = new Vector<>();

    private void drawWords(int index) {
        if (index >= word_books.length - 1 || index < 0) {
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
            for (WordEntry w: WordEntries) {
                Element wordEntry = document.createElement("wordEntry");
                wordEntry.setAttribute("id", "entry" + i);
                Element word = document.createElement("word");
                word.setTextContent(w.word);
                Element phonetic = document.createElement("phonetic"); // 音标
                phonetic.setTextContent(w.symbols);
                Element pron = document.createElement("pron"); // 发音源
                pron.setTextContent(w.pronunciation);
                Element poses = document.createElement("poses");
                Element syn = document.createElement("synonyms");
                syn.setTextContent(w.synonyms);
                Element ant = document.createElement("antonyms");
                ant.setTextContent(w.antonyms);
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
        int index = 0;
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
        String path[] = {"CET-6.xml", "TOEFL.xml", "GRE.xml"};
        File file = new File(path[index]);
        DOMCreate(file);
        WordEntries.removeAllElements();
    }
}
