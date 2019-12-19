

public class DirectedEdge<V> { 

    private final V v;
    private final V w;
    private final double weight;


    /**
     * Initializes a directed edge from node {@code v} to node {@code w} with
     * the given {@code weight}.
     * @param  v  the starting node
     * @param  w  the ending node
     * @param  weight  the weight of the directed edge
     * @throws IllegalArgumentException if the edge weight is negative
     */
    public DirectedEdge(V v, V w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
        if (weight <= 0) 
            throw new IllegalArgumentException("edge " + this + " has negative weight (or zero)");
    }


    /**
     * Initializes a directed edge from node {@code v} to node {@code w} with
     * the default weight 1.0.
     * @param v  the starting node
     * @param w  the ending node
     */
    public DirectedEdge(V v, V w) {
        this.v = v;
        this.w = w;
        this.weight = 1.0;
    }


    public DirectedEdge<V> reverse() {
        return new DirectedEdge<>(w, v, weight);
    }


    /**
     * @return the starting node of the directed edge
     */
    public V from() {
        return v;
    }


    /**
     * @return the ending node of the directed edge
     */
    public V to() {
        return w;
    }


    /**
     * @return the weight of the directed edge
     */
    public double weight() {
        return weight;
    }


    /**
     * @return a string representation of the directed edge
     */
    public String toString() {
        return String.format("%s -> %s [%s]", v, w, weight);
    }


    /**
     * Unit tests the class
     * @param args  the command-line arguments
     */
    public static void main(String[] args) {
        DirectedEdge<String> e = new DirectedEdge<>("apa", "bepa", 5.67);
        System.out.println(e);
        DirectedEdge<Integer> f = new DirectedEdge<>(12, 34);
        System.out.println(f);
    }

}
