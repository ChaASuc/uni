package edu.uni.grademanagement.controller;

import edu.uni.administrativestructure.bean.University;
import edu.uni.administrativestructure.service.UniversityService;
import edu.uni.bean.Result;
import edu.uni.bean.ResultType;
import edu.uni.educateAffair.bean.Semester;
import edu.uni.educateAffair.service.SemesterService;
import edu.uni.grademanagement.bean.*;
import edu.uni.grademanagement.config.GradeManagementConfig;
import edu.uni.grademanagement.constants.GradeConstant;
import edu.uni.grademanagement.constants.RoleConstant;
import edu.uni.grademanagement.dto.StuGradeItemDetailDto;
import edu.uni.grademanagement.dto.StuGradeItemDto;
import edu.uni.grademanagement.service.*;
import edu.uni.grademanagement.utils.FileNameUtil;
import edu.uni.grademanagement.utils.FileUtil;
import edu.uni.grademanagement.utils.ScoreToPointUtil;
import edu.uni.grademanagement.vo.GmApplyVO;
import edu.uni.professionalcourses.bean.Course;
import edu.uni.professionalcourses.service.CourseService;
import edu.uni.userBaseInfo1.bean.User;
import edu.uni.userBaseInfo1.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author deschen
 * @Create 2019/6/17
 * @Description
 * @Since 1.0.0
 */
@Api(description = "申请模块")
@RestController
@RequestMapping("/json/gradeManagement")
public class ApplyController {

    @Autowired
    private GmApplyService gmApplyService;

    @Autowired
    private GmApplyApprovalService gmApplyApprovalService;

    @Autowired
    private UniversityService universityService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private SemesterService semesterService;

    @Autowired
    private UserService userService;

    @Autowired
    private StuGradeItemDetailService stuGradeItemDetailService;

    @Autowired
    private CourseItemService courseItemService;

    @Autowired
    private StuGradeItemService stuGradeItemService;

    @Autowired
    private StuGradeMainService stuGradeMainService;

    @Autowired
    private ScoreToPointUtil scoreToPointUtil;

    @Autowired
    private GradeManagementConfig config;

    @ApiOperation(value = "申请修改成绩", notes = "除任课教师外角色")
    @PostMapping("/apply")
    public Result createApply(
            @RequestParam Long universityId,
            @RequestParam Long semesterId,
            @RequestParam Long studentId,
            @RequestParam Long courseId,
            @RequestParam Long courseItemId,
            @RequestParam Long courseItemDetailId,
            @RequestParam Long stuItemGradeDetailId,
            @RequestParam Long byWho,
            @RequestParam Long sendWho,
            @RequestParam MultipartFile file,
            @RequestParam String reason) throws IOException {

        // 上传文件
        FileUtil fileUtil = new FileUtil(config.getAbsoluteExcelDir());
        String filePath = fileUtil.uploadFile(file);
        // 保存路径
        GmApply gmApply = new GmApply();
//        BeanUtils.copyProperties(form, gmApply);
        gmApply.setUniversityId(universityId);
        gmApply.setSemesterId(semesterId);
        gmApply.setCourseId(courseId);
        gmApply.setStudentId(studentId);
        gmApply.setCourseItemId(courseItemId);
        gmApply.setCourseItemDetailId(courseItemDetailId);
        gmApply.setStuItemGradeDetailId(stuItemGradeDetailId);
        gmApply.setByWho(byWho);
        gmApply.setSendWho(sendWho);
        gmApply.setReason(reason);
        gmApply.setAttachment(filePath);
        // 存储数据库
        gmApplyService.insertGmApply(gmApply);
        return Result.build(ResultType.Success);
    }

