import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by GuihaoLiang on 11/3/16.
 * Provides synonyms based on string, backed by file.
 * (obj1, obj2, ... ) -> Unique Id
 * Unique Id -> obj1, obj3, ...
 */
public class Synonyms implements Synonymsor<String, Integer> {
    private String synName;
    private Map<String, List<Integer>> synonyms;
    private Map<Integer, List<String>> inversedSyno;

    public Synonyms() {
        this("synonyms.txt");
    }

    public Synonyms(String synName) {
        this.synName = synName;
        buildSynonyms();
    }

    public String getSynName() {
        return synName;
    }

    public void setSynName(String synName) {
        this.synName = synName;
    }

    public Map<String, List<Integer>> getSynonyms() {
        return synonyms;
    }

    public Map<Integer, List<String>> getInversedSyno() {
        return inversedSyno;
    }

    public void buildSynonyms() {
        if (synName == null || synName.length() == 0) {
            System.err.println("Synonyms Input Error");
            throw new IllegalArgumentException("Invalid Synonyms Input");
        }
        // union-find-like structure.
        HashMap<String, List<Integer>> syno = new HashMap<>();
        HashMap<Integer, List<String>> isyno = new HashMap<>();

        try {
            BufferedReader synof = new BufferedReader(new FileReader(synName));
            int uniqueId = 0;
            String line;
            while ((line = synof.readLine()) != null) {
                // a series of string
                String[] wd = line.split("\\s");
                // if the wd array is empty, just skip
                if (wd.length != 0) {
                    List<String> wl = new ArrayList<>();
                    // unique id -> words in this group
                    isyno.put(uniqueId, wl);
                    for (String w : wd) {
                        wl.add(w);
                        // words -> unique id
                        List<Integer> lst = syno.get(w);
                        if (lst == null) {
                            lst = new ArrayList<>();
                            syno.put(w, lst);
                        }
                        lst.add(uniqueId);
                    }
                    uniqueId++;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("synonyms file not found");
            System.exit(2);
        } catch (IOException io) {
            System.err.println("synonyms file IO exception");
            System.exit(2);
        }

        synonyms = syno;
        inversedSyno = isyno;
    }

    public Map<String, List<Integer>> resetSynonyms(String newFileName) {
        setSynName(newFileName);
        buildSynonyms();
        return getSynonyms();
    }
}
