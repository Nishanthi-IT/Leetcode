class Solution {
    static final int MOD = 1_000_000_007;
    int LOG;
    int[][] up;
    int[] depth;

    public int[] assignEdgeWeights(int[][] edges, int[][] queries) {
        int n = edges.length + 1;

        LOG = 1;
        while ((1 << LOG) <= n) LOG++;

        java.util.ArrayList<Integer>[] graph = new java.util.ArrayList[n + 1];
        for (int i = 1; i <= n; i++) {
            graph[i] = new java.util.ArrayList<>();
        }

        for (int[] e : edges) {
            int u = e[0], v = e[1];
            graph[u].add(v);
            graph[v].add(u);
        }

        depth = new int[n + 1];
        up = new int[LOG][n + 1];

        java.util.ArrayDeque<Integer> queue = new java.util.ArrayDeque<>();
        queue.offer(1);
        up[0][1] = 0;
        depth[1] = 0;

        boolean[] visited = new boolean[n + 1];
        visited[1] = true;

        while (!queue.isEmpty()) {
            int node = queue.poll();

            for (int nei : graph[node]) {
                if (!visited[nei]) {
                    visited[nei] = true;
                    depth[nei] = depth[node] + 1;
                    up[0][nei] = node;
                    queue.offer(nei);
                }
            }
        }

        for (int j = 1; j < LOG; j++) {
            for (int i = 1; i <= n; i++) {
                int mid = up[j - 1][i];
                up[j][i] = (mid == 0) ? 0 : up[j - 1][mid];
            }
        }

        long[] pow2 = new long[n + 1];
        pow2[0] = 1;
        for (int i = 1; i <= n; i++) {
            pow2[i] = (pow2[i - 1] * 2) % MOD;
        }

        int m = queries.length;
        int[] ans = new int[m];

        for (int i = 0; i < m; i++) {
            int u = queries[i][0];
            int v = queries[i][1];

            int lca = lca(u, v);
            int dist = depth[u] + depth[v] - 2 * depth[lca];

            if (dist == 0) {
                ans[i] = 0;
            } else {
                ans[i] = (int) pow2[dist - 1];
            }
        }

        return ans;
    }

    private int lca(int a, int b) {
        if (depth[a] < depth[b]) {
            int temp = a;
            a = b;
            b = temp;
        }

        int diff = depth[a] - depth[b];

        for (int j = 0; j < LOG; j++) {
            if (((diff >> j) & 1) == 1) {
                a = up[j][a];
            }
        }

        if (a == b) return a;

        for (int j = LOG - 1; j >= 0; j--) {
            if (up[j][a] != up[j][b]) {
                a = up[j][a];
                b = up[j][b];
            }
        }

        return up[0][a];
    }
}