    @ApiOperation(value = "申请修改成绩", notes = "任课教师角色")
    @PostMapping("/apply/tec")
    public Result createApplyTec(
            @RequestParam Long universityId,
            @RequestParam Long semesterId,
            @RequestParam Long studentId,
            @RequestParam Long courseId,
            @RequestParam Long courseItemId,
            @RequestParam Long courseItemDetailId,
            @RequestParam Long stuItemGradeDetailId,
            @RequestParam Double score,
            @RequestParam Long byWho,
            @RequestParam Long sendWho,
            @RequestParam MultipartFile file,
            @RequestParam String reason) throws IOException {

        // 上传文件
        FileUtil fileUtil = new FileUtil(config.getAbsoluteExcelDir());
        String filePath = fileUtil.uploadFile(file);
        // 保存路径
        GmApply gmApply = new GmApply();
//        BeanUtils.copyProperties(form, gmApply);
        gmApply.setUniversityId(universityId);
        gmApply.setSemesterId(semesterId);
        gmApply.setCourseId(courseId);
        gmApply.setStudentId(studentId);
        gmApply.setCourseItemId(courseItemId);
        gmApply.setCourseItemDetailId(courseItemDetailId);
        gmApply.setByWho(byWho);
        gmApply.setSendWho(sendWho);
        gmApply.setReason(reason);
        gmApply.setAttachment(filePath);

        StuItemGradeDetail stuItemGradeDetail =
                stuGradeItemDetailService.selectByStuItemGradeDetailId(stuItemGradeDetailId);

        stuItemGradeDetail.setId(null);
        stuItemGradeDetail.setScore(score);
        stuItemGradeDetail.setCache(GradeConstant.CACHE_VALID);
        stuGradeItemDetailService.insertStuItemGradeDetail(stuItemGradeDetail);

        StuItemGradeDetail stuItemGradeDetail1 = stuGradeItemDetailService.selectByStuItemGradeIdAndCourseItemDetailId(
                stuItemGradeDetail.getStuItemGradeId(), stuItemGradeDetail.getCourseItemDetailId(), stuItemGradeDetail.getCache()
        );

        gmApply.setStuItemGradeDetailId(stuItemGradeDetail1.getId());

        // 存储数据库
        gmApplyService.insertGmApply(gmApply);
        return Result.build(ResultType.Success);
    }

    @ApiOperation(value = "获取用户的申请记录", notes = "除任课教师外角色")
    @GetMapping("/apply/{userId}")
    public void getApplyVO(
            @PathVariable Long userId,
            @RequestParam Long universityId,
            @RequestParam Long semesterId,
            @RequestParam Long courseId,
            HttpServletResponse response
    ) throws IOException {
        getApply(
                userId, semesterId, courseId, response, universityId,
                GradeConstant.APPLY, RoleConstant.EMPLOYEE_EXINCLUDE_TEACHER);
    }

