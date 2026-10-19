package algorithms;

import graph.Edge;
import graph.Graph;
import metrics.Metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Prim {
    private static class Item {
        int vertex;
        int parent;
        int weight;

        Item(int vertex, int parent, int weight) {
            this.vertex = vertex;
            this.parent = parent;
            this.weight = weight;
        }
    }

    public static MSTResult run(Graph graph) {
        Metrics m = new Metrics();
        boolean[] used = new boolean[graph.size()];
        List<Edge> chosen = new ArrayList<>();
        long total = 0;
        int components = 0;

        PriorityQueue<Item> pq = new PriorityQueue<>((a, b) -> {
            m.comparisons++;
            return Integer.compare(a.weight, b.weight);
        });

        for (int start = 0; start < graph.size(); start++) {
            if (used[start]) continue;
            components++;
            pq.add(new Item(start, -1, 0));

            while (!pq.isEmpty()) {
                Item cur = pq.poll();
                if (used[cur.vertex]) continue;
                used[cur.vertex] = true;

                if (cur.parent != -1) {
                    chosen.add(new Edge(cur.parent, cur.vertex, cur.weight));
                    total += cur.weight;
                }

                for (Edge e : graph.neighbors(cur.vertex)) {
                    m.edgeScans++;
                    if (!used[e.to]) {
                        pq.add(new Item(e.to, cur.vertex, e.weight));
                    }
                }
            }
        }
        return new MSTResult(total, chosen, components, m);
    }
}
