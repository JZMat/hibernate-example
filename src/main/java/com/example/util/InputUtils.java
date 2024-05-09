// Package declaration
package com.example.util;

// Import statements
import java.math.BigDecimal; // Import for BigDecimal class
import java.math.RoundingMode; // Import for RoundingMode enum
import java.util.InputMismatchException;
import java.util.Scanner; // Import for Scanner class

// Utility class for handling user input
public class InputUtils {
    // Static scanner instance
    public static final Scanner scanner = new Scanner(System.in);

    // Private constructor to prevent instantiation
    private InputUtils() {
    }

    /**
     * Static method to get a string input from the user.
     *
     * @param message the message to prompt the user
     * @return the string input provided by the user
     */
    public static String getStringInput(String message) {
        System.out.print(message);
        String input;
        input = scanner.nextLine();
        // There's no need to consume a newline character in this method because nextLine() consumes the newline character automatically.
        return input;
    }

    /**
     * Static method to get an integer input from the user.
     *
     * @param message the message to prompt the user
     * @return the integer input provided by the user
     */
    public static int getIntInput(String message) {
        System.out.print(message);
        int input = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        return input;
    }

    /**
     * Static method to get a BigDecimal input from the user.
     *
     * @param message the message to prompt the user
     * @return the BigDecimal input provided by the user
     */
    public static BigDecimal getBigDecimalInput(String message) {
        System.out.print(message);

        BigDecimal amount = null;
        while (amount == null) {
            try {
                String input;
                input = scanner.nextLine();
                input = input.trim().replace(",", "."); // Remove any commas in the input

                amount = new BigDecimal(input);
                System.out.println("Amount BigDecimal: " + amount);
                if (amount.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.print("Invalid input. Please enter a non-negative value: ");
                    amount = null;
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid number: ");
            }
        }
        return amount.setScale(2, RoundingMode.HALF_UP); // Round to 2 decimal places
    }

    public static int getValidatedInput(String prompt, int min, int max) {
        int input;
        while (true) {
            try {
                input = InputUtils.getIntInput(prompt);
                if (input < min || input > max) {
                    throw new IndexOutOfBoundsException();
                }
                break; // Break the loop if input is valid
            } catch (InputMismatchException e) {
                System.out.println("Invalid input type. Please enter a number.");
                scanner.nextLine(); // Consume the rest of the line of input
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Invalid number. Please choose a number between " + min + " and " + max + ".");
            }
        }
        return input;
    }
}
