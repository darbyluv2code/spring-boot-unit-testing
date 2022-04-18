package com.luv2code.springmvc.repository;

import com.luv2code.springmvc.models.MathGrade;
import org.springframework.data.repository.CrudRepository;

public interface MathGradesDao extends CrudRepository<MathGrade, Integer> {

    public Iterable<MathGrade> findGradeByStudentId(int id);

    public void deleteByStudentId(int id);
}
