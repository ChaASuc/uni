package edu.uni.grademanagement.service.impl;

import edu.uni.administrativestructure.bean.Class;
import edu.uni.administrativestructure.bean.ClassExample;
import edu.uni.administrativestructure.mapper.ClassMapper;
import edu.uni.grademanagement.service.AdministrativestructureService;
import edu.uni.userBaseInfo1.bean.Student;
import edu.uni.userBaseInfo1.bean.StudentExample;
import edu.uni.userBaseInfo1.mapper.StudentMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.internal.Classes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 陈汉槟
 * @Create 2019/6/8
 * @Description
 * @Since 1.0.0
 */
@Service
@Slf4j
public class AdministrativestructureServiceImpl implements AdministrativestructureService {

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private StudentMapper studentMapper;
    @Override
    public Class selectClassByClassId(Long classId) {
        Class aClass = classMapper.selectByPrimaryKey(classId);
        return aClass;
    }

    @Override
    public List<Long> selectStudentIdsByClassIds(List<Long> classIds) {
        StudentExample studentExample = new StudentExample();
        studentExample.createCriteria()
                .andClassIdIn(classIds)
                .andDeletedEqualTo(false);
        List<Student> studentList =
                studentMapper.selectByExample(studentExample);
        List<Long> studentIds =
                studentList.stream().map(Student::getUserId).collect(Collectors.toList());
        return studentIds;
    }


    @Override
    public String selectClassByClassNoAndClassName(String classNo, String className) {
        ClassExample classExample = new ClassExample();
        classExample.createCriteria()
                .andCodeEqualTo(classNo)
                .andNameEqualTo(className);
        List<Class> classes =
                classMapper.selectByExample(classExample);
        if (!CollectionUtils.isEmpty(classes)) {
            return "";
        }
        return "班级号与班级不匹配";
    }
}
