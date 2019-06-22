package edu.uni.grademanagement.service;

import edu.uni.professionalcourses.bean.Course;

import java.util.List;

/**
 * @Author 陈汉槟
 * @Create 2019/6/4
 * @Description
 * @Since 1.0.0
 */
public interface CurriculumForGradeService {

    List<Long> selectCourseIdBySemeterId(Long semesterId);

    List<Long> selectCourseId();
}
