

public class RunPathFinder {    

    private static void testGridGraph(GridGraph graph, String algorithm, String start, String goal) {
        PathFinder<GridGraph.Coord> finder = new PathFinder<>(graph);
        String[] startC = start.split(":");
        String[] goalC = goal.split(":");
        PathFinder<GridGraph.Coord>.Result<GridGraph.Coord> result =
            finder.search(algorithm,
                          new GridGraph.Coord(Integer.valueOf(startC[0]), Integer.valueOf(startC[1])),
                          new GridGraph.Coord(Integer.valueOf(goalC[0]), Integer.valueOf(goalC[1])));
        if (result.success && graph.width() < 250 && graph.height() < 250)
            System.out.println(graph.showGrid(result.path));
        System.out.println(result);
    }


    public static void main(String[] args) {
        try {
            if (args.length != 5) throw new IllegalArgumentException();
            String algorithm = args[0], graphType = args[1], filePath = args[2], start = args[3], goal = args[4];
            PathFinder<String> finder;
            switch (graphType) {

            case "AdjacencyGraph":
                finder = new PathFinder<>(new AdjacencyGraph(filePath));
                System.out.println(finder.search(algorithm, start, goal));
                break;

            case "WordLadder":
                finder = new PathFinder<>(new WordLadder(filePath));
                System.out.println(finder.search(algorithm, start, goal));
                break;

            case "NPuzzle":
                finder = new PathFinder<>(new NPuzzle(Integer.valueOf(filePath)));
                System.out.println(finder.search(algorithm, start, goal));
                break;

            case "GridGraph":
                testGridGraph(new GridGraph(filePath), algorithm, start, goal);
                break;

            default:
                throw new IllegalArgumentException("Unknown graph type: " + graphType);
            }

        } catch (Exception e) {
            // If there is an error, print it and a little command-line help
            e.printStackTrace();
            System.err.println();
            System.err.println("Usage: java RunPathFinder algorithm graphtype graph start goal");
            System.err.println("  where algorithm = random | dijkstra | astar");
            System.err.println("        graphtype = AdjacencyGraph | WordLadder | NPuzzle | GridGraph");
            System.exit(1);
        }
    }

}

