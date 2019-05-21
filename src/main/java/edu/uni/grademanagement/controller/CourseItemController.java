package edu.uni.grademanagement.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.uni.bean.Result;
import edu.uni.bean.ResultType;
import edu.uni.example.bean.Product;
import edu.uni.example.controller.ProductController;
import edu.uni.grademanagement.bean.*;
import edu.uni.grademanagement.dto.CourseItemDto;
import edu.uni.grademanagement.dto.StuGradeItemDetailDto;
import edu.uni.grademanagement.dto.StuGradeItemDto;
import edu.uni.grademanagement.form.CreateCousrseItemDetailForm;
import edu.uni.grademanagement.form.CreateCousrseItemForm;
import edu.uni.grademanagement.form.DeletedCourseItemDtoForm;
import edu.uni.grademanagement.mapper.CourseItemDetailMapper;
import edu.uni.grademanagement.mapper.CourseItemMapper;
import edu.uni.grademanagement.service.CourseItemService;
import edu.uni.grademanagement.service.StuGradeItemDetailService;
import edu.uni.grademanagement.service.StuGradeItemService;
import edu.uni.grademanagement.service.StuGradeMainService;
import edu.uni.utils.JsonUtils;
import edu.uni.utils.RedisCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

/**
 * @Author 陈汉槟
 * @Create 2019/5/8
 * @Description 课程成绩项目控制层(所有角色)
 * @Since 1.0.0
 */
@Api(value = "CourseItemController", tags = {"课程成绩项目控制层"})
@Controller
@RequestMapping("/json/gradeManagement")
@Slf4j
public class CourseItemController {

    @Autowired
    private CourseItemService courseItemService;

    @Autowired
    private StuGradeItemService stuGradeItemService;

    @Autowired
    private StuGradeItemDetailService stuGradeItemDetailService;

    @Autowired
    private StuGradeMainService stuGradeMainService;


    @Autowired
    private RedisCache redisCache;

    static class CacheNameHelper {
        // 根据课程成绩项courserItemId获取课程成绩项及明细
        // gm_courseItem_{courseItemId}
        public static final String CacheNamePrefix = "gm_courseItem_%s";
        // 根据课程成绩项所有课程成绩项及明细
        // gm_courseItem_{courseItemId}
        public static final String All_CacheNamePrefix = "gm_courseItem_*";
        // 根据学校id，教师id和课程id获取所有课程成绩项及明细
        // gm_courseItem_{universityId}_{teacherId}_{courseId}
        public static final String List_CacheNamePrefix = "gm_courseItem_list_%s_%s_%s";
        // 根据获取所有课程成绩项及明细
        // gm_courseItem_*
        public static final String List_All_CacheNamePrefix = "gm_courseItem_*";
    }

