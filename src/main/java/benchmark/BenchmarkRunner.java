package benchmark;

import algorithms.*;
import graph.Graph;
import metrics.Metrics;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Supplier;

public class BenchmarkRunner {
    private static final int[] SIZES = {1000, 3162, 10000, 31623, 100000};
    private static final long[] REF_MST_SPARSE = {147970, 468911, 1486139, 4715515, 15012094};
    private static final long[] REF_MST_DENSE = {30363, 97589, 309550, 968517, 3056681};
    private static final long[] REF_SCC_SPARSE = {27, 137, 373, 1188, 3923};
    private static final long[] REF_SCC_DENSE = {1, 1, 1, 1, 1};
    private static final long[] REF_DAG_SPARSE = {396274, 1271911, 3941828, 12551891, 40096539};
    private static final long[] REF_DAG_DENSE = {252411, 723157, 2381342, 7376612, 23512087};

    private record Row(String algorithm, String graph, int n, int m, double timeMs,
                       long dsuOps, long edgeScans, long comparisons, long maxDepth, long result) {}

    private static <T> double median5(Supplier<T> action) {
        double[] times = new double[5];
        for (int i = 0; i < 5; i++) {
            long start = System.nanoTime();
            action.get();
            long end = System.nanoTime();
            times[i] = (end - start) / 1_000_000.0;
        }
        Arrays.sort(times);
        return times[2];
    }

    private static void check(long actual, long expected, String name) {
        if (actual != expected) {
            throw new IllegalStateException(name + " reference mismatch: got " + actual + ", expected " + expected);
        }
    }

    private static void writeRow(BufferedWriter out, Row r) throws IOException {
        out.write(String.format(Locale.US, "%s,%s,%d,%d,%.3f,%d,%d,%d,%d,%d%n",
                r.algorithm, r.graph, r.n, r.m, r.timeMs, r.dsuOps,
                r.edgeScans, r.comparisons, r.maxDepth, r.result));
        out.flush();
    }

    public static void main(String[] args) throws Exception {
        Files.createDirectories(Path.of("results"));
        Path csv = Path.of("results", "results.csv");

        try (BufferedWriter out = Files.newBufferedWriter(csv)) {
            out.write("algorithm,graph,n,m,time_ms,dsu_ops,edge_scans,comparisons,max_depth,result\n");

            for (int i = 0; i < SIZES.length; i++) {
                int n = SIZES[i];
                for (int densityIndex = 0; densityIndex < 2; densityIndex++) {
                    boolean dense = densityIndex == 1;
                    int m = (dense ? 20 : 4) * n;
                    String density = dense ? "dense" : "sparse";
                    System.out.println("Running n=" + n + " m=" + m + " (" + density + ")");

                    {
                    Graph undirected = GraphGenerator.undirected(n, m);
                    MSTResult kr = Kruskal.run(undirected);
                    MSTResult pr = Prim.run(undirected);
                    if (kr.totalWeight != pr.totalWeight) {
                        throw new IllegalStateException("Kruskal and Prim disagree");
                    }
                    long expectedMst = dense ? REF_MST_DENSE[i] : REF_MST_SPARSE[i];
                    check(kr.totalWeight, expectedMst, "MST");
                    double krTime = median5(() -> Kruskal.run(undirected));
                    double prTime = median5(() -> Prim.run(undirected));
                    writeRow(out, new Row("Kruskal", "mst_" + density, n, m, krTime,
                            kr.metrics.dsuOps(), kr.metrics.edgeScans, kr.metrics.comparisons,
                            kr.metrics.maxDepth, kr.totalWeight));
                    writeRow(out, new Row("Prim", "mst_" + density, n, m, prTime,
                            pr.metrics.dsuOps(), pr.metrics.edgeScans, pr.metrics.comparisons,
                            pr.metrics.maxDepth, pr.totalWeight));
                    }
                    System.gc();

                    {
                    Graph directed = GraphGenerator.directed(n, m);
                    SCCResult scc = Kosaraju.run(directed);
                    long expectedScc = dense ? REF_SCC_DENSE[i] : REF_SCC_SPARSE[i];
                    check(scc.components.size(), expectedScc, "SCC");
                    check(scc.metrics.edgeScans, 2L * m, "Kosaraju edge scans");
                    double sccTime = median5(() -> Kosaraju.run(directed));
                    writeRow(out, new Row("Kosaraju", "directed_" + density, n, m, sccTime,
                            scc.metrics.dsuOps(), scc.metrics.edgeScans, scc.metrics.comparisons,
                            scc.metrics.maxDepth, scc.components.size()));
                    }
                    System.gc();

                    {
                    Graph dag = GraphGenerator.dag(n, m);
                    TopologicalResult topo = Kahn.run(dag);
                    if (topo.hasCycle || !Kahn.isValidOrder(dag, topo.order)) {
                        throw new IllegalStateException("Invalid topological order");
                    }
                    check(topo.metrics.edgeScans, m, "Kahn edge scans");
                    ShortestPathResult sp = DagShortestPath.run(dag);
                    long sum = DagShortestPath.sumDistances(sp);
                    long expectedDag = dense ? REF_DAG_DENSE[i] : REF_DAG_SPARSE[i];
                    check(sum, expectedDag, "DAG shortest paths");
                    check(sp.metrics.edgeScans, m, "DAG shortest path edge scans");
                    double kahnTime = median5(() -> Kahn.run(dag));
                    double spTime = median5(() -> DagShortestPath.run(dag));
                    writeRow(out, new Row("Kahn", "dag_" + density, n, m, kahnTime,
                            topo.metrics.dsuOps(), topo.metrics.edgeScans, topo.metrics.comparisons,
                            topo.metrics.maxDepth, topo.order.size()));
                    writeRow(out, new Row("DagShortestPath", "dag_" + density, n, m, spTime,
                            sp.metrics.dsuOps(), sp.metrics.edgeScans, sp.metrics.comparisons,
                            sp.metrics.maxDepth, sum));
                    }
                    System.gc();
                }
            }
        }
        System.out.println("Done. CSV: " + csv.toAbsolutePath());
    }
}
