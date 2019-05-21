package edu.uni.grademanagement.dto;

import edu.uni.professionalcourses.bean.Course;
import lombok.Data;

/**
 * @Author 陈汉槟
 * @Create 2019/5/11
 * @Description 通过专业课程模块提供bean创建dto实体类
 * @Since 1.0.0
 */
@Data
public class CourseDto {

    private Long id;

    private Long universityId;

    private String number;

    private String name;

    private String ename;

    private Long speciesId;

    private String speciesName;

    private Long categoryId;

    private String categoryName;
}
