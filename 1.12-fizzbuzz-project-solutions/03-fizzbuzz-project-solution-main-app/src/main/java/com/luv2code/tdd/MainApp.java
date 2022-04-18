package com.luv2code.tdd;

public class MainApp {

    public static void main(String[] args) {

        for (int i=1; i <= 100; i++) {

            // System.out.println(i + "," + FizzBuzz.compute(i));
            System.out.println(FizzBuzz.compute(i));
        }
    }
}
