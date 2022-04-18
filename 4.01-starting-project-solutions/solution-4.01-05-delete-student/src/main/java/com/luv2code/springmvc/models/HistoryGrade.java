package com.luv2code.springmvc.models;

import javax.persistence.*;

@Entity
@Table(name = "history_grade")
public class HistoryGrade implements Grade {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @Column(name="student_id")
    private int studentId;
    @Column(name="grade")
    private double grade;

    public HistoryGrade() {

    }

    public HistoryGrade(double grade) {
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    @Override
    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}
