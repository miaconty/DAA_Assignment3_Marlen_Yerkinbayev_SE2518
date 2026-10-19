package algorithms;

import metrics.Metrics;

public class DSU {
    private final int[] parent;
    private final int[] rank;
    private int components;
    private final Metrics metrics;

    public DSU(int n, Metrics metrics) {
        this.parent = new int[n];
        this.rank = new int[n];
        this.components = n;
        this.metrics = metrics;
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }

    public int find(int x) {
        metrics.findCalls++;
        int root = x;
        while (root != parent[root]) {
            root = parent[root];
        }
        while (x != root) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }
        return root;
    }

    public boolean union(int a, int b) {
        int ra = find(a);
        int rb = find(b);
        if (ra == rb) {
            return false;
        }
        if (rank[ra] < rank[rb]) {
            parent[ra] = rb;
        } else if (rank[ra] > rank[rb]) {
            parent[rb] = ra;
        } else {
            parent[rb] = ra;
            rank[ra]++;
        }
        components--;
        metrics.successfulUnions++;
        return true;
    }

    public int components() {
        return components;
    }
}
