package edu.uni.professionalcourses.service;

import edu.uni.professionalcourses.bean.CourseExperimentPlan;

import java.util.List;

/**
 * author : 黄永佳
 * create : 2019/6/9 0009 21:40
 * modified :
 * 功能 :
 **/
public interface CourseExperimentPlanService {
    /**
     * 新增信息
     * @param courseExperimentPlan
     * @return
     */
    boolean insert(CourseExperimentPlan courseExperimentPlan);

    /**
     * 删除信息
     * @param id
     * @return
     */
    boolean delete(long id);

    /**
     * 更新信息
     * @param courseExperimentPlan
     * @return
     */
    boolean update(CourseExperimentPlan courseExperimentPlan);

    /**
     * 根据id查找信息
     * @param id
     * @return
     */
    CourseExperimentPlan select(long id);
    /**
     * 查找所有课程实验进度
     * @return
     */
    List<CourseExperimentPlan> selectAll();
    List<CourseExperimentPlan> selectByCourseId(long course_id);
}
