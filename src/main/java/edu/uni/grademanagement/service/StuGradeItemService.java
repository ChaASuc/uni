package edu.uni.grademanagement.service;

import edu.uni.grademanagement.bean.CourseItem;
import edu.uni.grademanagement.bean.StuGradeMain;
import edu.uni.grademanagement.bean.StuItemGrade;
import edu.uni.grademanagement.dto.StuGradeItemDto;
import edu.uni.grademanagement.form.UpdateStuGradeMainForm;

import java.util.List;

/**
 * @Author 陈汉槟
 * @Create 2019/5/5
 * @Description 课程成绩组成项目及该成绩业务层接口
 * @Since 1.0.0
 */
public interface StuGradeItemService {


    /**
     *
     * @param stuGradeMainId
     * @return
     * @Dwscription 不同角色根据学生主表id获取
     */
    List<StuGradeItemDto> selectByStuGradeMainId(Long stuGradeMainId, Integer flag);

    /**
     *
     * @param courseItem
     * @param stuGradeMains
     * @return
     * @description 根据课程成绩项何成绩何成绩id创建StuGradeItemDto
     */
    boolean insertStuGradeItemDto(CourseItem courseItem, List<StuGradeMain> stuGradeMains);


    /**
     *
     * @param stuItemGrades
     * @return
     * @description: 批量删除成绩主表中课程成绩项，把deleted改为1 无效
     */
    boolean deletedUpdateStuGradeItem(List<StuItemGrade> stuItemGrades);

    /**
     *
     * @param stuGradeMainId
     * @param courseItemId
     * @return
     * @description: 根据成绩主表id何课程成绩项id获取成绩项
     */
    StuItemGrade selectStuItemGradeByStuGradeMainId(Long stuGradeMainId, Long courseItemId);


    /**
     *
     * @param stuGradeMainId
     * @param id
     * @return
     * @description: 根据成绩主表id何课程成绩项id获取成绩项，删除
     */
    boolean deleteStuItemGradeByStuGradeMainIdAndCourseItemId(Long stuGradeMainId, Long id);

    /**
     *
     * @param stuItemGrade
     * @return
     * @description: 根据成绩项更新信息
     */
    boolean updateStuItemGrade(StuItemGrade stuItemGrade);

    /**
     *
     * @param stuGradeMainId
     * @return
     * @description: 根据原学生id查询成绩项，并插入到新的成绩主表id的成绩项中
     */
    UpdateStuGradeMainForm insertStuItemGradeByStuGradeMainId(Long stuGradeMainId, Long cacheStuGradeMainId);

    /**
     *
     * @param universityId
     * @param courseItemId
     * @param teacherId
     * @param stuGradeMainId
     * @return
     * @description: 根据学校id，课程成绩项id，教师id，学生主表id 创建成绩项
     */
    boolean insertStuGradeItemByExcelContent(Long universityId, Long courseItemId, Long teacherId, Long stuGradeMainId);

    /**
     *
     * @param stuItemGradeId
     * @return
     * @description: 根据成绩项id获取成绩项
     */
    StuItemGrade selectByStuItemGradeId(Long stuItemGradeId);
}
