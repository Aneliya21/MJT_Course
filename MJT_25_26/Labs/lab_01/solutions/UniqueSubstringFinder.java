package solutions;
public class UniqueSubstringFinder {

    private static final int ARRAY_MAX_SIZE = 256;

    public static String longestUniqueSubstring(String s) {

        if (s == null || s.isEmpty()) {
            return "";
        }

        int n = s.length();
        int maxLen = 0;
        int startIndex = 0;

        for (int i = 0; i < n; i++) {
            boolean[] visited = new boolean[ARRAY_MAX_SIZE];
            for (int j = i; j < n; j++) {
                char c = s.charAt(j);

                if (visited[c]) {
                    break;
                } else {
                    visited[c] = true;
                    if (j - i + 1 > maxLen) {
                        maxLen = j - i + 1;
                        startIndex = i;
                    }
                }
            }
        }

        return s.substring(startIndex, startIndex + maxLen);
    }
}
