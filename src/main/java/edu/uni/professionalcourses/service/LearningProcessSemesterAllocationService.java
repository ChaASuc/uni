package edu.uni.professionalcourses.service;

import com.github.pagehelper.PageInfo;
import edu.uni.professionalcourses.bean.LearningProcessSemesterAllocation;

import java.util.List;

/**
 * author : 黄永佳
 * create : 2019/4/29 0029 16:31
 * modified :
 * 功能 :创建LearningProcessSemesterAllocation表的接口
 **/
public interface LearningProcessSemesterAllocationService {
    /**
     * 新增学习计划学期分配信息
     * @param learningProcessSemesterAllocation
     * @return
     */
    boolean insert(LearningProcessSemesterAllocation learningProcessSemesterAllocation);

    /**
     * 删除专业课程信息
     * @param id
     * @return
     */
    boolean delete(long id);

    /**
     * 修改学习计划学期分配信息
     * @param
     * @return
     */
    boolean update(LearningProcessSemesterAllocation learningProcessSemesterAllocation);

    /**
     * 查询学习计划学期分配信息
     * @param id
     * @return
     */
    LearningProcessSemesterAllocation select(long id);

    /**
     * 分页查询所有学习计划学期分配信息
     * @param pageNum
     * @return
     */
    PageInfo<LearningProcessSemesterAllocation> selectPage(int pageNum);
    /**
     * 分学校分页查询学习计划学期分配信息
     * @param pageNum
     * @param universityId
     * @return
     */
    PageInfo<LearningProcessSemesterAllocation> selectPageByUniversity(int pageNum, long universityId);
    /**
     * 分专业分页查询专学习计划学期分配信息
     * @param pageNum
     * @param learningprocessId
     * @return
     */
    PageInfo<LearningProcessSemesterAllocation> selectPageByLearningprocess(int pageNum, long learningprocessId);
    /**
     * 查找所有
     * @return
     */
    List<LearningProcessSemesterAllocation> selectAll();
}
