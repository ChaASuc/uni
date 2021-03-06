/*
author:  邓凯丰
create:  2019.5.10
modified:  2019.5.11
*/
package edu.uni.professionalcourses.controller;

import com.github.pagehelper.PageInfo;
import edu.uni.professionalcourses.bean.CourseExperimentPlan;
import edu.uni.professionalcourses.bean.CourseExperimentSchedule;
import edu.uni.professionalcourses.service.CourseExperimentPlanService;
import edu.uni.professionalcourses.service.CourseExperimentScheduleService;
import edu.uni.bean.Result;
import edu.uni.bean.ResultType;
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
import java.util.ArrayList;
import java.util.List;

@Api(description = "专业课程模块")
@Controller
@RequestMapping("json/professionalCourses")

public class CourseExperimentScheduleController {
    @Autowired
    private CourseExperimentScheduleService courseExperimentScheduleService;
    @Autowired
    private CourseExperimentPlanService courseExperimentPlanService;

    @Autowired
    private RedisCache cache;

    /**
     * 内部类，专门用来管理每个方法所对应缓存的名称。
     */
    static class CacheNameHelper{
        // as_d_department_{部门id}
        private static final String Receive_CacheNamePrefix = "pc_courseExperimentSchedule_";
        // as_d_department_list_{页码}
        private static final String List_CacheNamePrefix = "pc_courseExperimentSchedule_list_";
        // as_d_departments_listByUniversityID_{学校id}_{页码}
        private static final String ListByUniversityID_CacheNamePrefix = "pc_courseExperimentSchedule_listByUniversityID_";
        //as_d_department_selectAll
        public static final String ListAll_CacheName = "pc_courseExperimentSchedule_selectAll";
    }

    /**
     * 新增实验教学进度
     * @param
     * @return
     */
    @ApiOperation(value="新增实验教学进度", notes = "已测试")
    @ApiImplicitParam(name= "courseExperimentSchedule",value = "实验教学进度实体类", required = true, dataType = "CourseExperimentSchedule")
    @PostMapping("courseExperimentSchedule")
    @ResponseBody
    public Result create(@RequestBody(required = false) CourseExperimentSchedule courseExperimentSchedule){
        if(courseExperimentSchedule != null){
            boolean success = courseExperimentScheduleService.insert(courseExperimentSchedule);
            if(success){
                cache.deleteByPaterm(CacheNameHelper.List_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListAll_CacheName );
                cache.deleteByPaterm(CacheNameHelper.ListByUniversityID_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.Receive_CacheNamePrefix + "*");
                return Result.build(ResultType.Success);
            }else{
                return Result.build(ResultType.Failed);
            }
        }
        return Result.build(ResultType.ParamError);
    }

    /**
     * 删除实验教学进度
     * @param id
     * @return
     */
    @ApiOperation(value = "根据实验教学进度id删除实验教学进度", notes = "已测试")
    @ApiImplicitParam(name = "id", value = "课程id", required = true, dataType = "bigint", paramType = "path")
    @DeleteMapping("courseExperimentSchedule/{id}")
    @ResponseBody
    public Result destroy(@PathVariable long id){
        boolean success = courseExperimentScheduleService.delete(id);
        if(success){
            cache.delete(CacheNameHelper.Receive_CacheNamePrefix + id);
            cache.deleteByPaterm(CacheNameHelper.List_CacheNamePrefix + "*");
            cache.deleteByPaterm(CacheNameHelper.ListByUniversityID_CacheNamePrefix + "*");
            cache.deleteByPaterm(CacheNameHelper.ListAll_CacheName );
            return Result.build(ResultType.Success);
        }else{
            return Result.build(ResultType.Failed);
        }
    }
    /**
     * 修改实验教学进度
     * @param
     * @return
     */
    @ApiOperation(value="根据实验教学进度id修改实验教学进度", notes = "已测试")
    @ApiImplicitParam(name="courseExperimentSchedule", value = "课程实体类", required = true, dataType = "CourseExperimentSchedule")
    @PutMapping("courseExperimentSchedule")
    @ResponseBody
    public Result update(@RequestBody(required = false) CourseExperimentSchedule courseExperimentSchedule){
        if(courseExperimentSchedule != null && courseExperimentSchedule.getId() != null){
            boolean success = courseExperimentScheduleService.update(courseExperimentSchedule);
            if(success){
                cache.delete(CacheNameHelper.Receive_CacheNamePrefix + courseExperimentSchedule.getId());
                cache.deleteByPaterm(CacheNameHelper.List_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListByUniversityID_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListAll_CacheName );
                return Result.build(ResultType.Success);
            }else{
                return Result.build(ResultType.Failed);
            }
        }
        return Result.build(ResultType.ParamError);
    }

