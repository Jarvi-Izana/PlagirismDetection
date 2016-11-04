/**
 * Created by GuihaoLiang on 11/4/16.
 * Client class using PlagiarismDetection
 */
public class Client {
    public static void main(String[] args) {
        if (args.length < 2 || args.length > 4) {
            System.err.println("Usage: synonyms source target [N]");
            System.exit(1);
        }

        Synonyms syno = new Synonyms(args[0]);
        Text src = new Text(args[1]);
        Text tar = new Text(args[2]);

        int N = args.length == 4 ? Integer.parseInt(args[3]) : 3;
        PlagiarismDetection<String> pd = new PlagiarismDetection<>(syno, src, tar, N);

        pd.detection(src, tar, true);
    }
}
