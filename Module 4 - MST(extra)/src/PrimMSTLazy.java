import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class PrimMSTLazy implements MST {
    private final Bag<WeightedEdge> tree = new Bag<>();
    private final boolean[] marked;
    private final MinPQ<WeightedEdge> possibleEdge = new MinPQ<>();
    private double weight;
    private final EdgeWeightedGraph graph;

    PrimMSTLazy(EdgeWeightedGraph G) {
        graph = G;
        marked = new boolean[graph.V()];

        marked[0] = true;
        for (WeightedEdge edge : graph.adj(0)) {
            possibleEdge.insert(edge);
        }

        while (!possibleEdge.isEmpty() && tree.size() < graph.V() - 1) {
            WeightedEdge edge = possibleEdge.delMin();

            int v = edge.either();
            int w = edge.other(v);
            if (!(marked[v] && marked[w])) {
                weight += edge.weight();
                tree.add(edge);

                if (marked[v]) {
                    visit(w);
                }
                else {
                    visit(v);
                }
            }
        }
    }

    private void visit(int v) {
        marked[v] = true;
        for (WeightedEdge edge : graph.adj(v)) {
            int w = edge.other(v);
            if (!marked[w]) {
                possibleEdge.insert(edge);
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
        MST mst = new PrimMSTLazy(graph);
        for (WeightedEdge edge : mst.edges()) {
            StdOut.print(edge);
        }
        StdOut.println(mst.weight());
    }
}
