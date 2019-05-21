package edu.uni.grademanagement.service;

import edu.uni.grademanagement.dto.CourseDto;
import edu.uni.professionalcourses.bean.Course;

import java.sql.SQLException;

/**
 * @Author 陈汉槟
 * @Create 2019/5/11
 * @Description  通过专业课程模块提供mapper创建课程业务层
 * @Since 1.0.0
 */
public interface CourseDtoService {

    CourseDto selectByCourseId(Long courseId) throws SQLException;

    Course selectCourseByCourseId(Long courseId) throws SQLException;
}
