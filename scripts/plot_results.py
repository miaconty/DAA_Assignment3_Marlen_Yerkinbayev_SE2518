import csv
from pathlib import Path
import matplotlib.pyplot as plt

ROOT = Path(__file__).resolve().parents[1]
CSV = ROOT / 'results' / 'results.csv'
OUT = ROOT / 'results' / 'plots'
OUT.mkdir(parents=True, exist_ok=True)

with CSV.open() as f:
    rows = list(csv.DictReader(f))

for r in rows:
    for k in ['n','m','dsu_ops','edge_scans','comparisons','max_depth','result']:
        r[k] = int(r[k])
    r['time_ms'] = float(r['time_ms'])
    r['operations'] = r['dsu_ops'] + r['edge_scans'] + r['comparisons']
    r['scans_per_edge'] = r['edge_scans'] / r['m']

# 1. Operations vs n
plt.figure(figsize=(10, 6))
for algorithm in ['Kruskal','Prim','Kosaraju','Kahn','DagShortestPath']:
    for density in ['sparse','dense']:
        pts = [r for r in rows if r['algorithm'] == algorithm and r['graph'].endswith(density)]
        pts.sort(key=lambda x: x['n'])
        plt.plot([p['n'] for p in pts], [p['operations'] for p in pts], marker='o', label=f'{algorithm} {density}')
plt.xscale('log')
plt.yscale('log')
plt.xlabel('n (vertices)')
plt.ylabel('Counted operations')
plt.title('Operations vs n')
plt.grid(True, which='both', alpha=0.25)
plt.legend(fontsize=8, ncol=2)
plt.tight_layout()
plt.savefig(OUT / 'operations_vs_n.png', dpi=160)
plt.close()

# 2. Scans per edge
plt.figure(figsize=(10, 6))
for algorithm in ['Kruskal','Prim','Kosaraju','Kahn','DagShortestPath']:
    for density in ['sparse','dense']:
        pts = [r for r in rows if r['algorithm'] == algorithm and r['graph'].endswith(density)]
        pts.sort(key=lambda x: x['n'])
        plt.plot([p['n'] for p in pts], [p['scans_per_edge'] for p in pts], marker='o', label=f'{algorithm} {density}')
plt.xscale('log')
plt.xlabel('n (vertices)')
plt.ylabel('edgeScans / m')
plt.title('Scans per edge')
plt.grid(True, which='both', alpha=0.25)
plt.legend(fontsize=8, ncol=2)
plt.tight_layout()
plt.savefig(OUT / 'scans_per_edge.png', dpi=160)
plt.close()

# 3. Kruskal share of examined edges
plt.figure(figsize=(8, 5))
for density in ['sparse','dense']:
    pts = [r for r in rows if r['algorithm'] == 'Kruskal' and r['graph'].endswith(density)]
    pts.sort(key=lambda x: x['n'])
    plt.plot([p['n'] for p in pts], [100 * p['scans_per_edge'] for p in pts], marker='o', label=density.capitalize())
plt.xscale('log')
plt.xlabel('n (vertices)')
plt.ylabel('Examined edges (%)')
plt.title('Share of edges Kruskal examines')
plt.grid(True, which='both', alpha=0.25)
plt.legend()
plt.tight_layout()
plt.savefig(OUT / 'kruskal_edge_share.png', dpi=160)
plt.close()

print('Created 3 plots in', OUT)
