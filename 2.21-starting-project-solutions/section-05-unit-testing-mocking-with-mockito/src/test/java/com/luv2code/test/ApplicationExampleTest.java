package com.luv2code.test;

import com.luv2code.component.MvcTestingExampleApplication;
import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes= MvcTestingExampleApplication.class)
public class ApplicationExampleTest {

    private static int count = 0;

    @Value("${info.app.name}")
    private String appInfo;

    @Value("${info.app.description}")
    private String appDescription;

    @Value("${info.app.version}")
    private String appVersion;

    @Value("${info.school.name}")
    private String schoolName;

    @Autowired
    CollegeStudent student;

    @Autowired
    StudentGrades studentGrades;

    @Autowired
    ApplicationContext context;

    @BeforeEach
    public void beforeEach() {
        count = count + 1;
        System.out.println("Testing: " + appInfo + " which is " + appDescription +
                "  Version: " + appVersion + ". Execution of test method " + count);
        student.setFirstname("Eric");
        student.setLastname("Roby");
        student.setEmailAddress("eric.roby@luv2code_school.com");
        studentGrades.setMathGradeResults(new ArrayList<>(Arrays.asList(100.0, 85.0, 76.50, 91.75)));
        student.setStudentGrades(studentGrades);
    }

    @DisplayName("Add grade results for student grades not equal")
    @Test
    public void addGradeResultsForStudentGradesAssertNotEquals() {
        assertNotEquals(0, studentGrades.addGradeResultsForSingleClass(
                student.getStudentGrades().getMathGradeResults()
        ));
    }

    @DisplayName("Is grade greater")
    @Test
    public void isGradeGreaterStudentGrades() {
        assertTrue(studentGrades.isGradeGreater(90, 75),
                "failure - should be true");
    }

    @DisplayName("Is grade greater false")
    @Test
    public void isGradeGreaterStudentGradesAssertFalse() {
        assertFalse(studentGrades.isGradeGreater(89, 92),
                "failure - should be false");
    }

    @DisplayName("Check Null for student grades")
    @Test
    public void checkNullForStudentGrades() {
        assertNotNull(studentGrades.checkNull(student.getStudentGrades().getMathGradeResults()),
                "object should not be null");
    }

    @DisplayName("Create student without grade init")
    @Test
    public void createStudentWithoutGradesInit() {
        CollegeStudent studentTwo = context.getBean("collegeStudent", CollegeStudent.class);
        studentTwo.setFirstname("Chad");
        studentTwo.setLastname("Darby");
        studentTwo.setEmailAddress("chad.darby@luv2code_school.com");
        assertNotNull(studentTwo.getFirstname());
        assertNotNull(studentTwo.getLastname());
        assertNotNull(studentTwo.getEmailAddress());
        assertNull(studentGrades.checkNull(studentTwo.getStudentGrades()));
    }

    @DisplayName("Verify students are prototypes")
    @Test
    public void verifyStudentsArePrototypes() {
        CollegeStudent studentTwo = context.getBean("collegeStudent", CollegeStudent.class);

        assertNotSame(student, studentTwo);
    }

    @DisplayName("Find Grade Point Average")
    @Test
    public void findGradePointAverage() {
        assertAll("Testing all assertEquals",
                ()-> assertEquals(353.25, studentGrades.addGradeResultsForSingleClass(
                        student.getStudentGrades().getMathGradeResults())),
                ()-> assertEquals(88.31, studentGrades.findGradePointAverage(
                        student.getStudentGrades().getMathGradeResults()))
        );
    }
}










