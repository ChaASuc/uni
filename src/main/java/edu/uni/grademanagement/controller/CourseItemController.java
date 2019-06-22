package edu.uni.grademanagement.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.uni.administrativestructure.service.UniversityService;
import edu.uni.bean.Result;
import edu.uni.bean.ResultType;
import edu.uni.educateAffair.service.SemesterService;
import edu.uni.example.bean.Product;
import edu.uni.example.controller.ProductController;
import edu.uni.grademanagement.bean.*;
import edu.uni.grademanagement.dto.CourseItemDto;
import edu.uni.grademanagement.service.*;
import edu.uni.professionalcourses.bean.Course;
import edu.uni.utils.JsonUtils;
import edu.uni.utils.RedisCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
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
@Api(value = "CourseItemController",description = "课程成绩项目控制层")
@Controller
@RequestMapping("/json/gradeManagement/courseItemDto")
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
    private CurriculumForGradeService curriculumForGradeService;

    @Autowired
    private CourseForGradeService courseForGradeService;

    @Autowired
    private UniversityService universityService;

    @Autowired
    private EducateAffairService educateAffairService;


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
    @ApiOperation(value = "获取课程项", notes = "任课教师角色")
    @GetMapping("/list/{universityId}/{semesterId}/{courseId}")
    public void getCourseItemDtos(
            @ApiParam(value = "学校id", required = true)
            @PathVariable(name = "universityId", required = true) Long universityId,
            @ApiParam(value = "学期id", required = true)
            @PathVariable(name = "semesterId", required = true) Long semesterId,
            @ApiParam(value = "课程id", required = true)
            @PathVariable(name = "courseId", required = true) Long courseId,
            HttpServletResponse response
    ) throws IOException {
//        // 1 获取key
//        String cacheName = String.format(CacheNameHelper.List_CacheNamePrefix,
//                universityId, teacherId, courseId);
        // 2 获取缓存
//        String courseItemDtoJson = redisCache.get(cacheName);
        String json = null;
        // 3 判断是否存在缓存
        if (json == null) {
            // 3.1 获取数据
            List<CourseItem> courseItems = courseItemService.selectCouseItems(
                    universityId, semesterId, courseId
            );

            json = Result.build(ResultType.Success).appendData("courseItems", courseItems).convertIntoJSON();
            // 3.2 缓存
//            redisCache.set(cacheName, courseItemDtoJson);
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }


    /**
     *
     * @Param [courseItemId, response]
     * @Return:void
     * @Author: 陈汉槟
     * @Date: 2019/5/8 1:04
     * @Description: 根据课程成绩项目courseItemId获取
     */
    @ApiOperation(value = "根据课程成绩项目courseItemId获取", notes = "任课老师")
    @GetMapping("/{courseItemId}/{teacherId}")
    public void getCourseItemDto(
            @ApiParam(value = "courseItemId", required = true)
            @PathVariable(name = "courseItemId", required = true) Long courseItemId,
            @ApiParam(value = "teacherId", required = true)
            @PathVariable(name = "teacherId", required = true) Long teacherId,
            HttpServletResponse response
    ) throws IOException {
        // 1 获取key
//        String cacheName = String.format(CacheNameHelper.CacheNamePrefix,
//                courseItemId);
        // 2 获取缓存
//        String courseItemDtoJson = redisCache.get(cacheName);
        String json = null;
        // 3 判断是否存在缓存
//        if (courseItemDtoJson == null) {
            // 3.1 获取数据
            List<CourseItemDetail> courseItemDetails = courseItemService.selectCourseItemDetailsByCourseItemIdAndTeacherId(
                    courseItemId, teacherId);
            json = Result.build(ResultType.Success).appendData("courseItemDetails", courseItemDetails).convertIntoJSON();
            // 3.2 缓存
//            redisCache.set(cacheName, courseItemDtoJson);
//        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }



    /**
     *
     * @Param [courseItems, response]
     * @Return:edu.uni.bean.Result
     * @Author: 陈汉槟
     * @Date: 2019/5/8 1:05
     * @Description: 根据课程成绩项目courseItem创建
     */
    @ApiOperation(value = "创建课程成绩项目，必须是具体学期的某门课的", notes = "课程组老师")
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
                    courseItems.get(0).getSemesterId(),
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
            double d = 1 - oleRate - newRate;
            if (d < -0.01) {
                log.info("【课程成绩项】比率和不为1");
                return Result.build(ResultType.Failed);
            }
            boolean success = courseItemService.insertCourseItem(courseItems);
            if (success) {
//                redisCache.deleteByPaterm(CourseItemController.CacheNameHelper.List_All_CacheNamePrefix);
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
     * @Modify: 修复创建ourseItemDetail集合时courseItemId必须一个的错误
     */
    @ApiOperation(value = "根据课程成绩评分项目courseItemDetail创建", notes = "任课老师")
    @PostMapping("/courseItemDetail")
    @ResponseBody
    public Result createCourseItemDetail(
            @ApiParam(value = "courseItemDetails", required = true)
            @RequestBody List<CourseItemDto> courseItemDtos,
            HttpServletResponse response
    ) throws IOException {

        List<CourseItemDetail> courseItemDetailArrayList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(courseItemDtos)) {
            // 遍历运行
            for (CourseItemDto courseItemDto:
                 courseItemDtos) {
                Long courseItemId = courseItemDto.getId();
                Long teacherId = courseItemDto.getByWho();
                Long universityId = courseItemDto.getUniversityId();
                // 要插入的课程评分项
                List<CourseItemDetail> courseItemDetails =
                        courseItemDto.getCourseItemDetails();
                courseItemDetails.stream().forEach(
                        courseItemDetail -> {
                            courseItemDetail.setCourseItemId(courseItemId);
                            courseItemDetail.setByWho(teacherId);
                            courseItemDetail.setUniversityId(universityId);
                        }
                );

                // 获取课程成绩项的次数
                CourseItem courseItem = courseItemService.selectCouseItem(courseItemId);
                // 根据课程成绩项的id获取原有的课程评分项
                List<CourseItemDetail> courseItemDetailList =
                        courseItemService.selectCourseItemDetailsByCourseItemId(courseItemId);
                // 判断是否数量超标
                if (courseItem.getCount() - courseItemDetailList.size() - courseItemDetails.size() < 0) {
                    log.info(
                            "【创建课程成绩评分项】失败，数量超标,courseItemId={}," +
                                    " courseItemDetailSize={}, insertCourseItemDetailSize+{}",
                            courseItemId, courseItemDetailList.size(), courseItemDetails.size());
                    return Result.build(ResultType.Failed);
                }

                courseItemDetailArrayList.addAll(courseItemDetails);

            }
            // 插入
            boolean success = false;
            try {
                success = courseItemService.insertCourseItemDetails(courseItemDetailArrayList);
            } catch (SQLException e) {
                return Result.build(ResultType.Failed);
            }

//            redisCache.deleteByPaterm(CourseItemController.CacheNameHelper.List_All_CacheNamePrefix);
            return Result.build(ResultType.Success);
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
    @ApiOperation(value = "根据CourseItemId批量删除", notes = "课程组老师")
    @DeleteMapping("/courseItem")
    @ResponseBody
    public Result deletedCourseItem(
            @ApiParam(value = "courseItemIds", required = true)
            @RequestParam List<Long> courseItemIds
            ) throws SQLException {
        if (!CollectionUtils.isEmpty(courseItemIds)) {

            Boolean success = courseItemService.deleteCourseItem(courseItemIds);
            if (success) {
//            redisCache.deleteByPaterm(CourseItemController.CacheNameHelper.List_All_CacheNamePrefix);
            return Result.build(ResultType.Success);

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
    @ApiOperation(value = "根据CourseItemDetailId批量删除", notes = "课程组老师")
    @DeleteMapping("/courseItemDetail")
    @ResponseBody
    public Result deletedCourseItemDetail(
            @ApiParam(value = "courseItemDetailIds", required = true)
            @RequestParam List<Long> courseItemDetailIds
    ) throws SQLException {
        if (!CollectionUtils.isEmpty(courseItemDetailIds)) {

            Boolean success = courseItemService.deleteCourseItemDetail(courseItemDetailIds);
            if (success) {
//            redisCache.deleteByPaterm(CourseItemController.CacheNameHelper.List_All_CacheNamePrefix);
                return Result.build(ResultType.Success);

            }
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
    @ApiOperation(value="课程成绩项修改", notes = "课程组老师")
    @PutMapping("/courseItem")
    @ResponseBody
    public Result updateCourseItem(
            @ApiParam(value = "courseItem", required = true)
            @RequestBody(required = true) List<CourseItem> courseItems
    ){
        if(!CollectionUtils.isEmpty(courseItems)){
            double oldRates = 0;  // 判断是否超值

            CourseItem courseItem1 = courseItems.get(0);
            Long universityId = courseItem1.getUniversityId();
            Long teacherId = courseItem1.getByWho();
            Long courseId = courseItem1.getCourseId();
            Long semesterId = courseItem1.getSemesterId();

            List<CourseItem> courseItemList =
                    courseItemService.selectCouseItems(universityId, semesterId, courseId);


            for (CourseItem courseItem :
                    courseItemList) {
                // 原先的课程成绩项比率
                oldRates += courseItem.getRate();
            }

            for (CourseItem item :
                    courseItems) {
                // 替换原有的比率
                if (item.getRate() != null) {
                    CourseItem courseItem = courseItemService.selectCouseItem(item.getId());
                    oldRates -= courseItem.getRate();
                    oldRates += item.getRate();
                }

            }
            if (1 - oldRates < 0) {
                throw new RuntimeException("课程成绩项目比率大于1");
            }

            boolean success = courseItemService.updateCouseItem(courseItems);
            if(success){
                /*courseItemDtos.stream().forEach(
                        dto -> {
                            if (dto.getId() != null) {
                                // redisCache.delete(String.format(CacheNameHelper.CacheNamePrefix, dto.getId()));
                            }
                        }
                );*/
                // redisCache.delete(String.format(CacheNameHelper.List_CacheNamePrefix ,
//                        universityId,
//                        teacherId,
//                        courseId));
                return Result.build(ResultType.Success);
            }else{
                return Result.build(ResultType.Failed);
            }
        }
        return Result.build(ResultType.ParamError);
    }

    @ApiOperation(value="课程评分项目修改", notes = "任课老师")
    @PutMapping("/courseItemDetail")
    @ResponseBody
    public Result updateCourseItemDetail(
            @ApiParam(value = "courseItemDetails", required = true)
            @RequestBody(required = true) List<CourseItemDetail> courseItemDetails
    ){
        if(!CollectionUtils.isEmpty(courseItemDetails)){

            Boolean success = courseItemService.updateCouseItemDetail(courseItemDetails);
            if(success){
//                courseItemDtos.stream().forEach(
//                        dto -> {
//                            if (dto.getId() != null) {
//                                // redisCache.delete(String.format(CacheNameHelper.CacheNamePrefix, dto.getId()));
//                            }
//                        }
//                );
                // redisCache.delete(String.format(CacheNameHelper.List_CacheNamePrefix ,
//                        universityId,
//                        teacherId,
//                        courseId));
                return Result.build(ResultType.Success);
            }else{
                return Result.build(ResultType.Failed);
            }
        }
        return Result.build(ResultType.ParamError);
    }



    @ApiOperation(value = "获取某学期课表的课程，semesterId无代表当前学期", notes = "课程组老师,已测试")
    @GetMapping("/tec/courses")
    public void getCourses(
            @RequestParam(required = false) Long semesterId,
            HttpServletResponse response
    ) throws IOException {

        if (semesterId == null) {
            semesterId = 40L;
        }

        List<Long> courseIds =
                educateAffairService.selectCourseByTeachingTaskBySemesterId(semesterId);

        List<Course> courses =
                courseForGradeService.selectCoursesByCourseIds(courseIds);

        String json = Result.build(ResultType.Success).appendData("courses", courses).convertIntoJSON();
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }


}
