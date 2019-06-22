package edu.uni.grademanagement.service;

import edu.uni.administrativestructure.bean.Class;

import java.util.List;

/**
 * @Author 陈汉槟
 * @Create 2019/6/8
 * @Description
 * @Since 1.0.0
 */
public interface AdministrativestructureService {

    String selectClassByClassNoAndClassName(
            String classNo, String className
    );

    Class selectClassByClassId(Long classId);

    List<Long> selectStudentIdsByClassIds(List<Long> classIds);

}
