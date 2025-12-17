import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        int n = G.V();
        digraph = new Digraph(n);
        for (int i = 0; i < n; i++) {
            for (int j : G.adj(i)) {
                digraph.addEdge(i, j);
            }
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        Bag<Integer> bagV = new Bag<>();
        Bag<Integer> bagW = new Bag<>();

        bagV.add(v);
        bagW.add(w);

        return length(bagV, bagW);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        Bag<Integer> bagV = new Bag<>();
        Bag<Integer> bagW = new Bag<>();

        bagV.add(v);
        bagW.add(w);

        return ancestor(bagV, bagW);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return getAncestor(v, w)[1];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return getAncestor(v, w)[0];
    }

    private int[] getAncestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();

        for (Integer i : v) {
            if (i == null || i < 0 || i >= digraph.V()) throw new IllegalArgumentException();
        }

        for (Integer i : w) {
            if (i == null || i < 0 || i >= digraph.V()) throw new IllegalArgumentException();
        }

        int[] nearestAncestor = { -1, -1 };

        int[] depthOfV = bfs(digraph, v);
        int[] depthOfW = bfs(digraph, w);

        for (int i = 0; i < digraph.V(); i++) {
            int length = depthOfV[i] + depthOfW[i];
            if (depthOfV[i] != -1 && depthOfW[i] != -1
                    && (length < nearestAncestor[1] || nearestAncestor[1] == -1)) {
                nearestAncestor[0] = i;
                nearestAncestor[1] = length;
            }
        }

        return nearestAncestor;
    }

    private static int[] bfs(Digraph digraph, Iterable<Integer> start) {
        int[] depth = new int[digraph.V()];
        for (int i = 0; i < depth.length; i++) {
            depth[i] = -1;
        }

        Queue<Integer> vInThisTurn = new Queue<>();
        for (int i : start) {
            vInThisTurn.enqueue(i);
        }

        Queue<Integer> vInNextTurn = new Queue<>();
        int turn = 0;
        while (!vInThisTurn.isEmpty()) {
            while (!vInThisTurn.isEmpty()) {
                int v = vInThisTurn.dequeue();
                if (depth[v] == -1) {
                    depth[v] = turn;
                    for (int adj : digraph.adj(v)) {
                        vInNextTurn.enqueue(adj);
                    }
                }
            }
            turn++;
            vInThisTurn = vInNextTurn;
            vInNextTurn = new Queue<>();
        }

        return depth;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}