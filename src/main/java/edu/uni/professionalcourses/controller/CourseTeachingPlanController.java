package edu.uni.professionalcourses.controller;

import com.github.pagehelper.PageInfo;
import edu.uni.bean.Result;
import edu.uni.bean.ResultType;
import edu.uni.professionalcourses.bean.CourseTeachingPlan;
import edu.uni.professionalcourses.service.CourseTeachingPlanService;
import edu.uni.utils.RedisCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * author : 黄永佳
 * create : 2019/5/2 0002 12:58
 * modified :
 * 功能 :CourseTeachingPlan表的控制跳转
 **/
@Api(description = "专业课程模块")
@Controller
@RequestMapping("json/professionalCourses")
public class CourseTeachingPlanController {
    @Autowired
    private CourseTeachingPlanService courseTeachingPlanService;
    @Autowired
    private RedisCache cache;

    //    缓存内部类
    static class CacheNameHelper {
        // pc_c_courseteachingplan_{主id}
        private static final String Receive_CacheNamePrefix = "pc_courseteachingplan_";
        // pc_c_courseteachingplan_list_{页码}
        private static final String List_CacheNamePrefix = "pc_courseteachingplan_list_";
        // pc_c_courseteachingplan_listByUniversityid_{学校id}_{页码}
        private static final String ListByUniversityId_CacheNamePrefix = "pc_courseteachingplan_listByUniversityId_";
        // pc_c_courseteachingplan_listByCourseteachingscheduleId_{课程教学进度表id}_{页码}
        private static final String ListByCourseteachingscheduleId_CacheNamePrefix = "pc_courseteachingplan_listByCourseteachingscheduleId_";
        // pc_c_courseteachingplan_listByCourseid_{课程id}_{页码}
        private static final String ListByCourseId_CacheNamePrefix = "pc_courseteachingplan_listByCourseId_";
        // pc_c_courseteachingplan_listByCourseteachingscheduleId_{课程实验进度表id}_{页码}
        private static final String ListByCourseexperimentscheduleId_CacheNamePrefix = "pc_courseteachingplan_listByCourseexperimentscheduleId_";
    }

    /**
     * 新增课程教学计划
     *
     * @param courseTeachingPlan
     * @return
     */
    @ApiOperation(value = "新增课程教学计划", notes = "未测试")
    @ApiImplicitParam(name = "courseTeachingPlan", value = "课程教学计划实体类", required = true, dataType = "CourseTeachingPlan")
    @PostMapping("courseTeachingPlan")
    @ResponseBody
    public Result create(@RequestBody(required = false) CourseTeachingPlan courseTeachingPlan) {
        if (courseTeachingPlan != null) {
            boolean success = courseTeachingPlanService.insert(courseTeachingPlan);
            if (success) {
                cache.deleteByPaterm(CacheNameHelper.List_CacheNamePrefix + "*");
//                cache.deleteByPaterm(CacheNameHelper.ListAll_CacheName );
                cache.deleteByPaterm(CacheNameHelper.ListByUniversityId_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.Receive_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListByCourseteachingscheduleId_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListByCourseId_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListByCourseexperimentscheduleId_CacheNamePrefix + "*");

                return Result.build(ResultType.Success);
            } else {
                return Result.build(ResultType.Failed);
            }
        }
        return Result.build(ResultType.ParamError);
    }