    /**
     *
     * @param userId
     * @param semesterId
     * @param courseId
     * @param response
     * @param universityId
     * @param applyFlag
     * @param flag    //教师申请还是其他角色申请记录
     * @throws IOException
     */
    private void getApply(
            @PathVariable Long userId, @RequestParam Long semesterId,
            @RequestParam Long courseId, HttpServletResponse response,
            @RequestParam Long universityId, Integer applyFlag, Integer flag) throws IOException {
        List<GmApply> gmApplies = gmApplyService.selectGmApplyList(
                userId, universityId, semesterId, courseId, applyFlag);
        User byWho =
                userService.selectUserById(userId);
        String byWhoName = byWho.getUserName();

        String univeersityName = null;
        if (universityId != null) {
            University university =
                    universityService.select(universityId);
            univeersityName = university.getName();
        }

        String semesterName = null;
        if (semesterId != null) {
            Semester semester =
                    semesterService.selectById(semesterId);
            semesterName = semester.getName();
        }

        String courseName = null;
        if (courseId != null) {
            Course course =
                    courseService.select(courseId);
            courseName = course.getName();
        }
        String finalUniveersityName = univeersityName;
        String finalSemesterName = semesterName;
        String finalCourseName = courseName;
        List<GmApplyVO> gmApplyVOS = gmApplies.stream().map(
                gmApply -> {
                    GmApplyVO gmApplyVO = new GmApplyVO();
                    BeanUtils.copyProperties(gmApply, gmApplyVO);
                    String attachment =
                            new FileNameUtil(gmApply.getAttachment()).getFileName();
                    gmApplyVO.setAttachment(attachment);

                    if (finalUniveersityName != null) {
                        gmApplyVO.setUniversityName(finalUniveersityName);
                    } else {
                        University university =
                                universityService.select(gmApply.getUniversityId());
                        gmApplyVO.setUniversityName(university.getName());
                    }

                    if (finalSemesterName != null) {
                        gmApplyVO.setSemesterName(finalSemesterName);
                    } else {
                        Semester semester =
                                semesterService.selectById(gmApply.getSemesterId());
                        gmApplyVO.setSemesterName(semester.getName());
                    }

                    if (finalCourseName != null) {
                        gmApplyVO.setCourseName(finalCourseName);
                    } else {
                        Course course =
                                courseService.select(gmApply.getCourseId());
                        gmApplyVO.setCourseName(course.getName());
                    }

                    User student =
                            userService.selectUserById(gmApply.getStudentId());
                    gmApplyVO.setStudentName(student.getUserName());

                    CourseItem item =
                            courseItemService.selectCouseItem(gmApply.getCourseItemId());
                    gmApplyVO.setCourseItemName(item.getName());

                    CourseItemDetail courseItemDetail = courseItemService.selectCourseItemDetailByCouserItemDetailId(
                            gmApply.getCourseItemDetailId());
                    gmApplyVO.setCourseItemDetailName(courseItemDetail.getContent());

                    StuItemGradeDetail stuItemGradeDetail =
                            stuGradeItemDetailService.selectByStuItemGradeDetailId(gmApply.getStuItemGradeDetailId());
                    if (flag.equals(RoleConstant.TEACHER)) {
                        StuItemGradeDetail stuItemGradeDetail1 = stuGradeItemDetailService.selectByStuItemGradeIdAndCourseItemDetailId(
                                stuItemGradeDetail.getStuItemGradeId(), stuItemGradeDetail.getCourseItemDetailId(),
                                GradeConstant.CACHE_INVALID
                        );
                        gmApplyVO.setOldScore(stuItemGradeDetail1.getScore());
                        gmApplyVO.setNewScore(stuItemGradeDetail.getScore());
                    } else {
                        gmApplyVO.setOldScore(stuItemGradeDetail.getScore());
                    }

                    gmApplyVO.setByWhoName(byWhoName);

                    User sendWho =
                            userService.selectUserById(gmApply.getSendWho());
                    gmApplyVO.setSendWhoName(sendWho.getUserName());

                    GmApplyApproval gmApplyApproval =
                            gmApplyApprovalService.selectGmApplyApproval(gmApply.getId());

                    gmApplyVO.setGmApplyApprovalId(gmApplyApproval.getId());
                    gmApplyVO.setApplicationStatus(gmApplyApproval.getApplicationStatus());
                    gmApplyVO.setReply(gmApplyApproval.getReason());

                    return gmApplyVO;
                }
        ).collect(Collectors.toList());

        response.setContentType("application/json;charset=utf-8");
        String json = Result.build(ResultType.Success).appendData("gmApplyVOS", gmApplyVOS).convertIntoJSON();
        response.getWriter().write(json);
    }


    @ApiOperation(value = "获取用户的申请记录", notes = "任课教师")
    @GetMapping("/apply/tec/{userId}")
    public void getApplyApprovalTecVO(
            @PathVariable Long userId,
            @RequestParam Long universityId,
            @RequestParam Long semesterId,
            @RequestParam Long courseId,
            HttpServletResponse response
    ) throws IOException {
        getApply(
                userId,  semesterId, courseId, response, universityId, GradeConstant.APPLY, RoleConstant.TEACHER
        );
    }

    @ApiOperation(value = "审核记录", notes = "任课教师")
    @GetMapping("/approval/tec/{userId}")
    public void getApplyTecVO(
            @PathVariable Long userId,
            @RequestParam Long universityId,
            @RequestParam Long semesterId,
            @RequestParam Long courseId,
            HttpServletResponse response
    ) throws IOException {
        getApply(
                userId, semesterId, courseId, response, universityId,
                GradeConstant.APPROVAL, RoleConstant.EMPLOYEE_EXINCLUDE_TEACHER
        );
    }