    /**
     * 获取实验教学进度
     * @param id
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "根据实验教学进度id获取实验教学进度", notes = "已测试")
    @ApiImplicitParam(name = "id", value = "课程id", required = true, dataType = "bigint", paramType = "path")
    @GetMapping("courseExperimentSchedule/{id}")
    public void receive(@PathVariable long id,
                        HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.Receive_CacheNamePrefix + id;
        String json = cache.get(cacheName);
        if(json == null){
            CourseExperimentSchedule courseExperimentSchedule= courseExperimentScheduleService.select(id);
            json = Result.build(ResultType.Success).appendData("courseExperimentSchedule", courseExperimentSchedule).convertIntoJSON();
            if(courseExperimentSchedule != null){
                cache.set(cacheName, json);
            }
        }
        response.getWriter().write(json);
    }

    /**
     * 根据页码分页查询所有实验教学进度
     * @param pageNum
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "根据页码分页查询所有实验教学进度", notes = "已测试")
    @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "Integer", paramType = "path")
    @GetMapping(value = "/courseExperimentSchedules/list/{pageNum}")
    public void list(
            @PathVariable Integer pageNum ,
            HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.List_CacheNamePrefix + pageNum;
        String json = cache.get(cacheName);
        if(json == null){
            PageInfo<CourseExperimentSchedule> pageInfo = courseExperimentScheduleService.selectPage(pageNum);
            json = Result.build(ResultType.Success).appendData("pageInfo", pageInfo).convertIntoJSON();
            if(pageInfo != null) {
                cache.set(cacheName, json);
            }
        }
        response.getWriter().write(json);
    }

    /**
     * 根据学校id和页码来分页查询实验教学进度
     * @param university_id
     * @param pageNum
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "根据学校id和页码来分页查询实验教学进度", notes = "已测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name="university_id", value = "学校id", required = true, dataType = "bigint", paramType = "path"),
            @ApiImplicitParam(name="pageNum", value = "页码", required = true, dataType = "Integer", paramType = "path")
    })
    @GetMapping(value = "/courseExperimentSchedules/listByUniversityId/{university_id}/{pageNum}")
    public void listByUniversityID(@PathVariable long university_id,
                                   @PathVariable Integer pageNum,
                                   HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.ListByUniversityID_CacheNamePrefix + university_id + "_" + pageNum;
        String json = cache.get(cacheName);
        if(json == null){
            PageInfo<CourseExperimentSchedule> pageInfo = courseExperimentScheduleService.selectPageByUniversity(pageNum, university_id);
            json = Result.build(ResultType.Success).appendData("pageInfo", pageInfo).convertIntoJSON();
            if(pageInfo != null) {
                cache.set(cacheName, json);
            }
        }
        response.getWriter().write(json);
    }
    /**
     * 列举所有实验教学进度
     * @param response
     * @throws IOException
     */
    @ApiOperation(value="列举所有实验教学进度", notes="已测试")
    @GetMapping(value = "/courseExperimentSchedules/listAll")
    public void list(HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.ListAll_CacheName;
        String json = cache.get(cacheName);
        if(json == null){
            json = Result.build(ResultType.Success).appendData("courseExperimentSchedules", courseExperimentScheduleService.selectAll()).convertIntoJSON();
            cache.set(cacheName, json);
        }
        response.getWriter().write(json);
    }

    @GetMapping(value = "/courseExperimentSchedules/listByCourseId/{course_id}")
    public void listByCourseId(@PathVariable long course_id, HttpServletResponse response)throws IOException{
        response.setContentType("application/json;charset=utf-8");
        String json;
        List<CourseExperimentPlan> courseExperimentPlanList=courseExperimentPlanService.selectByCourseId(course_id);
        List<CourseExperimentSchedule> courseExperimentSchedules=new ArrayList<>();
        for(int i=0;i<courseExperimentPlanList.size();i++){
            CourseExperimentSchedule courseExperimentSchedule=courseExperimentScheduleService.select( courseExperimentPlanList.get(i).getCourseExperimentScheduleId());
            courseExperimentSchedules.add(courseExperimentSchedule);
        }
        json=Result.build(ResultType.Success).appendData("courseExperimentSchedules",courseExperimentSchedules).convertIntoJSON();
        response.getWriter().write(json);
    }
}