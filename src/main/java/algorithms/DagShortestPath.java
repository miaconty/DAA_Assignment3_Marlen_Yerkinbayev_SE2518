package algorithms;

import graph.Edge;
import graph.Graph;
import metrics.Metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DagShortestPath {
    public static ShortestPathResult run(Graph graph) {
        TopologicalResult topo = Kahn.run(graph);
        if (topo.hasCycle) {
            throw new IllegalArgumentException("Graph is not a DAG");
        }

        Metrics m = new Metrics();
        int n = graph.size();
        int[] indegree = new int[n];
        for (Edge e : graph.edges()) indegree[e.to]++;

        long inf = Long.MAX_VALUE / 4;
        long[] dist = new long[n];
        int[] parent = new int[n];
        for (int i = 0; i < n; i++) {
            dist[i] = inf;
            parent[i] = -1;
            if (indegree[i] == 0) dist[i] = 0;
        }

        for (int v : topo.order) {
            if (dist[v] == inf) continue;
            for (Edge e : graph.neighbors(v)) {
                m.edgeScans++;
                long nd = dist[v] + e.weight;
                if (nd < dist[e.to]) {
                    dist[e.to] = nd;
                    parent[e.to] = v;
                }
            }
        }
        return new ShortestPathResult(dist, parent, m);
    }

    public static List<Integer> restorePath(ShortestPathResult result, int target) {
        List<Integer> path = new ArrayList<>();
        int v = target;
        while (v != -1) {
            path.add(v);
            v = result.parent[v];
        }
        Collections.reverse(path);
        return path;
    }

    public static long sumDistances(ShortestPathResult result) {
        long sum = 0;
        for (long d : result.distance) {
            if (d < Long.MAX_VALUE / 8) sum += d;
        }
        return sum;
    }
}
