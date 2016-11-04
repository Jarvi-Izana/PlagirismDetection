import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GuihaoLiang on 11/3/16.
 * test on the function.
 */
public final class Test {
    private Test() {}

    public static void main(String[] args) {
        synonyms_test("synonyms.txt");
        System.out.println(Test.class.getProtectionDomain().getCodeSource().getLocation().getFile());
    }

    private static void synonyms_test(String in) {
        HashMap<String, List<Integer>> syno = new PlagiarismDetection().buildSynonyms(in);

        // enable java -ea
        assert syno != null;
        assert syno.get("run").get(0) == 0;
        assert syno.get("jaeger").get(0) != 0;

        // show the whole table
        for (Map.Entry<String, List<Integer>> et : syno.entrySet()) {
            System.out.println(et.toString());
        }
    }
}
