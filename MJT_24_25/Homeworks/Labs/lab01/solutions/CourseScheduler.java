public class CourseScheduler {

    public static void swapTwoIntervals(int[] a, int[] b) {
        int temp = a[0];
        a[0] = b[0];
        b[0] = temp;

        int temp2 = a[1];
        a[1] = b[1];
        b[1] = temp2;
    }
    public static int compareTwoIntervals(int[] a, int[] b) {
        if(a[0] == b[0] && a[1] == b[1]){
            return 0;
        }
        else if(a[0] == b[0] && a[1] < b[1]){
            return -1;
        }
        else if(a[0] == b[0] && a[1] > b[1]){
            return 1;
        }

        else if(a[0] < b[0]){
            return -1;
        }
        return 1; // if a[0] > b[0]
    }
    public static void sortCourses(int[][] courses) {
        int rows = courses.length;
        for (int i = 0; i < rows - 1; i++) {
            for(int j = 0; j < rows - i - 1; j++) {
                if(compareTwoIntervals(courses[j], courses[j + 1]) == 1){
                    swapTwoIntervals(courses[j], courses[j + 1]);
                }
            }
        }
    }
    public static int maxNonOverlappingCourses(int[][] courses) {

        int rows = courses.length;

        if(rows == 0) {
            return 0;
        }

        sortCourses(courses);

        int maxCount = 1, currentCount = 1;

        int columnIndex = 0;
        int currentEnd = courses[0][columnIndex + 1];

        for (int i = 0; i < rows - 1; i++) {
            for (int j = i + 1; j < rows; j++) {
                if(currentEnd <= courses[j][columnIndex] && courses[j - 1][columnIndex] < courses[j][columnIndex]) {
                    currentCount++;
                    currentEnd = courses[j][columnIndex + 1];
                }
            }

            currentEnd = courses[i + 1][columnIndex + 1];

            if(currentCount > maxCount) {
                maxCount = currentCount;
            }
            currentCount = 1;
        }
        return maxCount;
    }

    public static void main(String[] args) {

        int[][] courses = {{15, 18}, {9,11}, {12, 14}};

        System.out.println(maxNonOverlappingCourses(courses));

    }
}

  
