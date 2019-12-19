
import java.util.*;

import java.util.stream.Collectors;

public class PathFinder<V> {

    private DirectedGraph<V> graph;
    private long startTimeMillis;

    public PathFinder(DirectedGraph<V> graph) {
        this.graph = graph;
    }

    public class Result<V> {
        public final boolean success;
        public final V start;
        public final V goal;
        public final double cost;
        public final List<V> path;
        public final int visitedNodes;
        public final double elapsedTime;

        public Result(boolean success, V start, V goal, double cost, List<V> path, int visitedNodes) {
            this.success = success;
            this.start = start;
            this.goal = goal;
            this.cost = cost;
            this.path = path;
            this.visitedNodes = visitedNodes;
            this.elapsedTime = (System.currentTimeMillis() - startTimeMillis) / 1000.0;
        }

        public String toString() {
            String s = "";
            s += String.format("Visited nodes: %d\n", visitedNodes);
            s += String.format("Elapsed time: %.1f seconds\n", elapsedTime);
            if (success) {
                s += String.format("Total cost from %s -> %s: %s\n", start, goal, cost);
                s += "Path: " + path.stream().map(x -> x.toString()).collect(Collectors.joining(" -> "));
            } else {
                s += String.format("No path found from %s", start);
            }
            return s;
        }
    }

    public Result<V> search(String algorithm, V start, V goal) {
        startTimeMillis = System.currentTimeMillis();
        switch (algorithm) {
            case "random":
                return searchRandom(start, goal);
            case "dijkstra":
                return searchDijkstra(start, goal);
            case "astar":
                return searchAstar(start, goal);
        }
        throw new IllegalArgumentException("Unknown search algorithm: " + algorithm);
    }

    public Result<V> searchRandom(V start, V goal) {
        int visitedNodes = 0;
        LinkedList<V> path = new LinkedList<>();
        double cost = 0.0;
        Random random = new Random();

        V current = start;
        path.add(current);
        while (current != null) {
            visitedNodes++;
            if (current.equals(goal)) {
                return new Result<>(true, start, current, cost, path, visitedNodes);
            }

            List<DirectedEdge<V>> neighbours = graph.outgoingEdges(start);
            if (neighbours == null || neighbours.size() == 0) {
                break;
            } else {
                DirectedEdge<V> edge = neighbours.get(random.nextInt(neighbours.size()));
                cost += edge.weight();
                current = edge.to();
                path.add(current);
            }
        }
        return new Result<>(false, start, null, -1, null, visitedNodes);
    }

    public Result<V> searchDijkstra(V start, V goal) {
        int visitedNodes = 0;
        HashMap<V, DirectedEdge<V>> edgeTo = new HashMap<>();
        HashMap<V, Double> distTo = new HashMap<>();
        PriorityQueue<V> queue = new PriorityQueue<>(Comparator.comparingDouble(distTo::get));
        HashSet<V> visited = new HashSet<>();
        queue.add(start);
        distTo.put(start, 0.0);
        while (!queue.isEmpty()) {
            V current = queue.remove();
            visitedNodes++;
            if (!visited.contains(current)) {
                visited.add(current);
                if (current.equals(goal)) {
                    ArrayList<V> path = new ArrayList<>();
                    DirectedEdge<V> currentEdge = edgeTo.get(goal);
                    while (currentEdge != null) {
                        path.add(currentEdge.to());
                        currentEdge = edgeTo.get(currentEdge.from());
                    }
                    path.add(start);
                    Collections.reverse(path);
                    return new Result<>(true, start, goal, distTo.get(goal), path, visitedNodes);
                }
                for (DirectedEdge<V> edge : graph.outgoingEdges(current)) {
                    V to = edge.to();
                    double newDist = distTo.get(current) + edge.weight();
                    if (distTo.get(to) == null || distTo.get(to) > newDist) {
                        distTo.put(to, newDist);
                        edgeTo.put(to, edge);
                        queue.add(to);
                    }
                }
            }
        }
        return new Result<>(false, start, null, -1, null, visitedNodes);
    }

    public Result<V> searchAstar(V start, V goal) {
        int visitedNodes = 0;
        HashMap<V, DirectedEdge<V>> edgeTo = new HashMap<>();
        HashMap<V, Double> distTo = new HashMap<>();
        PriorityQueue<V> queue = new PriorityQueue<>((left, right) -> {
            double leftDist = distTo.get(left) + graph.guessCost(left, goal);
            double rightDist = distTo.get(right) + graph.guessCost(right, goal);
            return Double.compare(leftDist, rightDist);
        });
        HashSet<V> visited = new HashSet<>();
        queue.add(start);
        distTo.put(start, 0.0);
        while (!queue.isEmpty()) {
            V current = queue.remove();
            if (!visited.contains(current)) {
                visitedNodes++;
                visited.add(current);
                if (current.equals(goal)) {
                    ArrayList<V> path = new ArrayList<>();
                    DirectedEdge<V> currentEdge = edgeTo.get(goal);
                    while (currentEdge != null) {
                        path.add(currentEdge.to());
                        currentEdge = edgeTo.get(currentEdge.from());
                    }
                    path.add(start);
                    Collections.reverse(path);
                    return new Result<>(true, start, goal, distTo.get(goal), path, visitedNodes);
                }
                for (DirectedEdge<V> edge : graph.outgoingEdges(current)) {
                    V to = edge.to();
                    double newDist = distTo.get(current) + edge.weight();
                    if (distTo.get(to) == null || distTo.get(to) > newDist) {
                        distTo.put(to, newDist);
                        edgeTo.put(to, edge);
                        queue.add(to);
                    }
                }
            }
        }
        return new Result<>(false, start, null, -1, null, visitedNodes);
    }

}
