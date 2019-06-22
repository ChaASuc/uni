package edu.uni.grademanagement.service.impl;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.regexp.internal.RE;
import edu.uni.grademanagement.bean.CourseItem;
import edu.uni.grademanagement.bean.CourseItemDetail;
import edu.uni.grademanagement.bean.CourseItemDetailExample;
import edu.uni.grademanagement.bean.CourseItemExample;
import edu.uni.grademanagement.constants.GradeConstant;
import edu.uni.grademanagement.dto.CourseItemDto;
import edu.uni.grademanagement.mapper.CourseItemDetailMapper;
import edu.uni.grademanagement.mapper.CourseItemMapper;
import edu.uni.grademanagement.service.CourseItemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 陈汉槟
 * @Create 2019/5/2
 * @Description
 * @Since 1.0.0
 */
@Service
@Slf4j
public class CourseItemServiceImpl implements CourseItemService {

    @Autowired
    private CourseItemMapper courseItemMapper;

    @Autowired
    private CourseItemDetailMapper courseItemDetailMapper;

    @Override
    /**
     *
     * @Param [universityId, teacherId, courseId]
     * @Return:java.util.List<edu.uni.grademanagement.dto.CourseItemDto>
     * @Author: 陈汉槟
     * @Date: 2019/5/6 7:53
     * @Description: 根据学院id教师id和课程id获取课程成绩组成项目
     */
    public List<CourseItemDto> selectCouseItemDtos(Long universityId, Long teacherId, Long courseId) {

        CourseItemExample courseItemExample = new CourseItemExample();
        courseItemExample.createCriteria()
//                .andByWhoEqualTo(teacherId)
                .andCourseIdEqualTo(courseId)
                .andUniversityIdEqualTo(universityId)
                .andDeletedEqualTo(GradeConstant.RECORD_VALID);
        List<CourseItem> courseItems = courseItemMapper.selectByExample(courseItemExample);
        if (CollectionUtils.isEmpty(courseItems)) {
            return null;
        }
        List<CourseItemDto> courseItemDtos = courseItems.stream().map(
                courseItem -> {
                    CourseItemDto courseItemDto = getCourseItemDto(courseItem);
                    return courseItemDto;
                }
        ).collect(Collectors.toList());
        return courseItemDtos;
    }




    @Override
    /**
     *
     * @Param [courseItemId]
     * @Return:edu.uni.grademanagement.dto.CourseItemDto
     * @Author: 陈汉槟
     * @Date: 2019/5/6 13:44
     * @Description:
     */
    public CourseItemDto selectCouseItemDto(Long courseItemId) {
        CourseItem courseItem = courseItemMapper.selectByPrimaryKey(courseItemId);
        if (courseItem == null) {
            return null;
        }
        CourseItemDto courseItemDto = getCourseItemDto(courseItem);
        return courseItemDto;
    }

    @Override

    /**
     *
     * @Param [courseItemId]
     * @Return:edu.uni.grademanagement.bean.CourseItem
     * @Author: 陈汉槟
     * @Date: 2019/5/9 22:20
     * @Description: 根据课程成绩项id获取课程成绩项
     */
    public CourseItem selectCouseItem(Long courseItemId) {
        CourseItem courseItem = courseItemMapper.selectByPrimaryKey(courseItemId);
        return courseItem;
    }

    @Override

    /**
     *
     * @Param [courseItems]
     * @Return:java.lang.Boolean
     * @Author: 陈汉槟
     * @Date: 2019/5/6 10:55
     * @Description: 课程成绩项目添加
     */
    @Transactional //todo
    public Boolean insertCourseItem(List<CourseItem> courseItems) {
        for (CourseItem courseItem :
                courseItems) {
            int i = courseItemMapper.insertSelective(courseItem);
            if (i == 0) {
                return false;
            }
        }
//        int i = courseItemMapper.insertCourseItemLists(courseItems);
//        if (i == 0) {
//            return false;
//        }
        return true;
    }

