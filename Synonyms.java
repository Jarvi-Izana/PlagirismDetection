import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GuihaoLiang on 11/3/16.
 */
public class Synonyms implements Synonymsor<String, Integer>  {
    private String syn;

    Synonyms() {
        this("synonyms.txt");
    }

    public String getSyn() {
        return syn;
    }

    public void setSyn(String syn) {
        this.syn = syn;
    }

    public Synonyms(String syn) {
        this.syn = syn;
    }

    public Map<String, List<Integer>> getSynonyms() {
        if (syn == null || syn.length() == 0) {
            System.err.println("Synonyms Input Error");
            throw new IllegalArgumentException("Invalid Synonyms Input");
        }
        // union find like structure.
        HashMap<String, List<Integer>> syno = new HashMap<>();
        try {
            BufferedReader synof = new BufferedReader(new FileReader(syn));
            // fist line is always comment. So omit the fist line.
            String line;
            int uniqueId = 0;
            while ((line = synof.readLine()) != null) {
                String[] wd = line.split("\\s");
                // find a deputy for this group
                for (String w : wd) {
                    List<Integer> lst = syno.get(w);
                    if (lst == null) {
                        lst = new ArrayList<>();
                        syno.put(w, lst);
                    }
                    lst.add(uniqueId);
                }
                uniqueId++;
            }
        } catch (FileNotFoundException e) {
            System.err.println("synonyms file not found");
            System.exit(2);
        } catch (IOException io) {
            System.err.println("synonyms file IO exception");
            System.exit(2);
        }
        return syno;
    }
}
