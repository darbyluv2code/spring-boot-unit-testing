package com.luv2code.springmvc;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.repository.HistoryGradesDao;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.ScienceGradesDao;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


// @ExtendWith(SpringExtension.class)
// @ContextHierarchy({@ContextConfiguration(classes = MvcTestingExampleApplication.class)})
// @ActiveProfiles("test")
// @DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
// @TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class})
// @WebAppConfiguration
// @Transactional
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class GradebookControllerTest {

	private static MockHttpServletRequest request ;

	@Autowired
	private JdbcTemplate jdbc;

	@PersistenceContext
	private EntityManager entityMgr;

	@Autowired
	private StudentDao studentDao;

	@Autowired
	private MathGradesDao mathGradesDao;

	@Autowired
	private ScienceGradesDao scienceGradesDao;

	@Autowired
	private HistoryGradesDao historyGradesDao;

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private StudentAndGradeService studentCreateServiceMock;

	@Autowired
	Gradebook gradebook;

	@Autowired
	CollegeStudent student;

	@Autowired
    StudentAndGradeService studentService;

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

	@BeforeAll
	public static void setup() {

		request = new MockHttpServletRequest();

		request.setParameter("firstname", "Chad");

		request.setParameter("lastname", "Darby");

		request.setParameter("emailAddress", "chad.darby@luv2code_school.com");

	}
	
	@BeforeEach
	public void each() {
//		MockitoAnnotations.initMocks(this);
		// this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilters(new CharacterEncodingFilter()).build();
		jdbc.execute(sqlAddStudent);
		jdbc.execute(sqlAddMathGrade);
		jdbc.execute(sqlAddScienceGrade);
		jdbc.execute(sqlAddHistoryGrade);
	}


	@Transactional
	@Test
	public void getStudentsHttpRequest () throws Exception {

		GradebookCollegeStudent studentOne = new GradebookCollegeStudent("Eric", "Roby",
				"eric_roby@luv2code_school.com");
		studentOne.setStudentGrades(new StudentGrades());
		GradebookCollegeStudent studentTwo = new GradebookCollegeStudent("Chad", "Darby",
				"chad_darby@luv2code_school.com");
		studentTwo.setStudentGrades(new StudentGrades());
		List<GradebookCollegeStudent> students = new ArrayList<>(Arrays.asList(studentOne, studentTwo));
		gradebook.setStudents(students);

		when(studentCreateServiceMock.getGradebook()).thenReturn(gradebook);

		assertEquals("Roby", studentCreateServiceMock.getGradebook().getStudents().get(0).getLastname(), "Id should be 1");
		assertEquals("Chad", studentCreateServiceMock.getGradebook().getStudents().get(1).getFirstname(), "Firstname Chad");

		student.setFirstname("Chad");
		student.setLastname("Darby");
		student.setEmailAddress("chad.darby@luv2code_school.com");
		entityMgr.persist(student);
		entityMgr.flush();

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/"))
				.andExpect(status().isOk()).andReturn();

		ModelAndView mav = mvcResult.getModelAndView();
		
		ModelAndViewAssert.assertViewName(mav, "index");
	}
	
	@Test
	public void createStudentHttpRequest () throws Exception {
		
		MvcResult mvcResult = this.mockMvc.perform(post("/")
				.contentType(MediaType.APPLICATION_JSON)
				.param("firstname", request.getParameterValues("firstname"))
			    .param("lastname", request.getParameterValues("lastname"))
				.param("emailAddress", request.getParameterValues("emailAddress")))
				.andExpect(status().isOk())
				.andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();

		ModelAndViewAssert.assertViewName(mav, "index");

		CollegeStudent verifyStudent = studentDao.findByEmailAddress("chad.darby@luv2code_school.com");

		assertNotNull(verifyStudent, "Student should be found after create");

	}


	@Test
	public void deleteStudentHttpRequest () throws Exception {

		assertTrue(studentDao.findById(1).isPresent());

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/delete/student/{id}", 1))
				.andExpect(status().isOk()).andReturn();

		ModelAndView mav = mvcResult.getModelAndView();

		ModelAndViewAssert.assertViewName(mav, "index");

		assertFalse(studentDao.findById(1).isPresent());
	}



	@Test
	public void studentInformationHttpRequest () throws Exception {

		assertTrue(studentDao.findById(1).isPresent());

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}", 1))
				.andExpect(status().isOk()).andReturn();

		ModelAndView mav = mvcResult.getModelAndView();

		ModelAndViewAssert.assertViewName(mav, "studentInformation");
	}

	@Test
	public void studentInformationHttpStudentDoesNotExistRequest () throws Exception {

		assertTrue(studentDao.findById(1).isPresent());

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}", 1001))
				.andExpect(status().isOk()).andReturn();

		ModelAndView mav = mvcResult.getModelAndView();

		ModelAndViewAssert.assertViewName(mav, "error");
	}


	@Test
	public void createMathGradeHttpRequest () throws Exception {

		assertTrue(studentDao.findById(1).isPresent());

		GradebookCollegeStudent student = studentService.studentInformation(1);

		assertEquals(1, student.getStudentGrades().getMathGradeResults().size());

		MvcResult mvcResult = this.mockMvc.perform(post("/grades")
				.contentType(MediaType.APPLICATION_JSON)
				.param("grade", "85.00")
				.param("gradeType", "math")
				.param("studentId", "1"))
				.andExpect(status().isOk())
				.andReturn();

		ModelAndView mav = mvcResult.getModelAndView();

		ModelAndViewAssert.assertViewName(mav, "studentInformation");

		student = studentService.studentInformation(1);

		assertEquals(2, student.getStudentGrades().getMathGradeResults().size());

	}

	@Test
	public void createScienceGradeHttpRequest () throws Exception {

		assertTrue(studentDao.findById(1).isPresent());

		GradebookCollegeStudent student = studentService.studentInformation(1);

		assertEquals(1, student.getStudentGrades().getScienceGradeResults().size());

		MvcResult mvcResult = this.mockMvc.perform(post("/grades")
				.contentType(MediaType.APPLICATION_JSON)
				.param("grade", "85.00")
				.param("gradeType", "science")
				.param("studentId", "1"))
				.andExpect(status().isOk())
				.andReturn();

		ModelAndView mav = mvcResult.getModelAndView();

		ModelAndViewAssert.assertViewName(mav, "studentInformation");

		student = studentService.studentInformation(1);

		assertEquals(2, student.getStudentGrades().getScienceGradeResults().size());

	}

	@Test
	public void createHistoryGradeHttpRequest () throws Exception {

		assertTrue(studentDao.findById(1).isPresent());

		GradebookCollegeStudent student = studentService.studentInformation(1);

		assertEquals(1, student.getStudentGrades().getHistoryGradeResults().size());

		MvcResult mvcResult = this.mockMvc.perform(post("/grades")
				.contentType(MediaType.APPLICATION_JSON)
				.param("grade", "85.00")
				.param("gradeType", "history")
				.param("studentId", "1"))
				.andExpect(status().isOk())
				.andReturn();

		ModelAndView mav = mvcResult.getModelAndView();

		ModelAndViewAssert.assertViewName(mav, "studentInformation");

		student = studentService.studentInformation(1);

		assertEquals(2, student.getStudentGrades().getHistoryGradeResults().size());
	}

	@Test
	public void createHistoryGradeHttpStudentDoesNotExistEmptyResponse () throws Exception {

		MvcResult mvcResult = this.mockMvc.perform(post("/grades")
				.contentType(MediaType.APPLICATION_JSON)
				.param("grade", "85.00")
				.param("gradeType", "history")
				.param("studentId", "0"))
				.andExpect(status().isOk())
				.andReturn();

		ModelAndView mav = mvcResult.getModelAndView();

		ModelAndViewAssert.assertViewName(mav, "error");
	}

	@Test
	public void createHistoryGradeHttpGradeTypeDoesNotExistEmptyResponse () throws Exception {

		MvcResult mvcResult = this.mockMvc.perform(post("/grades")
				.contentType(MediaType.APPLICATION_JSON)
				.param("grade", "85.00")
				.param("gradeType", "literature")
				.param("studentId", "1"))
				.andExpect(status().isOk())
				.andReturn();

		ModelAndView mav = mvcResult.getModelAndView();

		ModelAndViewAssert.assertViewName(mav, "error");
	}

	@Test
	public void createHistoryGradeHttpGradeIsHigherThan100EmptyResponse () throws Exception {

		MvcResult mvcResult = this.mockMvc.perform(post("/grades")
				.contentType(MediaType.APPLICATION_JSON)
				.param("grade", "101.00")
				.param("gradeType", "history")
				.param("studentId", "1"))
				.andExpect(status().isOk())
				.andReturn();

		ModelAndView mav = mvcResult.getModelAndView();

		ModelAndViewAssert.assertViewName(mav, "error");
	}

	@Test
	public void createHistoryGradeHttpGradeIsNegativeEmptyResponse () throws Exception {

		MvcResult mvcResult = this.mockMvc.perform(post("/grades")
				.contentType(MediaType.APPLICATION_JSON)
				.param("grade", "-5")
				.param("gradeType", "history")
				.param("studentId", "1"))
				.andExpect(status().isOk())
				.andReturn();

		ModelAndView mav = mvcResult.getModelAndView();

		ModelAndViewAssert.assertViewName(mav, "error");
	}

	@Test
	public void deleteMathGradeHttpRequest () throws Exception {

		Optional<MathGrade> mathGrade = mathGradesDao.findById(1);

		assertTrue(mathGrade.isPresent());

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/grades/{id}/{gradeType}", 1, "math"))
				.andExpect(status().isOk()).andReturn();

		ModelAndView mav = mvcResult.getModelAndView();

		ModelAndViewAssert.assertViewName(mav, "studentInformation");

		mathGrade = mathGradesDao.findById(1);

		assertFalse(mathGrade.isPresent());
	}

	@Test
	public void deleteScienceGradeHttpRequest () throws Exception {

		Optional<ScienceGrade> scienceGrade = scienceGradesDao.findById(1);

		assertTrue(scienceGrade.isPresent());

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/grades/{id}/{gradeType}", 1, "science"))
				.andExpect(status().isOk()).andReturn();

		ModelAndView mav = mvcResult.getModelAndView();

		ModelAndViewAssert.assertViewName(mav, "studentInformation");

		scienceGrade = scienceGradesDao.findById(1);

		assertFalse(scienceGrade.isPresent());
	}

	@Test
	public void deleteHistoryGradeHttpRequest () throws Exception {

		Optional<HistoryGrade> historyGrade = historyGradesDao.findById(1);

		assertTrue(historyGrade.isPresent());

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/grades/{id}/{gradeType}", 1, "history"))
				.andExpect(status().isOk()).andReturn();

		ModelAndView mav = mvcResult.getModelAndView();

		ModelAndViewAssert.assertViewName(mav, "studentInformation");

		historyGrade = historyGradesDao.findById(1);

		assertFalse(historyGrade.isPresent());
	}

	@Test
	public void deleteGradeHttpRequestStudentIdDoesNotExistEmptyResponse () throws Exception {

		Optional<HistoryGrade> historyGrade = historyGradesDao.findById(2);

		assertFalse(historyGrade.isPresent());

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/grades/{id}/{gradeType}", 2, "history"))
				.andExpect(status().isOk()).andReturn();

		ModelAndView mav = mvcResult.getModelAndView();

		ModelAndViewAssert.assertViewName(mav, "error");
	}

	@Test
	public void deleteGradeHttpRequestGradeTypeDoesNotExistEmptyResponse () throws Exception {

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/grades/{id}/{gradeType}", 2, "literature"))
				.andExpect(status().isOk()).andReturn();

		ModelAndView mav = mvcResult.getModelAndView();

		ModelAndViewAssert.assertViewName(mav, "error");

	}


	@AfterEach
	public void setupAfterTransaction() {
		jdbc.execute(sqlDeleteStudent);
		jdbc.execute(sqlDeleteMathGrade);
		jdbc.execute(sqlDeleteScienceGrade);
		jdbc.execute(sqlDeleteHistoryGrade);
	}
}
