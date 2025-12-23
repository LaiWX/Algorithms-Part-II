public class WeightedDirectedEdge implements Comparable<WeightedDirectedEdge> {
    private final int v, w;
    private final double weight;

    WeightedDirectedEdge(int v, int w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public int from() {
        return v;
    }

    public int to() {
        return w;
    }

    public double weight() {
        return weight;
    }

    public String toString() {
        return v + " -> " + w + "\tweight: " + weight;
    }

    public int compareTo(WeightedDirectedEdge that) {
        return Double.compare(this.weight, that.weight);
    }
}
