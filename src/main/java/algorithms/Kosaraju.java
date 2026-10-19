package algorithms;

import graph.Edge;
import graph.Graph;
import metrics.Metrics;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Kosaraju {
    private static class Frame {
        int v;
        int nextIndex;
        Frame(int v) { this.v = v; }
    }

    public static SCCResult run(Graph graph) {
        Metrics m = new Metrics();
        int n = graph.size();
        boolean[] visited = new boolean[n];
        List<Integer> order = new ArrayList<>(n);

        for (int s = 0; s < n; s++) {
            if (visited[s]) continue;
            Deque<Frame> stack = new ArrayDeque<>();
            visited[s] = true;
            stack.push(new Frame(s));
            m.updateDepth(stack.size());

            while (!stack.isEmpty()) {
                Frame f = stack.peek();
                List<Edge> list = graph.neighbors(f.v);
                if (f.nextIndex < list.size()) {
                    Edge e = list.get(f.nextIndex++);
                    m.edgeScans++;
                    if (!visited[e.to]) {
                        visited[e.to] = true;
                        stack.push(new Frame(e.to));
                        m.updateDepth(stack.size());
                    }
                } else {
                    order.add(f.v);
                    stack.pop();
                }
            }
        }

        Graph rev = graph.reversed();
        boolean[] seen = new boolean[n];
        List<List<Integer>> result = new ArrayList<>();

        for (int i = order.size() - 1; i >= 0; i--) {
            int s = order.get(i);
            if (seen[s]) continue;
            List<Integer> comp = new ArrayList<>();
            Deque<Integer> stack = new ArrayDeque<>();
            stack.push(s);
            seen[s] = true;
            m.updateDepth(stack.size());

            while (!stack.isEmpty()) {
                int v = stack.pop();
                comp.add(v);
                for (Edge e : rev.neighbors(v)) {
                    m.edgeScans++;
                    if (!seen[e.to]) {
                        seen[e.to] = true;
                        stack.push(e.to);
                        m.updateDepth(stack.size());
                    }
                }
            }
            result.add(comp);
        }

        return new SCCResult(result, m);
    }
}
