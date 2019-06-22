package edu.uni.educateAffair.conroller;

import com.alibaba.druid.support.json.JSONParser;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.util.JSONPObject;
import edu.uni.bean.Result;
import edu.uni.bean.ResultType;
import edu.uni.educateAffair.bean.Canlendar;
import edu.uni.educateAffair.bean.Semester;
import edu.uni.educateAffair.service.CanlendarService;
import edu.uni.educateAffair.service.SemesterService;
import edu.uni.utils.JsonUtils;
import edu.uni.utils.RedisCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author:梁俊杰
 * @Description:学期表
 * @Date:Created in 8:28 2019/4/29
 */

@Api(description = "学期表模块")
@Controller
@RequestMapping("/json/educateAffair")
public class SemesterController {
    @Autowired
    private SemesterService semesterService;
    @Autowired
    private CanlendarService canlendarService;
    @Autowired
    private RedisCache cache;


    /**
        *@Author:梁俊杰
        *@Description:查找所有学期信息
        *@Date:Created in {14:35} {2019/4/30}
    */
    @ApiOperation(value = "查找所有学期信息" , notes = "")
    @GetMapping(value = "/semester/listAll")
    public void selectAll(HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Calendar.getInstance().getTime());
//        String cacheName = CacheNameHelper.ListAll_CacheName;
//        String json = cache.get(cacheName);
        String json = null;
        Long sid = null;
        List<Semester> semesterList = semesterService.selectAll();
        for (Semester semester : semesterList){
            if(calendar.getTime().after(semester.getStart()) && calendar.getTime().before(semester.getEnd())){
                sid = semester.getId();
            }
        }
        if(json == null){
            json = Result.build(ResultType.Success).appendData("semesters",semesterList).appendData("NowSemester",sid).convertIntoJSON();
//            cache.set(cacheName,json);
        }

        response.getWriter().write(json);
    }
/**
    *@Author:梁俊杰
    *@Description:按ID查找学期信息
    *@Date:Created in {14:48} {2019/4/30}
*/
    @ApiOperation(value = "按ID查找学期信息" , notes = "")
    @ApiImplicitParam(name = "id" , value = "学期ID" , required = true ,dataType = "Long")
    @GetMapping("/semester/id={id}")
    public void selectSemesterById(@PathVariable(value = "id") Long id , HttpServletResponse response) throws IOException{
        response.setContentType("application/json;charset=utf-8");
/*        String cacheName = CacheNameHelper.ListBySid_CacheNamePrefix + id;
        String json = cache.get(cacheName);*/
            String json = Result.build(ResultType.Success).appendData("semester",semesterService.selectById(id)).appendData("canlendar",canlendarService.selectBySemesterId(id)).convertIntoJSON();
            //cache.set(cacheName,json);
        response.getWriter().write(json);
    }

    /**
        *@Author:梁俊杰
        *@Description:返回当前时间所属学期
        *@Date:Created in {13:02} {2019/5/16}
    */
    @ApiOperation(value = "按ID删除学期信息" , notes = "")
    @GetMapping("/semester/getNowSemester")
    public void selectNowSemester(HttpServletResponse response) throws Exception{
        response.setContentType("application/json;charset=utf-8");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Calendar.getInstance().getTime());
        System.out.println(calendar.getTime());
        List<Semester> semesterList = semesterService.selectAll();
        Long sid = null;
        for (Semester semester : semesterList){
            if(calendar.getTime().after(semester.getStart()) && calendar.getTime().before(semester.getEnd())){
                sid = semester.getId();
            }
        }
        String json = Result.build(ResultType.Success).appendData("sid",sid).convertIntoJSON();
        response.getWriter().write(json);
    }



}
