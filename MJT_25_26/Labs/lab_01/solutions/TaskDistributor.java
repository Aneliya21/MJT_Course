package solutions;
public class TaskDistributor {

    public static int minDifference(int[] tasks) {
        if (tasks == null || tasks.length == 0) {
            return 0;
        }

        int total = 0;
        for (int t : tasks) {
            total += t;
        }

        int half = total / 2;
        boolean[] dp = new boolean[half + 1];
        dp[0] = true;

        for (int t : tasks) {
            for (int j = half; j >= t; j--) {
                dp[j] = dp[j] || dp[j - t];
            }
        }

        int best = 0;
        for (int i = half; i >= 0; i--) {
            if (dp[i]) {
                best = i;
                break;
            }
        }

        return total - 2 * best;
    }
}
