package com.luv2code.springmvc.models;

import org.springframework.stereotype.Component;

@Component
public interface Grade {
    public double getGrade();

    public int getId();

    public void setId(int id);

    public int getStudentId();

    public void setStudentId(int studentId);

    public void setGrade(double grade);
}
