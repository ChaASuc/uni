package edu.uni.professionalcourses.controller;

import com.github.pagehelper.PageInfo;
import edu.uni.bean.Result;
import edu.uni.bean.ResultType;
import edu.uni.professionalcourses.bean.ExperimentSettingContent;
import edu.uni.professionalcourses.service.ExperimentSettingContentService;
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
 * create : 2019/5/5 0005 17:18
 * modified :
 * 功能 :控制ExperimentSettingContent表的请求跳转
 **/
@Api(description = "专业课程模块")
@Controller
@RequestMapping("json/professionalCourses")
public class ExperimentSettingContentController {
    @Autowired
    private ExperimentSettingContentService experimentSettingContentService;
    @Autowired
    private RedisCache cache;
    //    缓存内部类
    static class CacheNameHelper{
        // pc_e_experimentSettingContent_{主id}
        private static final String Receive_CacheNamePrefix = "pc_experimentSettingContent_";
        // pc_e_experimentSettingContent_list_{页码}
        private static final String List_CacheNamePrefix = "pc_experimentSettingContent_list_";
        // pc_e_experimentSettingContent_listByUniversityid_{学校id}_{页码}
        private static final String ListByUniversityId_CacheNamePrefix = "pc_experimentSettingContent_listByUniversityId_";
        // pc_e_experimentSettingContent_listByCourseexperimentdescriptionId_{实验教学内容id}_{页码}
        private static final String ListByCourseexperimentdescriptionId_CacheNamePrefix = "pc_experimentSettingContent_listByCourseexperimentdescription_Id_";
    }
    /**
     * 新增实验项目设置与内容
     * @param experimentSettingContent
     * @return
     */
    @ApiOperation(value="新增课程教学进度", notes = "未测试")
    @ApiImplicitParam(name= "experimentSettingContent",value = "课程教学进度实体类", required = true, dataType = "ExperimentSettingContent")
    @PostMapping("experimentSettingContent")
    @ResponseBody
    public Result create(@RequestBody(required = false) ExperimentSettingContent experimentSettingContent){
        if(experimentSettingContent != null){
            boolean success = experimentSettingContentService.insert(experimentSettingContent);
            if(success){
                cache.deleteByPaterm(CacheNameHelper.List_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListByUniversityId_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.Receive_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListByCourseexperimentdescriptionId_CacheNamePrefix+"*");
                return Result.build(ResultType.Success);
            }else{
                return Result.build(ResultType.Failed);
            }
        }
        return Result.build(ResultType.ParamError);
    }
    /**
     * 删除实验项目设置与内容
     * @param id
     * @return
     */
    @ApiOperation(value = "根据表主键id删除实验项目设置与内容", notes = "未测试")
    @ApiImplicitParam(name = "id", value = "表主键id", required = true, dataType = "bigint", paramType = "path")
    @DeleteMapping("experimentSettingContent/{id}")
    @ResponseBody
    public Result destroy(@PathVariable long id){
        boolean success = experimentSettingContentService.delete(id);
        if(success){
            cache.deleteByPaterm(CacheNameHelper.List_CacheNamePrefix + "*");
            cache.deleteByPaterm(CacheNameHelper.ListByUniversityId_CacheNamePrefix + "*");
            cache.deleteByPaterm(CacheNameHelper.Receive_CacheNamePrefix + "*");
            cache.deleteByPaterm(CacheNameHelper.ListByCourseexperimentdescriptionId_CacheNamePrefix+"*");
            return Result.build(ResultType.Success);
        }else{
            return Result.build(ResultType.Failed);
        }
    }
    /**
     * 根据主键id获取实验项目设置与内容
     * @param id
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "根据主键id获取实验项目设置与内容", notes = "未测试")
    @ApiImplicitParam(name = "id", value = "主键id", required = true, dataType = "bigint", paramType = "path")
    @GetMapping("experimentSettingContent/{id}")
    public void receive(@PathVariable long id,
                        HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.Receive_CacheNamePrefix + id;
        String json = cache.get(cacheName);
        if(json == null){
            ExperimentSettingContent experimentSettingContent= experimentSettingContentService.select(id);
            json = Result.build(ResultType.Success).appendData("experimentSettingContent", experimentSettingContent).convertIntoJSON();
            if(experimentSettingContent != null){
                cache.set(cacheName, json);
            }
        }
        response.getWriter().write(json);
    }
    /**
     * 修改课程教学进度
     * @param experimentSettingContent
     * @return
     */
    @ApiOperation(value="根据表主键id修改实验项目设置与内容", notes = "未测试")
    @ApiImplicitParam(name="experimentSettingContent", value = "实验项目设置与内容实体类", required = true, dataType = "ExperimentSettingContent")
    @PutMapping("experimentSettingContent")
    @ResponseBody
    public Result update(@RequestBody(required = false) ExperimentSettingContent experimentSettingContent){
        if(experimentSettingContent != null && experimentSettingContent.getId() != null){
            boolean success = experimentSettingContentService.update(experimentSettingContent);
            if(success){
                cache.deleteByPaterm(CacheNameHelper.List_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListByUniversityId_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.Receive_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListByCourseexperimentdescriptionId_CacheNamePrefix+"*");
                return Result.build(ResultType.Success);
            }else{
                return Result.build(ResultType.Failed);
            }
        }
        return Result.build(ResultType.ParamError);
    }
    /**
     * 根据页码分页查询所有实验项目设置与内容
     * @param pageNum
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "根据页码分页查询所有课程教学进度", notes = "未测试")
    @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "Integer", paramType = "path")
    @GetMapping(value = "/experimentSettingContent/list/{pageNum}")
    public void list(
            @PathVariable Integer pageNum ,
            HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.List_CacheNamePrefix + pageNum;
        String json = cache.get(cacheName);
        if(json == null){
            PageInfo<ExperimentSettingContent> pageInfo = experimentSettingContentService.selectPage(pageNum);
            json = Result.build(ResultType.Success).appendData("pageInfo", pageInfo).convertIntoJSON();
            if(pageInfo != null) {
                cache.set(cacheName, json);
            }
        }
        response.getWriter().write(json);
    }
    /**
     * 根据学校id和页码来分页查询实验项目设置与内容
     * @param university_id
     * @param pageNum
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "根据学校id和页码来分页查询实验项目设置与内容", notes = "未测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name="university_id", value = "学校id", required = true, dataType = "bigint", paramType = "path"),
            @ApiImplicitParam(name="pageNum", value = "页码", required = true, dataType = "Integer", paramType = "path")
    })
    @GetMapping(value = "/experimentSettingContent/listByUniversityId/{university_id}/{pageNum}")
    public void listByUniversityID(@PathVariable long university_id,
                                   @PathVariable Integer pageNum,
                                   HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.ListByUniversityId_CacheNamePrefix + university_id + "_" + pageNum;
        String json = cache.get(cacheName);
        if(json == null){
            PageInfo<ExperimentSettingContent> pageInfo = experimentSettingContentService.selectPageByUniversity(pageNum, university_id);
            json = Result.build(ResultType.Success).appendData("pageInfo", pageInfo).convertIntoJSON();
            if(pageInfo != null) {
                cache.set(cacheName, json);
            }
        }
        response.getWriter().write(json);
    }
    /**
     * 根据实验教学内容id和页码来分页查询实验项目设置与内容
     * @param course_experiment_description_Id
     * @param pageNum
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "根据实验教学内容id和页码来分页查询实验项目设置与内容", notes = "未测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name="course_experiment_description_Id", value = "实验教学内容id", required = true, dataType = "bigint", paramType = "path"),
            @ApiImplicitParam(name="pageNum", value = "页码", required = true, dataType = "Integer", paramType = "path")
    })
    @GetMapping(value = "/experimentSettingContent/listByCourseexperimentdescription_Id/{course_experiment_description_Id}/{pageNum}")
    public void listByCourse_experiment_description_Id(@PathVariable long course_experiment_description_Id,
                                   @PathVariable Integer pageNum,
                                   HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.ListByCourseexperimentdescriptionId_CacheNamePrefix + course_experiment_description_Id + "_" + pageNum;
        String json = cache.get(cacheName);
        if(json == null){
            PageInfo<ExperimentSettingContent> pageInfo = experimentSettingContentService.selectPageByCourseexperimentdescriptionId(pageNum, course_experiment_description_Id);
            json = Result.build(ResultType.Success).appendData("pageInfo", pageInfo).convertIntoJSON();
            if(pageInfo != null) {
                cache.set(cacheName, json);
            }
        }
        response.getWriter().write(json);
    }
}
