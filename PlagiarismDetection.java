import com.sun.istack.internal.NotNull;

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
public class PlagiarismDetection<E extends Comparable<E>> {
    private int N;
    private HashSet<String> src;
    private Map<E, List<Integer>> synos;
    private Map<Integer, List<E>> isynos;
    private Map<E, List<Integer>> fastAccess;

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

    private PlagiarismDetection() {}

    PlagiarismDetection(Synonymsor<E, Integer> synonyms, Indexable<E> source, Indexable<E> target) {
        this(synonyms, source, target, 3);
    }

    PlagiarismDetection(Synonymsor<E, Integer> synonyms, Indexable<E> source, Indexable<E> target, int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N should be positive");
        }

        if (N > source.size() || N > target.size())
            throw new IllegalArgumentException("Input size less than N");

        this.N = N;
        synos  = getSynonyms(synonyms);
        isynos = getInversedSyno(synonyms);
        fastAccess = fastAccess(source);
    }


    public Map<E, List<Integer>> getSynonyms(Synonymsor<E, Integer> syno) {
        return syno.getSynonyms();
    }

    public Map<Integer, List<E>> getInversedSyno(Synonymsor<E, Integer> syno) { return syno.getInversedSyno(); }
    /**
     * DFS or Permutation
     * @param N
     *  the window size
     * @return
     *  true, if the pattern of N words in source can be matched with target, otherwise false
     */
    private boolean detection(Indexable<E> source, Indexable<E> target, int sidx, int tidx, int N) {
        if (N == 0)
            return true;

        List<Integer> syno_src = synos.get(source.get(sidx));
        List<Integer> syno_tar = synos.get(target.get(tidx));

        if (syno_src == null) {
            return syno_tar == null && (source.get(sidx).equals(target.get(tidx))) && detection(source, target, sidx + 1, tidx + 1, N - 1);
        } else {
            if (syno_tar == null)
                return false;
            else {
                // translate the synonyms to their deputy.
                for (int src : syno_src) {
                    for (int tar : syno_tar) {
                        if (src == tar && detection(source, target, sidx + 1, tidx + 1, N - 1))
                            return true;
                    }
                }
            }
        }

        return false;
    }

    public double detection(Indexable<E> source, Indexable<E> target) {
        int len = target.size() - this.N;
        double base = (len);

        int plagiarism = 0;
        // traverse on the target file.
        List<Integer> posInSrc;
        for (int i = 0; i <= len; i++) {
            E targtWord = target.get(i);
            List<Integer> synoLst = synos.get(targtWord);
            if (synoLst == null) {
                // no synonyms for current word in target
                posInSrc = fastAccess.get(targtWord);
                // System.out.println(i + " : " + targtWord + " : " + posInSrc);
                if (posInSrc != null) {
                    for (int k : posInSrc) {
                        if (detection(source, target, k, i, N)) {
                            System.out.println(i);
                            plagiarism++;
                            break;
                        }
                    }
                }
            } else {
                boolean isPlag = false;
                for (int j : synoLst) {
                    // for all its synonyms, including itself
                    for (E e : isynos.get(j)) {
                        // find the starting idx of this synonyms
                        posInSrc = fastAccess.get(e);
                        if (posInSrc == null) continue;
                        for (int k : posInSrc) {
                            isPlag = detection(source, target, k, i, N);
                            if (isPlag) {
                                System.out.println(i);
                                plagiarism++;
                                break;
                            }
                        }
                        if (isPlag) break;
                    }
                    if (isPlag) break;
                }
            }
        }

        return plagiarism / base;
    }

    private Map<E, List<Integer>> fastAccess(@NotNull Indexable<E> in) {
        Map<E, List<Integer>> fa = new HashMap<>();

        for (int i = 0, len = in.size() - N; i <= len; i++) {
            E e = in.get(i);
            List<Integer> lst = fa.get(e);
            if (lst == null) {
                lst = new ArrayList<>();
                fa.put(e, lst);
            }
            lst.add(i);
            // System.out.println("fastAccess " + e + " : " + lst);
        }

        return fa;
    }
}
