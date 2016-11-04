import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by GuihaoLiang on 11/3/16.
 * home assignment by TripAdvisor
 * DFS of the level.
 */
public class PlagiarismDetection {
    private int N;
    private HashSet<String> src;
    private HashMap<String, List<Integer>> synos;
    private HashMap<Integer, Set<String>> reverseSynos;

    List<String[]> sourceByN;

//    public static void main(String[] args) {
//        if (args.length < 2 || args.length > 4) {
//            System.err.println("Usage: synonyms source target [N]");
//            System.exit(1);
//        }
//
//        HashMap<String, List<String>> syno = synonyms(args[0]);
//
//        // the default value for N is 3.
//        int N = args.length == 4 ? Integer.parseInt(args[3]) : 3;
//
//        // read the source and target file.
////        try {
////
////        } catch (IOException e) {
////
////        }
//
//    }

    PlagiarismDetection() {
        this("synonyms.txt", "source.txt", "target.txt");
    }

    PlagiarismDetection(String synonyms, String source, String target) {
        this(synonyms, source, target, 3);
    }

    PlagiarismDetection(String synonyms, String source, String target, int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N should be positive");
        }
        this.N = N;
        synos  = buildSynonyms(synonyms);
    }



    public HashMap<String, List<Integer>> buildSynonyms(String syn) {
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

    private String[] buildSourceByN(String source) {
        return null;
    }

    private String[] buildTarget(String target) {
        return null;
    }

    /**
     * DFS or Permutation
     * @param idx
     * @param N
     *  the window size
     * @return
     *  true, if the pattern of N words in source can be matched with target, otherwise false
     */
    public boolean detection(String[] source, String[] target, int idx, int N) {
        if (N == 0)
            return true;

        List<Integer> syno_src = synos.get(source[idx]);
        List<Integer> syno_tar = synos.get(target[idx]);

        if (syno_src == null) {
            return syno_tar == null && (source[idx].equals(target[idx])) && detection(source, target, idx + 1, N - 1);
        } else {
            if (syno_tar == null)
                return false;
            else {
                // translate the synonyms to their deputy.
                for (int src : syno_src) {
                    for (int tar : syno_tar) {
                        if (src == tar && detection(source, target, idx + 1, N - 1))
                            return true;
                    }
                }
            }
        }

        return false;
    }
}
