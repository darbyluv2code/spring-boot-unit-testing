package com.luv2code.springmvc.exceptionhandling;

public class StudentOrGradeNotFoundException extends RuntimeException {

    public StudentOrGradeNotFoundException(String message) {
        super(message);
    }

    public StudentOrGradeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public StudentOrGradeNotFoundException(Throwable cause) {
        super(cause);
    }
}
