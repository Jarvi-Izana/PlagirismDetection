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
        Synonyms syno = new Synonyms("synonyms.txt");
        Text src = new Text("source.txt");
        Text tar = new Text("target.txt");
        PlagiarismDetection<String> pd = new PlagiarismDetection<>(syno, src, tar);

        synonyms_test(syno, pd);
        text_test(new Text());
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
    }

    private static void text_test(@NotNull Text txt) {
        assert txt.get(0).equals("went");
        assert txt.get(2).equals("a");

        for (String w : txt) {
            System.out.println(w);
        }
    }



}
