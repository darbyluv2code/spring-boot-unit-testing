package com.luv2code.springmvc.models;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class StudentGrades {

    private List<Grade> mathGradeResults;

    private List<Grade> scienceGradeResults;

    private List<Grade> historyGradeResults;

    public StudentGrades() {}

    public double addGradeResultsForSingleClass(List<Grade> grades) {
        double result = 0;
        for (Grade i : grades) {
            result += i.getGrade();
        }
        return result;
    }

    public double findGradePointAverage (List<Grade> grades ) {
        int lengthOfGrades = grades.size();
        double sum = addGradeResultsForSingleClass(grades);
        double result = sum / lengthOfGrades;

        // add a round function
        BigDecimal resultRound = BigDecimal.valueOf(result);
        resultRound = resultRound.setScale(2, RoundingMode.HALF_UP);
        return resultRound.doubleValue();

    }

    public List<Grade> getMathGradeResults() {
        return mathGradeResults;
    }

    public void setMathGradeResults(List<Grade> mathGradeResults) {
        this.mathGradeResults = mathGradeResults;
    }

    public List<Grade> getScienceGradeResults() {
        return scienceGradeResults;
    }

    public void setScienceGradeResults(List<Grade> scienceGradeResults) {
        this.scienceGradeResults = scienceGradeResults;
    }

    public List<Grade> getHistoryGradeResults() {
        return historyGradeResults;
    }

    public void setHistoryGradeResults(List<Grade> historyGradeResults) {
        this.historyGradeResults = historyGradeResults;
    }

    @Override
    public String toString() {
        return "StudentGrades{" +
                "mathGradeResults=" + mathGradeResults +
                '}';
    }
}
