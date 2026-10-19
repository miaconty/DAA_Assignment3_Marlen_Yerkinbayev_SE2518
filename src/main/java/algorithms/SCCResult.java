package algorithms;

import metrics.Metrics;
import java.util.List;

public class SCCResult {
    public final List<List<Integer>> components;
    public final Metrics metrics;

    public SCCResult(List<List<Integer>> components, Metrics metrics) {
        this.components = components;
        this.metrics = metrics;
    }
}
