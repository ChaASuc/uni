package edu.uni.grademanagement.service;

import edu.uni.grademanagement.bean.CourseItemDetail;
import edu.uni.grademanagement.bean.StuItemGrade;
import edu.uni.grademanagement.bean.StuItemGradeDetail;
import edu.uni.grademanagement.dto.StuGradeItemDetailDto;
import edu.uni.grademanagement.form.UpdateStuGradeMainForm;

import java.sql.SQLException;
import java.util.List;

/**
 * @Author 陈汉槟
 * @Create 2019/5/5
 * @Description 成绩评分项及该成绩业务层接口
 * @Since 1.0.0
 */
public interface StuGradeItemDetailService {


    /**
     * @param stuItemGradeId
     * @return
     * @Dwscription 根据学生课程成绩项目id获取
     * @Modify: 根据不同角色获取不同角色成绩作业项
     */
    List<StuGradeItemDetailDto> selectByStuItemGradeId(Long stuItemGradeId, Integer flag);


    /**
     * @param courseItemDetail
     * @param stuGradeMainId
     * @return
     * @description 根据课程成绩项何成绩何成绩id创建StuGradeItemDto
     */
    boolean insertStuGradeItemDetailDto(CourseItemDetail courseItemDetail, Long stuGradeMainId);

    /**
     *
     * @param stuItemGrades
     * @return
     * @description: 批量删除成绩主表中课程成绩项，把deleted改为1 无效
     */
    boolean deletedUpdateStuGradeItem(List<StuItemGrade> stuItemGrades);


    /**
     *
     * @param stuItemGradeDetail
     * @return
     * @description 插入学生作业项
     */
    boolean insertStuItemGradeDetail(StuItemGradeDetail stuItemGradeDetail);




    /**
     *
     * @param stuItemGradeId
     * @param courseItemDetailId
     * @return
     * @description 根据学生成绩项id何课程评分项id获取作业项目，并删除
     */
    boolean deleteStuItemGradeDetailByStuItemGradeIdAndCourseItemDetailId(Long stuItemGradeId, Long courseItemDetailId);


    /**
     *
     * @param stuItemGradeDetails
     * @return
     * @description 根据学生成绩项评分项集合更新
     */
    boolean updateStuItemGradeDetails(List<StuItemGradeDetail> stuItemGradeDetails) throws SQLException;


    /**
     *
     * @param form
     * @return
     * @description 根据UpdateStuGradeMainForm里原学生成绩项id集合与缓存学生成绩项id集合
     */
    boolean insertStuItemGradeDetailsByUpdateStuGradeMainForm(
            UpdateStuGradeMainForm form
    );

    /**
     *
     * @param stuItemGradeId
     * @param courseItemDetailId
     * @param universityId
     * @param teacherId
     * @description 根据学校id，课程评分项id，教师id，成绩项id, 分数，评论创建成绩作业项
     */
    boolean insertStuGradeItemByExcelContent(Long stuItemGradeId, Long courseItemDetailId, Long universityId, Long teacherId,
                                             double score, String note) throws SQLException;

    /**
     *
     * @param stuItemGradeId
     * @param courseItemDetailId
     * @return
     * @description: 根据成绩项id，课程评分项id获取成绩作业项
     */
    StuItemGradeDetail selectByStuItemGradeIdAndCourseItemDetailId(
            Long stuItemGradeId, Long courseItemDetailId, Integer cache);

    /**
     *
     * @param stuItemGradeId
     * @return
     * @description: 根据成绩项目id获取所有成绩评分项
     */
    List<StuItemGradeDetail> selectStuItemGradeDetailByStuItemGradeId(Long stuItemGradeId, Integer flag);


    /**
     *
     * @param stuItemGradeDetailId
     * @return
     * @Description: 根据成绩明细id获取成绩评分项
     */
    StuItemGradeDetail selectByStuItemGradeDetailId(Long stuItemGradeDetailId);

    void updateStuGradeItemDetailStateToCacheInvalid(List<Long> stuItemGradeIds);

    void updateStuItemGradeDetail(StuItemGradeDetail stuItemGradeDetail);

    void rebulidStuGradeItemDetail(
            Long stuItemGradeId, Long rebulidStuItemGradeId, Integer teacher);
}
