package edu.uni.grademanagement.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author 陈汉槟
 * @Create 2019/5/14
 * @Description 前端交互，显示课程+班级+考试情况
 * @Since 1.0.0
 */
@Data
public class StuGradeCurriculumVO implements Serializable {

    /*学校id和名*/
    private Long universityId;

    private String universityName;

    /*学期id和名*/
    private Long semesterId;

    private String semesterName;

    /*班级id和名和编号和学生人数*/
    private Long classId;

    private String className;

    private String classCode;

    private Integer classStudentNumber;

    /*课程id和名和编号*/
    private Long courseId;

    private String courseName;

    private String courseNumber;


    /*考试通过和不通过人数*/
    private Integer stuGradeSuccessNumber;

    private Integer stuGradeFailNumber;

    //教师ID
    private Long teacherId;

    private String teacherName;


}
