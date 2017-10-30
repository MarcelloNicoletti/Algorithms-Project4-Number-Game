import java.util.Arrays;

public class Greedy {
    public static void main (String[] args) {
        int bestTotal = greedy(new int[]{3, 1, 7, 5, 8, 4}, 0, 0, 0);
        System.out.println("\n The best is " + bestTotal);
    }

    private static int greedy (int[] digits, int runningTotalA,
            int runningTotalB, int depth) {
        if (digits.length <= 0) {
            return runningTotalA;
        }

        int additional = 0;
        int[] newDigits;
        if (digits[0] < digits[digits.length - 1]) {
            additional = digits[digits.length - 1];
            newDigits = Arrays.copyOf(digits, digits.length - 1);
        } else {
            additional = digits[0];
            newDigits = Arrays.copyOfRange(digits, 1, digits.length);
        }

        if (depth % 2 == 0) {
            runningTotalA += additional;
        } else {
            runningTotalB += additional;
        }

        return greedy(newDigits, runningTotalA, runningTotalB, depth + 1);
    }
}
