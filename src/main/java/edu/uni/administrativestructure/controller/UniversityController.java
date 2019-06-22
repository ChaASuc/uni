package edu.uni.administrativestructure.controller;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.github.pagehelper.PageInfo;
import edu.uni.administrativestructure.bean.University;
import edu.uni.administrativestructure.service.UniversityService;
import edu.uni.bean.Result;
import edu.uni.bean.ResultType;
import edu.uni.utils.RedisCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static edu.uni.bean.ResultType.Failed;
import static edu.uni.bean.ResultType.Success;

/**
 * author：黄育林
 * create: 2019.4.20
 * mofified：2019.6.13
 */

@Api(description = "行政架构模块")
@Controller
@RequestMapping("json/administrativestructure")
public class UniversityController {
    @Autowired
    private UniversityService universityService;

    @Autowired
    private RedisCache cache;

    /**
     * 内部类，专门用来管理每个get方法所对应缓存的名称。
     */
    static class CacheNameHelper{
        // as_u_university_{学校id}
        public static final String Receive_CacheNamePrefix = "as_university_";
        private static final String List_CacheNamePrefix = "as_university_list_";
        private static final String ListLikeName_CacheNamePrefix = "as_university_listLikeName_";
        // as_u_university_listAll
        public static final String ListAll_CacheName = "as_university_listAll";
    }

    /**
     * 新增学校
     * @param university
     * @return
     */
    @ApiOperation(value="新增学校", notes="已测试")
    @ApiImplicitParam(name = "university", value = "学校详情实体", required = true, dataType = "University")
    @PostMapping("/university")
    @ResponseBody
    public Result create(@RequestBody(required = false) University university){
        if(university != null){
            boolean success = universityService.insert(university);
            if(success){
                // 清空相关缓存
                cache.deleteByPaterm(CacheNameHelper.ListAll_CacheName + "*");
                cache.deleteByPaterm(CacheNameHelper.List_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.Receive_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListLikeName_CacheNamePrefix+ "*");
                return Result.build(Success);
            }else{
                return Result.build(Failed);
            }
        }
        return Result.build(ResultType.ParamError);
    }

    /**
     * 删除学校
     * @param id
     * @return
     */
    @ApiOperation(value="删除学校", notes="已测试")
    @ApiImplicitParam(name = "id", value = "学校id", required = true, dataType = "bigint", paramType = "path")
    @DeleteMapping("/university/{id}")
    @ResponseBody
    public Result destroy(@PathVariable long id){
        boolean success = universityService.delete(id);
        if(success){
            // 清空相关缓存
            cache.deleteByPaterm(CacheNameHelper.ListAll_CacheName + "*");
            cache.deleteByPaterm(CacheNameHelper.List_CacheNamePrefix + "*");
            cache.deleteByPaterm(CacheNameHelper.Receive_CacheNamePrefix + "*");
            cache.deleteByPaterm(CacheNameHelper.ListLikeName_CacheNamePrefix + "*");
            return Result.build(Success);
        }else{
            return Result.build(Failed);
        }
    }

    /**
     * 更新学校
     * @param university
     * @return
     */
    @ApiOperation(value="根据学校id更新学校信息", notes="已测试")
    @ApiImplicitParam(name = "university", value = "学校实体", required = true, dataType = "University")
    @PutMapping("/university")
    @ResponseBody
    public Result update(@RequestBody(required = false) University university){
        if(university != null && university.getId() != null){
            boolean success = universityService.update(university);
            if(success){
                cache.deleteByPaterm(CacheNameHelper.ListAll_CacheName + "*");
                cache.deleteByPaterm(CacheNameHelper.List_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.Receive_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListLikeName_CacheNamePrefix + "*");
                return Result.build(Success);
            }else{
                return Result.build(Failed);
            }
        }
        return Result.build(ResultType.ParamError);
    }

