package edu.uni.grademanagement.service;

import edu.uni.professionalcourses.bean.Course;

import java.util.List;

/**
 * @Author 陈汉槟
 * @Create 2019/6/8
 * @Description
 * @Since 1.0.0
 */
public interface CourseForGradeService {

    Course selectCourseByCourseId(Long courseId);

    List<Course> selectCoursesByCourseIds(List<Long> courseIds);
}
