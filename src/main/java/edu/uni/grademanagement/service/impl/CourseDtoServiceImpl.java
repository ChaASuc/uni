package edu.uni.grademanagement.service.impl;

import edu.uni.grademanagement.dto.CourseDto;
import edu.uni.grademanagement.service.CourseDtoService;
import edu.uni.professionalcourses.bean.*;
import edu.uni.professionalcourses.mapper.CourseCategoryMapper;
import edu.uni.professionalcourses.mapper.CourseMapper;
import edu.uni.professionalcourses.mapper.CourseSpeciesMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * @Author 陈汉槟
 * @Create 2019/5/11
 * @Description 通过专业课程模块提供mapper创建课程业务层
 * @Since 1.0.0
 */
@Service
public class CourseDtoServiceImpl implements CourseDtoService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseSpeciesMapper courseSpeciesMapper;

    @Autowired
    private CourseCategoryMapper courseCategoryMapper;

    @Override
    public CourseDto selectByCourseId(Long courseId) throws SQLException {
        CourseDto courseDto = new CourseDto();
        CourseExample courseExample = new CourseExample();
        courseExample.createCriteria()
                .andIdEqualTo(courseId)
                .andDeletedEqualTo(false);
        List<Course> courseList =
                courseMapper.selectByExample(courseExample);
        if (CollectionUtils.isEmpty(courseList)) {
            return null;
        } else if (courseList.size() > 1) {
            throw new SQLException("课程获取错误");
        }
        Course course = courseList.get(0);
        BeanUtils.copyProperties(
                course, courseDto
        );

        CourseSpeciesExample courseSpeciesExample
                = new CourseSpeciesExample();
        courseSpeciesExample.createCriteria()
                .andIdEqualTo(course.getSpeciesId())
                .andDeletedEqualTo(false);
        List<CourseSpecies> courseSpeciesList =
                courseSpeciesMapper.selectByExample(courseSpeciesExample);
        if (CollectionUtils.isEmpty(courseSpeciesList)) {
            courseDto.setSpeciesName(null);
        } else if (courseSpeciesList.size() > 1) {
            throw new SQLException("课程类别获取错误");
        }else {
            CourseSpecies courseSpecies = courseSpeciesList.get(0);
            courseDto.setSpeciesName(courseSpecies.getName());
        }

        CourseCategoryExample courseCategoryExample = new CourseCategoryExample();
        courseCategoryExample.createCriteria()
                .andIdEqualTo(course.getCategoryId())
                .andDeletedEqualTo(false);
        List<CourseCategory> courseCategoryList =
                courseCategoryMapper.selectByExample(courseCategoryExample);
        if (CollectionUtils.isEmpty(courseCategoryList)) {
            courseDto.setCategoryName(null);
        } else if (courseCategoryList.size() > 1) {
            throw new SQLException("课程分类获取错误");
        }else {
            CourseCategory courseCategory = courseCategoryList.get(0);
            courseDto.setCategoryName(courseCategory.getName());
        }

        return courseDto;
    }

    @Override
    public Course selectCourseByCourseId(Long courseId) throws SQLException {

        CourseExample courseExample = new CourseExample();
        courseExample.createCriteria()
                .andIdEqualTo(courseId)
                .andDeletedEqualTo(false);
        List<Course> courseList =
                courseMapper.selectByExample(courseExample);
        if (CollectionUtils.isEmpty(courseList)) {
            return null;
        } else if (courseList.size() > 1) {
            throw new SQLException("课程获取错误");
        }
        Course course = courseList.get(0);
        return course;
    }
}
