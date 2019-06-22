package edu.uni.administrativestructure.controller;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.github.pagehelper.PageInfo;
import edu.uni.administrativestructure.bean.Position;
import edu.uni.administrativestructure.service.PositionService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import static edu.uni.bean.ResultType.Failed;
import static edu.uni.bean.ResultType.Success;

/**
 * author：黄育林
 * create: 2019.4.23
 * modified: 2019.5.17
 */

@Api(description = "行政架构模块")
@Controller
@RequestMapping("json/administrativestructure")
public class PositionController {
    @Autowired
    private PositionService positionService;

    @Autowired
    private RedisCache cache;

    /**
     * 内部类，专门用来管理每个方法所对应缓存的名称。
     */
    static class CacheNameHelper{
        // as_p_position_{岗位信息id}
        private static final String Receive_CacheNamePrefix = "as_position_";
        // as_p_position_list_{页码}
        private static final String List_CacheNamePrefix = "as_position_list_";
        public static final String ListAll_CacheName = "as_position_listAll";
        public static final String ListLikeName_CacheName = "as_position_listLikeName";
        private static final String ListByUniversityID_CacheNamePrefix = "as_position_listByUniversityID_";
    }

    /**
     * 新增岗位信息
     * @param position
     * @return
     */
    @ApiOperation(value="新增岗位信息", notes = "已测试")
    @ApiImplicitParam(name= "position",value = "岗位信息实体类", required = true, dataType = "Position")
    @PostMapping("position")
    @ResponseBody
    public Result create(@RequestBody(required = false) Position position){
        if(position != null){
            boolean success = positionService.insert(position);
            if(success){
                cache.deleteByPaterm(CacheNameHelper.List_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListAll_CacheName);
                cache.deleteByPaterm(CacheNameHelper.ListByUniversityID_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.Receive_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListLikeName_CacheName + "*");
                return Result.build(ResultType.Success);
            }else{
                return Result.build(ResultType.Failed);
            }
        }
        return Result.build(ResultType.ParamError);
    }

    /**
     * 删除岗位信息
     * @param id
     * @return
     */
    @ApiOperation(value = "根据岗位信息id删除岗位信息", notes = "已测试")
    @ApiImplicitParam(name = "id", value = "岗位信息id", required = true, dataType = "bigint", paramType = "path")
    @DeleteMapping("position/{id}")
    @ResponseBody
    public Result destroy(@PathVariable long id){
        boolean success = positionService.delete(id);
        if(success){
            cache.deleteByPaterm(CacheNameHelper.List_CacheNamePrefix + "*");
            cache.deleteByPaterm(CacheNameHelper.ListAll_CacheName);
            cache.deleteByPaterm(CacheNameHelper.ListByUniversityID_CacheNamePrefix + "*");
            cache.deleteByPaterm(CacheNameHelper.Receive_CacheNamePrefix + "*");
            cache.deleteByPaterm(CacheNameHelper.ListLikeName_CacheName + "*");
            return Result.build(ResultType.Success);
        }else{
            return Result.build(ResultType.Failed);
        }
    }

    /**
     * 修改岗位信息
     * @param position
     * @return
     */
    @ApiOperation(value="根据岗位信息id修改岗位信息", notes = "已测试")
    @ApiImplicitParam(name="position", value = "岗位信息实体类", required = true, dataType = "Position")
    @PutMapping("position")
    @ResponseBody
    public Result update(@RequestBody(required = false) Position position){
        if(position != null && position.getId() != null){
            boolean success = positionService.update(position);
            if(success){
                cache.deleteByPaterm(CacheNameHelper.List_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListAll_CacheName);
                cache.deleteByPaterm(CacheNameHelper.ListByUniversityID_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.Receive_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListLikeName_CacheName + "*");
                return Result.build(ResultType.Success);
            }else{
                return Result.build(ResultType.Failed);
            }
        }
        return Result.build(ResultType.ParamError);
    }

    /**
     * 获取岗位信息详情
     * @param id
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "根据岗位信息id获取岗位信息详情", notes = "已测试")
    @ApiImplicitParam(name = "id", value = "岗位信息id", required = true, dataType = "bigint", paramType = "path")
    @GetMapping("position/{id}")
    public void receive(@PathVariable long id,
                        HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.Receive_CacheNamePrefix + id;
        String json = cache.get(cacheName);
        if(json == null){
            Position position= positionService.select(id);
            json = Result.build(ResultType.Success).appendData("position", position).convertIntoJSON();
            if(position != null){
                cache.set(cacheName, json);
            }
        }
        response.getWriter().write(json);
    }

    /**
     * 根据岗位名称模糊查询获取岗位详情
     * @param name
     * @param response
     * @throws IOException
     */
    @ApiOperation(value="根据岗位名称模糊查询获取岗位详情", notes="已测试")
    @ApiImplicitParam(name = "name", value = "岗位名称", required = false, dataType = "String", paramType = "path")
    @GetMapping("/position/listLikeName/{name}")
    public void receive(@PathVariable String name, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.ListLikeName_CacheName + name;
        String json = cache.get(cacheName);
        if(json == null){
            json = Result.build(ResultType.Success).appendData("position",  positionService.selectLikeName(name)).convertIntoJSON();
            cache.set(cacheName, json);
        }
        response.getWriter().write(json);
    }

