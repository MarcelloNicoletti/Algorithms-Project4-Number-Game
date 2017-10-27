public class BruteForce {
    public static void main (String[] args) {
        int bestTotal = bruteForce("317584", 0, 0, 0);
        System.out.println("\n The best is " + bestTotal);
    }

    private static int bruteForce (String number, int runningTotalA, int
            runningTotalB, int depth) {
        if (number.length() <= 0) {
            return runningTotalA;
        }

        int leftAdd = number.charAt(number.length() - 1) - '0';
        String notLast = number.substring(0, number.length() - 1);
        int leftTotalA = (depth % 2 == 0 ? leftAdd : 0) + runningTotalA;
        int leftTotalB = (depth % 2 != 0 ? leftAdd : 0) + runningTotalB;
        int left = bruteForce(notLast, leftTotalA, leftTotalB, depth + 1);

        for (int i = 0; i < depth; i++) {
            System.out.print("    ");
        }
        System.out.printf("%d:%s:%d%n", runningTotalA, number, runningTotalB);

        int rightAdd = number.charAt(0) - '0';
        String notFirst = number.substring(1, number.length());
        int rightTotalA = (depth % 2 == 0 ? rightAdd : 0) + runningTotalA;
        int rightTotalB = (depth % 2 != 0 ? rightAdd : 0) + runningTotalB;
        int right = bruteForce(notFirst, rightTotalA, rightTotalB, depth + 1);

        return Math.max(left, right);
    }
}
