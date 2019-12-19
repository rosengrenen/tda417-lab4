
import java.util.List;


public interface DirectedGraph<V> {
    List<DirectedEdge<V>> outgoingEdges(V v);

    double guessCost(V v, V w);
}
