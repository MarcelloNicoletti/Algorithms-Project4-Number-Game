import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static Scanner stdIn = new Scanner(System.in);

    public static void main (String args[]) {
        System.out.println("Welcome to the number game.");
        System.out.print("How many digits will there be? >");
        int numDigits = stdIn.nextInt(); stdIn.nextLine();
        int[] digits = new int[numDigits];

        for (int i = 0; i < numDigits; i++) {
            System.out.print("Enter digit for position " + (i + 1) + " >");
            digits[i] = stdIn.nextInt() % 10;
        }
        System.out.println("Playing with " + Arrays.toString(digits));

        MemoMatrix<Solution> memo = new MemoMatrix<>(numDigits, numDigits);

        int bestScore = gameSolution(digits.length - 1, 0, digits, memo)
                .total;
        memo.printMatrix();
    }

    private static Solution gameSolution (int i, int j, int[] digits,
            MemoMatrix<Solution> memo) {
        if (i < 0 || j < 0 || i >= digits.length || j >= digits.length) {
            return new Solution(0, 0, false);
        }
        if (i < j) {
            Solution sol = new Solution(0, 0, false);
            memo.memoize(i, j, sol);
            return sol;
        }
        if (memo.isMemoized(i, j)) {
            return memo.recall(i, j);
        }

        Solution solFront = gameSolution(i, j + 1, digits, memo);
        int frontTotal = solFront.previousTotal + digits[j];
        Solution solBack = gameSolution(i - 1, j, digits, memo);
        int backTotal = solBack.previousTotal + digits[i];

        Solution sol;
        if (frontTotal >= backTotal) {
            sol = new Solution(frontTotal, solFront.total, true);
        } else {
            sol = new Solution(backTotal, solBack.total, false);
        }
        memo.memoize(i, j, sol);
        return sol;
    }
}
