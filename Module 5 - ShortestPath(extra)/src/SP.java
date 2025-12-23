import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class SP {
    private final WeightedDirectedEdge[] edgeTo;
    private final double[] distTo;
    private final IndexMinPQ<Double> distPQ;

    SP(EdgeWeightedDigraph G, int s) {
        int V = G.V();

        edgeTo = new WeightedDirectedEdge[V];
        distTo = new double[V];

        for (int i = 0; i < V; i++) {
            distTo[i] = Double.POSITIVE_INFINITY;
        }
        distTo[s] = 0;

        distPQ = new IndexMinPQ<>(V);

        distPQ.insert(s, 0.0);

        while (!distPQ.isEmpty()) {
            int minIndex = distPQ.delMin();
            for (WeightedDirectedEdge edge : G.adj(minIndex)) {
                relax(edge);
            }
        }
    }

    private void relax(WeightedDirectedEdge edge) {
        int v = edge.from();
        int w = edge.to();

        double newDist = distTo[v] + edge.weight();
        if (newDist < distTo[w]) {
            distTo[w] = newDist;
            edgeTo[w] = edge;
            if (distPQ.contains(w)) {
                distPQ.decreaseKey(w, newDist);
            }
            else {
                distPQ.insert(w, newDist);
            }
        }
    }

    public double distTo(int v) {
        return distTo[v];
    }

    public Iterable<WeightedDirectedEdge> pathTo(int v) {
        Stack<WeightedDirectedEdge> path = new Stack<>();
        while (edgeTo[v] != null) {
            path.push(edgeTo[v]);
            v = edgeTo[v].from();
        }
        return path;
    }


    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedDigraph digraph = new EdgeWeightedDigraph(in);
        SP sp = new SP(digraph, 0);
        for (int i = 0; i < digraph.V(); i++) {
            StdOut.println(i + ": \t" + sp.edgeTo[i] + "\t" + sp.distTo[i]);
        }
    }
}