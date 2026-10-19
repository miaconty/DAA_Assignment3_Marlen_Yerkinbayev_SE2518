package algorithms;

import graph.Edge;
import metrics.Metrics;
import java.util.List;

public class MSTResult {
    public final long totalWeight;
    public final List<Edge> edges;
    public final int components;
    public final Metrics metrics;

    public MSTResult(long totalWeight, List<Edge> edges, int components, Metrics metrics) {
        this.totalWeight = totalWeight;
        this.edges = edges;
        this.components = components;
        this.metrics = metrics;
    }
}
