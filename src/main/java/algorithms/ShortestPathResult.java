package algorithms;

import metrics.Metrics;

public class ShortestPathResult {
    public final long[] distance;
    public final int[] parent;
    public final Metrics metrics;

    public ShortestPathResult(long[] distance, int[] parent, Metrics metrics) {
        this.distance = distance;
        this.parent = parent;
        this.metrics = metrics;
    }
}
