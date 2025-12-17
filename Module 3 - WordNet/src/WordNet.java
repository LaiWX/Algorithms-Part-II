import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class WordNet {
    private final RedBlackBST<String, ArrayList<Integer>> words = new RedBlackBST<>();
    private final ArrayList<String> synset = new ArrayList<>();
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();

        In readSynsets = new In(synsets);
        while (!readSynsets.isEmpty()) {
            String[] line = readSynsets.readLine().split(",");
            int index = Integer.parseInt(line[0]);
            String[] synonyms = line[1].split(" ");
            for (String word : synonyms) {
                ArrayList<Integer> indexs = words.get(word);
                if (indexs == null) {
                    indexs = new ArrayList<>();
                    words.put(word, indexs);
                }
                indexs.add(index);
            }
            synset.add(line[1]);
        }

        int n = synset.size();

        Digraph hypernymsDg = new Digraph(n);

        In readHypernyms = new In(hypernyms);
        while (!readHypernyms.isEmpty()) {
            String[] line = readHypernyms.readLine().split(",");
            int v = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) {
                int w = Integer.parseInt(line[i]);
                hypernymsDg.addEdge(v, w);
            }
        }

        if (directedCycle(hypernymsDg)) throw new IllegalArgumentException();

        int rootCnt = 0;
        for (int i = 0; i < n; i++) {
            if (hypernymsDg.outdegree(i) == 0) {
                if (rootCnt == 0) {
                    rootCnt++;
                }
                else {
                    throw new IllegalArgumentException();
                }
            }
        }

        sap = new SAP(hypernymsDg);
    }

    private static boolean directedCycle(Digraph digraph) {
        int[] status = new int[digraph.V()]; // 0: not access; 1: be accessing; 2: have accessed;

        return isCycle(digraph, status, 0);
    }

    private static boolean isCycle(Digraph digraph, int[] status, int v) {
        if (status[v] == 1) return true;
        if (status[v] == 2) return false;

        status[v] = 1;
        for (int i : digraph.adj(v)) {
            if (isCycle(digraph, status, i)) return true;
        }
        status[v] = 2;
        return false;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return words.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return words.get(word) != null;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();

        ArrayList<Integer> v = words.get(nounA);
        ArrayList<Integer> w = words.get(nounB);

        return sap.length(v, w);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();

        ArrayList<Integer> v = words.get(nounA);
        ArrayList<Integer> w = words.get(nounB);

        int ancestorIndex = sap.ancestor(v, w);

        return synset.get(ancestorIndex);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        StdOut.println(wordnet.isNoun("a"));
    }
}