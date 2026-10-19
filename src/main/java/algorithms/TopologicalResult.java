package algorithms;

import metrics.Metrics;
import java.util.List;

public class TopologicalResult {
    public final List<Integer> order;
    public final boolean hasCycle;
    public final Metrics metrics;

    public TopologicalResult(List<Integer> order, boolean hasCycle, Metrics metrics) {
        this.order = order;
        this.hasCycle = hasCycle;
        this.metrics = metrics;
    }
}
