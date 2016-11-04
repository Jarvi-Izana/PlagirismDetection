import java.util.*;

/**
 * Created by GuihaoLiang on 11/3/16.
 * Home assignment by TripAdvisor
 */
public class PlagiarismDetection<E extends Comparable<E>> {
    /* N is the tuple size, default value is 3*/
    private int N;
    /* E object -> the unique group id it belongs to */
    private Map<E, List<Integer>> synos;
    /* unique Id for synonyms group -> list of E object in group. */
    private Map<Integer, List<E>> isynos;
    /* to log every E with its occurrence in abstract source file */
    private Map<E, List<Integer>> fastAccess;

    private PlagiarismDetection() {}

    public PlagiarismDetection(Synonymsor<E, Integer> synonyms, Indexable<E> source, Indexable<E> target) {
        this(synonyms, source, target, 3);
    }

    public PlagiarismDetection(Synonymsor<E, Integer> synonyms, Indexable<E> source, Indexable<E> target, int N) {
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

    /**
     * @param syno
     *      synonymous abstract file
     * @return
     *      Map: E -> all the synonyms group it belongs to
     */
    public Map<E, List<Integer>> getSynonyms(Synonymsor<E, Integer> syno) {
        return syno.getSynonyms();
    }


    /**
     * @param syno
     *      synonymous abstract file
     * @return
     *      Map: unique id -> all related E
     */
    public Map<Integer, List<E>> getInversedSyno(Synonymsor<E, Integer> syno) { return syno.getInversedSyno(); }

    /**
     * DFS or Permutation
     * e.g.1 "went for a" and "go for a"
     * Since "went" and "go" have same unique id, so they are treated equal.
     *
     * e.g.2 "buy a jaeger" and "buy a audi"
     * since "jaeger" has two meaning, so it have two unique id, only one is
     * synonyms to "audi", meaning a brand of car.
     *
     * @param source
     *      the abstract file to be compared, original one
     * @param target
     *      the target abstract file
     * @param sidx
     *      the index to index source
     * @param tidx
     *      the index to index target
     * @param N
     *      the window size
     * @return
     *  true, if the pattern of N words in source can be matched with target, otherwise false
     */
    private boolean detection(Indexable<E> source, Indexable<E> target, int sidx, int tidx, int N) {
        if (N == 0)
            return true;

        /* get the list of their unique synonyms group id */
        List<Integer> syno_src = synos.get(source.get(sidx));
        List<Integer> syno_tar = synos.get(target.get(tidx));

        if (syno_src == null) {
            /* if E to be compared in target has no synonyms */
            return syno_tar == null && (source.get(sidx).equals(target.get(tidx)))
                    && detection(source, target, sidx + 1, tidx + 1, N - 1);
        } else {
            if (syno_tar == null)
                /* current E in source has its synonyms but current E in target doesn't have */
                return false;
            else {
                // translate the synonyms to their deputy.
                for (int src : syno_src) {
                    for (int tar : syno_tar) {
                        /* if they have same deputy, similar to union find. Then move to next */
                        if (src == tar && detection(source, target, sidx + 1, tidx + 1, N - 1))
                            return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * loop over the target, iteratively checking plagiarism with detection method above
     *
     * If the E in target has synonyms, permute on all of its synonyms.
     * Otherwise, just check all the occurrence of this E in source.
     *
     * e.g.1
     * source: A B C D, target A, synonym A C
     * When encounter A, check all its synonyms A C, then start detection from index 0 and 2
     *
     * @param source
     *      source abstract file implements Indexable interface
     * @param target
     *      target abstract file to be detected
     * @param print
     *      print out all the plagiarism found in target
     * @return
     *      the rate of plagiarism
     *      formula: #(tuple matched with source) / (total number of N tuple in target)
     */

    public double detection(Indexable<E> source, Indexable<E> target, boolean print) {
        int len = target.size() - this.N;
        double base = (len);

        List<Integer> show = new ArrayList<>();

        int plagiarism = 0;
        // traverse on the target file.
        List<Integer> posInSrc;
        for (int i = 0; i <= len; i++) {
            E targtWord = target.get(i);
            // check whether the word has synonyms
            List<Integer> synoLst = synos.get(targtWord);

            // no synonyms for current word in target
            if (synoLst == null) {
                // check all occurrence of E in source
                posInSrc = fastAccess.get(targtWord);
                // the E in target is in the source
                if (posInSrc != null) {
                    for (int k : posInSrc) {
                        if (detection(source, target, k, i, N)) {
                            show.add(i);
                            plagiarism++;
                            break;
                        }
                    }
                }
            } else {
                // once find one plagiarism, get out of entire loop
                boolean isPlagiarism = false;
                // permute from all of its synonyms group
                for (int j : synoLst) {
                    // for all its synonyms in unique id j, including itself (different semantics)
                    for (E e : isynos.get(j)) {
                        // find the indexes of this synonyms
                        posInSrc = fastAccess.get(e);

                        // the E in target is not in source, just skip
                        if (posInSrc == null) continue;

                        // check all the possible position in source
                        for (int k : posInSrc) {
                            isPlagiarism = detection(source, target, k, i, N);
                            if (isPlagiarism) {
                                show.add(i);
                                plagiarism++;
                                break;
                            }
                        }
                        if (isPlagiarism) break;
                    }
                    if (isPlagiarism) break;
                }
            }
        }

        if (print) {
            System.out.println("Plagiarism detected");
            System.out.println("+++++++++++++++++++");
            for (int i : show) {
                System.out.print("[ ");
                for (int j = 0; j < N; j++) {
                    System.out.print(target.get(i + j) + " ");
                }
                System.out.println("]");
            }
            System.out.println("+++++++++++++++++++");
        }

        return plagiarism / base;
    }

    /**
     * This method is used to indexing on source.
     * For instance, N = 3, "go for a run and for a sprint",
     * we will map "for" to [1, 5]
     * When we encounter a "for" in the target file,
     * we know we should search from index 1 and 5 for potential similarity detection
     * @param source
     *      source abstract file implements Indexable interface
     * @return fa
     *      Map: E -> index in its abstract file model
     */
    private Map<E, List<Integer>> fastAccess(Indexable<E> source) {
        Map<E, List<Integer>> fa = new HashMap<>();

        // build an map for a word in source to all of its index.
        for (int i = 0, len = source.size() - N; i <= len; i++) {
            E e = source.get(i);
            List<Integer> lst = fa.get(e);
            if (lst == null) {
                lst = new ArrayList<>();
                fa.put(e, lst);
            }
            lst.add(i);
        }
        return fa;
    }
}
