import com.sun.istack.internal.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Created by GuihaoLiang on 11/3/16.
 * test on the function.
 * enable java -ea, check assertion at run time.
 */
public final class Test {
    private Test() {}

    public static void main(String[] args) {
        Synonyms syno = new Synonyms("test_synonyms.txt");
        Text src = new Text("test_source.txt");
        Text tar = new Text("test_target.txt");
        PlagiarismDetection<String> pd = new PlagiarismDetection<>(syno, src, tar);

        synonyms_test(syno, pd);
        System.out.println();

        text_test(src);
        System.out.println();

        detection_test(pd, src, tar);
    }

    private static void synonyms_test(@NotNull Synonyms in, @NotNull PlagiarismDetection<String> pd) {
        Map<String, List<Integer>> syno = pd.getSynonyms(in);

        assert syno != null;
        assert syno.get("run").get(0) == 0;
        assert syno.get("jaeger").get(0) != 0;
        assert syno.get("audi").get(0) == 2;

        // show the whole table
        for (Map.Entry<String, List<Integer>> et : syno.entrySet()) {
            System.out.println(et.toString());
        }


        Map<Integer, List<String>> isyno = pd.getInversedSyno(in);

        assert isyno.get(1).get(0).equals("jaeger");
        assert isyno.get(2).get(0).equals("jaeger");
        assert isyno.get(0).get(2).equals("jog");

        for (Map.Entry<Integer, List<String>> et : isyno.entrySet()) {
            System.out.println(et.toString());
        }
    }

    private static void text_test(@NotNull Text txt) {
        assert txt.get(2).equals("a");

        for (String w : txt) {
            System.out.println(w);
        }
    }

    private static void detection_test(@NotNull PlagiarismDetection<String> pd, Indexable<String> source, Indexable<String> target) {
        System.out.println(pd.detection(source, target, true));
    }
}
