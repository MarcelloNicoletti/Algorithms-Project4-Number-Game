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

        int bestScore = calcGameSolution(digits.length - 1, 0, digits, memo)
                .total;
        memo.printMatrix();

        System.out.println("\nPlaying the game against computer.\n");

        playGame(digits, memo);
    }

    private static Solution calcGameSolution (int i, int j, int[] digits,
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

        Solution solFirst = calcGameSolution(i, j + 1, digits, memo);
        int firstTotal = solFirst.previousTotal + digits[j];

        Solution solLast = calcGameSolution(i - 1, j, digits, memo);
        int lastTotal = solLast.previousTotal + digits[i];

        Solution sol;
        if (firstTotal >= lastTotal) {
            sol = new Solution(firstTotal, solFirst.total, true);
        } else {
            sol = new Solution(lastTotal, solLast.total, false);
        }
        memo.memoize(i, j, sol);
        return sol;
    }

    private static void playGame (int[] digits, MemoMatrix<Solution> memo) {
        int i = digits.length - 1, // Back pointer
                j = 0, // Front pointer
                round = 1,
                sumPlayer = 0,
                sumComputer = 0;

        while (i >= j) {
            System.out.printf("Round %d: ", round);
            for (int k = 0; k < digits.length; k++) {
                if (k < j || k > i) {
                    System.out.print("  ");
                } else {
                    System.out.print(digits[k] + " ");
                }
            }
            System.out.printf(" Player: %d, Computer: %d%n", sumPlayer,
                    sumComputer);

            if (round % 2 == 0) {
                // Computer Turn
                Solution sol = memo.recall(i, j);
                if (sol.fromFirst) {
                    System.out.println("Computer chooses First");
                    sumComputer += digits[j];
                    j++;
                } else {
                    System.out.println("Computer chooses Last");
                    sumComputer += digits[i];
                    i--;
                }
            } else {
                // Player Turn
                System.out.printf("Take first (%d) or last (%d)? > ",
                        digits[j], digits[i]);
                char choice = stdIn.nextLine().charAt(0);

                if (choice == 'f' || choice == 'F' || choice - '0' ==
                        digits[j]) {
                    System.out.println("Player chooses First");
                    sumPlayer += digits[j];
                    j++;
                } else {
                    System.out.println("Player chooses Last");
                    sumPlayer += digits[i];
                    i--;
                }
            }

            round++;
        }

        System.out.println();
        if (sumPlayer > sumComputer) {
            System.out.printf("Player wins %d to %d.", sumPlayer, sumComputer);
        } else if (sumComputer > sumPlayer) {
            System.out.printf("Computer wins %d to %d.", sumComputer,
                    sumPlayer);
        } else {
            System.out
                    .printf("Player and Computer tied at %d each.", sumPlayer);
        }
    }
}
