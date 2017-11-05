import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static Scanner stdIn = new Scanner(System.in);

    public static void main (String args[]) {
        System.out.println("Welcome to the number game.");

        int[] numbers = getNumbers();

        System.out.println("Playing with " + Arrays.toString(numbers));

        MemoMatrix<GameSolution> memo = new MemoMatrix<>(numbers.length,
                numbers.length);
        int bestScore = calcGameSolutions(numbers.length - 1, 0, numbers, memo)
                .total;
        memo.printMatrix();

        System.out.println("\nPlaying the game against computer.\n");

        playGame(numbers, memo);
    }

    private static int[] getNumbers () {
        System.out.print("How many numbers will there be? > ");
        int numNumbers = stdIn.nextInt();
        stdIn.nextLine();
        int[] numbers = new int[numNumbers];

        for (int i = 0; i < numbers.length; i++) {
            System.out.print("Enter number for position " + (i + 1) + " > ");
            numbers[i] = stdIn.nextInt();
            stdIn.nextLine();
        }
        return numbers;
    }

    private static GameSolution calcGameSolutions (int i, int j, int[] numbers,
            MemoMatrix<GameSolution> memo) {
        if (i < 0 || j < 0 || i >= numbers.length || j >= numbers.length) {
            return new GameSolution(0, 0, false);
        }
        if (i < j) {
            GameSolution sol = new GameSolution(0, 0, false);
            memo.memoize(i, j, sol);
            return sol;
        }
        if (memo.isMemoized(i, j)) {
            return memo.recall(i, j);
        }

        GameSolution solFirst = calcGameSolutions(i, j + 1, numbers, memo);
        int firstTotal = solFirst.previousTotal + numbers[j];

        GameSolution solLast = calcGameSolutions(i - 1, j, numbers, memo);
        int lastTotal = solLast.previousTotal + numbers[i];

        GameSolution sol;
        if (firstTotal >= lastTotal) {
            sol = new GameSolution(firstTotal, solFirst.total, true);
        } else {
            sol = new GameSolution(lastTotal, solLast.total, false);
        }
        memo.memoize(i, j, sol);
        return sol;
    }

    private static void playGame (int[] numbers, MemoMatrix<GameSolution> memo) {
        int i = numbers.length - 1, // Back pointer
                j = 0, // Front pointer
                round = 1,
                sumPlayer = 0,
                sumComputer = 0,
                maxNumLen = Integer.toString(Arrays.stream(numbers).max()
                        .orElse(Integer.MIN_VALUE)).length();

        while (i >= j) {
            System.out.printf("Round %d: ", round);
            for (int k = 0; k < numbers.length; k++) {
                if (k < j || k > i) {
                    System.out.printf("%"+maxNumLen+"s ", " ");
                } else {
                    System.out.printf("%"+maxNumLen+"s ", numbers[k]);
                }
            }
            System.out.printf(" Player: %d, Computer: %d%n", sumPlayer,
                    sumComputer);

            if (round % 2 == 0) {
                // Computer Turn
                GameSolution sol = memo.recall(i, j);
                if (sol.fromFirst) {
                    System.out.println("Computer chooses First");
                    sumComputer += numbers[j];
                    j++;
                } else {
                    System.out.println("Computer chooses Last");
                    sumComputer += numbers[i];
                    i--;
                }
            } else {
                // Player Turn
                System.out.printf("Take first (%d) or last (%d)? > ",
                        numbers[j], numbers[i]);
                String choiceStr = stdIn.nextLine();
                boolean choseFront = false;
                choseFront = choiceStr.equalsIgnoreCase("first") ||
                             choiceStr.equalsIgnoreCase("f") ||
                             (choiceStr.matches("-?\\d+") &&
                                 Integer.parseInt(choiceStr) == numbers[j]);

                if (choseFront) {
                    System.out.println("Player chooses First");
                    sumPlayer += numbers[j];
                    j++;
                } else {
                    System.out.println("Player chooses Last");
                    sumPlayer += numbers[i];
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
