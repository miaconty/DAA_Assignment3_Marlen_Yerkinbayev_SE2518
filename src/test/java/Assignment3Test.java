import algorithms.*;
import graph.Edge;
import graph.Graph;
import metrics.Metrics;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class Assignment3Test {

    @Test
    void dsuMatchesSimpleReference() {
        int n = 200;
        DSU dsu = new DSU(n, new Metrics());
        int[] ref = new int[n];
        for (int i = 0; i < n; i++) ref[i] = i;
        Random rnd = new Random(42);

        for (int step = 0; step < 1000; step++) {
            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);
            dsu.union(a, b);
            int old = ref[b];
            int value = ref[a];
            for (int i = 0; i < n; i++) {
                if (ref[i] == old) ref[i] = value;
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                assertEquals(ref[i] == ref[j], dsu.find(i) == dsu.find(j));
            }
        }
        assertEquals(Arrays.stream(ref).distinct().count(), dsu.components());
    }

    @Test
    void kruskalAndPrimAgreeOn100Graphs() {
        Random rnd = new Random(42);
        for (int test = 0; test < 100; test++) {
            int n = 5 + rnd.nextInt(26);
            Graph g = new Graph(n, false);
            for (int v = 1; v < n; v++) {
                int u = rnd.nextInt(v);
                g.addEdge(u, v, 1 + rnd.nextInt(50));
            }
            for (int e = 0; e < n * 2; e++) {
                int a = rnd.nextInt(n);
                int b = rnd.nextInt(n);
                if (a != b) g.addEdge(a, b, 1 + rnd.nextInt(50));
            }
            MSTResult k = Kruskal.run(g);
            MSTResult p = Prim.run(g);
            assertEquals(k.totalWeight, p.totalWeight);
            assertEquals(n - 1, k.edges.size());
            assertEquals(n - 1, p.edges.size());
        }
    }

    @Test
    void kosarajuMatchesBruteForce() {
        Random rnd = new Random(7);
        for (int test = 0; test < 30; test++) {
            int n = 5 + rnd.nextInt(25);
            Graph g = new Graph(n, true);
            for (int i = 0; i < n * 3; i++) {
                int a = rnd.nextInt(n);
                int b = rnd.nextInt(n);
                if (a != b) g.addEdge(a, b, 1);
            }

            SCCResult result = Kosaraju.run(g);
            int[] actual = componentIds(n, result.components);
            boolean[][] reach = reachability(g);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    assertEquals(reach[i][j] && reach[j][i], actual[i] == actual[j]);
                }
            }
        }
    }

    @Test
    void topologicalOrderAndCycleDetection() {
        Graph dag = new Graph(6, true);
        dag.addEdge(0, 1, 1);
        dag.addEdge(0, 2, 1);
        dag.addEdge(1, 3, 1);
        dag.addEdge(2, 3, 1);
        dag.addEdge(3, 4, 1);
        dag.addEdge(4, 5, 1);
        TopologicalResult good = Kahn.run(dag);
        assertFalse(good.hasCycle);
        assertTrue(Kahn.isValidOrder(dag, good.order));

        Graph cyclic = new Graph(3, true);
        cyclic.addEdge(0, 1, 1);
        cyclic.addEdge(1, 2, 1);
        cyclic.addEdge(2, 0, 1);
        assertTrue(Kahn.run(cyclic).hasCycle);
    }

    @Test
    void dagShortestPathMatchesBellmanFordWithNegativeWeights() {
        Random rnd = new Random(99);
        for (int test = 0; test < 40; test++) {
            int n = 8 + rnd.nextInt(15);
            Graph g = new Graph(n, true);
            for (int i = 0; i < n * 3; i++) {
                int a = rnd.nextInt(n - 1);
                int b = a + 1 + rnd.nextInt(n - a - 1);
                int w = -10 + rnd.nextInt(31);
                g.addEdge(a, b, w);
            }
            ShortestPathResult actual = DagShortestPath.run(g);
            long[] expected = bellmanFordFromAllSources(g);
            assertArrayEquals(expected, actual.distance);
        }
    }

    @Test
    void kosarajuIsStackSafeOn100000Path() {
        Graph g = new Graph(100000, true);
        for (int i = 0; i < 99999; i++) g.addEdge(i, i + 1, 1);
        SCCResult result = assertDoesNotThrow(() -> Kosaraju.run(g));
        assertEquals(100000, result.components.size());
    }

    private int[] componentIds(int n, List<List<Integer>> comps) {
        int[] id = new int[n];
        for (int i = 0; i < comps.size(); i++) {
            for (int v : comps.get(i)) id[v] = i;
        }
        return id;
    }

    private boolean[][] reachability(Graph g) {
        int n = g.size();
        boolean[][] r = new boolean[n][n];
        for (int s = 0; s < n; s++) {
            ArrayDeque<Integer> q = new ArrayDeque<>();
            q.add(s);
            r[s][s] = true;
            while (!q.isEmpty()) {
                int v = q.remove();
                for (Edge e : g.neighbors(v)) {
                    if (!r[s][e.to]) {
                        r[s][e.to] = true;
                        q.add(e.to);
                    }
                }
            }
        }
        return r;
    }

    private long[] bellmanFordFromAllSources(Graph g) {
        int n = g.size();
        int[] indegree = new int[n];
        for (Edge e : g.edges()) indegree[e.to]++;
        long inf = Long.MAX_VALUE / 4;
        long[] d = new long[n];
        Arrays.fill(d, inf);
        for (int i = 0; i < n; i++) if (indegree[i] == 0) d[i] = 0;

        for (int pass = 0; pass < n - 1; pass++) {
            boolean changed = false;
            for (Edge e : g.edges()) {
                if (d[e.from] != inf && d[e.from] + e.weight < d[e.to]) {
                    d[e.to] = d[e.from] + e.weight;
                    changed = true;
                }
            }
            if (!changed) break;
        }
        return d;
    }
}
