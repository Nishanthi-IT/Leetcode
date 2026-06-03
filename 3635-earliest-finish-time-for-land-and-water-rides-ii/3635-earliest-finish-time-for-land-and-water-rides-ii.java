import java.util.*;

class Solution {
    public int earliestFinishTime(int[] landStartTime, int[] landDuration,
                                  int[] waterStartTime, int[] waterDuration) {

        long ans = Math.min(
                solve(landStartTime, landDuration, waterStartTime, waterDuration),
                solve(waterStartTime, waterDuration, landStartTime, landDuration)
        );

        return (int) ans;
    }

    private long solve(int[] startA, int[] durA, int[] startB, int[] durB) {
        int m = startB.length;

        int[][] rides = new int[m][2];
        for (int i = 0; i < m; i++) {
            rides[i][0] = startB[i];
            rides[i][1] = durB[i];
        }

        Arrays.sort(rides, (x, y) -> Integer.compare(x[0], y[0]));

        int[] starts = new int[m];
        long[] prefixMinDur = new long[m];
        long[] suffixMinOpenPlusDur = new long[m];

        for (int i = 0; i < m; i++) {
            starts[i] = rides[i][0];
        }

        prefixMinDur[0] = rides[0][1];
        for (int i = 1; i < m; i++) {
            prefixMinDur[i] = Math.min(prefixMinDur[i - 1], rides[i][1]);
        }

        suffixMinOpenPlusDur[m - 1] =
                (long) rides[m - 1][0] + rides[m - 1][1];

        for (int i = m - 2; i >= 0; i--) {
            suffixMinOpenPlusDur[i] = Math.min(
                    suffixMinOpenPlusDur[i + 1],
                    (long) rides[i][0] + rides[i][1]
            );
        }

        long best = Long.MAX_VALUE;

        for (int i = 0; i < startA.length; i++) {
            long finishA = (long) startA[i] + durA[i];

            int pos = upperBound(starts, (int) finishA);

            long cur = Long.MAX_VALUE;

            if (pos >= 0) {
                cur = Math.min(cur, finishA + prefixMinDur[pos]);
            }

            if (pos + 1 < m) {
                cur = Math.min(cur, suffixMinOpenPlusDur[pos + 1]);
            }

            best = Math.min(best, cur);
        }

        return best;
    }

    private int upperBound(int[] arr, int target) {
        int l = 0, r = arr.length;
        while (l < r) {
            int mid = l + (r - l) / 2;
            if (arr[mid] <= target) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l - 1;
    }
}