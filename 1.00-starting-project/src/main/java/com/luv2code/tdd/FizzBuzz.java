package com.luv2code.tdd;

public class FizzBuzz {

    // Number divisible by 3, print Fizz
    // Number divisible by 5, print Buzz
    // Number divisible by 3 and 5, print FizzBuzz
    // Number not divisible by 3 and, print number

    //Refactor
    public static String compute(int i) {
        StringBuilder result = new StringBuilder();

        if(i % 3 == 0){
            result.append("Fizz");
        }
        
        if(i % 5 == 0){
            result.append("Buzz");
        }

        if(result.length() == 0){
            result.append(i);
        } 

        return result.toString();
    }

    // public static String compute(int i) {
    //     if ((i % 3 == 0) && (i % 5 == 0)) {
    //         return "FizzBuzz";
    //     } else if (i % 3 == 0) {
    //         return "Fizz";
    //     } else if (i % 5 == 0) {
    //         return "Buzz";
    //     }else{
    //         return Integer.toString(i);
    //     }
    // }

    

}
