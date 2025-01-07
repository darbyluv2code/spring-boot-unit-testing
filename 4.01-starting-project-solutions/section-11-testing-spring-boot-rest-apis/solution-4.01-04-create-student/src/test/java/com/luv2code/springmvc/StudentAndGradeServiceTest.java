package com.luv2code.springmvc;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.repository.HistoryGradesDao;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.ScienceGradesDao;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class StudentAndGradeServiceTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private MathGradesDao mathGradeDao;

    @Autowired
    private ScienceGradesDao scienceGradeDao;

    @Autowired
    private HistoryGradesDao historyGradeDao;

    @Autowired
    private StudentAndGradeService studentService;

    @Value("${sql.script.create.student}")
    private String sqlAddStudent;

    @Value("${sql.script.create.math.grade}")
    private String sqlAddMathGrade;

    @Value("${sql.script.create.science.grade}")
    private String sqlAddScienceGrade;

    @Value("${sql.script.create.history.grade}")
    private String sqlAddHistoryGrade;

    @Value("${sql.script.delete.student}")
    private String sqlDeleteStudent;

    @Value("${sql.script.delete.math.grade}")
    private String sqlDeleteMathGrade;

    @Value("${sql.script.delete.science.grade}")
    private String sqlDeleteScienceGrade;

    @Value("${sql.script.delete.history.grade}")
    private String sqlDeleteHistoryGrade;


    @BeforeEach
    public void setupDatabase() {
        jdbc.execute(sqlAddStudent);
        jdbc.execute(sqlAddMathGrade);
        jdbc.execute(sqlAddScienceGrade);
        jdbc.execute(sqlAddHistoryGrade);
    }

    @Test
    public void isStudentNullCheck() {

        assertTrue(studentService.checkIfStudentIsNull(1), "@BeforeTransaction creates student : return true");

        assertFalse(studentService.checkIfStudentIsNull(0), "No student should have 0 id : return false");
    }


    @Test
    public void createStudentService() {

        studentService.createStudent("Chad", "Darby", "chad.darby@luv2code_school.com");

        CollegeStudent student = studentDao.findByEmailAddress("chad.darby@luv2code_school.com");

        assertEquals("chad.darby@luv2code_school.com", student.getEmailAddress(), "find by email");
    }

    @Test
    public void deleteStudentService() {

        Optional<CollegeStudent> deletedCollegeStudent = studentDao.findById(1);
        Optional<MathGrade> deletedMathGrade = mathGradeDao.findById(1);
        Optional<ScienceGrade> deletedScienceGrade = scienceGradeDao.findById(1);
        Optional<HistoryGrade> deletedHistoryGrade = historyGradeDao.findById(1);

        assertTrue(deletedCollegeStudent.isPresent(), "return true");
        assertTrue(deletedMathGrade.isPresent(), "return true");
        assertTrue(deletedScienceGrade.isPresent(),  "return true");
        assertTrue(deletedHistoryGrade.isPresent(), "return true");

        studentService.deleteStudent(1);

        deletedCollegeStudent = studentDao.findById(1);
        deletedMathGrade = mathGradeDao.findById(1);
        deletedScienceGrade = scienceGradeDao.findById(1);
        deletedHistoryGrade = historyGradeDao.findById(1);

        assertFalse(deletedCollegeStudent.isPresent(), "return false");
        assertFalse(deletedMathGrade.isPresent(), "return false");
        assertFalse(deletedScienceGrade.isPresent(),  "return false");
        assertFalse(deletedHistoryGrade.isPresent(), "return false");
    }


    @Test
    public void studentInformationService() {
        GradebookCollegeStudent gradebookCollegeStudentTest = studentService.studentInformation(1);

        assertNotNull(gradebookCollegeStudentTest);
        assertEquals(1, gradebookCollegeStudentTest.getId());
        assertNotNull(gradebookCollegeStudentTest.getFirstname());
        assertNotNull(gradebookCollegeStudentTest.getLastname());
        assertNotNull(gradebookCollegeStudentTest.getEmailAddress());
        assertNotNull(gradebookCollegeStudentTest.getStudentGrades().getMathGradeResults());
        assertNotNull(gradebookCollegeStudentTest.getStudentGrades().getScienceGradeResults());
        assertNotNull(gradebookCollegeStudentTest.getStudentGrades().getHistoryGradeResults());

        assertEquals("Eric", gradebookCollegeStudentTest.getFirstname());
        assertEquals("Roby", gradebookCollegeStudentTest.getLastname());
        assertEquals("eric.roby@luv2code_school.com", gradebookCollegeStudentTest.getEmailAddress());

    }

    @Test
    public void isGradeNullCheck() {

        assertTrue(studentService.checkIfGradeIsNull(1, "math"),
                "@BeforeTransaction creates student : return true");

        assertTrue(studentService.checkIfGradeIsNull(1, "science"),
                "@BeforeTransaction creates student : return true");

        assertTrue(studentService.checkIfGradeIsNull(1, "history"),
                "@BeforeTransaction creates student : return true");

        assertFalse(studentService.checkIfGradeIsNull(0, "science"),
                "No student should have 0 id : return false");

        assertFalse(studentService.checkIfGradeIsNull(0, "Literature"),
                "No student should have 0 id : return false");
    }

    @Test
    public void deleteGradeService() {

        assertEquals(1, studentService.deleteGrade(1, "math"),
                "@BeforeTransaction creates student : return true");

        assertEquals(1, studentService.deleteGrade(1, "science"),
                "@BeforeTransaction creates student : return true");

        assertEquals(1, studentService.deleteGrade(1, "history"),
                "@BeforeTransaction creates student : return true");

        assertEquals(0, studentService.deleteGrade(0, "science"),
                "No student should have 0 id : return false");

        assertEquals(0, studentService.deleteGrade(1, "literature"),
                "No student should have 0 id : return false");
    }

    @Test
    public void createGradeService() {

        studentService.createGrade(80.50, 2, "math");
        studentService.createGrade(80.50, 2, "science");
        studentService.createGrade(80.50, 2, "history");
        studentService.createGrade(80.50, 2, "literature");

        Iterable<MathGrade> mathGrades  = mathGradeDao.findGradeByStudentId(2);

        Iterable<ScienceGrade> scienceGrades  = scienceGradeDao.findGradeByStudentId(2);

        Iterable<HistoryGrade> historyGrades  = historyGradeDao.findGradeByStudentId(2);

        assertTrue(mathGrades.iterator().hasNext(),
                "Student Service creates the grade: return true");
        assertTrue(scienceGrades.iterator().hasNext(),
                "Student Service creates the grade: return true");
        assertTrue(historyGrades.iterator().hasNext(),
                "Student Service creates the grade: return true");

    }

    @SqlGroup({ @Sql(scripts = "/insertData.sql", config = @SqlConfig(commentPrefix = "`")),
            @Sql("/overrideData.sql"),
            @Sql("/insertGrade.sql")})
    @Test
    public void getGradebookService() {

        Gradebook gradebook = studentService.getGradebook();

        Gradebook gradebookTest = new Gradebook();

        for (GradebookCollegeStudent student : gradebook.getStudents()) {
            if (student.getId() > 10) {
                gradebookTest.getStudents().add(student);
            }
        }

        assertEquals(4, gradebookTest.getStudents().size());
        assertTrue(gradebookTest.getStudents().get(0).getStudentGrades().getHistoryGradeResults() != null);
        assertTrue(gradebookTest.getStudents().get(0).getStudentGrades().getScienceGradeResults() != null);
        assertTrue(gradebookTest.getStudents().get(0).getStudentGrades().getMathGradeResults() != null);
    }


    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute(sqlDeleteStudent);
        jdbc.execute(sqlDeleteMathGrade);
        jdbc.execute(sqlDeleteScienceGrade);
        jdbc.execute(sqlDeleteHistoryGrade);
    }
}
