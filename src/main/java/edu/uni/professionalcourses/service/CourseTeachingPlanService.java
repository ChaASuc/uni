package edu.uni.professionalcourses.service;

import com.github.pagehelper.PageInfo;
import edu.uni.professionalcourses.bean.CourseTeachingPlan;

import java.util.List;

/**
 * author : 黄永佳
 * create : 2019/4/30 0030 16:19
 * modified :
 * 功能 :创建Course_Teaching_Plan表的接口
 **/
public interface CourseTeachingPlanService {
    /**
     * 新增课程教学计划
     * @param courseTeachingPlan
     * @return
     */
    boolean insert(CourseTeachingPlan courseTeachingPlan);

    /**
     * 删除课程教学计划
     * @param id
     * @return
     */
    boolean delete(long id);

    /**
     * 修改课程教学计划
     * @param
     * @return
     */
    boolean update(CourseTeachingPlan courseTeachingPlan);

    /**
     * 查询课程教学计划
     * @param id
     * @return
     */
    CourseTeachingPlan select(long id);

    /**
     * 分页查询所有课程教学计划
     * @param pageNum
     * @return
     */
    PageInfo<CourseTeachingPlan> selectPage(int pageNum);
    /**
     * 分学校分页查询课程教学计划
     * @param pageNum
     * @param universityId
     * @return
     */
    PageInfo<CourseTeachingPlan> selectPageByUniversity(int pageNum, long universityId);
    /**
     * 分课程分页查询课程教学计划
     * @param pageNum
     * @param courseId
     * @return
     */
    PageInfo<CourseTeachingPlan> selectPageByCourse(int pageNum, long courseId);
    /**
     * 分课程教学进度分页查询课程教学计划
     * @param pageNum
     * @param courseteachingscheduleId
     * @return
     */
    PageInfo<CourseTeachingPlan> selectPageByCourseteachingschedule(int pageNum, long courseteachingscheduleId);
    /**
     * 分课程实验进度分页查询课程教学计划
     * @param pageNum
     * @param courseexperimentscheduleId
     * @return
     */
//    PageInfo<CourseTeachingPlan> selectPageByCourseexperimentschedule(int pageNum, long courseexperimentscheduleId);

    /**
     * 查找所有
     * @return
     */
    List<CourseTeachingPlan> selectAll();

    List<CourseTeachingPlan> selectByCourseId(long course_id);
}
