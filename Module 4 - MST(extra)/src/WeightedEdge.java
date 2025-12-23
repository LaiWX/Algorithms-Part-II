public class WeightedEdge implements Comparable<WeightedEdge> {
    private final int v, w;
    private final double weight;

    WeightedEdge(int v, int w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public int either() {
        return v;
    }

    public int other(int oneV) {
        if (oneV == this.v) {
            return w;
        }
        else {
            return v;
        }
    }

    public double weight() {
        return weight;
    }

    public int compareTo(WeightedEdge that) {
        return Double.compare(this.weight, that.weight);
    }

    public String toString() {
        return v + " - " + w + "\tweight: " + weight + "\n";
    }
}
