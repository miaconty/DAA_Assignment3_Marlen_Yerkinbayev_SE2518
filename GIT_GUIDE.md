# Git and GitHub Submission Guide

The ZIP already contains a local Git repository with the required branches and tag. After creating an empty GitHub repository, run these commands from the project folder:

```bash
git status
git branch -a
git tag
git log --oneline --graph --decorate --all
```

Add your GitHub remote:

```bash
git remote add origin YOUR_GITHUB_REPOSITORY_URL
git push -u origin main
git push origin feature/dsu feature/mst feature/scc feature/paths feature/metrics
git push origin v1.0
```

Then put the same GitHub URL into `README.md` before final submission.

Useful defense commands:

```bash
mvn clean test
mvn -q exec:java -Dexec.mainClass=benchmark.Demo
mvn -q exec:java -Dexec.mainClass=benchmark.BenchmarkRunner
```
