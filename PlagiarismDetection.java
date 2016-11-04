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
public class PlagiarismDetection<T> {
    private int N;
    private HashSet<String> src;
    private Map<String, List<Integer>> synos;
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
        synos  = getSynonyms(new Synonyms(synonyms));
    }



    public Map<String, List<Integer>> getSynonyms(Synonyms syno) {
        return syno.getSynonyms();
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
