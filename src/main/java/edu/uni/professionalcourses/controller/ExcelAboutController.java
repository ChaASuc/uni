//package edu.uni.professionalcourses.controller;
//
//
//
//import edu.uni.bean.Result;
//import edu.uni.professionalcourses.service.ExcelAboutService;
//import edu.uni.professionalcourses.service.ExcelService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
//import static edu.uni.bean.ResultType.Failed;
//import static edu.uni.bean.ResultType.Success;
//
//@Api
//@RestController
//@RequestMapping("json/excel")
//public class ExcelAboutController {
//
//
//
//    @Autowired
//    private ExcelAboutService excelAboutService;
//
//    @ApiOperation("上传信息")
//    @RequestMapping(value = "/newCourse/import", method = RequestMethod.POST)
//    public Result uploadNewStudentBed(MultipartFile file, HttpServletRequest request) throws IOException {
//        //获取登录人id、角色
////        User user = authService.getUser();
////        if(user == null) return Result.build(Failed);
////        String loginId = user.getId().toString();
//        //根据身份判断是否有能申请的权限
//        if(file != null) {
//            String savePath = request.getServletContext().getRealPath("/");
//            if(excelAboutService.uploadCourse(savePath, file) > 0) {
//                return Result.build(Success);
//            }
//        }
//        return Result.build(Failed);
//    }
//}
