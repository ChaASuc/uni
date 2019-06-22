package edu.uni.professionalcourses.controller;

import com.github.pagehelper.PageInfo;
import edu.uni.bean.Result;
import edu.uni.bean.ResultType;
import edu.uni.professionalcourses.bean.LearningProcess;
import edu.uni.professionalcourses.service.LearningProcessService;
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
 * create : 2019/4/30 0030 15:51
 * modified :
 * 功能 :控制对LearningProcess表的请求跳转
 **/
@Api(description = "专业课程模块")
@Controller
@RequestMapping("json/professionalCourses")
public class LearningProcessController {
    @Autowired
    private LearningProcessService learningProcessService;
    @Autowired
    private RedisCache cache;
    //    缓存内部类
    static class CacheNameHelper{
        // pc_l_learningprocess_{主id}
        private static final String Receive_CacheNamePrefix = "pc_learningprocess_";
        // pc_l_learningprocess_list_{页码}
        private static final String List_CacheNamePrefix = "pc_learningprocess_list_";
        // pc_l_learningprocess_listByUniversityid_{学校id}_{页码}
        private static final String ListByUniversityId_CacheNamePrefix = "pc_learningprocess_listByUniversityId_";
        //pc_l_learningprocess_list
//        public static final String ListAll_CacheName = "pc_specialtycourse_listAll";
        // pc_l_learningprocess_listBySpecialtyid_{专业id}_{页码}
        private static final String ListBySpecialtyId_CacheNamePrefix = "pc_learningprocess_listBySpecialtyId_";
        // pc_l_learningprocess_listByCourseid_{课程id}_{页码}
        private static final String ListByCourseId_CacheNamePrefix = "pc_learningprocess_listByCourseId_";
    }
    /**
     * 新增专业学习计划
     * @param learningProcess
     * @return
     */
    @ApiOperation(value="新增专业学习计划", notes = "未测试")
    @ApiImplicitParam(name= "learningProcess",value = "专业学习计划实体类", required = true, dataType = "LearningProcess")
    @PostMapping("learningProcess")
    @ResponseBody
    public Result create(@RequestBody(required = false) LearningProcess learningProcess){
        if(learningProcess != null){
            boolean success = learningProcessService.insert(learningProcess);
            if(success){
                cache.deleteByPaterm(CacheNameHelper.List_CacheNamePrefix + "*");
//                cache.deleteByPaterm(CacheNameHelper.ListAll_CacheName );
                cache.deleteByPaterm(CacheNameHelper.ListByUniversityId_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.Receive_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListBySpecialtyId_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListByCourseId_CacheNamePrefix + "*");
                return Result.build(ResultType.Success);
            }else{
                return Result.build(ResultType.Failed);
            }
        }
        return Result.build(ResultType.ParamError);
    }
    /**
     * 删除专业学习计划
     * @param id
     * @return
     */
    @ApiOperation(value = "根据表主键id删除专业学习计划", notes = "未测试")
    @ApiImplicitParam(name = "id", value = "表主键id", required = true, dataType = "bigint", paramType = "path")
    @DeleteMapping("learningProcess/{id}")
    @ResponseBody
    public Result destroy(@PathVariable long id){
        boolean success = learningProcessService.delete(id);
        if(success){
            cache.delete(CacheNameHelper.Receive_CacheNamePrefix + id);
            cache.deleteByPaterm(CacheNameHelper.List_CacheNamePrefix + "*");
            cache.deleteByPaterm(CacheNameHelper.ListByUniversityId_CacheNamePrefix + "*");
//            cache.deleteByPaterm(CacheNameHelper.ListAll_CacheName );
            cache.deleteByPaterm(CacheNameHelper.ListBySpecialtyId_CacheNamePrefix + "*");
            cache.deleteByPaterm(CacheNameHelper.ListByCourseId_CacheNamePrefix + "*");
            return Result.build(ResultType.Success);
        }else{
            return Result.build(ResultType.Failed);
        }
    }
    /**
     * 根据主键id获取专业学习计划
     * @param id
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "根据主键id获取专业学习计划", notes = "未测试")
    @ApiImplicitParam(name = "id", value = "主键id", required = true, dataType = "bigint", paramType = "path")
    @GetMapping("learningProcess/{id}")
    public void receive(@PathVariable long id,
                        HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.Receive_CacheNamePrefix + id;
        String json = cache.get(cacheName);
        if(json == null){
            LearningProcess learningProcess= learningProcessService.select(id);
            json = Result.build(ResultType.Success).appendData("learningProcess", learningProcess).convertIntoJSON();
            if(learningProcess != null){
                cache.set(cacheName, json);
            }
        }
        response.getWriter().write(json);
    }
    /**
     * 修改专业学习计划
     * @param learningProcess
     * @return
     */
    @ApiOperation(value="根据表主键id修改专业学习计划", notes = "未测试")
    @ApiImplicitParam(name="learningProcess", value = "专业学习计划实体类", required = true, dataType = "LearningProcess")
    @PutMapping("learningProcess")
    @ResponseBody
    public Result update(@RequestBody(required = false) LearningProcess learningProcess){
        if(learningProcess != null && learningProcess.getId() != null){
            boolean success = learningProcessService.update(learningProcess);
            if(success){
                cache.delete(CacheNameHelper.Receive_CacheNamePrefix + learningProcess.getId());
                cache.deleteByPaterm(CacheNameHelper.List_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListByUniversityId_CacheNamePrefix + "*");
//                cache.deleteByPaterm(CacheNameHelper.ListAll_CacheName );
                cache.deleteByPaterm(CacheNameHelper.ListBySpecialtyId_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListByCourseId_CacheNamePrefix + "*");

                return Result.build(ResultType.Success);
            }else{
                return Result.build(ResultType.Failed);
            }
        }
        return Result.build(ResultType.ParamError);
    }
    /**
     * 根据页码分页查询所有专业学习计划
     * @param pageNum
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "根据页码分页查询所有专业学习计划", notes = "未测试")
    @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "Integer", paramType = "path")
    @GetMapping(value = "/learningProcess/list/{pageNum}")
    public void list(
            @PathVariable Integer pageNum ,
            HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.List_CacheNamePrefix + pageNum;
        String json = cache.get(cacheName);
        if(json == null){
            PageInfo<LearningProcess> pageInfo = learningProcessService.selectPage(pageNum);
            json = Result.build(ResultType.Success).appendData("pageInfo", pageInfo).convertIntoJSON();
            if(pageInfo != null) {
                cache.set(cacheName, json);
            }
        }
        response.getWriter().write(json);
    }
    /**
     * 根据学校id和页码来分页查询一二级部门关系
     * @param university_id
     * @param pageNum
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "根据学校id和页码来分页查询专业学习计划", notes = "未测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name="university_id", value = "学校id", required = true, dataType = "bigint", paramType = "path"),
            @ApiImplicitParam(name="pageNum", value = "页码", required = true, dataType = "Integer", paramType = "path")
    })
    @GetMapping(value = "/learningProcess/listByUniversityId/{university_id}/{pageNum}")
    public void listByUniversityID(@PathVariable long university_id,
                                   @PathVariable Integer pageNum,
                                   HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.ListByUniversityId_CacheNamePrefix + university_id + "_" + pageNum;
        String json = cache.get(cacheName);
        if(json == null){
            PageInfo<LearningProcess> pageInfo = learningProcessService.selectPageByUniversity(pageNum, university_id);
            json = Result.build(ResultType.Success).appendData("pageInfo", pageInfo).convertIntoJSON();
            if(pageInfo != null) {
                cache.set(cacheName, json);
            }
        }
        response.getWriter().write(json);
    }
    /**
     * 根据专业id和页码来分页查询专业课程信息
     * @param specialty_Id
     * @param pageNum
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "根据专业id和页码来分页查询专业学习计划", notes = "未测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name="specialty_Id", value = "专业id", required = true, dataType = "bigint", paramType = "path"),
            @ApiImplicitParam(name="pageNum", value = "页码", required = true, dataType = "Integer", paramType = "path")
    })
    @GetMapping(value = "/learningProcess/listBySpecialtyId/{specialty_Id}/{pageNum}")
    public void listByDepartmentID(@PathVariable long specialty_Id,
                                   @PathVariable Integer pageNum,
                                   HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.ListBySpecialtyId_CacheNamePrefix + specialty_Id + "_" + pageNum;
        String json = cache.get(cacheName);
        if(json == null){
            PageInfo<LearningProcess> pageInfo = learningProcessService.selectPageBySpecialty(pageNum, specialty_Id);
            json = Result.build(ResultType.Success).appendData("pageInfo", pageInfo).convertIntoJSON();
            if(pageInfo != null) {
                cache.set(cacheName, json);
            }
        }
        response.getWriter().write(json);
    }
    /**
     * 根据课程id和页码来分页查询专业课程信息
     * @param course_Id
     * @param pageNum
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "根据课程id和页码来分页查询专业学习计划", notes = "未测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name="course_Id", value = "课程id", required = true, dataType = "bigint", paramType = "path"),
            @ApiImplicitParam(name="pageNum", value = "页码", required = true, dataType = "Integer", paramType = "path")
    })
    @GetMapping(value = "/learningProcess/listByCourseId/{course_Id}/{pageNum}")
    public void listByCourseID(@PathVariable long course_Id,
                               @PathVariable Integer pageNum,
                               HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.ListByCourseId_CacheNamePrefix + course_Id + "_" + pageNum;
        String json = cache.get(cacheName);
        if(json == null){
            PageInfo<LearningProcess> pageInfo = learningProcessService.selectPageByCourse(pageNum, course_Id);
            json = Result.build(ResultType.Success).appendData("pageInfo", pageInfo).convertIntoJSON();
            if(pageInfo != null) {
                cache.set(cacheName, json);
            }
        }
        response.getWriter().write(json);
    }
}
