import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class EdgeWeightedGraph {
    private final Bag<WeightedEdge>[] adj;
    private final Bag<WeightedEdge> edges = new Bag<>();
    private final int V;
    private int E;

    EdgeWeightedGraph(int v) {
        this.V = v;
        adj = (Bag<WeightedEdge>[]) new Bag[V];
        for (int i = 0; i < v; i++) {
            adj[i] = new Bag<>();
        }
    }

    EdgeWeightedGraph(In in) {
        V = Integer.parseInt(in.readLine());

        adj = (Bag<WeightedEdge>[]) new Bag[V];

        for (int i = 0; i < V; i++) {
            adj[i] = new Bag<>();
        }

        while (!in.isEmpty()) {
            String[] line = in.readLine().split(" ");

            int v = Integer.parseInt(line[0]);
            int w = Integer.parseInt(line[1]);
            double weight = Double.parseDouble(line[2]);

            addEdge(v, w, weight);
        }
    }

    public void addEdge(int v, int w, double weight) {
        WeightedEdge edge = new WeightedEdge(v, w, weight);

        adj[v].add(edge);
        adj[w].add(edge);

        edges.add(edge);

        E++;
    }

    public Iterable<WeightedEdge> adj(int v) {
        return adj[v];
    }

    public Iterable<WeightedEdge> edges() {
        return edges;
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < V; i++) {
            s.append("v: " + i + "\n");
            for (WeightedEdge edge : this.adj(i)) {
                s.append(edge);
            }
        }

        return s.toString();
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph graph = new EdgeWeightedGraph(in);
        StdOut.print(graph);
    }
}
