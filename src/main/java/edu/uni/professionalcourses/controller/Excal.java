package edu.uni.professionalcourses.controller;

/**
 * author : 黄永佳
 * create : 2019/6/12 0012 21:51
 * modified :
 * 功能 :
 **/

import edu.uni.bean.Result;
import edu.uni.professionalcourses.service.ExcelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static edu.uni.bean.ResultType.Failed;
import static edu.uni.bean.ResultType.Success;

@Api
@RestController
@RequestMapping("json/excel")
public class Excal {

    @Autowired
    private ExcelService excelService;

    @ApiOperation("上传Department信息")
    @RequestMapping(value = "/newDepartment/import", method = RequestMethod.POST)
    public Result uploadNewStudentBed(MultipartFile file, HttpServletRequest request) throws IOException {
        //获取登录人id、角色
//        User user = authService.getUser();
//        if(user == null) return Result.build(Failed);
//        String loginId = user.getId().toString();
        //根据身份判断是否有能申请的权限
        if(file != null) {
            String savePath = request.getServletContext().getRealPath("/");
            if(excelService.uploadDepartment(savePath, file) > 0) {
                return Result.build(Success);
            }
        }
        return Result.build(Failed);
    }

    @ApiOperation("上传Course_syllabus_description信息")
    @RequestMapping(value = "/newuploadCourseSyllabusDescription/import", method = RequestMethod.POST)
    public Result uploadCourse_syllabus_description(MultipartFile file, HttpServletRequest request) throws IOException {

        if(file != null) {
            String savePath = request.getServletContext().getRealPath("/");
            if(excelService.uploadCourse_syllabus_description(savePath, file) > 0) {
                return Result.build(Success);
            }
        }
        return Result.build(Failed);
    }
    @ApiOperation("上传CourseTeachingSchedule信息")
    @RequestMapping(value = "/newuploadCourseTeachingSchedule/import", method = RequestMethod.POST)
    public Result uploadCourseTeachingSchedule(MultipartFile file, HttpServletRequest request) throws IOException {

        if(file != null) {
            String savePath = request.getServletContext().getRealPath("/");
            if(excelService.uploadCourseTeachingSchedule(savePath, file) > 0) {
                return Result.build(Success);
            }
        }
        return Result.build(Failed);
    }
    @ApiOperation("上传ExperimentSettingContent信息")
    @RequestMapping(value = "/newuploadExperimentSettingContent/import", method = RequestMethod.POST)
    public Result uploadExperimentSettingContent(MultipartFile file, HttpServletRequest request) throws IOException {

        if(file != null) {
            String savePath = request.getServletContext().getRealPath("/");
            if(excelService.uploadExperimentSettingContent(savePath, file) > 0) {
                return Result.build(Success);
            }
        }
        return Result.build(Failed);
    }
    @ApiOperation("上传LearningProcess信息")
    @RequestMapping(value = "/newuploadLearningProcess/import", method = RequestMethod.POST)
    public Result uploadLearningProcess(MultipartFile file, HttpServletRequest request) throws IOException {

        if(file != null) {
            String savePath = request.getServletContext().getRealPath("/");
            if(excelService.uploadLearningProcess(savePath, file) > 0) {
                return Result.build(Success);
            }
        }
        return Result.build(Failed);
    }
    @ApiOperation("上传LearningProcessSemesterAllocation信息")
    @RequestMapping(value = "/newuploadLearningProcessSemesterAllocation/import", method = RequestMethod.POST)
    public Result uploadLearningProcessSemesterAllocation(MultipartFile file, HttpServletRequest request) throws IOException {

        if(file != null) {
            String savePath = request.getServletContext().getRealPath("/");
            if(excelService.uploadLearningProcessSemesterAllocation(savePath, file) > 0) {
                return Result.build(Success);
            }
        }
        return Result.build(Failed);
    }
    @ApiOperation("上传Class信息")
    @RequestMapping(value = "/newuploadClass/import", method = RequestMethod.POST)
    public Result uploadclass(MultipartFile file, HttpServletRequest request) throws IOException {

        if(file != null) {
            String savePath = request.getServletContext().getRealPath("/");
            if(excelService.uploadClass(savePath, file) > 0) {
                return Result.build(Success);
            }
        }
        return Result.build(Failed);
    }

}
