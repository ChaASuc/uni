//package edu.uni.userBaseInfo1.controller;
//
//import edu.uni.bean.Result;
//import edu.uni.bean.ResultType;
//import edu.uni.professionalcourses.bean.DisciplineCategory;
//import edu.uni.userBaseInfo1.service.MyDisciplineCategoryService;
//import edu.uni.utils.RedisCache;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.File;
//import java.io.IOException;
//import java.time.LocalDateTime;
//
///**
// * @Author laizhouhao
// * @Description 关于学科类别表信息模块的Controller层（Http URL请求）的具体实现方法
// * @Date 16:15 2019/4/29
// **/
////填写description内容可以在测试模块显示相应的文字和模块
//@Api(description = "学科类别表信息模块")
////Controller类（或者说Http）的请求路径
////如果添加了路径，则在需要调用该类的方法时需要在方法请求mapping路径前加上类的mapping路径
//@RequestMapping("json/disciplineCategory")
////标志这个类是一个controller类，用于被Spring扫描然后配置添加和配置相应的bean
//@Controller
//public class MyDisciplineCategoryController {
//    //把Picture的Service接口层的所有方法自动装配到该对象中
//    @Autowired
//    private MyDisciplineCategoryService myDisciplineCategoryService;
//
//    //把缓存工具类RedisCache相应的方法自动装配到该对象
//    @Autowired
//    private RedisCache cache;
//
//    //内部类，专门用来管理每个get方法所对应缓存的名称。
//    static class CacheNameHelper{
//        // ub1_d_disciplineCategory_{学科类别表信息记录id}
//        public static final String Receive_CacheNamePrefix = "ub1_d_disciplineCategory_";
//        // ub1_d_disciplineCategory_listAll
//        public static final String ListAll_CacheName = "ub1_d_disciplineCategory_listAll";
//    }
//
//    /**
//     * Author: laizhouhao 9:55 2019/4/30
//     * @param id
//     * @return response
//     * @apiNote: 根据id获取学科类别表信息
//     */
//    //以下说明为本类中所有方法的注解的解释，仅在本处注释（因为都几乎是一个模版）
//    //@ApiOperation：用于在swagger2页面显示方法的提示信息
//    //@GetMapping：规定方法的请求路径和方法的请求方式（Get方法）
//    //@ApiImplicitParam：用于在swagger2页面测试时用于测试的变量，详细解释可以看Swagger2注解说明
//    //@ResponseBody：指明该方法效果等同于通过response对象输出指定格式的数据（JSON）
//    @ApiOperation( value = "根据id获取学科类别表信息",notes = "2019年5月6日 18:12:21 已通过测试" )
//    @GetMapping("disciplineCategory/{id}")
//    @ApiImplicitParam(name = "id", value = "DisciplineCategory表的一个id", required = false, dataType = "Long" , paramType = "path")
//    @ResponseBody
//    public void receive(@PathVariable Long id, HttpServletResponse response) throws IOException {
//        //设置返回的数据格式
//        response.setContentType("application/json;charset=utf-8");
//        //拼接缓存键名（字符串）
//        String cacheName = CacheNameHelper.Receive_CacheNamePrefix + id;
//        //尝试在缓存中通过键名获取相应的键值
//        //因为在Redis中，数据是以”“” "键-值"对 的形式储存的
//        String json = cache.get(cacheName);
//        //如果在缓存中找不到，那就从数据库里找
//        if(json == null){
//            DisciplineCategory disciplineCategory = myDisciplineCategoryService.selectById(id);
//            //把查询到的结果用Result工具类转换成json格式的字符串
//            json = Result.build(ResultType.Success).appendData("disciplineCategory",disciplineCategory).convertIntoJSON();
//            //如果有查询到数据，就把在数据库查到的数据放到缓存中
//            if(disciplineCategory != null){
//                cache.set(cacheName,json);
//            }
//        }
//        //到最后通过response对象返回json格式字符串的数据
//        response.getWriter().write(json);
//
//    }
//
//    /**
//     * Author: laizhouhao 16:26 2019/4/29
//     * @return response
//     * @apiNote: 查询学科类别表信息的所有记录
//     */
//    @ApiOperation( value = "查询学科类别表信息的所有记录",notes = "2019年5月6日 18:12:35 已通过测试" )
//    @GetMapping("disciplineCategorys/listAll")
//    @ResponseBody
//    public void selectAll(HttpServletResponse response)throws Exception{
//        response.setContentType("application/json;charset=utf-8");
//        String cacheName = CacheNameHelper.ListAll_CacheName;
//        String json = cache.get(cacheName);
//        if(json==null){
//            json = Result.build(ResultType.Success).appendData("disciplineCategorys", myDisciplineCategoryService.selectAll()).convertIntoJSON();
//            cache.set(json, cacheName);
//        }
//        response.getWriter().write(json);
//    }
//
//    /**
//     * Author: laizhouhao 16:40 2019/4/29
//     * @param disciplineCategory
//     * @return 新增学科类别表信息信息结果
//     * @apiNote 新增学科类别表信息信息
//     */
//    @ApiOperation(value="新增学科类别表信息信息记录", notes="2019年5月6日 18:13:07 已通过测试")
//    @ApiImplicitParam(name = "disciplineCategory", value = "学科类别表信息实体", required = true, dataType = "DisciplineCategory")
//    @PostMapping("/disciplineCategory")  //post请求方式
//    @ResponseBody
//    public Result create(@RequestBody(required = false) DisciplineCategory disciplineCategory){
//        if(disciplineCategory!=null){
//            boolean success = myDisciplineCategoryService.insert(disciplineCategory);
//            if(success){
//                //清楚缓存
//                cache.delete(CacheNameHelper.ListAll_CacheName);
//                return Result.build(ResultType.Success);
//            }else{
//                return Result.build(ResultType.Failed);
//            }
//        }
//        return Result.build(ResultType.ParamError);
//    }
//
//    /**
//     * Author: laizhouhao 16:47 2019/4/29
//     * @param id
//     * @return 删除学科类别表信息结果
//     * @apiNote 删除学科类别表信息
//     */
//    @ApiOperation(value="删除学科类别表信息", notes="2019年5月6日 18:13:21 已通过测试")
//    @ApiImplicitParam(name = "id", value = "学科类别表信息id", required = true, dataType = "Long", paramType = "path")
//    @DeleteMapping("/disciplineCategory/{id}")   //delete请求
//    @ResponseBody
//    public Result destroy(@PathVariable long id){
//        boolean success = myDisciplineCategoryService.delete(id);
//        if(success){
//            // 清空相关缓存
//            cache.delete(CacheNameHelper.ListAll_CacheName);
//            return Result.build(ResultType.Success);
//        }else{
//            return Result.build(ResultType.Failed);
//        }
//    }
//
//    /**
//     * Author: laizhouhao 11:01 2019/4/30
//     * @param disciplineCategory
//     * @return 更新操作结果
//     * @apiNote 更新学科类别表信息
//     */
//    @ApiOperation(value="更新学科类别表信息信息", notes="2019年5月6日 18:13:35 已通过测试")
//    @ApiImplicitParam(name = "disciplineCategory", value = "学科类别表信息详情实体", required = true, dataType = "DisciplineCategory")
//    @PutMapping("/disciplineCategory")   //Put请求
//    @ResponseBody
//    public Result update(@RequestBody(required = false) DisciplineCategory disciplineCategory){
//        if(disciplineCategory != null && disciplineCategory.getId() != null){
//            boolean success = myDisciplineCategoryService.update(disciplineCategory);
//            if(success){
//                //清除相应的缓存
//                cache.delete(CacheNameHelper.Receive_CacheNamePrefix + disciplineCategory.getId());
//                cache.delete(CacheNameHelper.ListAll_CacheName);
//                return Result.build(ResultType.Success);
//            }else{
//                return Result.build(ResultType.Failed);
//            }
//        }
//        return Result.build(ResultType.ParamError);
//    }
//
//    /**
//     * <p>
//     *     上传文件方法
//     * </p>
//     * @param uploadDir 上传文件目录，如 F:\\file\\ , /home/file/
//     * @param file
//     * @return 文件名
//     * @throws Exception
//     */
//    private String executeUpload(String uploadDir, MultipartFile file) throws Exception{
//        //获取文件后缀名
//        //String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
//        //上传文件名
//        //String filename = CommonUtils.generateUUID() + suffix;
//        String filename = LocalDateTime.now() + "-" + file.getOriginalFilename();
//        //服务端保存的文件对象
//        File serverFile = new File(uploadDir + filename);
//        //将上传的文件写入服务器端文件内
//        file.transferTo(serverFile);
//        return filename;
//    }
//}
