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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
                .andByWhoEqualTo(teacherId)
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
    public Boolean insertCourseItemDetails(List<CourseItemDetail> courseItemDetails) {
        int insert = courseItemDetailMapper.insertCourseItemDetailLists(courseItemDetails);
        if (insert == 0) {
            return false;
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
                    return false;
                }
            }
            List<CourseItemDetail> courseItemDetails = courseItemDto.getCourseItemDetails();
            if (CollectionUtils.isEmpty(courseItemDetails)) {
                return true;
            }
            for (CourseItemDetail courseItemDetail :
                    courseItemDetails) {
                int update = courseItemDetailMapper.updateByPrimaryKeySelective(courseItemDetail);
                if (update == 0) {
                    // todo 报错
                    return false;
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
    public Boolean updateCouseItemDetail(CourseItemDetail courseItemDetail) {
        int update = courseItemDetailMapper.updateByPrimaryKeySelective(courseItemDetail);
        if (update == 0) {
            return false;
        }
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
    public Boolean deleteCourseItem(Long courseItemId) {
        /*CourseItem courseItem = new CourseItem();
        courseItem.setId(courseItemId);
        courseItem.setDeleted(GradeConstant.RECORD_INVALID);
        int delete_courseItem = courseItemMapper.updateByPrimaryKeySelective(courseItem);
        if (delete_courseItem == 0) {
            log.info("【删除课程成绩项】 更新deleted 失败，courseItemId = {}",
                    courseItemId);
            return false;
        }
        CourseItemDetailExample courseItemDetailExample = new CourseItemDetailExample();
        courseItemDetailExample.createCriteria()
                .andCourseItemIdEqualTo(courseItemId)
                .andDeletedEqualTo(GradeConstant.RECORD_VALID);
        List<CourseItemDetail> courseItemDetails = courseItemDetailMapper.selectByExample(courseItemDetailExample);
        for (CourseItemDetail courseItemDetail:
             courseItemDetails) {
            courseItemDetail.setDeleted(GradeConstant.RECORD_INVALID);
            int delete_courseItemDetail = courseItemDetailMapper.updateByPrimaryKey(courseItemDetail);
            if (delete_courseItem == 0) {
                log.info("【删除课程成绩作业项】 更新deleted 失败，courseItemDetailId = {}",
                        courseItemDetail.getId());
                return false;
            }
        }
        return true;*/
        CourseItem courseItem = courseItemMapper.selectByPrimaryKey(courseItemId);
        courseItem.setDeleted(GradeConstant.RECORD_INVALID);
        int update = courseItemMapper.updateByPrimaryKey(courseItem);
        if (update == 0) {
            return false;
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
    public Boolean deleteCourseItemDetail(Long courseItemDetailId) {
//        int delete_courseItemDetail = courseItemDetailMapper.updateDeletedByPrimaryKey(courseItemDetailId, GradeConstant.RECORD_INVALID);
        CourseItemDetail courseItemDetail = courseItemDetailMapper.selectByPrimaryKey(courseItemDetailId);
        courseItemDetail.setDeleted(GradeConstant.RECORD_INVALID);
        int update = courseItemDetailMapper.updateByPrimaryKey(courseItemDetail);
        if (update == 0) {
            return false;
        }
        return true;
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
     * @Description: 根据学校id教师id和课程获取这个教师教的课的课程成绩项
     */
    public List<CourseItem> selectCouseItems(Long universityId, Long byWho, Long courseId) {
        CourseItemExample courseItemExample = new CourseItemExample();
        courseItemExample.createCriteria()
                .andUniversityIdEqualTo(universityId)
                .andByWhoEqualTo(byWho)
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
