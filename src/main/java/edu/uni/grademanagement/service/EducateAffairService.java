package edu.uni.grademanagement.service;

import edu.uni.administrativestructure.bean.Class;
import edu.uni.educateAffair.bean.Teachingtask;

import java.util.List;

/**
 * @Author 陈汉槟
 * @Create 2019/6/12
 * @Description 学历模块事务层
 * @Since 1.0.0
 */
public interface EducateAffairService {

    List<Teachingtask> selectTeachingTaskByIds(
            List<Long> semesterIds, List<Long> teacherIds,
            List<Long> courseIds, List<Long> classIds, List<Long> departmentIds
    );

    /**
     *
     * @param semesterId
     * @return
     * @desription: 获取课表某学期的所有课程id集合
     */
    List<Long> selectCourseByTeachingTaskBySemesterId(Long semesterId);

    /**
     *
     * @param universityId
     * @param semesterId
     * @param courseId
     * @return
     * @description: 获取课表某校某学期某课的班级id集合
     */
    List<Teachingtask> selectTeachingTaskById(
            Long universityId, Long semesterId,
            Long courseId);

}
