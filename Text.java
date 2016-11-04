import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by GuihaoLiang on 11/3/16.
 *
 */
public class Text implements Indexable<String>, Iterable<String> {
    private int idx;
    private List<String> words = new ArrayList<>();

    Text() {
        this("source.txt");
    }

    Text(String fileName) {
        cacheFile(fileName);
    }

    private void cacheFile(String fileName) {
        try {
            BufferedReader file = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = file.readLine()) != null) {
                words.addAll(Arrays.asList(line.split("\\s")));
            }
        } catch (FileNotFoundException e) {
            System.err.println(fileName + " file not found");
            System.exit(2);
        } catch (IOException io) {
            System.err.println(fileName + "file IO exception");
            System.exit(2);
        }
    }

    public String get(int i) {
        return words.get(i);
    }

    public int size() {
        return words.size();
    }

    public int currentIdx() {
        return idx;
    }

    public int resetIdx() {
        idx = 0;
        return idx;
    }

    public Iterator<String> iterator() {
        return words.iterator();
    }
}
