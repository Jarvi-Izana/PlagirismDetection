/**
 * Created by GuihaoLiang on 11/4/16.
 * Client class using PlagiarismDetection
 */
public class Client {
    public static void main(String[] args) {
        if (args.length < 2 || args.length > 4) {
            System.out.println("Input Format Error!"); // show to usr
            System.err.println("Usage: synonyms source target [N]"); // debug info
            System.exit(1);
        }

        Synonyms syno = new Synonyms(args[0]);
        Text src = new Text(args[1]);
        Text tar = new Text(args[2]);

        int N = args.length == 4 ? Integer.parseInt(args[3]) : 3;
        PlagiarismDetection<String> pd = new PlagiarismDetection<>(syno, src, tar, N);

        double rate = pd.detection(src, tar, true);
        System.out.printf("Plagiarism rate is %2.6f\n", rate);
    }
}
