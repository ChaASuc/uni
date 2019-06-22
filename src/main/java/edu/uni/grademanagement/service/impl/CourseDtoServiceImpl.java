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
    public CourseDto selectByCourseId(Long courseId){
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
            throw new RuntimeException("课程获取错误");
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
            throw new RuntimeException("课程类别获取错误");
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
            throw new RuntimeException("课程分类获取错误");
        }else {
            CourseCategory courseCategory = courseCategoryList.get(0);
            courseDto.setCategoryName(courseCategory.getName());
        }

        return courseDto;
    }

    @Override
    public Course selectCourseByCourseId(Long courseId){

        CourseExample courseExample = new CourseExample();
        courseExample.createCriteria()
                .andIdEqualTo(courseId)
                .andDeletedEqualTo(false);
        List<Course> courseList =
                courseMapper.selectByExample(courseExample);
        if (CollectionUtils.isEmpty(courseList)) {
            return null;
        } else if (courseList.size() > 1) {
            throw new RuntimeException("课程获取错误");
        }
        Course course = courseList.get(0);
        return course;
    }

    @Override

    /**
     * @Param: [courseNo, courseName]
     * @Return:boolean
     * @Author: 陈汉槟
     * @Date: 2019/6/8 18:55
     * @Description: 根据课程号和课程名查看是否在
     */
    public String selectCourseByCourseNoAndCourseName(String courseNo, String courseName) {
        CourseExample courseExample = new CourseExample();
        courseExample.createCriteria()
                .andNumberEqualTo(courseNo)
                .andNameEqualTo(courseName);
        List<Course> courses = courseMapper.selectByExample(courseExample);
        if (!CollectionUtils.isEmpty(courses)) {
            return "";
        }
        return "课程编号与课程名称不匹配";
    }

    @Override
    public List<Course> selectByUniversityId(Long universityId) {
        CourseExample courseExample = new CourseExample();
        courseExample.createCriteria()
                .andUniversityIdEqualTo(universityId)
                .andDeletedEqualTo(false);
        List<Course> courses = courseMapper.selectByExample(courseExample);
        return courses;
    }
}
