package edu.uni.professionalcourses.service;

import com.github.pagehelper.PageInfo;
import edu.uni.professionalcourses.bean.LearningProcess;

import java.util.List;

/**
 * author : 黄永佳
 * create : 2019/4/30 0030 15:36
 * modified :
 * 功能 :创建LearningProcess表的接口
 **/
public interface LearningProcessService {
    /**
     * 新增专业学习计划
     * @param learningProcess
     * @return
     */
    boolean insert(LearningProcess learningProcess);

    /**
     * 删除专业学习计划
     * @param id
     * @return
     */
    boolean delete(long id);

    /**
     * 修改专业学习计划
     * @param
     * @return
     */
    boolean update(LearningProcess learningProcess);

    /**
     * 查询专业学习计划
     * @param id
     * @return
     */
    LearningProcess select(long id);

    /**
     * 分页查询所有专业学习计划
     * @param pageNum
     * @return
     */
    PageInfo<LearningProcess> selectPage(int pageNum);
    /**
     * 分学校分页查询专业学习计划
     * @param pageNum
     * @param universityId
     * @return
     */
    PageInfo<LearningProcess> selectPageByUniversity(int pageNum, long universityId);
    /**
     * 分专业分页查询专业学习计划
     * @param pageNum
     * @param specialtyid
     * @return
     */
    PageInfo<LearningProcess> selectPageBySpecialty(int pageNum, long specialtyid);
    /**
     * 分课程分页查询专业学习计划
     * @param pageNum
     * @param courseid
     * @return
     */
    PageInfo<LearningProcess> selectPageByCourse(int pageNum, long courseid);
    /**
     * 查找所有
     * @return
     */
    List<LearningProcess> selectAll();
}
