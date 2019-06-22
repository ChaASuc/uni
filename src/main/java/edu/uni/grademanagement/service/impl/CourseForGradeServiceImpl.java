package edu.uni.grademanagement.service.impl;

import edu.uni.grademanagement.service.CourseForGradeService;
import edu.uni.professionalcourses.bean.Course;
import edu.uni.professionalcourses.bean.CourseExample;
import edu.uni.professionalcourses.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 陈汉槟
 * @Create 2019/6/8
 * @Description
 * @Since 1.0.0
 */
@Service
public class CourseForGradeServiceImpl implements CourseForGradeService {
    @Autowired
    private CourseMapper courseMapper;

    @Override
    public Course selectCourseByCourseId(Long courseId) {
        Course course = courseMapper.selectByPrimaryKey(courseId);

        return course;
    }

    @Override
    public List<Course> selectCoursesByCourseIds(List<Long> courseIds) {
        CourseExample courseExample = new CourseExample();
        courseExample.createCriteria()
                .andIdIn(courseIds);
        List<Course> courses =
                courseMapper.selectByExample(courseExample);
        return courses;
    }
}
