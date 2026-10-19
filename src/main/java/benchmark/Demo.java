package benchmark;

import algorithms.*;
import graph.Graph;

import java.nio.file.Path;

public class Demo {
    public static void main(String[] args) throws Exception {
        Graph g = Graph.fromFile(Path.of("data", "connected.txt"));
        MSTResult k = Kruskal.run(g);
        MSTResult p = Prim.run(g);
        System.out.println("Kruskal MST weight: " + k.totalWeight);
        System.out.println("Prim MST weight: " + p.totalWeight);
        System.out.println("Components: " + k.components);

        Graph dag = Graph.fromFile(Path.of("data", "dag_negative.txt"));
        TopologicalResult t = Kahn.run(dag);
        System.out.println("Topological order: " + t.order);
        ShortestPathResult sp = DagShortestPath.run(dag);
        System.out.println("Path to 5: " + DagShortestPath.restorePath(sp, 5));
        System.out.println("Distance to 5: " + sp.distance[5]);
    }
}