    @Override

    /**
     *
     * @Param [courseItemDetail]
     * @Return:java.lang.Boolean
     * @Author: 陈汉槟
     * @Date: 2019/5/6 10:55
     * @Description: 课程成绩评分项目添加
     */
    @Transactional
    public Boolean insertCourseItemDetails(List<CourseItemDetail> courseItemDetails) throws SQLException {
        for (CourseItemDetail courseItemDetail :
                courseItemDetails){
            int insert = courseItemDetailMapper.insertSelective(courseItemDetail);
                if (insert == 0) {
                    throw new SQLException("插入失败");
                }
            }
        return true;
    }

    @Override

    /**
     *
     * @Param [courseItemDtos]
     * @Return:java.lang.Boolean
     * @Author: 陈汉槟
     * @Date: 2019/5/6 10:56
     * @Description: 批量更新课程成绩项目及明细
     */
    @Transactional
    public Boolean updateCouseItemDto(List<CourseItemDto> courseItemDtos) {
        for (CourseItemDto courseItemDto:
             courseItemDtos) {
            if (courseItemDto.getId() != null) {
                CourseItem courseItem = new CourseItem();
                BeanUtils.copyProperties(courseItemDto, courseItem);
                int update = courseItemMapper.updateByPrimaryKeySelective(courseItem);
                if (update == 0) {
                    // todo 报错
                    try {
                        throw new SQLException("课程成绩项更新失败");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            }
            List<CourseItemDetail> courseItemDetails = courseItemDto.getCourseItemDetails();
            if (!CollectionUtils.isEmpty(courseItemDetails)) {
                for (CourseItemDetail courseItemDetail :
                        courseItemDetails) {
                    int update = courseItemDetailMapper.updateByPrimaryKeySelective(courseItemDetail);
                    if (update == 0) {
                        // todo 报错
                        try {
                            throw new SQLException("课程成绩评分项更新失败");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
        return true;

    }

    @Override

    /**
     *
     * @Param [courseItemDetail]
     * @Return:java.lang.Boolean
     * @Author: 陈汉槟
     * @Date: 2019/5/6 10:57
     * @Description: 更新课程成绩评分项
     */
    @Transactional
    public Boolean updateCouseItemDetail(List<CourseItemDetail> courseItemDetails) {
        courseItemDetails.stream().forEach(
                courseItemDetail -> {
                    int update = courseItemDetailMapper.updateByPrimaryKeySelective(courseItemDetail);
                    if (update == 0) {
                        try {
                            throw new SQLException("课程评分项目更新失败");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        return true;
    }

    @Override

    /**
     *
     * @Param [courseItem]
     * @Return:java.lang.Boolean
     * @Author: 陈汉槟
     * @Date: 2019/5/6 10:58
     * @Description: 删除课程成绩项
     */
    @Transactional
    public Boolean deleteCourseItem(List<Long> courseItemIds) throws SQLException {
        CourseItemExample courseItemExample = new CourseItemExample();
        courseItemExample.createCriteria()
                .andIdIn(courseItemIds);
        CourseItem item = new CourseItem();
        item.setDeleted(GradeConstant.RECORD_INVALID);
        int update = courseItemMapper.updateByExampleSelective(item, courseItemExample);
        if (update == 0) {
            throw new SQLException("删除课程成绩项失败");
        }
        return true;
    }

    @Override

    /**
     *
     * @Param [courseItemDetail]
     * @Return:java.lang.Boolean
     * @Author: 陈汉槟
     * @Date: 2019/5/6 10:58
     * @Description: 删除课程成绩评分项
     */
    @Transactional
    public Boolean deleteCourseItemDetail(List<Long> courseItemDetailIds) throws SQLException {
//        int delete_courseItemDetail = courseItemDetailMapper.updateDeletedByPrimaryKey(courseItemDetailId, GradeConstant.RECORD_INVALID);
        CourseItemDetailExample courseItemDetailExample = new CourseItemDetailExample();
        courseItemDetailExample.createCriteria()
                .andIdIn(courseItemDetailIds);
        CourseItemDetail courseItemDetail = new CourseItemDetail();
        courseItemDetail.setDeleted(GradeConstant.RECORD_INVALID);
        int update = courseItemDetailMapper.updateByExampleSelective(courseItemDetail, courseItemDetailExample);
        if (update == 0) {
            throw new SQLException("课程评分项删除失败");
        }
        return true;
    }

    @Transactional
    public Boolean deleteCourseItemDetailsByCourseItemId(Long courseItemId) {
//        int delete_courseItemDetail = courseItemDetailMapper.updateDeletedByPrimaryKey(courseItemDetailId, GradeConstant.RECORD_INVALID);
        //判断课程成绩项中评分项是否存在
        CourseItemDetailExample courseItemDetailExample
                = new CourseItemDetailExample();
        courseItemDetailExample.createCriteria()
                .andCourseItemIdEqualTo(courseItemId);
        CourseItemDetail courseItemDetail = new CourseItemDetail();
        courseItemDetail.setDeleted(GradeConstant.RECORD_INVALID);
        int update = courseItemDetailMapper.updateByExampleSelective(courseItemDetail, courseItemDetailExample);
        if (update == 0) {
            return false;
        }
        return true;
    }

    @Override

    /**
     * @Param: [id]
     * @Return:edu.uni.grademanagement.bean.CourseItemDetail
     * @Author: 陈汉槟
     * @Date: 2019/5/24 13:31
     * @Description: 根据课程评分项id获取课程评分项
     */
    public CourseItem selectCourseItemByCourseItemDetailId(Long id) {
        CourseItemDetail courseItemDetail = courseItemDetailMapper.selectByPrimaryKey(id);
        CourseItem courseItem = courseItemMapper.selectByPrimaryKey(courseItemDetail.getCourseItemId());
        return courseItem;
    }

    @Override
    @Transactional
    public boolean updateCouseItem(List<CourseItem> courseItems) {
        courseItems.stream().forEach(
                courseItem -> {
                    int success = courseItemMapper.updateByPrimaryKeySelective(courseItem);
                    if (success != 1) {
                        try {
                            throw new SQLException("课程成绩项更新失败");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        return true;
    }

    @Override
    public List<CourseItemDetail> selectCourseItemDetailsByCourseItemIdAndTeacherId(Long courseItemId, Long teacherId) {
        CourseItemDetailExample courseItemDetailExample = new CourseItemDetailExample();
        courseItemDetailExample.createCriteria()
                .andCourseItemIdEqualTo(courseItemId)
                .andByWhoEqualTo(teacherId)
                .andDeletedEqualTo(GradeConstant.RECORD_VALID);
        List<CourseItemDetail> courseItemDetailList =
                courseItemDetailMapper.selectByExample(courseItemDetailExample);

        return courseItemDetailList;
    }

    @Override
    public CourseItemDetail selectCourseItemDetailByCouserItemDetailId(Long courseItemDetailId) {
        CourseItemDetail courseItemDetail =
                courseItemDetailMapper.selectByPrimaryKey(courseItemDetailId);
        return courseItemDetail;
    }

    @Override

    /**
     *
     * @Param [courseItems]
     * @Return:java.util.List<edu.uni.grademanagement.bean.CourseItem>
     * @Author: 陈汉槟
     * @Date: 2019/5/9 11:54
     * @Description: 根据课程成绩项集合查询完整的集合
     */
    public List<CourseItem> selectCourseItems(List<CourseItem> courseItems) {
        List<CourseItem> courseItemList = courseItems.stream().map(
                courseItem -> {
                    CourseItem courseItem1 = courseItemMapper.selectByCourseItem(courseItem);
                    return courseItem1;

                }
        ).collect(Collectors.toList());
        return courseItemList;
    }

    @Override

    /**
     *
     * @Param [courseItemDetails]
     * @Return:java.util.List<edu.uni.grademanagement.bean.CourseItemDetail>
     * @Author: 陈汉槟
     * @Date: 2019/5/9 21:24
     * @description: 根据课程成绩评分项集合查询完整的集合
     */
    public List<CourseItemDetail> selectCourseItemDetails(List<CourseItemDetail> courseItemDetails) {
        List<CourseItemDetail> courseItemDetailList = courseItemDetails.stream().map(
                courseItemDetail -> {
                    CourseItemDetail courseItemDetail1 = courseItemDetailMapper.selectByCourseItemDetail(courseItemDetail);
                    System.out.println(courseItemDetail.toString());
                    return courseItemDetail1;
                }
        ).collect(Collectors.toList());
//        courseItemDetailMapper.selectByExample();
        return courseItemDetailList;
    }

    @Override

    /**
     *
     * @Param [courseItemId]
     * @Return:java.util.List<edu.uni.grademanagement.bean.CourseItemDetail>
     * @Author: 陈汉槟
     * @Date: 2019/5/10 13:10
     * @Description: 根据课程成绩项id获取课程成绩评分项
     */
    public List<CourseItemDetail> selectCourseItemDetailsByCourseItemId(Long courseItemId) {
        CourseItemDetailExample courseItemDetailExample = new CourseItemDetailExample();
        courseItemDetailExample.createCriteria()
                .andCourseItemIdEqualTo(courseItemId)
                .andDeletedEqualTo(GradeConstant.RECORD_VALID);
        List<CourseItemDetail> courseItemDetailList = courseItemDetailMapper.selectByExample(courseItemDetailExample);
        return courseItemDetailList ;
    }

    @Override

    /**
     *
     * @Param [universityId, byWho, courseId]
     * @Return:java.util.List<edu.uni.grademanagement.bean.CourseItem>
     * @Author: 陈汉槟
     * @Date: 2019/5/10 13:56
     * @Description: 根据学校id学期id和课程id获取这学期的课程成绩项
     * @Modify: 2019/6/12 17:39
     */
    public List<CourseItem> selectCouseItems(
            Long universityId, Long semesterId, Long courseId) {
        CourseItemExample courseItemExample = new CourseItemExample();
        courseItemExample.createCriteria()
                .andUniversityIdEqualTo(universityId)
                .andSemesterIdEqualTo(semesterId)
                .andCourseIdEqualTo(courseId)
                .andDeletedEqualTo(GradeConstant.RECORD_VALID);
        List<CourseItem> courseItemList = courseItemMapper.selectByExample(courseItemExample);
        return courseItemList;

    }


    /**
     *
     * @Param [courseItem]
     * @Return:
     * @Author: 陈汉槟
     * @Date: 2019/5/6 13:53
     * @Description: 根据courseItem获取某门课的所有有效评分项目
     */
    public CourseItemDto getCourseItemDto(CourseItem courseItem) {
        CourseItemDto courseItemDto = new CourseItemDto();
        BeanUtils.copyProperties(courseItem, courseItemDto);
        CourseItemDetailExample courseItemDetailExample = new CourseItemDetailExample();
        courseItemDetailExample.createCriteria()
                .andUniversityIdEqualTo(courseItem.getUniversityId())
                .andCourseItemIdEqualTo(courseItem.getId())
                .andByWhoEqualTo(courseItem.getByWho())
                .andDeletedEqualTo(GradeConstant.RECORD_VALID);
        List<CourseItemDetail> courseItemDetails =
                courseItemDetailMapper.selectByExample(courseItemDetailExample);
        courseItemDto.setCourseItemDetails(courseItemDetails);
        return courseItemDto;
    }
}
