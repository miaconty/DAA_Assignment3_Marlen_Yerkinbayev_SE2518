package metrics;

public class Metrics {
    public long findCalls;
    public long successfulUnions;
    public long edgeScans;
    public long comparisons;
    public long maxDepth;

    public long dsuOps() {
        return findCalls + successfulUnions;
    }

    public void updateDepth(long depth) {
        if (depth > maxDepth) {
            maxDepth = depth;
        }
    }
}