    /**
     * 删除课程教学计划
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据表主键id删除课程教学计划", notes = "未测试")
    @ApiImplicitParam(name = "id", value = "表主键id", required = true, dataType = "bigint", paramType = "path")
    @DeleteMapping("courseTeachingPlan/{id}")
    @ResponseBody
    public Result destroy(@PathVariable long id) {
        boolean success = courseTeachingPlanService.delete(id);
        if (success) {
            cache.deleteByPaterm(CacheNameHelper.List_CacheNamePrefix + "*");
//                cache.deleteByPaterm(CacheNameHelper.ListAll_CacheName );
            cache.deleteByPaterm(CacheNameHelper.ListByUniversityId_CacheNamePrefix + "*");
            cache.deleteByPaterm(CacheNameHelper.Receive_CacheNamePrefix + "*");
            cache.deleteByPaterm(CacheNameHelper.ListByCourseteachingscheduleId_CacheNamePrefix + "*");
            cache.deleteByPaterm(CacheNameHelper.ListByCourseId_CacheNamePrefix + "*");
            cache.deleteByPaterm(CacheNameHelper.ListByCourseexperimentscheduleId_CacheNamePrefix + "*");
            return Result.build(ResultType.Success);
        } else {
            return Result.build(ResultType.Failed);
        }
    }

    /**
     * 根据主键id获取专业学习计划
     *
     * @param id
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "根据主键id获取课程教学计划", notes = "未测试")
    @ApiImplicitParam(name = "id", value = "主键id", required = true, dataType = "bigint", paramType = "path")
    @GetMapping("courseTeachingPlan/{id}")
    public void receive(@PathVariable long id,
                        HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.Receive_CacheNamePrefix + id;
        String json = cache.get(cacheName);
        if (json == null) {
            CourseTeachingPlan courseTeachingPlan = courseTeachingPlanService.select(id);
            json = Result.build(ResultType.Success).appendData("courseTeachingPlan", courseTeachingPlan).convertIntoJSON();
            if (courseTeachingPlan != null) {
                cache.set(cacheName, json);
            }
        }
        response.getWriter().write(json);
    }

    /**
     * 修改课程教学计划
     *
     * @param courseTeachingPlan
     * @return
     */
    @ApiOperation(value = "根据表主键id修改课程教学计划", notes = "未测试")
    @ApiImplicitParam(name = "courseTeachingPlan", value = "课程教学计划实体类", required = true, dataType = "CourseTeachingPlan")
    @PutMapping("courseTeachingPlan")
    @ResponseBody
    public Result update(@RequestBody(required = false) CourseTeachingPlan courseTeachingPlan) {
        if (courseTeachingPlan != null && courseTeachingPlan.getId() != null) {
            boolean success = courseTeachingPlanService.update(courseTeachingPlan);
            if (success) {
                cache.deleteByPaterm(CacheNameHelper.List_CacheNamePrefix + "*");
//                cache.deleteByPaterm(CacheNameHelper.ListAll_CacheName );
                cache.deleteByPaterm(CacheNameHelper.ListByUniversityId_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.Receive_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListByCourseteachingscheduleId_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListByCourseId_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListByCourseexperimentscheduleId_CacheNamePrefix + "*");

                return Result.build(ResultType.Success);
            } else {
                return Result.build(ResultType.Failed);
            }
        }
        return Result.build(ResultType.ParamError);
    }