    @ApiOperation(value = "审核记录", notes = "班主任")
    @GetMapping("/approval/admin/{userId}")
    public void getApplyAdminVO(
            @PathVariable Long userId,
            @RequestParam Long universityId,
            @RequestParam Long semesterId,
            @RequestParam Long courseId,
            HttpServletResponse response
    ) throws IOException {
        getApply(
                userId, semesterId, courseId, response, universityId,
                GradeConstant.APPROVAL, RoleConstant.TEACHER
        );
    }

    @ApiOperation(value = "回复审核", notes = "任课教师")
    @PutMapping("/approval/tec")
    public Result updateApprovalTecVO(
            @RequestParam Long gmApplyApprovalId,
            @RequestParam Integer applicationStatus,
            @RequestParam String reply
    ) throws IOException {

        GmApplyApproval gmApplyApproval = new GmApplyApproval();
        gmApplyApproval.setId(gmApplyApprovalId);
        gmApplyApproval.setApplicationStatus(applicationStatus);
        gmApplyApproval.setReason(reply);

        gmApplyApprovalService.updateGmApplyApproval(gmApplyApproval);

        return Result.build(ResultType.Success);
    }

    @ApiOperation(value = "回复审核", notes = "班主任")
    @PutMapping("/approval/admin")
    public Result updateApprovalAdminVO(
            @RequestParam Long gmApplyApprovalId,
            @RequestParam Long stuItemGradeDetailId,
            @RequestParam Integer applicationStatus,
            @RequestParam String reply
    ) throws IOException {

        updateApprovalTecVO(gmApplyApprovalId,
                applicationStatus, reply);

        // 获取新的成绩明细项
        StuItemGradeDetail stuItemGradeDetail =
                stuGradeItemDetailService.selectByStuItemGradeDetailId(stuItemGradeDetailId);
        stuItemGradeDetail.setCache(GradeConstant.CACHE_INVALID);

        // 获取旧的成绩明细项
        StuItemGradeDetail stuItemGradeDetail1 = stuGradeItemDetailService.selectByStuItemGradeIdAndCourseItemDetailId(
                stuItemGradeDetail.getStuItemGradeId(), stuItemGradeDetail.getCourseItemDetailId(),
                GradeConstant.CACHE_INVALID
        );

        // 更新新的成绩明细
        stuGradeItemDetailService.updateStuItemGradeDetail(stuItemGradeDetail);


        // 删除旧的成绩明细
        stuItemGradeDetail1.setDeleted(GradeConstant.RECORD_INVALID);
        stuGradeItemDetailService.updateStuItemGradeDetail(stuItemGradeDetail1);


        StuItemGrade stuItemGrade =
                stuGradeItemService.selectByStuItemGradeId(stuItemGradeDetail1.getStuItemGradeId());
        // 更新成绩项成绩
        List<StuItemGradeDetail> stuGradeItemDetailList = stuGradeItemDetailService.selectStuItemGradeDetailByStuItemGradeId(
                stuItemGrade.getId(), RoleConstant.TEACHER
        );

        CourseItem item =
                courseItemService.selectCouseItem(stuItemGrade.getCourseItemId());
        Double stuItemGradeScore = stuGradeItemDetailList.stream()
                .collect(Collectors.averagingDouble(StuItemGradeDetail::getScore));
        stuItemGrade.setScore(stuItemGradeScore*item.getRate());
        stuGradeItemService.updateStuItemGrade(stuItemGrade);

        // 更新成绩主表
        StuGradeMain stuGradeMain =
                stuGradeMainService.selectByStuGradeMainId(stuItemGrade.getStuGradeMainId());

        List<StuGradeItemDto> stuGradeItemDtos =
                stuGradeItemService.selectByStuGradeMainId(stuItemGrade.getStuGradeMainId(), RoleConstant.TEACHER);
        Double stuGradeMainScore = stuGradeItemDtos.stream()
                .collect(Collectors.summingDouble(StuGradeItemDto::getScore));
        stuGradeMain.setScore(stuGradeMainScore);
        stuGradeMain.setPoint(scoreToPointUtil.getPoint(stuGradeMainScore));
        stuGradeMainService.updateStuGradeMain(stuGradeMain);
        // 更新成绩
        return Result.build(ResultType.Success);
    }

}
