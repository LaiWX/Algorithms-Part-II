import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;

public class EdgeWeightedDigraph {
    private final int V;
    private int E;
    private Bag<WeightedDirectedEdge>[] adj;
    private Bag<WeightedDirectedEdge> edges = new Bag<>();

    EdgeWeightedDigraph(int V) {
        this.V = V;

        InitAdj();
    }

    EdgeWeightedDigraph(In in) {
        V = Integer.parseInt(in.readLine());
        InitAdj();

        while (!in.isEmpty()) {
            String[] line = in.readLine().split(" ");

            int v = Integer.parseInt(line[0]);
            int w = Integer.parseInt(line[1]);
            double weight = Double.parseDouble(line[2]);

            addEdge(v, w, weight);
        }
    }

    private void InitAdj() {
        adj = (Bag<WeightedDirectedEdge>[]) new Bag[V];

        for (int i = 0; i < V; i++) {
            adj[i] = new Bag<>();
        }
    }

    public void addEdge(int from, int to, double weight) {
        WeightedDirectedEdge edge = new WeightedDirectedEdge(from, to, weight);

        adj[from].add(edge);
        edges.add(edge);

        E++;
    }

    public Iterable<WeightedDirectedEdge> adj(int v) {
        return adj[v];
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public Iterable<WeightedDirectedEdge> edges() {
        return edges;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int v = 0; v < V; v++) {
            s.append("Vertice: " + v + "\n");
            for (WeightedDirectedEdge edge : adj(v)) {
                s.append(edge + "\n");
            }
        }

        return s.toString();
    }
}