    /**
     * 根据页码分页查询所有课程教学计划
     *
     * @param pageNum
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "根据页码分页查询所有课程教学计划", notes = "未测试")
    @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "Integer", paramType = "path")
    @GetMapping(value = "/courseTeachingPlan/list/{pageNum}")
    public void list(
            @PathVariable Integer pageNum,
            HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.List_CacheNamePrefix + pageNum;
        String json = cache.get(cacheName);
        if (json == null) {
            PageInfo<CourseTeachingPlan> pageInfo = courseTeachingPlanService.selectPage(pageNum);
            json = Result.build(ResultType.Success).appendData("pageInfo", pageInfo).convertIntoJSON();
            if (pageInfo != null) {
                cache.set(cacheName, json);
            }
        }
        response.getWriter().write(json);
    }

    /**
     * 根据学校id和页码来分页查询课程教学计划
     *
     * @param university_id
     * @param pageNum
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "根据学校id和页码来分页查询课程教学计划", notes = "未测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "university_id", value = "学校id", required = true, dataType = "bigint", paramType = "path"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "Integer", paramType = "path")
    })
    @GetMapping(value = "/courseTeachingPlan/listByUniversityId/{university_id}/{pageNum}")
    public void listByUniversityID(@PathVariable long university_id,
                                   @PathVariable Integer pageNum,
                                   HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.ListByUniversityId_CacheNamePrefix + university_id + "_" + pageNum;
        String json = cache.get(cacheName);
        if (json == null) {
            PageInfo<CourseTeachingPlan> pageInfo = courseTeachingPlanService.selectPageByUniversity(pageNum, university_id);
            json = Result.build(ResultType.Success).appendData("pageInfo", pageInfo).convertIntoJSON();
            if (pageInfo != null) {
                cache.set(cacheName, json);
            }
        }
        response.getWriter().write(json);
    }

    /**
     * 根据课程id和页码来分页查询课程教学计划
     *
     * @param course_Id
     * @param pageNum
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "根据课程id和页码来分页查询课程教学计划", notes = "未测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "course_Id", value = "课程id", required = true, dataType = "bigint", paramType = "path"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "Integer", paramType = "path")
    })
    @GetMapping(value = "/courseTeachingPlan/listByCourseId/{course_Id}/{pageNum}")
    public void listByCourseID(@PathVariable long course_Id,
                               @PathVariable Integer pageNum,
                               HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.ListByCourseId_CacheNamePrefix + course_Id + "_" + pageNum;
        String json = cache.get(cacheName);
        if (json == null) {
            PageInfo<CourseTeachingPlan> pageInfo = courseTeachingPlanService.selectPageByCourse(pageNum, course_Id);
            json = Result.build(ResultType.Success).appendData("pageInfo", pageInfo).convertIntoJSON();
            if (pageInfo != null) {
                cache.set(cacheName, json);
            }
        }
        response.getWriter().write(json);
    }

    /**
     * 根据课程教学进度表id和页码来分页查询课程教学计划
     *
     * @param course_teaching_schedule_Id
     * @param pageNum
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "根据课程教学进度表id和页码来分页查询课程教学计划", notes = "未测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "course_teaching_schedule_Id", value = "课程教学进度表id", required = true, dataType = "bigint", paramType = "path"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "Integer", paramType = "path")
    })
    @GetMapping(value = "/courseTeachingPlan/listByCourseteachingscheduleId/{course_teaching_schedule_Id}/{pageNum}")
    public void listByCourseteachingscheduleID(@PathVariable long course_teaching_schedule_Id,
                                               @PathVariable Integer pageNum,
                                               HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.ListByCourseteachingscheduleId_CacheNamePrefix + course_teaching_schedule_Id + "_" + pageNum;
        String json = cache.get(cacheName);
        if (json == null) {
            PageInfo<CourseTeachingPlan> pageInfo = courseTeachingPlanService.selectPageByCourseteachingschedule(pageNum, course_teaching_schedule_Id);
            json = Result.build(ResultType.Success).appendData("pageInfo", pageInfo).convertIntoJSON();
            if (pageInfo != null) {
                cache.set(cacheName, json);
            }
        }
        response.getWriter().write(json);
    }
}
//    /**
//     * 根据课程教学进度表id和页码来分页查询课程教学计划
//     * @param course_experiment_schedule_Id
//     * @param pageNum
//     * @param response
//     * @throws IOException
//     */
//    @ApiOperation(value = "根据课程实验进度表id和页码来分页查询课程教学计划", notes = "未测试")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name="course_experiment_schedule_Id", value = "课程实验进度表id", required = true, dataType = "bigint", paramType = "path"),
//            @ApiImplicitParam(name="pageNum", value = "页码", required = true, dataType = "Integer", paramType = "path")
//    })
//    @GetMapping(value = "/courseTeachingPlan/listByCourseexperimentscheduleId/{course_experiment_schedule_Id}/{pageNum}")
//    public void listByCourseexperimentscheduleId(@PathVariable long course_experiment_schedule_Id,
//                                               @PathVariable Integer pageNum,
//                                               HttpServletResponse response) throws IOException {
//        response.setContentType("application/json;charset=utf-8");
//        String cacheName = CacheNameHelper.ListByCourseexperimentscheduleId_CacheNamePrefix + course_experiment_schedule_Id+ "_" + pageNum;
//        String json = cache.get(cacheName);
//        if(json == null){
//            PageInfo<CourseTeachingPlan> pageInfo = courseTeachingPlanService.selectPageByCourseexperimentschedule(pageNum, course_experiment_schedule_Id);
//            json = Result.build(ResultType.Success).appendData("pageInfo", pageInfo).convertIntoJSON();
//            if(pageInfo != null) {
//                cache.set(cacheName, json);
//            }
//        }
//        response.getWriter().write(json);
//    }
//
//
//}
