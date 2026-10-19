package benchmark;

import graph.Graph;

import java.util.Random;

public class GraphGenerator {
    private static void shuffle(int[] a, Random rnd) {
        for (int i = a.length - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            int t = a[i];
            a[i] = a[j];
            a[j] = t;
        }
    }

    public static Graph undirected(int n, int m) {
        Random rnd = new Random(42);
        Graph g = new Graph(n, false);
        int[] perm = new int[n];
        for (int i = 0; i < n; i++) perm[i] = i;
        shuffle(perm, rnd);

        for (int i = 1; i < n; i++) {
            int u = perm[rnd.nextInt(i)];
            int v = perm[i];
            int w = 1 + rnd.nextInt(1000);
            g.addEdge(u, v, w);
        }
        for (int i = n - 1; i < m; i++) {
            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);
            while (b == a) b = rnd.nextInt(n);
            int w = 1 + rnd.nextInt(1000);
            g.addEdge(a, b, w);
        }
        return g;
    }

    public static Graph directed(int n, int m) {
        Random rnd = new Random(42);
        Graph g = new Graph(n, true);
        for (int i = 0; i < m; i++) {
            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);
            while (b == a) b = rnd.nextInt(n);
            int w = 1 + rnd.nextInt(1000);
            g.addEdge(a, b, w);
        }
        return g;
    }

    public static Graph dag(int n, int m) {
        Random rnd = new Random(42);
        Graph g = new Graph(n, true);
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) rank[i] = i;
        shuffle(rank, rnd);

        for (int i = 0; i < m; i++) {
            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);
            while (b == a) b = rnd.nextInt(n);
            if (rank[a] > rank[b]) {
                int t = a;
                a = b;
                b = t;
            }
            int w = 1 + rnd.nextInt(1000);
            g.addEdge(a, b, w);
        }
        return g;
    }
}
