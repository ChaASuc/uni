package edu.uni.grademanagement.service;

import edu.uni.educateAffair.bean.Teachingtask;
import edu.uni.grademanagement.bean.StuGradeMain;

import java.util.List;

/**
 * @Author 陈汉槟
 * @Create 2019/4/23
 * @Description 成绩业务层
 * @Since 1.0.0
 */
public interface StuGradeMainService {

/*
    *//**
     * 报废
     * @param studentId
     * @param semesterIds
     * @param pageNum
     * @return
     * @Description: 根据学生id、学期id集合和页码获取分页学生主表成绩
     *//*
    PageInfo<StuGradeMain> selectByStudentId(Long studentId, List<Long> semesterIds, int pageNum);*/

    /**
     *
     * @param universityId
     * @param teacherId
     * @param semesterId
     * @param studentIds
     * @param courseId
     * @return
     * @Description: 根据学院id、学期id、学生id集合、课程id和页码获取分页学生主表成绩
     * @Modify: 2019/5/19 添加缓存查询功能和教师、除学生、教师之外的在校角色区别查询
     */
    List<StuGradeMain> selectByCurriculum(Long universityId, Long teacherId, Long semesterId,
                                          List<Long> studentIds, Long courseId, Integer flag);


   /**
     *
     * @param universityId
     * @param teacherId
     * @param semesterId
     * @param courseId
     * @return
     * @Description: 根据学院id、学期id和教师id何课程id获取学生主表成绩集合
     */
    List<StuGradeMain> selectStuGradeMainByIds(
            Long universityId, Long teacherId, Long semesterId, Long courseId
    );

    /**
     *
     * @param stuGradeMainIds
     * @return
     * @Description: 根据成绩主表id集合获取学生主表成绩集合
     */
    List<StuGradeMain> selectStuGradeMainIds(
            List<Long> stuGradeMainIds
    );

    /**
     *
     * @param universityId
     * @param teacherId
     * @param semesterId
     * @param studentIds
     * @param courseId
     * @return
     * @Description: 根据学校id，教师id,学期id，学生id集合创建学生主表成绩分页
     * @Modify: 添加缓存功能
     */
    boolean insertStuGradeMainByIds(Long universityId, Long teacherId, Long semesterId,
                                    List<Long> studentIds, Long courseId, Integer state);


    /**
     *
     * @param studentId
     * @param deleted
     * @return
     * @Description: 获取该学生所有学期id
     */
    List<Long> selectDistinctSemesterId(Long studentId, Integer deleted);


    /**
     *
     * @param studentId
     * @return
     * @Description: 根据学生id获取该学生所有成绩
     */
    List<StuGradeMain> selectStugradeMainByStudentId(Long studentId, Long semesterId);
/*
    *//**
     * 报废
     * @param stuGradeMainId
     * @return
     * @Description: 根据学生id创建学生主表
     *//*
    Long insertStuGradeMainById(Long stuGradeMainId);*/

 /**
  * @param stuGradeMainId
  * @return
  * @Description: 根据学生id更新缓存状态
  */
 void updateStuGradeMainStateToCacheInvalid(Long stuGradeMainId);

 /**
     *
     * @param stuGradeMain
     * @return
     * @Description: 根据成绩主表实体类更新成绩主表
     */
    boolean updateStuGradeMain(StuGradeMain stuGradeMain);

     /**
      *
      * @param stuGradeMainId
      * @return
      * @description: 根据成绩主表id查询成绩主表
      */
     StuGradeMain selectByStuGradeMainId(Long stuGradeMainId);

    boolean insertStuGradeMainByIdsAndTeachingTasks(
            Long universityId, Long semesterId,
            Long courseId, List<Teachingtask> teachingtasks,
            Integer mainStateNoscore);

    List<StuGradeMain> selectByUniversityIdAndCourseIdAndSemesterId(Long universityId, Long semesterId, Long courseId);

    void rebulidStuGradeMainsSelective(Long stuGradeMainId, Integer state);

}