    /**
     *
     * @Param [universityId, teacherId, courseId, response]
     * @Return:void
     * @Author: 陈汉槟
     * @Date: 2019/5/8 1:03
     * @Description: 根据学校id，教师id，课程id获取课程成绩项
     */
    @ApiOperation(value = "获取课程成绩项", tags = {"获取有效信息", "教师角色", "可用"}, notes = "可有可无参数")
    @GetMapping("/courseItemDto/list/{universityId}/{teacherId}/{courseId}")
    public void getCourseItemDtos(
            @ApiParam(value = "学校id", required = true)
                    @PathVariable(name = "universityId", required = true) long universityId,
            @ApiParam(value = "教师id", required = true)
                    @PathVariable(name = "teacherId", required = true) Long teacherId,
            @ApiParam(value = "课程id", required = true)
                    @PathVariable(name = "courseId", required = true) Long courseId,
            HttpServletResponse response
    ) throws IOException {
//        // 1 获取key
//        String cacheName = String.format(CacheNameHelper.List_CacheNamePrefix,
//                universityId, teacherId, courseId);
        // 2 获取缓存
//        String courseItemDtoJson = redisCache.get(cacheName);
        String courseItemDtoJson = null;
        // 3 判断是否存在缓存
        if (courseItemDtoJson == null) {
            // 3.1 获取数据
            List<CourseItemDto> courseItemDtos = courseItemService.selectCouseItemDtos(
                    universityId, teacherId, courseId
            );
            courseItemDtoJson = JsonUtils.obj2String(courseItemDtos);
            // 3.2 缓存
//            redisCache.set(cacheName, courseItemDtoJson);
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(courseItemDtoJson);
    }


    /**
     *
     * @Param [courseItemId, response]
     * @Return:void
     * @Author: 陈汉槟
     * @Date: 2019/5/8 1:04
     * @Description: 根据课程成绩项目courseItemId获取
     */
    @ApiOperation(value = "根据课程成绩项目courseItemId获取", tags = {"获取有效信息", "教师角色", "可用"})
    @GetMapping("/courseItemDtos/{courseItemId}")
    public void getCourseItemDto(
            @ApiParam(value = "课程成绩项courseItemId", required = true)
                @PathVariable(name = "courseItemId", required = true) long courseItemId,
            HttpServletResponse response
    ) throws IOException {
        // 1 获取key
        String cacheName = String.format(CacheNameHelper.CacheNamePrefix,
                courseItemId);
        // 2 获取缓存
        String courseItemDtoJson = redisCache.get(cacheName);
        // 3 判断是否存在缓存
        if (courseItemDtoJson == null) {
            // 3.1 获取数据
            CourseItemDto courseItemDto = courseItemService.selectCouseItemDto(courseItemId);
            courseItemDtoJson = JsonUtils.obj2String(courseItemDto);
            // 3.2 缓存
            redisCache.set(cacheName, courseItemDtoJson);
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(courseItemDtoJson);
    }



    /**
     *
     * @Param [courseItems, response]
     * @Return:edu.uni.bean.Result
     * @Author: 陈汉槟
     * @Date: 2019/5/8 1:05
     * @Description: 根据课程成绩项目courseItem创建
     */
    @ApiOperation(value = "根据课程成绩项目courseItem创建", tags = {"获取有效信息", "教师角色", "可用"})
    @PostMapping("/courseItem")
    @ResponseBody
    public Result createCourseItem(
            @ApiParam(value = "courseItems", required = true)
            @RequestBody List<CourseItem> courseItems,
            HttpServletResponse response
    ) throws IOException {

        if (!CollectionUtils.isEmpty(courseItems)) {
            double oleRate = 0;
            double newRate = 0;

            List<CourseItem> courseItemList = courseItemService.selectCouseItems(
                    courseItems.get(0).getUniversityId(),
                    courseItems.get(0).getByWho(),
                    courseItems.get(0).getCourseId()
            );

            for (CourseItem courseItem :
                    courseItemList) {
                oleRate += courseItem.getRate();
            }
            for (CourseItem courseItem :
                    courseItems) {

                newRate += courseItem.getRate();
            }
            if (1 - oleRate - newRate < 0) {
                log.info("【课程成绩项】比率和不为1");
                return Result.build(ResultType.Failed);
            }
            boolean success = courseItemService.insertCourseItem(courseItems);
            if (success) {
                redisCache.deleteByPaterm(CourseItemController.CacheNameHelper.List_All_CacheNamePrefix);
                return Result.build(ResultType.Success);
            } else {
                return Result.build(ResultType.Failed);
            }
        }
        return Result.build(ResultType.ParamError);
    }



    /**
     *
     * @Param [courseItemDetails, response]
     * @Return:edu.uni.bean.Result
     * @Author: 陈汉槟
     * @Date: 2019/5/8 1:07
     * @Description: 根据课程成绩评分项目courseItemDetail创建
     */
    @ApiOperation(value = "根据课程成绩评分项目courseItemDetail创建", tags = {"获取有效信息", "教师角色", "可用"})
    @PostMapping("/courseItemDetail")
    @ResponseBody
    public Result createCourseItemDetail(
            @ApiParam(value = "courseItemDetails", required = true)
            @RequestBody List<CourseItemDetail> courseItemDetails,
            HttpServletResponse response
    ) throws IOException {

        if (!CollectionUtils.isEmpty(courseItemDetails)) {
            Long courseItemId = courseItemDetails.get(0).getCourseItemId();
            CourseItem courseItem = courseItemService.selectCouseItem(courseItemId);
            List<CourseItemDetail> courseItemDetailList =
                    courseItemService.selectCourseItemDetailsByCourseItemId(courseItemId);
            if (courseItem.getCount() - courseItemDetailList.size() - courseItemDetails.size() < 0) {
                log.info("【创建课程成绩评分项】失败，数量超标");
                return Result.build(ResultType.Failed);
            }
            boolean success = courseItemService.insertCourseItemDetails(courseItemDetails);
            if (success) {
                redisCache.deleteByPaterm(CourseItemController.CacheNameHelper.List_All_CacheNamePrefix);
                return Result.build(ResultType.Success);
            }

            else {
                return Result.build(ResultType.Failed);
            }

        }
        return Result.build(ResultType.ParamError);
    }


    /**
     *
     * @Param [courseItemDtos]
     * @Return:edu.uni.bean.Result
     * @Author: 陈汉槟
     * @Date: 2019/5/12 1:07
     * @Description: 根据CourseItemDto批量删除
     */
    @ApiOperation(value = "根据CourseItemDto批量删除")
    @DeleteMapping("/courseItemDto")
    @ResponseBody
    public Result deletedCourseItemDto(
            @ApiParam(value = "courseItemDtos", required = true)
            @RequestBody List<CourseItemDto> courseItemDtos
            ) {
        if (!CollectionUtils.isEmpty(courseItemDtos)) {

            for (CourseItemDto courseItemDto :
                    courseItemDtos) {
                if (courseItemDto.getId() != null) {
                    Boolean bCourseItem
                            = courseItemService.deleteCourseItem(courseItemDto.getId());
                    if (!bCourseItem) {
                        return Result.build(ResultType.Failed);
                    }
                    List<CourseItemDetail> courseItemDetails =
                            courseItemDto.getCourseItemDetails();
                    for (CourseItemDetail courseItemDetail :
                            courseItemDetails) {
                        Boolean bCourseItemDetail =
                                courseItemService.deleteCourseItemDetail(courseItemDetail.getId());
                        if (!bCourseItemDetail) {
                            return Result.build(ResultType.Failed);
                        }
                    }
                }
            }
            redisCache.deleteByPaterm(CourseItemController.CacheNameHelper.List_All_CacheNamePrefix);
            return Result.build(ResultType.Success);
        }
        return Result.build(ResultType.ParamError);
    }



    /**
     *
     * @Param [courseItemDtos]
     * @Return:edu.uni.bean.Result
     * @Author: 陈汉槟
     * @Date: 2019/5/12 1:08
     * @Description: 根据成绩项及明细修改
     */
    @ApiOperation(value="根据成绩项及明细修改")
    @PutMapping("/courseItemDto")
    @ResponseBody
    public Result updateCourseItemDtos(
            @ApiParam(value = "courseItemDtos", required = true)
            @RequestBody(required = true) List<CourseItemDto> courseItemDtos
    ){
        if(!CollectionUtils.isEmpty(courseItemDtos)){
            double oldRates = 0;  // 判断是否超值
            List<CourseItem> courseItemList = new ArrayList<>();
            Long universityId = 0L;
            Long coureseId = 0L;
            Long teacherId = 0L;
            for (CourseItemDto courseItemDto :
                    courseItemDtos) {
                Long courseItemId = courseItemDto.getId();
                if (courseItemId != null) {
                    CourseItem courseItem = courseItemService.selectCouseItem(courseItemId);
                    universityId = courseItem.getUniversityId();
                    coureseId = courseItem.getCourseId();
                    teacherId = courseItem.getByWho();
                    courseItemList = courseItemService.selectCouseItems(
                            universityId,
                            coureseId,
                            teacherId
                    );
                    break;
                }
            }

            for (CourseItem courseItem :
                    courseItemList) {
                oldRates += courseItem.getRate();
            }

            for (CourseItemDto courseItemDto :
                    courseItemDtos) {

                if (courseItemDto.getId() != null) {
                    CourseItem courseItem = courseItemService.selectCouseItem(courseItemDto.getId());
                    oldRates -= courseItem.getRate();
                    oldRates += courseItemDto.getRate();
                }
            }
            if (1 - oldRates < 0) {
                return Result.build(ResultType.Failed);
            }
            boolean success = courseItemService.updateCouseItemDto(courseItemDtos);
            if(success){
                courseItemDtos.stream().forEach(
                        courseItemDto -> {
                            if (courseItemDto.getId() != null) {
                                redisCache.delete(String.format(CacheNameHelper.CacheNamePrefix, courseItemDto.getId()));
                            }
                        }
                );
                redisCache.delete(String.format(CacheNameHelper.List_CacheNamePrefix ,
                        universityId,
                        teacherId,
                        coureseId));
                return Result.build(ResultType.Success);
            }else{
                return Result.build(ResultType.Failed);
            }
        }
        return Result.build(ResultType.ParamError);
    }



}
