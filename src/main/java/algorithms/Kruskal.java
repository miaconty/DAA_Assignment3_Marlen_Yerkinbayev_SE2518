package algorithms;

import graph.Edge;
import graph.Graph;
import metrics.Metrics;

import java.util.ArrayList;
import java.util.List;

public class Kruskal {
    public static MSTResult run(Graph graph) {
        Metrics m = new Metrics();
        List<Edge> sorted = new ArrayList<>(graph.edges());
        sorted.sort((a, b) -> {
            m.comparisons++;
            return Integer.compare(a.weight, b.weight);
        });

        DSU dsu = new DSU(graph.size(), m);
        List<Edge> chosen = new ArrayList<>();
        long total = 0;

        for (Edge e : sorted) {
            m.edgeScans++;
            int ru = dsu.find(e.from);
            int rv = dsu.find(e.to);
            if (ru != rv) {
                if (dsu.union(e.from, e.to)) {
                    chosen.add(e);
                    total += e.weight;
                    if (chosen.size() == graph.size() - 1) {
                        break;
                    }
                }
            }
        }
        return new MSTResult(total, chosen, dsu.components(), m);
    }
}