    /**
     * 获取学校详情
     * @param id
     * @param response
     * @throws IOException
     */
    @ApiOperation(value="根据学校id获取学校详情", notes="已测试")
    @ApiImplicitParam(name = "id", value = "学校id", required = false, dataType = "bigint", paramType = "path")
    @GetMapping("/university/{id}")
    public void receive(@PathVariable long id, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.Receive_CacheNamePrefix + id;
        String json = cache.get(cacheName);
        if(json == null){
            json = Result.build(Success).appendData("university",  universityService.select(id)).convertIntoJSON();
            cache.set(cacheName, json);
        }
        response.getWriter().write(json);
    }

    /**
     * 根据学校名称模糊查询获取学校详情
     * @param name
     * @param response
     * @throws IOException
     */
    @ApiOperation(value="根据学校名称模糊查询获取学校详情", notes="已测试")
    @ApiImplicitParam(name = "name", value = "学校名称", required = false, dataType = "String", paramType = "path")
    @GetMapping("/university/listLikeName/{name}")
    public void receive(@PathVariable String name, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.ListLikeName_CacheNamePrefix + name;
        String json = cache.get(cacheName);
        if(json == null){
            json = Result.build(Success).appendData("university",  universityService.selectLikeName(name)).convertIntoJSON();
            cache.set(cacheName, json);
        }
        response.getWriter().write(json);
    }

    /**
     * 列举所有学校
     * @param response
     * @throws IOException
     */
    @ApiOperation(value="列举所有学校", notes="已测试")
    @GetMapping(value = "/universitys/listAll")
    public void listAll(HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.ListAll_CacheName;
        String json = cache.get(cacheName);
        if(json == null){
            json = Result.build(Success).appendData("universitys", universityService.selectAll()).convertIntoJSON();
            cache.set(cacheName, json);
        }
        response.getWriter().write(json);
    }

    /**
     * 根据页码分页查询所有学校
     * @param pageNum
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "根据页码分页查询所有学校", notes = "已测试")
    @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "Integer", paramType = "path")
    @GetMapping(value = "/universities/list/{pageNum}")
    public void list(
            @PathVariable Integer pageNum ,
            HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.List_CacheNamePrefix + pageNum;
        String json = cache.get(cacheName);
        if(json == null){
            PageInfo<University> pageInfo = universityService.selectPage(pageNum);
            json = Result.build(Success).appendData("pageInfo", pageInfo).convertIntoJSON();
            if(pageInfo != null) {
                cache.set(cacheName, json);
            }
        }
        response.getWriter().write(json);
    }

    /**
     * excel导出所有学校
     * @param response
     * @throws IOException
     */
    @ApiOperation(value="excel导出所有学校", notes="已测试/默认路径为E:\\")
    @GetMapping(value = "/universitys/excelOut")
    public void excelOut(HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.ListAll_CacheName;
        String json = cache.get(cacheName);
        //文件输出位置
        OutputStream out = null;
        try {
            out = new FileOutputStream("E:\\university.xlsx");
            ExcelWriter writer = EasyExcelFactory.getWriter(out);
            // 写仅有一个 Sheet 的 Excel 文件
            Sheet sheet1 = new Sheet(1, 0, University.class);
            // 第一个 sheet 名称
            sheet1.setSheetName("sheet_1");
//            if (json == null) {
//                json = Result.build(ResultType.Success).appendData("universitys", universityService.selectAll()).convertIntoJSON();
//                cache.set(cacheName, json);
//            }
            writer.write(universityService.selectAll(), sheet1);
            writer.finish();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                // 关闭流
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ApiOperation(value="excel导入", notes="已测试")
    @RequestMapping(value = "/university/import", method = RequestMethod.POST)
    public Result importuniversity(MultipartFile file, HttpServletRequest request) throws IOException {
        //获取登录人id、角色
//        User user = authService.getUser();
//        if(user == null) return Result.build(Failed);
//        String loginId = user.getId().toString();
        //根据身份判断是否有能申请的权限
        if(file != null) {
            String savePath = request.getServletContext().getRealPath("/");
            if(universityService.uploadUniversity(savePath, file) > 0) {
                // 清空相关缓存
                cache.deleteByPaterm(CacheNameHelper.ListAll_CacheName + "*");
                cache.deleteByPaterm(CacheNameHelper.List_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.Receive_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListLikeName_CacheNamePrefix+ "*");
                return Result.build(Success);
            }
        }
        return Result.build(Failed);
    }

}
