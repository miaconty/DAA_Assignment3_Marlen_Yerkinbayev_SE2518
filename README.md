# DAA Assignment 3 - Graphs

**Student:** Marlen Yerkinbayev  
**Group:** SE2518

This Maven project implements the required Assignment 3 algorithms: DSU, Kruskal, Prim, Kosaraju, Kahn topological ordering, and shortest paths in a DAG.

## Requirements

- JDK 17 or newer
- Maven 3.8+

## Build and test

```bash
mvn clean test
```

## Run the small demo

```bash
mvn -q exec:java -Dexec.mainClass=benchmark.Demo
```

## Run the full benchmark

```bash
mvn -q exec:java -Dexec.mainClass=benchmark.BenchmarkRunner
```

The benchmark creates `results/results.csv` and checks the required reference values automatically. The included CSV has 50 data rows: five algorithms for each of ten `(n, m)` cases.

## Recreate the three plots

```bash
python scripts/plot_results.py
```

The PNG files are written to `results/plots/`.

## Input format

First line:

```text
n m directed|undirected
```

Then `m` lines:

```text
u v w
```

Example input files are in `data/`, including a disconnected graph, a cyclic graph, and a DAG with negative weights.

## Project layout

```text
src/main/java/graph        Graph and Edge
src/main/java/algorithms   DSU and graph algorithms
src/main/java/metrics      Operation counters
src/main/java/benchmark    Generator, demo and benchmark
src/test/java              JUnit 5 tests
data/                      Small input graphs
results/results.csv        Benchmark output
results/plots/             Three required PNG charts
REPORT.md                  Assignment report
```

## Git workflow

Required local branches are included in the repository: `main`, `feature/dsu`, `feature/mst`, `feature/scc`, `feature/paths`, and `feature/metrics`. The release tag is `v1.0`.

Before Moodle submission, create your GitHub repository, push all branches and tags, and replace the line below with your real repository URL:

**GitHub repository:** `ADD_YOUR_GITHUB_LINK_HERE`

## AI-use acknowledgement

AI assistance was used to help organize the project, review requirements, and prepare explanations. The algorithms, tests, benchmark outputs, and report should be reviewed and understood by the student before defense.
