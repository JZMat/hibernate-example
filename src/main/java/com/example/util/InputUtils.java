// Package declaration
package com.example.util;

// Import statement
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
        return scanner.nextInt();
    }
}

