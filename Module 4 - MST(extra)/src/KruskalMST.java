import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.UF;

public class KruskalMST implements MST {
    private Bag<WeightedEdge> tree = new Bag<>();
    private double weight;

    KruskalMST(EdgeWeightedGraph G) {
        UF uf = new UF(G.V());

        MinPQ<WeightedEdge> edges = new MinPQ<>();
        for (WeightedEdge edge : G.edges()) {
            edges.insert(edge);
        }

        while (!edges.isEmpty() && tree.size() < G.E() - 1) {
            WeightedEdge edge = edges.delMin();

            int v = edge.either();
            int w = edge.other(v);

            if (uf.find(v) != uf.find(w)) {
                uf.union(v, w);
                tree.add(edge);
                weight += edge.weight();
            }
        }
    }

    public Iterable<WeightedEdge> edges() {
        return tree;
    }

    public double weight() {
        return weight;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph graph = new EdgeWeightedGraph(in);
        MST mst = new KruskalMST(graph);
        for (WeightedEdge edge : mst.edges()) {
            StdOut.print(edge);
        }
        StdOut.println(mst.weight());
    }
}
