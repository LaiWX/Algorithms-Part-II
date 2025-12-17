import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordnet;

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns) {
        int maxDis = -1;
        String outword = "";

        for (int i = 0; i < nouns.length; i++) {
            int distance = 0;

            for (String noun : nouns) {
                distance += wordnet.distance(nouns[i], noun);
            }

            if (distance > maxDis) {
                maxDis = distance;
                outword = nouns[i];
            }
        }

        return outword;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
