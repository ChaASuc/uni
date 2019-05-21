package edu.uni.grademanagement.service;

import edu.uni.grademanagement.bean.CourseItem;
import edu.uni.grademanagement.bean.CourseItemDetail;
import edu.uni.grademanagement.dto.CourseItemDto;
import edu.uni.grademanagement.dto.StuGradeItemDto;

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
    Boolean insertCourseItemDetails(List<CourseItemDetail> courseItemDetails);


    /**
     *
     * @param courseItemDtos
     * @return
     * @description 批量更新课程成绩项及明细
     */
    Boolean updateCouseItemDto(List<CourseItemDto> courseItemDtos);


    /**
     * @param courseItemDetail
     * @return
     * @description 更新课程成绩项
     */
    Boolean updateCouseItemDetail(CourseItemDetail courseItemDetail);


    /**
     *
     * @param courseItemId
     * @return
     * @description 删除课程成绩项及其组成
     */
    Boolean deleteCourseItem(Long courseItemId);

    /**
     *
     * @param courseItemDetailId
     * @return
     * @description 删除课程成绩项目
     */
    Boolean deleteCourseItemDetail(Long courseItemDetailId);


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
     * @param byWho
     * @param courseId
     * @return
     * @Description 根据学校id教师id和课程获取这个教师教的课的课程成绩项
     */
    List<CourseItem> selectCouseItems(Long universityId, Long byWho, Long courseId);
}
