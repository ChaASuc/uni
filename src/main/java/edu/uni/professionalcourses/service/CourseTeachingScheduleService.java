package edu.uni.professionalcourses.service;

import com.github.pagehelper.PageInfo;
import edu.uni.professionalcourses.bean.CourseTeachingSchedule;

import java.util.List;

/**
 * author : 黄永佳
 * create : 2019/5/5 0005 16:31
 * modified :
 * 功能 :创建CourseTeachingSchedule表的接口
 **/
public interface CourseTeachingScheduleService {
    /**
     * 新增课程教学进度
     * @param courseTeachingSchedule
     * @return
     */
    boolean insert(CourseTeachingSchedule courseTeachingSchedule, long courseId);

    /**
     * 删除课程教学进度
     * @param id
     * @return
     */
    boolean delete(long id);

    /**
     * 修改课程教学进度
     * @param
     * @return
     */
    boolean update(CourseTeachingSchedule courseTeachingSchedule);

    /**
     * 查询课程教学进度
     * @param id
     * @return
     */
    CourseTeachingSchedule select(long id);

    /**
     * 分页查询所有课程教学进度
     * @param pageNum
     * @return
     */
    PageInfo<CourseTeachingSchedule> selectPage(int pageNum);
    /**
     * 分学校分页查询课程教学进度
     * @param pageNum
     * @param universityId
     * @return
     */
    PageInfo<CourseTeachingSchedule> selectPageByUniversity(int pageNum, long universityId);
    /**
     * 查找所有
     * @return
     */
    List<CourseTeachingSchedule> selectAll();
}
