package edu.uni.grademanagement.service.impl;

import edu.uni.grademanagement.service.StudentForGradeService;
import edu.uni.userBaseInfo1.bean.Student;
import edu.uni.userBaseInfo1.bean.StudentExample;
import edu.uni.userBaseInfo1.mapper.StudentMapper;
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

    @Override
    public Student selectStudentByStuNo(String stuNo) {
        StudentExample studentExample = new StudentExample();
        studentExample.createCriteria()
                .andStuNoEqualTo(stuNo)
                .andDeletedEqualTo(true);
        List<Student> studentList = studentMapper.selectByExample(studentExample);
        if (CollectionUtils.isEmpty(studentList) || studentList.size() != 1) {
            // todo 优化 报错
        }
        return studentList.get(0);
    }
}
