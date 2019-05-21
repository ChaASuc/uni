package edu.uni.grademanagement.service;

import edu.uni.userBaseInfo1.bean.Student;

/**
 * 暂时用
 *
 * @Author 陈汉槟
 * @Create 2019/5/14
 * @Description
 * @Since 1.0.0
 */
public interface StudentForGradeService {

    Student selectStudentByStuNo(String stuNo);
}
