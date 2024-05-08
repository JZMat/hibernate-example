package com.example.test;

import java.math.BigDecimal;

public class Test {
    public static void main(String[] args) {
        Test test = new Test();
        test.start();
    }

    private void start() {
        String input = "5";
        System.out.println("\n" + input);
        System.out.println("Type of " + input + " input: " + input.getClass().getSimpleName());
        BigDecimal bigDecimal = new BigDecimal(input);
        System.out.println("BigDecimal: " + bigDecimal);
        input = "5.00";
        System.out.println("\n" + input);
        System.out.println("Type of " + input + " input: " + input.getClass().getSimpleName());
        bigDecimal = new BigDecimal(input);
        System.out.println("BigDecimal: " + bigDecimal);
        input = "5,00";
        System.out.println("\n" + input);
        System.out.println("Type of " + input + " input: " + input.getClass().getSimpleName());
        input = input.trim().replace(",", ".");
        bigDecimal = new BigDecimal(input);
        System.out.println("BigDecimal: " + bigDecimal);

        input = "5,00";
        System.out.println("\n" + input);
        System.out.println("Type of " + input + " input: " + input.getClass().getSimpleName());
        input = input.trim().replace(",", "");
        bigDecimal = new BigDecimal(input);
        System.out.println("BigDecimal: " + bigDecimal);
    }

}
