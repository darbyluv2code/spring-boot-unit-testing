package com.luv2code.tdd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FizzBuzzTest {
    
    // Number divisible by 3, print Fizz
    // Number divisible by 5, print Buzz
    // Number divisible by 3 and 5, print FizzBuzz
    // Number not divisible by 3 and, print number
    
    @Test
    @DisplayName("Divisible by three")
    @Order(1)
    public void testForDivisibleByThree(){
        String  expected = "Fizz";
        assertEquals(expected, FizzBuzz.compute(3), "Should return Fizz");

    }

    @Test
    @DisplayName("Divisible by five")
    @Order(2)
    public void testForDivisibleByFive(){
        String  expected = "Buzz";
        assertEquals(expected, FizzBuzz.compute(5), "Should return Buzz");

    }

    @Test
    @DisplayName("Divisible by three and five")
    @Order(3)
    public void testForDivisibleByThreeAndFive(){
        String  expected = "FizzBuzz";
        assertEquals(expected, FizzBuzz.compute(15), "Should return FizzBuzz");

    }

    @Test
    @DisplayName("Not divisible by three and five")
    @Order(5)
    public void testNotDivisibleByThreeAndFive(){
        String  expected = "1";
        assertEquals(expected, FizzBuzz.compute(1), "Should return 1");        
    }

    @DisplayName("Testing with Small data file")
    @ParameterizedTest(name="value={0}, expected={1}")
    @CsvFileSource(resources="/small-test-data.csv")
    @Order(5)
    void testSmallDataFile(int value, String expected){
        assertEquals(expected, FizzBuzz.compute(value));

    }
    
    @DisplayName("Testing with medium data file")
    @ParameterizedTest(name="value={0}, expected={1}")
    @CsvFileSource(resources="/medium-test-data.csv")
    @Order(6)
    void testMediumDataFile(int value, String expected){
        assertEquals(expected, FizzBuzz.compute(value));

    }

    @DisplayName("Testing with large data file")
    @ParameterizedTest(name="value={0}, expected={1}")
    @CsvFileSource(resources="/large-test-data.csv")
    @Order(6)
    void testLargeDataFile(int value, String expected){
        assertEquals(expected, FizzBuzz.compute(value));

    }



}
