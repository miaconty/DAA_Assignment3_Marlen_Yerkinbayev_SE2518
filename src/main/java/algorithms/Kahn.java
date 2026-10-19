package algorithms;

import graph.Edge;
import graph.Graph;
import metrics.Metrics;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Kahn {
    public static TopologicalResult run(Graph graph) {
        Metrics m = new Metrics();
        int n = graph.size();
        int[] indegree = new int[n];

        for (Edge e : graph.edges()) {
            indegree[e.to]++;
        }

        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) q.add(i);
        }

        List<Integer> order = new ArrayList<>(n);
        while (!q.isEmpty()) {
            int v = q.remove();
            order.add(v);
            for (Edge e : graph.neighbors(v)) {
                m.edgeScans++;
                indegree[e.to]--;
                if (indegree[e.to] == 0) q.add(e.to);
            }
        }

        return new TopologicalResult(order, order.size() != n, m);
    }

    public static boolean isValidOrder(Graph graph, List<Integer> order) {
        if (order.size() != graph.size()) return false;
        int[] pos = new int[graph.size()];
        for (int i = 0; i < order.size(); i++) pos[order.get(i)] = i;
        for (Edge e : graph.edges()) {
            if (pos[e.from] >= pos[e.to]) return false;
        }
        return true;
    }
}
