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
        int bestScore = calcSumsGameSolutions(numbers.length - 1, 0, numbers,
            memo).total;
        memo.printMatrix();

        System.out.println("\nPlaying the game against computer.\n");

        playSumsGame(numbers, memo);
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

    private static GameSolution calcSumsGameSolutions (int i, int j,
        int[] numbers, MemoMatrix<GameSolution> memo) {
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

        GameSolution solFirst = calcSumsGameSolutions(i, j + 1, numbers, memo);
        int firstSum = solFirst.previousTotal + numbers[j];

        GameSolution solLast = calcSumsGameSolutions(i - 1, j, numbers, memo);
        int lastSum = solLast.previousTotal + numbers[i];

        GameSolution sol;
        if (firstSum >= lastSum) {
            sol = new GameSolution(firstSum, solFirst.total, true);
        } else {
            sol = new GameSolution(lastSum, solLast.total, false);
        }
        memo.memoize(i, j, sol);
        return sol;
    }

    private static void playSumsGame (int[] numbers,
        MemoMatrix<GameSolution> memo) {
        int i = numbers.length - 1, // Back pointer
            j = 0, // Front pointer
            round = 1,
            scorePlayer = 0,
            scoreComputer = 0,
            maxNumLen = Integer.toString(Arrays.stream(numbers)
                .reduce(Integer.MIN_VALUE, Integer::max)).length();

        while (i >= j) {
            System.out.printf("%nRound %d: ", round);
            for (int k = 0; k < numbers.length; k++) {
                if (k < j || k > i) {
                    System.out.printf("%" + maxNumLen + "s ", " ");
                } else {
                    System.out.printf("%" + maxNumLen + "s ", numbers[k]);
                }
            }
            System.out.printf(" Player: %d, Computer: %d%n", scorePlayer,
                scoreComputer);

            if (round % 2 == 0) {
                // Computer Turn
                GameSolution sol = memo.recall(i, j);
                if (sol.fromFirst) {
                    System.out.printf("Computer chooses First (+%d)%n",
                        numbers[j]);
                    scoreComputer += numbers[j];
                    j++;
                } else {
                    System.out.printf("Computer chooses Last (+%d)%n",
                        numbers[i]);
                    scoreComputer += numbers[i];
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
                    System.out.printf("Player chooses First (+%d)%n",
                        numbers[j]);
                    scorePlayer += numbers[j];
                    j++;
                } else {
                    System.out.printf("Player chooses Last (+%d)%n",
                        numbers[i]);
                    scorePlayer += numbers[i];
                    i--;
                }
            }

            round++;
        }

        System.out.println();
        printFinalScores(scorePlayer, scoreComputer);
    }

    private static GameSolution calcProductsGameSolutions (int i, int j,
        int[] numbers, MemoMatrix<GameSolution> memo) {
        if (i < 0 || j < 0 || i >= numbers.length || j >= numbers.length) {
            return new GameSolution(1, 1, false);
        }
        if (i < j) {
            GameSolution sol = new GameSolution(1, 1, false);
            memo.memoize(i, j, sol);
            return sol;
        }
        if (memo.isMemoized(i, j)) {
            return memo.recall(i, j);
        }

        GameSolution solFirst =
            calcProductsGameSolutions(i, j + 1, numbers, memo);
        int firstProduct = solFirst.previousTotal * numbers[j];

        GameSolution solLast =
            calcProductsGameSolutions(i - 1, j, numbers, memo);
        int lastProduct = solLast.previousTotal * numbers[i];

        GameSolution sol;
        if (firstProduct >= lastProduct) {
            sol = new GameSolution(firstProduct, solFirst.total, true);
        } else {
            sol = new GameSolution(lastProduct, solLast.total, false);
        }
        memo.memoize(i, j, sol);
        return sol;
    }

    static void playProductsGame (int[] numbers,
        MemoMatrix<GameSolution> memo) {
        int i = numbers.length - 1, // Back pointer
            j = 0, // Front pointer
            round = 1,
            scorePlayer = 1,
            scoreComputer = 1,
            maxNumLen = Integer.toString(Arrays.stream(numbers)
                .reduce(Integer.MIN_VALUE, Integer::max)).length();

        while (i >= j) {
            System.out.printf("%nRound %d: ", round);
            for (int k = 0; k < numbers.length; k++) {
                if (k < j || k > i) {
                    System.out.printf("%" + maxNumLen + "s ", " ");
                } else {
                    System.out.printf("%" + maxNumLen + "s ", numbers[k]);
                }
            }
            System.out.printf(" Player: %d, Computer: %d%n", scorePlayer,
                scoreComputer);

            if (round % 2 == 0) {
                // Computer Turn
                GameSolution sol = memo.recall(i, j);
                if (sol.fromFirst) {
                    System.out.printf("Computer chooses First (*%d)%n",
                        numbers[j]);
                    scoreComputer *= numbers[j];
                    j++;
                } else {
                    System.out.printf("Computer chooses Last (*%d)%n",
                        numbers[i]);
                    scoreComputer *= numbers[i];
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
                    System.out.printf("Player chooses First (*%d)%n",
                        numbers[j]);
                    scorePlayer *= numbers[j];
                    j++;
                } else {
                    System.out.printf("Player chooses Last (*%d)%n",
                        numbers[i]);
                    scorePlayer *= numbers[i];
                    i--;
                }
            }

            round++;
        }

        System.out.println();
        printFinalScores(scorePlayer, scoreComputer);
    }

    private static void printFinalScores (int scorePlayer, int scoreComputer) {
        if (scorePlayer > scoreComputer) {
            System.out.printf("Player wins %d to %d.", scorePlayer,
                scoreComputer);
        } else if (scoreComputer > scorePlayer) {
            System.out.printf("Computer wins %d to %d.", scoreComputer,
                scorePlayer);
        } else {
            System.out
                .printf("Player and Computer tied at %d each.", scorePlayer);
        }
    }
}
