package com.luv2code.springmvc.service;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.repository.HistoryGradesDao;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.ScienceGradesDao;
import com.luv2code.springmvc.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentAndGradeService {

	@Autowired
	private StudentDao studentDao;

	@Autowired
	private MathGradesDao mathGradeDao;

	@Autowired
	private ScienceGradesDao scienceGradeDao;

	@Autowired
	private HistoryGradesDao historyGradeDao;

	@Autowired
	@Qualifier("mathGrades")
	private MathGrade mathGrade;

	@Autowired
	@Qualifier("scienceGrades")
	private ScienceGrade scienceGrade;

	@Autowired
	@Qualifier("historyGrades")
	private HistoryGrade historyGrade;

	@Autowired
	StudentGrades studentGrades;

	public void createStudent(String firstname, String lastname, String emailAddress){

		CollegeStudent student = new CollegeStudent(firstname, lastname, emailAddress);

		student.setId(0);

		studentDao.save(student);
	}

	public void deleteStudent(int id){
		if (checkIfStudentIsNull(id)) {
			studentDao.deleteById(id);
			mathGradeDao.deleteByStudentId(id);
			scienceGradeDao.deleteByStudentId(id);
			historyGradeDao.deleteByStudentId(id);
		}
	}

	public boolean checkIfStudentIsNull(int id){
		Optional<CollegeStudent> student = studentDao.findById(id);
		if (student.isPresent()) {
			return true;
		}
		return false;
	}

	public GradebookCollegeStudent studentInformation(int id) {
		Optional<CollegeStudent> student = studentDao.findById(id);

		if (!student.isPresent()) {
			return null;
		}

		Iterable<MathGrade> mathGrades = mathGradeDao.findGradeByStudentId(id);

		Iterable<ScienceGrade> scienceGrades = scienceGradeDao.findGradeByStudentId(id);

		Iterable<HistoryGrade> historyGrades = historyGradeDao.findGradeByStudentId(id);

		List<Grade> mathGradesList = new ArrayList<>();
		mathGrades.forEach(mathGradesList::add);

		List<Grade> scienceGradesList = new ArrayList<>();
		scienceGrades.forEach(scienceGradesList::add);

		List<Grade> historyGradesList = new ArrayList<>();
		historyGrades.forEach(historyGradesList::add);


		studentGrades.setMathGradeResults(mathGradesList);
		studentGrades.setScienceGradeResults(scienceGradesList);
		studentGrades.setHistoryGradeResults(historyGradesList);

		GradebookCollegeStudent gradebookCollegeStudent = new GradebookCollegeStudent(student.get().getId(), student.get().getFirstname(), student.get().getLastname(),
				student.get().getEmailAddress(), studentGrades);

		return gradebookCollegeStudent;
	}

	public boolean checkIfGradeIsNull(int id, String gradeType){
		if (gradeType.equals("math")) {
			Optional<MathGrade> grade = mathGradeDao.findById(id);
			if (grade.isPresent()) {
				return true;
			}
		}
		if (gradeType.equals("science")) {
			Optional<ScienceGrade> grade = scienceGradeDao.findById(id);
			if (grade.isPresent()) {
				return true;
			}
		}
		if (gradeType.equals("history")) {
			Optional<HistoryGrade> grade = historyGradeDao.findById(id);
			if (grade.isPresent()) {
				return true;
			}
		}

		return false;
	}

	public int deleteGrade(int id, String gradeType) {

		int studentId = 0;

		if (gradeType.equals("math")) {
			Optional<MathGrade> grade = mathGradeDao.findById(id);
			if (!grade.isPresent()) {
				return studentId;
			}
			studentId = grade.get().getStudentId();
			mathGradeDao.deleteById(id);
		}

		if (gradeType.equals("science")) {
			Optional<ScienceGrade> grade = scienceGradeDao.findById(id);
			if (!grade.isPresent()) {
				return studentId;
			}
			studentId = grade.get().getStudentId();
			scienceGradeDao.deleteById(id);
		}

		if (gradeType.equals("history")) {
			Optional<HistoryGrade> grade = historyGradeDao.findById(id);
			if (!grade.isPresent()) {
				return studentId;
			}
			studentId = grade.get().getStudentId();
			historyGradeDao.deleteById(id);
		}

		return studentId;
	}

	public boolean createGrade(double grade, int studentId, String gradeType) {
		if (grade >= 0 && grade <= 100) {
			if (gradeType.equals("math")) {
				mathGrade.setId(0);
				mathGrade.setGrade(grade);
				mathGrade.setStudentId(studentId);
				mathGradeDao.save(mathGrade);
				return true;
			}

			if (gradeType.equals("science")) {
				scienceGrade.setId(0);
				scienceGrade.setGrade(grade);
				scienceGrade.setStudentId(studentId);
				scienceGradeDao.save(scienceGrade);
				return true;
			}

			if (gradeType.equals("history")) {
				historyGrade.setId(0);
				historyGrade.setGrade(grade);
				historyGrade.setStudentId(studentId);
				historyGradeDao.save(historyGrade);
				return true;
			}
		}
		return false;
	}

	public Gradebook getGradebook () {

		Iterable<CollegeStudent> collegeStudents = studentDao.findAll();

		Iterable<MathGrade> mathGrades = mathGradeDao.findAll();

		Iterable<ScienceGrade> scienceGrades = scienceGradeDao.findAll();

		Iterable<HistoryGrade> historyGrades = historyGradeDao.findAll();

		Gradebook gradebook = new Gradebook();

		for (CollegeStudent collegeStudent : collegeStudents) {
			List<Grade> mathGradesPerStudent = new ArrayList<>();
			List<Grade> scienceGradesPerStudent = new ArrayList<>();
			List<Grade> historyGradesPerStudent = new ArrayList<>();

			for (MathGrade grade : mathGrades) {
				if (grade.getStudentId() == collegeStudent.getId()) {
					mathGradesPerStudent.add(grade);
				}
			}
			for (ScienceGrade grade : scienceGrades) {
				if (grade.getStudentId() == collegeStudent.getId()) {
					scienceGradesPerStudent.add(grade);
				}
			}

			for (HistoryGrade grade : historyGrades) {
				if (grade.getStudentId() == collegeStudent.getId()) {
					historyGradesPerStudent.add(grade);
				}
			}

			studentGrades.setMathGradeResults(mathGradesPerStudent);
			studentGrades.setScienceGradeResults(scienceGradesPerStudent);
			studentGrades.setHistoryGradeResults(historyGradesPerStudent);

			GradebookCollegeStudent gradebookCollegeStudent = new GradebookCollegeStudent(collegeStudent.getId(), collegeStudent.getFirstname(), collegeStudent.getLastname(),
					collegeStudent.getEmailAddress(), studentGrades);

			gradebook.getStudents().add(gradebookCollegeStudent);
		}

		return gradebook;
	}

	public void configureStudentInformationModel(int id, Model m) {

		GradebookCollegeStudent studentEntity = studentInformation(id);

		m.addAttribute("student", studentEntity);

		if (studentEntity.getStudentGrades().getMathGradeResults().size() > 0) {
			m.addAttribute("mathAverage", studentEntity.getStudentGrades().findGradePointAverage(studentEntity.getStudentGrades().getMathGradeResults()));
		} else {
			m.addAttribute("mathAverage", "N/A");
		}
		if (studentEntity.getStudentGrades().getScienceGradeResults().size() > 0) {
			m.addAttribute("scienceAverage", studentEntity.getStudentGrades().findGradePointAverage(studentEntity.getStudentGrades().getScienceGradeResults()));
		} else {
			m.addAttribute("scienceAverage", "N/A");
		}
		if (studentEntity.getStudentGrades().getHistoryGradeResults().size() > 0) {
			m.addAttribute("historyAverage", studentEntity.getStudentGrades().findGradePointAverage(studentEntity.getStudentGrades().getHistoryGradeResults()));
		} else {
			m.addAttribute("historyAverage", "N/A");
		}
	}
}
