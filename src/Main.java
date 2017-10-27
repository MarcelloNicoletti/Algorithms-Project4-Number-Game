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

        calculateGame(digits.length - 1, 0, digits, memo);
    }

    private static void calculateGame (int i, int j, int[] digits,
            MemoMatrix<Solution> memo) {
        
    }
}
