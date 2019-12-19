
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

import java.util.stream.Collectors;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;


public class AdjacencyGraph implements DirectedGraph<String> {

    private Map<String, List<DirectedEdge<String>>> adjacencyList;
    private int totalNrEdges;


    public AdjacencyGraph() {
        adjacencyList = new HashMap<>();
        totalNrEdges = 0;
    }


    public AdjacencyGraph(String file) throws IOException {
        adjacencyList = new HashMap<>();
        totalNrEdges = 0;
        Files.lines(Paths.get(file))
            .filter(line -> !line.startsWith("#"))
            .map(line -> line.split("\t"))
            .map(edge -> (edge.length == 2
                          ? new DirectedEdge<>(edge[0].trim(), edge[1].trim())
                          : new DirectedEdge<>(edge[0].trim(), edge[1].trim(), Double.valueOf(edge[2].trim()))
                          ))
            .forEach(edge -> addEdge(edge));
    }


    /**
     * @return the number of nodes in this graph (actually, the nodes that have outgoing edges)
     */
    public int nrNodes() {
        return adjacencyList.size();
    }


    /**
     * @return the number of edges in this graph
     */
    public int nrEdges() {
        return totalNrEdges;
    }


    /**
     * Adds the directed edge {@code e} to this edge-weighted graph.
     * @param e  the edge
     */
    public void addEdge(DirectedEdge<String> e) {
        List<DirectedEdge<String>> outgoing = adjacencyList.get(e.from());
        if (outgoing == null) {
            outgoing = new LinkedList<>();
            adjacencyList.put(e.from(), outgoing);
        }
        outgoing.add(e);
        totalNrEdges++;
    }


    /**
     * @param  v  the node
     * @return the edges incident on node {@code v} as a List
     */
    public List<DirectedEdge<String>> outgoingEdges(String v) {
        List<DirectedEdge<String>> outgoing = adjacencyList.get(v);
        if (outgoing == null) 
            outgoing = new LinkedList<>();
        return outgoing;
    }


    /**
     * @param  v  the node
     * @return the degree of node {@code v}               
     */
    public int degree(String v) {
        return this.outgoingEdges(v).size();
    }


    public double guessCost(String v, String w) {
        return 0;
    }


    /**
     * @return a string representation of the graph
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Adjacency graph with " + nrNodes() + " nodes, " + nrEdges() + " edges\n\n");
        int ctr = 0;
        for (String v : adjacencyList.keySet()) {
            List<DirectedEdge<String>> edges = outgoingEdges(v);
            if (edges.isEmpty()) continue;
            if (ctr++ > 10) break;
            s.append(v + " --> " + edges.stream()
                     .map(e -> e.to() + "[" + e.weight() + "]")
                     .collect(Collectors.joining(", ")) + "\n");
        }
        return s.toString();
    }


    /**
     * Unit tests the class
     * @param args  the command-line arguments
     */
    public static void main(String[] args) {
        try {
            System.out.println(new AdjacencyGraph(args[0]));
        } catch (Exception e) {
            // If there is an error, print it and a little command-line help
            e.printStackTrace();
            System.err.println();
            System.err.println("Usage: java AdjacencyGraph graphfile");
            System.exit(1);
        }
    }

}
