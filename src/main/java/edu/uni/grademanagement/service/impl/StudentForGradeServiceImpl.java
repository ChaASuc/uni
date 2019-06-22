package edu.uni.grademanagement.service.impl;

import edu.uni.grademanagement.service.StudentForGradeService;
import edu.uni.userBaseInfo1.bean.Student;
import edu.uni.userBaseInfo1.bean.StudentExample;
import edu.uni.userBaseInfo1.bean.User;
import edu.uni.userBaseInfo1.bean.UserExample;
import edu.uni.userBaseInfo1.mapper.StudentMapper;
import edu.uni.userBaseInfo1.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author 陈汉槟
 * @Create 2019/5/14
 * @Description
 * @Since 1.0.0
 */
@Service
public class StudentForGradeServiceImpl implements StudentForGradeService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Student selectStudentByStuNo(String stuNo) {
        StudentExample studentExample = new StudentExample();
        studentExample.createCriteria()
                .andStuNoEqualTo(stuNo)
                .andDeletedEqualTo(false);
        List<Student> studentList = studentMapper.selectByExample(studentExample);
        if (CollectionUtils.isEmpty(studentList) || studentList.size() != 1) {
            // todo 优化 报错
        }
        return studentList.get(0);
    }

    @Override

    /**
     * @Param: [stuNo, studentName]
     * @Return:edu.uni.userBaseInfo1.bean.Student
     * @Author: 陈汉槟
     * @Date: 2019/6/8 18:48
     * @Description: 根据学号和姓名查看该学生是否存在
     */
    public String selectStudentByStuNoAndStudentName(String stuNo, String studentName) {
        StudentExample studentExample = new StudentExample();
        studentExample.createCriteria()
                .andStuNoEqualTo(stuNo);
        List<Student> studentList =
                studentMapper.selectByExample(studentExample);
        if (!CollectionUtils.isEmpty(studentList)) {
            Student student = studentList.get(0);
            User user = userMapper.selectByPrimaryKey(student.getUserId());
            if (user != null) {
                /*boolean equals = user.getName().equals(studentName);
                if (!equals) {
                    return "学号和姓名不匹配";
                }*/
                return "";
            }
            return "学号和姓名不匹配";
        }
        return "不存在该学号";
    }

    @Override
    public Student selectStudentByStudentId(Long studentId) {
        Student student = studentMapper.selectByPrimaryKey(studentId);
        return student;
    }
}
