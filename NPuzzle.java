
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Arrays;

import java.util.stream.Collectors;

import java.io.IOException;


public class NPuzzle implements DirectedGraph<String> {

    protected int N;
    private static char separator = '/';
    private static char emptytile = '_';
    private static String tiles = "_ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";


    public NPuzzle(int N) {
        if (N < 2 || N > 6) {
            throw new IllegalArgumentException("We only support sizes of 2 <= N <= 6.");
        }
        this.N = N;
    }


    /**
     * @param  v  the node
     * @return the edges incident on node {@code v} as a List
     */
    public List<DirectedEdge<String>> outgoingEdges(String v) {
        List<DirectedEdge<String>> outgoing = new LinkedList<>();
        String w;
        int pos = v.indexOf(emptytile);
        int[] directions = {pos-1, pos+1, pos-(N+1), pos+(N+1)};
        for (int newpos : directions) {
            if (newpos > 0 && newpos < v.length() && v.charAt(newpos) != separator) {
                if (pos < newpos) 
                    w = v.substring(0,pos) + v.charAt(newpos) + v.substring(pos+1,newpos) +
                        emptytile + v.substring(newpos+1);
                else
                    w = v.substring(0,newpos) + emptytile + v.substring(newpos+1,pos) +
                        v.charAt(newpos) + v.substring(pos+1);
                outgoing.add(new DirectedEdge<>(v, w));
            }
        }
        return outgoing;
    }


    /**
     * @param  v  one state
     * @param  w  the other state
     * @return the total Manhattan distance between the two states
     */
    public double guessCost(String v, String w) {
        int diff = 0;
        for (int i = 0; i < v.length(); i++) {
            int j = w.indexOf(v.charAt(i)); // the position of v[i] in w
            diff += Math.abs(i-j) % (N+1);  // the number of differing horisontal tiles 
            diff += Math.abs(i-j) / (N+1);  // the number of differing vertical tiles
        }
        return diff;
    }


    private String makeState(String mytiles) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < N; i++)
            s.append(separator + mytiles.substring(N*i, N*(i+1)));
        s.append(separator);
        return s.toString();
    }


    /**
     * @return a string representation of the puzzle graph
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("NPuzzle graph of size " + N + " x " + N + ". ");
        s.append("States are strings of " + (N*N) + " chars '" + tiles.charAt(1) + "'...'" + tiles.charAt(N*N) + "', ");
        s.append("including exactly one '" + tiles.charAt(0) + "', denoting the empty tile; ");
        s.append("every " + N + " chars are interspersed with '" + separator + "'.\n\n");
        s.append("The traditional goal state is: " + makeState(tiles.substring(1,N*N) + tiles.charAt(0)) + "\n");
        s.append("Example random states and edges:\n");
        List<String> mytiles = Arrays.asList(tiles.substring(0, N*N).split(""));
        for (int i = 0; i < 10; i++) {
            Collections.shuffle(mytiles);
            String example = makeState(String.join("", mytiles));
            List<DirectedEdge<String>> edges = outgoingEdges(example);
            s.append(example + " --> " + edges.stream().map(e -> e.to()).collect(Collectors.joining(", ")) + "\n");
        }
        return s.toString();
    }


    /**
     * Unit tests the class
     * @param args  the command-line arguments
     */
    public static void main(String[] args) {
        try {
            System.out.println(new NPuzzle(Integer.valueOf(args[0])));
        } catch (Exception e) {
            // If there is an error, print it and a little command-line help
            e.printStackTrace();
            System.err.println();
            System.err.println("Usage: java AdjacencyGraph graph-file");
            System.exit(1);
        }
    }

}
