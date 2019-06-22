package edu.uni.grademanagement.service;

import edu.uni.grademanagement.dto.CourseDto;
import edu.uni.professionalcourses.bean.Course;

import java.sql.SQLException;
import java.util.List;

/**
 * @Author 陈汉槟
 * @Create 2019/5/11
 * @Description  通过专业课程模块提供mapper创建课程业务层
 * @Since 1.0.0
 */
public interface CourseDtoService {

    CourseDto selectByCourseId(Long courseId);

    Course selectCourseByCourseId(Long courseId);

    String selectCourseByCourseNoAndCourseName(String courseNo, String courseName);

    List<Course> selectByUniversityId(Long universityId);
}
