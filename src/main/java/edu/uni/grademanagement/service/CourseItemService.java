package edu.uni.grademanagement.service;

import edu.uni.grademanagement.bean.CourseItem;
import edu.uni.grademanagement.bean.CourseItemDetail;
import edu.uni.grademanagement.dto.CourseItemDto;
import edu.uni.grademanagement.dto.StuGradeItemDto;

import java.sql.SQLException;
import java.util.List;

/**
 * @Author 陈汉槟
 * @Create 2019/5/5
 * @Description 成绩组成项目及其明细业务层
 * @Since 1.0.0
 */
public interface CourseItemService {


    /**
     * @param universityId
     * @param teacherId
     * @param courseId
     * @return
     * @Description 根据学校id教师id和课程获取这个教师教的课的课程成绩项
     */
    List<CourseItemDto> selectCouseItemDtos(Long universityId, Long teacherId, Long courseId);

    /**
     *
     * @param courseItemId
     * @return
     * @Description 根据课程成绩项id获取课程成绩项及明细
     */
    CourseItemDto selectCouseItemDto(Long courseItemId);

    /**
     *
     * @param courseItemId
     * @return
     * @Description 根据课程成绩项id获取课程成绩项
     */
    CourseItem selectCouseItem(Long courseItemId);

    /**
     *
     * @param courseItem
     * @return
     * @Descriptino 创建课程成绩项
     */
    Boolean insertCourseItem(List<CourseItem> courseItem);


    /**
     *
     * @param courseItemDetails
     * @return
     * @Descriptino 创建课程成绩评分项
     */
    Boolean insertCourseItemDetails(List<CourseItemDetail> courseItemDetails) throws SQLException;


    /**
     *
     * @param courseItemDtos
     * @return
     * @description 批量更新课程成绩项及明细
     */
    Boolean updateCouseItemDto(List<CourseItemDto> courseItemDtos);


    /**
     * @param courseItemDetails
     * @return
     * @description 更新课程成绩项
     */
    Boolean updateCouseItemDetail(List<CourseItemDetail> courseItemDetails);


    /**
     *
     * @param courseItemIds
     * @return
     * @description 删除课程成绩项及其组成
     */
    Boolean deleteCourseItem(List<Long> courseItemIds) throws SQLException;

    /**
     *
     * @param courseItemDetailIds
     * @return
     * @description 删除课程成绩项目
     */
    Boolean deleteCourseItemDetail(List<Long> courseItemDetailIds) throws SQLException;


    /**
     *
     * @param courseItems
     * @return
     * @description: 根据课程成绩项集合查询完整的集合
     */
    List<CourseItem> selectCourseItems(List<CourseItem> courseItems);


    /**
     *
     * @param courseItemDetails
     * @return
     * @description: 根据课程成绩评分项集合查询完整的集合
     */
    List<CourseItemDetail> selectCourseItemDetails(List<CourseItemDetail> courseItemDetails);


    /**
     *
     * @param courseItemId
     * @return
     * @description 根据课程成绩项id获取课程成绩评分项
     */
    List<CourseItemDetail> selectCourseItemDetailsByCourseItemId(Long courseItemId);

    /**
     *
     * @param universityId
     * @param courseId
     * @return
     * @Description 根据学校id学期id和课程id获取这学期的课程成绩项
     * @Modify 2019/6/12 17:39
     */
    List<CourseItem> selectCouseItems(
            Long universityId, Long semesterId, Long courseId);

    /**
     * @param courseItemId
     * @return
     * @Description 根据课程成绩项id删除所属课程评分项目
     */
    Boolean deleteCourseItemDetailsByCourseItemId(Long courseItemId);

    /**
     *
     * @param id
     * @return
     * @Description 根据课程评分项id获取课程成绩项
     */
    CourseItem selectCourseItemByCourseItemDetailId(Long id);

    boolean updateCouseItem(List<CourseItem> courseItems);

    List<CourseItemDetail> selectCourseItemDetailsByCourseItemIdAndTeacherId(Long courseItemId, Long teacherId);

    CourseItemDetail selectCourseItemDetailByCouserItemDetailId(Long courseItemDetailId);

}
