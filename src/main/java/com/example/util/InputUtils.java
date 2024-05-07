// Package declaration
package com.example.util;

// Import statement
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

// Input utils class
public class InputUtils {
    // Static scanner instance
    private static final Scanner scanner = new Scanner(System.in);

    // Private constructor to prevent instantiation
    private InputUtils() {
    }

    // Static method to get string input
    public static String getStringInput(String message) {
        System.out.print(message);
        scanner.nextLine();
        return scanner.nextLine();
    }

    // Static method to get integer input
    public static int getIntInput(String message) {
        System.out.print(message);
        int input = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        return input;
        //return scanner.nextInt();
    }

    public static BigDecimal getBigDecimalInput(String message) {
        System.out.print(message);
        BigDecimal amount = null;
        while (amount == null) {
            try {
                String input = scanner.nextLine().trim().replace(",", ""); // Remove any commas in the input
                amount = new BigDecimal(input);
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

}

