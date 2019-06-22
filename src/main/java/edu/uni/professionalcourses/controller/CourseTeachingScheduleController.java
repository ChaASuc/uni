package edu.uni.professionalcourses.controller;

import com.github.pagehelper.PageInfo;
import edu.uni.bean.Result;
import edu.uni.bean.ResultType;
import edu.uni.professionalcourses.bean.CourseTeachingPlan;
import edu.uni.professionalcourses.bean.CourseTeachingSchedule;
import edu.uni.professionalcourses.bean.CourseTeachingSchedule2;
import edu.uni.professionalcourses.service.CourseTeachingPlanService;
import edu.uni.professionalcourses.service.CourseTeachingScheduleService;
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

/**
 * author : 黄永佳
 * create : 2019/5/5 0005 16:41
 * modified :
 * 功能 :控制对 CourseTeachingSchedule表的请求跳转
 **/
@Api(description = "专业课程模块")
@Controller
@RequestMapping("json/professionalCourses")
public class CourseTeachingScheduleController {
    @Autowired
    private CourseTeachingScheduleService courseTeachingScheduleService;
    @Autowired
    private CourseTeachingPlanService courseTeachingPlanService;
    @Autowired
    private RedisCache cache;
    //    缓存内部类
    static class CacheNameHelper{
        // pc_c_CourseTeachingSchedule_{主id}
        private static final String Receive_CacheNamePrefix = "pc_CourseTeachingSchedule_";
        // pc_c_CourseTeachingSchedule_list_{页码}
        private static final String List_CacheNamePrefix = "pc_CourseTeachingSchedule_list_";
        // pc_c_CourseTeachingSchedule_listByUniversityid_{学校id}_{页码}
        private static final String ListByUniversityId_CacheNamePrefix = "pc_CourseTeachingSchedule_listByUniversityId_";
        // pc_c_courseteachingplan_listByCourseteachingscheduleId_{课程教学进度表id}_{页码}
//        private static final String ListByCourseteachingscheduleId_CacheNamePrefix = "pc_courseteachingplan_listByCourseteachingscheduleId_";
        // pc_c_courseteachingplan_listByCourseid_{课程id}_{页码}
//        private static final String ListByCourseId_CacheNamePrefix = "pc_courseteachingplan_listByCourseId_";
        // pc_c_courseteachingplan_listByCourseteachingscheduleId_{课程实验进度表id}_{页码}
//        private static final String ListByCourseexperimentscheduleId_CacheNamePrefix = "pc_courseteachingplan_listByCourseexperimentscheduleId_";
    }
    /**
     * 新增课程教学进度
     * @param courseTeachingSchedule2
     * @return
     */
    @ApiOperation(value="新增课程教学进度", notes = "未测试")
//    @ApiImplicitParam(name= "courseTeachingSchedule",value = "课程教学进度实体类", required = true, dataType = "CourseTeachingSchedule")
    @PostMapping("courseTeachingSchedule2")
    @ResponseBody
    public Result create(@RequestBody(required = false) CourseTeachingSchedule2 courseTeachingSchedule2){
        System.out.println(courseTeachingSchedule2.getCourseId()+courseTeachingSchedule2.getUniversityId()+courseTeachingSchedule2.getExperimentHour());
        if(courseTeachingSchedule2 != null){
            CourseTeachingSchedule courseTeachingSchedule=new CourseTeachingSchedule();
            courseTeachingSchedule.setDeleted(courseTeachingSchedule2.getDeleted());
            courseTeachingSchedule.setActualProgress(courseTeachingSchedule2.getActualProgress());
            courseTeachingSchedule.setByWho(courseTeachingSchedule2.getByWho());
            courseTeachingSchedule.setChapter(courseTeachingSchedule2.getChapter());
            courseTeachingSchedule.setContent(courseTeachingSchedule2.getContent());
            courseTeachingSchedule.setDatetime(courseTeachingSchedule2.getDatetime());
            courseTeachingSchedule.setExperimentHour(courseTeachingSchedule2.getExperimentHour());
            courseTeachingSchedule.setHomework(courseTeachingSchedule2.getHomework());
            courseTeachingSchedule.setOutOfPlanReason(courseTeachingSchedule2.getOutOfPlanReason());
            courseTeachingSchedule.setTeachHour(courseTeachingSchedule2.getTeachHour());
            courseTeachingSchedule.setUniversityId(courseTeachingSchedule2.getUniversityId());
            courseTeachingSchedule.setWeek(courseTeachingSchedule2.getWeek());
            System.out.println("++++++++"+courseTeachingSchedule2.getCourseId());
            boolean success = courseTeachingScheduleService.insert(courseTeachingSchedule,courseTeachingSchedule2.getCourseId());
            if(success){
                cache.deleteByPaterm(CacheNameHelper.List_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListByUniversityId_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.Receive_CacheNamePrefix + "*");
                return Result.build(ResultType.Success);
            }else{
                return Result.build(ResultType.Failed);
            }
        }
        return Result.build(ResultType.ParamError);
    }
    /**
     * 删除课程教学进度
     * @param id
     * @return
     */
    @ApiOperation(value = "根据表主键id删除课程教学进度", notes = "未测试")
    @ApiImplicitParam(name = "id", value = "表主键id", required = true, dataType = "bigint", paramType = "path")
    @DeleteMapping("courseTeachingSchedule/{id}")
    @ResponseBody
    public Result destroy(@PathVariable long id){
        boolean success = courseTeachingScheduleService.delete(id);
        if(success){
            cache.deleteByPaterm(CacheNameHelper.List_CacheNamePrefix + "*");
            cache.deleteByPaterm(CacheNameHelper.ListByUniversityId_CacheNamePrefix + "*");
            cache.deleteByPaterm(CacheNameHelper.Receive_CacheNamePrefix + "*");
            return Result.build(ResultType.Success);
        }else{
            return Result.build(ResultType.Failed);
        }
    }
    /**
     * 根据主键id获取课程教学进度
     * @param id
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "根据主键id获取课程教学进度", notes = "未测试")
    @ApiImplicitParam(name = "id", value = "主键id", required = true, dataType = "bigint", paramType = "path")
    @GetMapping("courseTeachingSchedule/{id}")
    public void receive(@PathVariable long id,
                        HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.Receive_CacheNamePrefix + id;
        String json = cache.get(cacheName);
        if(json == null){
            CourseTeachingSchedule courseTeachingSchedule= courseTeachingScheduleService.select(id);
            json = Result.build(ResultType.Success).appendData("courseTeachingSchedule", courseTeachingSchedule).convertIntoJSON();
            if(courseTeachingSchedule != null){
                cache.set(cacheName, json);
            }
        }
        response.getWriter().write(json);
    }
    /**
     * 修改课程教学进度
     * @param courseTeachingSchedule
     * @return
     */
    @ApiOperation(value="根据表主键id修改课程教学进度", notes = "未测试")
    @ApiImplicitParam(name="courseTeachingSchedule", value = "课程教学进度实体类", required = true, dataType = "CourseTeachingSchedule")
    @PutMapping("courseTeachingSchedule")
    @ResponseBody
    public Result update(@RequestBody(required = false) CourseTeachingSchedule courseTeachingSchedule){
        if(courseTeachingSchedule != null && courseTeachingSchedule.getId() != null){

            boolean success = courseTeachingScheduleService.update(courseTeachingSchedule);
            if(success){
                cache.deleteByPaterm(CacheNameHelper.List_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListByUniversityId_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.Receive_CacheNamePrefix + "*");
                return Result.build(ResultType.Success);
            }else{
                return Result.build(ResultType.Failed);
            }
        }
        return Result.build(ResultType.ParamError);
    }
    /**
     * 根据页码分页查询所有课程教学进度
     * @param pageNum
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "根据页码分页查询所有课程教学进度", notes = "未测试")
    @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "Integer", paramType = "path")
    @GetMapping(value = "/courseTeachingSchedule/list/{pageNum}")
    public void list(
            @PathVariable Integer pageNum ,
            HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.List_CacheNamePrefix + pageNum;
        String json = cache.get(cacheName);
        if(json == null){
            PageInfo<CourseTeachingSchedule> pageInfo = courseTeachingScheduleService.selectPage(pageNum);
            json = Result.build(ResultType.Success).appendData("pageInfo", pageInfo).convertIntoJSON();
            if(pageInfo != null) {
                cache.set(cacheName, json);
            }
        }
        response.getWriter().write(json);
    }
    /**
     * 根据学校id和页码来分页查询课程教学进度
     * @param university_id
     * @param pageNum
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "根据学校id和页码来分页查询课程教学进度", notes = "未测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name="university_id", value = "学校id", required = true, dataType = "bigint", paramType = "path"),
            @ApiImplicitParam(name="pageNum", value = "页码", required = true, dataType = "Integer", paramType = "path")
    })
    @GetMapping(value = "/courseTeachingSchedule/listByUniversityId/{university_id}/{pageNum}")
    public void listByUniversityID(@PathVariable long university_id,
                                   @PathVariable Integer pageNum,
                                   HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.ListByUniversityId_CacheNamePrefix + university_id + "_" + pageNum;
        String json = cache.get(cacheName);
        if(json == null){
            PageInfo<CourseTeachingSchedule> pageInfo = courseTeachingScheduleService.selectPageByUniversity(pageNum, university_id);
            json = Result.build(ResultType.Success).appendData("pageInfo", pageInfo).convertIntoJSON();
            if(pageInfo != null) {
                cache.set(cacheName, json);
            }
        }
        response.getWriter().write(json);
    }


    @GetMapping(value = "/courseTeachingSchedule/listByCourseId/{course_id}")
    public void listByCourseId(@PathVariable long course_id, HttpServletResponse response)throws IOException{
        response.setContentType("application/json;charset=utf-8");
        String json;
        List<CourseTeachingPlan> courseTeachingPlanList=courseTeachingPlanService.selectByCourseId(course_id);
        List<CourseTeachingSchedule> courseTeachingSchedules=new ArrayList<>();
        for(int i=0;i<courseTeachingPlanList.size();i++){
            CourseTeachingSchedule courseTeachingSchedule=courseTeachingScheduleService.select( courseTeachingPlanList.get(i).getCourseTeachingScheduleId());
            courseTeachingSchedules.add(courseTeachingSchedule);
        }
        json=Result.build(ResultType.Success).appendData("courseTeachingSchedules",courseTeachingSchedules).convertIntoJSON();
        response.getWriter().write(json);
    }
}
