class Solution {
    private long[][][][][] cntMemo;
    private long[][][][][] wavMemo;
    private char[] digits;

    public long totalWaviness(long num1, long num2) {
        return solve(num2) - solve(num1 - 1);
    }

    private long solve(long x) {
        if (x <= 0) return 0;

        digits = String.valueOf(x).toCharArray();
        int n = digits.length;

        cntMemo = new long[n + 1][11][11][2][2];
        wavMemo = new long[n + 1][11][11][2][2];

        for (int i = 0; i <= n; i++) {
            for (int j = 0; j < 11; j++) {
                for (int k = 0; k < 11; k++) {
                    for (int s = 0; s < 2; s++) {
                        for (int t = 0; t < 2; t++) {
                            cntMemo[i][j][k][s][t] = -1;
                            wavMemo[i][j][k][s][t] = -1;
                        }
                    }
                }
            }
        }

        return dfs(0, 10, 10, 0, 1).wav;
    }

    private Node dfs(int pos, int prev2, int prev1, int started, int tight) {
        if (pos == digits.length) {
            return new Node(1, 0);
        }

        if (tight == 0 &&
            cntMemo[pos][prev2][prev1][started][0] != -1) {
            return new Node(
                cntMemo[pos][prev2][prev1][started][0],
                wavMemo[pos][prev2][prev1][started][0]
            );
        }

        int limit = tight == 1 ? digits[pos] - '0' : 9;

        long totalCnt = 0;
        long totalWav = 0;

        for (int d = 0; d <= limit; d++) {
            int ntight = (tight == 1 && d == limit) ? 1 : 0;

            if (started == 0 && d == 0) {
                Node nxt = dfs(pos + 1, 10, 10, 0, ntight);
                totalCnt += nxt.cnt;
                totalWav += nxt.wav;
            } else if (started == 0) {
                Node nxt = dfs(pos + 1, 10, d, 1, ntight);
                totalCnt += nxt.cnt;
                totalWav += nxt.wav;
            } else if (prev2 == 10) {
                Node nxt = dfs(pos + 1, prev1, d, 1, ntight);
                totalCnt += nxt.cnt;
                totalWav += nxt.wav;
            } else {
                long add = 0;

                if ((prev1 > prev2 && prev1 > d) ||
                    (prev1 < prev2 && prev1 < d)) {
                    add = 1;
                }

                Node nxt = dfs(pos + 1, prev1, d, 1, ntight);

                totalCnt += nxt.cnt;
                totalWav += nxt.wav + add * nxt.cnt;
            }
        }

        if (tight == 0) {
            cntMemo[pos][prev2][prev1][started][0] = totalCnt;
            wavMemo[pos][prev2][prev1][started][0] = totalWav;
        }

        return new Node(totalCnt, totalWav);
    }

    static class Node {
        long cnt;
        long wav;

        Node(long cnt, long wav) {
            this.cnt = cnt;
            this.wav = wav;
        }
    }
}