package graph;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final int n;
    private final boolean directed;
    private final List<List<Edge>> adj;
    private final List<Edge> edges;

    public Graph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;
        this.adj = new ArrayList<>(n);
        this.edges = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
    }

    public int size() {
        return n;
    }

    public boolean isDirected() {
        return directed;
    }

    public List<Edge> neighbors(int v) {
        return adj.get(v);
    }

    public List<Edge> edges() {
        return edges;
    }

    public void addEdge(int u, int v, int w) {
        if (u < 0 || u >= n || v < 0 || v >= n) {
            throw new IllegalArgumentException("Vertex out of range");
        }
        Edge e = new Edge(u, v, w);
        adj.get(u).add(e);
        edges.add(e);
        if (!directed) {
            adj.get(v).add(new Edge(v, u, w));
        }
    }

    public Graph reversed() {
        Graph r = new Graph(n, true);
        for (Edge e : edges) {
            r.addEdge(e.to, e.from, e.weight);
        }
        return r;
    }

    public static Graph fromFile(Path path) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String[] first = br.readLine().trim().split("\\s+");
            int n = Integer.parseInt(first[0]);
            int m = Integer.parseInt(first[1]);
            boolean directed = first[2].equalsIgnoreCase("directed");
            Graph g = new Graph(n, directed);
            for (int i = 0; i < m; i++) {
                String line = br.readLine();
                if (line == null) {
                    throw new IOException("Not enough edge lines");
                }
                String[] p = line.trim().split("\\s+");
                g.addEdge(Integer.parseInt(p[0]), Integer.parseInt(p[1]), Integer.parseInt(p[2]));
            }
            return g;
        }
    }
}
