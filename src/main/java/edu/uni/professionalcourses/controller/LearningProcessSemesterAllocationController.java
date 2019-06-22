package edu.uni.professionalcourses.controller;

import com.github.pagehelper.PageInfo;
import edu.uni.bean.Result;
import edu.uni.bean.ResultType;
import edu.uni.professionalcourses.bean.LearningProcessSemesterAllocation;
import edu.uni.professionalcourses.service.LearningProcessSemesterAllocationService;
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
 * create : 2019/4/29 0029 16:55
 * modified :
 * 功能 :控制对LearningProcessSemesterAllocation的请求跳转
 **/
@Api(description = "专业课程模块")
@Controller
@RequestMapping("json/professionalCourses")
public class LearningProcessSemesterAllocationController {
    @Autowired
    private LearningProcessSemesterAllocationService learningProcessSemesterAllocationService;
    @Autowired
    private RedisCache cache;
    static class CacheNameHelper{
        // pc_l_LearningProcessSemesterAllocation_{主id}
        private static final String Receive_CacheNamePrefix = "pc_LearningProcessSemesterAllocation_";
        // pc_l_LearningProcessSemesterAllocation_list_{页码}
        private static final String List_CacheNamePrefix = "pc_LearningProcessSemesterAllocation_list_";
        // pc_l_LearningProcessSemesterAllocation_listByUniversityid_{学校id}_{页码}
        private static final String ListByUniversityId_CacheNamePrefix = "pc_LearningProcessSemesterAllocation_listByUniversityId_";
        // pc_l_LearningProcessSemesterAllocation_listBySpecialtyid_{专业学习计划表id}_{页码}
        private static final String ListByLearningprocessId_CacheNamePrefix = "pc_LearningProcessSemesterAllocation_listByLearningprocessId_";
    }
    /**
     * 新增学习计划学期分配计划
     * @param learningProcessSemesterAllocation
     * @return
     */
    @ApiOperation(value="新增学习计划学期分配", notes = "已测试")
    @ApiImplicitParam(name= "learningProcessSemesterAllocation",value = "学习计划学期分配实体类", required = true, dataType = "LearningProcessSemesterAllocation")
    @PostMapping("learningProcessSemesterAllocation")
    @ResponseBody
    public Result create(@RequestBody(required = false) LearningProcessSemesterAllocation learningProcessSemesterAllocation){
        if(learningProcessSemesterAllocation != null){
            boolean success = learningProcessSemesterAllocationService.insert(learningProcessSemesterAllocation);
            if(success){
                cache.deleteByPaterm(CacheNameHelper.List_CacheNamePrefix + "*");
//                cache.deleteByPaterm(CacheNameHelper.ListAll_CacheName );
                cache.deleteByPaterm(CacheNameHelper.ListByUniversityId_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.Receive_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListByLearningprocessId_CacheNamePrefix + "*");
//                cache.deleteByPaterm(CacheNameHelper.ListByCourseId_CacheNamePrefix + "*");
                return Result.build(ResultType.Success);
            }else{
                return Result.build(ResultType.Failed);
            }
        }
        return Result.build(ResultType.ParamError);
    }
    /**
     * 删除学习计划学期分配计划
     * @param id
     * @return
     */
    @ApiOperation(value = "根据表主键id删除学习计划学期分配计划", notes = "已测试")
    @ApiImplicitParam(name = "id", value = "表主键id", required = true, dataType = "bigint", paramType = "path")
    @DeleteMapping("learningProcessSemesterAllocation/{id}")
    @ResponseBody
    public Result destroy(@PathVariable long id){
        boolean success = learningProcessSemesterAllocationService.delete(id);
        if(success){
            cache.delete(CacheNameHelper.Receive_CacheNamePrefix + id);
            cache.deleteByPaterm(CacheNameHelper.List_CacheNamePrefix + "*");
            cache.deleteByPaterm(CacheNameHelper.ListByUniversityId_CacheNamePrefix + "*");
//            cache.deleteByPaterm(CacheNameHelper.ListAll_CacheName );
            cache.deleteByPaterm(CacheNameHelper.ListByLearningprocessId_CacheNamePrefix + "*");
//            cache.deleteByPaterm(CacheNameHelper.ListByCourseId_CacheNamePrefix + "*");
            return Result.build(ResultType.Success);
        }else{
            return Result.build(ResultType.Failed);
        }
    }
    /**
     * 修改专业课程信息
     * @param learningProcessSemesterAllocation
     * @return
     */
    @ApiOperation(value="根据表主键id修改学习计划学期分配计划", notes = "已测试")
    @ApiImplicitParam(name="learningProcessSemesterAllocation", value = "学习计划学期分配计划实体类", required = true, dataType = "LearningProcessSemesterAllocation")
    @PutMapping("learningProcessSemesterAllocation")
    @ResponseBody
    public Result update(@RequestBody(required = false) LearningProcessSemesterAllocation learningProcessSemesterAllocation){
        if(learningProcessSemesterAllocation != null && learningProcessSemesterAllocation.getId() != null){
            boolean success = learningProcessSemesterAllocationService.update(learningProcessSemesterAllocation);
            if(success){
                cache.delete(CacheNameHelper.Receive_CacheNamePrefix + learningProcessSemesterAllocation.getId());
                cache.deleteByPaterm(CacheNameHelper.List_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListByUniversityId_CacheNamePrefix + "*");
//                cache.deleteByPaterm(CacheNameHelper.ListAll_CacheName );
                cache.deleteByPaterm(CacheNameHelper.ListByLearningprocessId_CacheNamePrefix + "*");
//                cache.deleteByPaterm(CacheNameHelper.ListByCourseId_CacheNamePrefix + "*");

                return Result.build(ResultType.Success);
            }else{
                return Result.build(ResultType.Failed);
            }
        }
        return Result.build(ResultType.ParamError);
    }
    /**
     * 根据主键id获取表信息
     * @param id
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "根据主键id获取信息", notes = "已测试")
    @ApiImplicitParam(name = "id", value = "主键id", required = true, dataType = "bigint", paramType = "path")
    @GetMapping("learningProcessSemesterAllocation/{id}")
    public void receive(@PathVariable long id,
                        HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.Receive_CacheNamePrefix + id;
        String json = cache.get(cacheName);
        if(json == null){
            LearningProcessSemesterAllocation learningProcessSemesterAllocation= learningProcessSemesterAllocationService.select(id);
            json = Result.build(ResultType.Success).appendData("learningProcessSemesterAllocation", learningProcessSemesterAllocation).convertIntoJSON();
            if(learningProcessSemesterAllocation != null){
                cache.set(cacheName, json);
            }
        }
        response.getWriter().write(json);
    }
    /**
     * 根据页码分页查询所有信息
     * @param pageNum
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "根据页码分页查询所有信息", notes = "已测试")
    @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "Integer", paramType = "path")
    @GetMapping(value = "/learningProcessSemesterAllocation/list/{pageNum}")
    public void list(
            @PathVariable Integer pageNum ,
            HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.List_CacheNamePrefix + pageNum;
        String json = cache.get(cacheName);
        if(json == null){
            PageInfo<LearningProcessSemesterAllocation> pageInfo = learningProcessSemesterAllocationService.selectPage(pageNum);
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
    @ApiOperation(value = "根据学校id和页码来分页查询信息", notes = "已测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name="university_id", value = "学校id", required = true, dataType = "bigint", paramType = "path"),
            @ApiImplicitParam(name="pageNum", value = "页码", required = true, dataType = "Integer", paramType = "path")
    })
    @GetMapping(value = "/learningProcessSemesterAllocation/listByUniversityId/{university_id}/{pageNum}")
    public void listByUniversityID(@PathVariable long university_id,
                                   @PathVariable Integer pageNum,
                                   HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.ListByUniversityId_CacheNamePrefix + university_id + "_" + pageNum;
        String json = cache.get(cacheName);
        if(json == null){
            PageInfo<LearningProcessSemesterAllocation> pageInfo = learningProcessSemesterAllocationService.selectPageByUniversity(pageNum, university_id);
            json = Result.build(ResultType.Success).appendData("pageInfo", pageInfo).convertIntoJSON();
            if(pageInfo != null) {
                cache.set(cacheName, json);
            }
        }
        response.getWriter().write(json);
    }
    /**
     * 根据专业学习计划表id和页码来分页查询信息
     * @param learning_process_Id
     * @param pageNum
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "根据专业学习计划表id和页码来分页查询信息", notes = "已测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name="learning_process_Id", value = "专业学习计划表id", required = true, dataType = "bigint", paramType = "path"),
            @ApiImplicitParam(name="pageNum", value = "页码", required = true, dataType = "Integer", paramType = "path")
    })
    @GetMapping(value = "/learningProcessSemesterAllocation/listByLearningprocessId/{learning_process_Id}/{pageNum}")
    public void listByDepartmentID(@PathVariable long learning_process_Id,
                                   @PathVariable Integer pageNum,
                                   HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.ListByLearningprocessId_CacheNamePrefix + learning_process_Id + "_" + pageNum;
        String json = cache.get(cacheName);
        if(json == null){
            PageInfo<LearningProcessSemesterAllocation> pageInfo = learningProcessSemesterAllocationService.selectPageByLearningprocess(pageNum, learning_process_Id);
            json = Result.build(ResultType.Success).appendData("pageInfo", pageInfo).convertIntoJSON();
            if(pageInfo != null) {
                cache.set(cacheName, json);
            }
        }
        response.getWriter().write(json);
    }

}