    /**
     * 根据页码分页查询所有岗位信息
     * @param pageNum
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "根据页码分页查询所有岗位信息", notes = "已测试")
    @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "Integer", paramType = "path")
    @GetMapping(value = "/positions/list/{pageNum}")
    public void list(
            @PathVariable Integer pageNum ,
            HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.List_CacheNamePrefix + pageNum;
        String json = cache.get(cacheName);
        if(json == null){
            PageInfo<Position> pageInfo = positionService.selectPage(pageNum);
            json = Result.build(ResultType.Success).appendData("pageInfo", pageInfo).convertIntoJSON();
            if(pageInfo != null) {
                cache.set(cacheName, json);
            }
        }
        response.getWriter().write(json);
    }

    /**
     * 列举所有岗位信息
     * @param response
     * @throws IOException
     */
    @ApiOperation(value="列举所有岗位信息", notes="已测试")
    @GetMapping(value = "/positions/listAll")
    public void list(HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.ListAll_CacheName;
        String json = cache.get(cacheName);
        if(json == null){
            json = Result.build(ResultType.Success).appendData("positions", positionService.selectAll()).convertIntoJSON();
            cache.set(cacheName, json);
        }
        response.getWriter().write(json);
    }

    /**
     * 根据学校id和页码来分页查询岗位信息
     * @param university_id
     * @param pageNum
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "根据学校id和页码来分页查询岗位信息", notes = "已测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name="university_id", value = "学校id", required = true, dataType = "bigint", paramType = "path"),
            @ApiImplicitParam(name="pageNum", value = "页码", required = true, dataType = "Integer", paramType = "path")
    })
    @GetMapping(value = "/positions/listByUniversityID/{university_id}/{pageNum}")
    public void listByUniversityID(@PathVariable long university_id,
                                   @PathVariable Integer pageNum,
                                   HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.ListByUniversityID_CacheNamePrefix + university_id + "_" + pageNum;
        String json = cache.get(cacheName);
        if(json == null){
            PageInfo<Position> pageInfo = positionService.selectPageByUniversity(pageNum, university_id);
            json = Result.build(ResultType.Success).appendData("pageInfo", pageInfo).convertIntoJSON();
            if(pageInfo != null) {
                cache.set(cacheName, json);
            }
        }
        response.getWriter().write(json);
    }

    /**
     * excel导出所有岗位信息
     * @param response
     * @throws IOException
     */
    @ApiOperation(value="excel导出所有岗位信息", notes="已测试/默认路径为E:\\")
    @GetMapping(value = "/positions/excelOut")
    public void excelOut(HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String cacheName = CacheNameHelper.ListAll_CacheName;
        String json = cache.get(cacheName);
        //文件输出位置
        OutputStream out = null;
        try {
            out = new FileOutputStream("E:\\position.xlsx");
            ExcelWriter writer = EasyExcelFactory.getWriter(out);
            // 写仅有一个 Sheet 的 Excel 文件
            Sheet sheet1 = new Sheet(1, 0, Position.class);
            // 第一个 sheet 名称
            sheet1.setSheetName("sheet_1");
//            if (json == null) {
//                json = Result.build(ResultType.Success).appendData("universitys", universityService.selectAll()).convertIntoJSON();
//                cache.set(cacheName, json);
//            }
            writer.write(positionService.selectAll(), sheet1);
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

    /**
     * excel导入
     * @param file
     * @param request
     * @return
     * @throws IOException
     */
    @ApiOperation(value="excel导入", notes="已测试")
    @RequestMapping(value = "/positions/import", method = RequestMethod.POST)
    public Result importposition(MultipartFile file, HttpServletRequest request) throws IOException {
        //获取登录人id、角色
//        User user = authService.getUser();
//        if(user == null) return Result.build(Failed);
//        String loginId = user.getId().toString();
        //根据身份判断是否有能申请的权限
        if(file != null) {
            String savePath = request.getServletContext().getRealPath("/");
            if(positionService.uploadposition(savePath, file) > 0) {
                cache.deleteByPaterm(CacheNameHelper.List_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListAll_CacheName);
                cache.deleteByPaterm(CacheNameHelper.ListByUniversityID_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.Receive_CacheNamePrefix + "*");
                cache.deleteByPaterm(CacheNameHelper.ListLikeName_CacheName + "*");
                return Result.build(Success);
            }
        }
        return Result.build(Failed);
    }

}
