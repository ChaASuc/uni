package edu.uni.grademanagement.controller;

import edu.uni.bean.Result;
import edu.uni.bean.ResultType;
import edu.uni.grademanagement.bean.CourseItem;

import edu.uni.grademanagement.bean.StuGradeMain;
import edu.uni.grademanagement.bean.StuItemGrade;
import edu.uni.grademanagement.bean.StuItemGradeDetail;
import edu.uni.grademanagement.constants.GradeConstant;
import edu.uni.grademanagement.constants.RoleConstant;
import edu.uni.grademanagement.dto.StuGradeItemDetailDto;
import edu.uni.grademanagement.dto.StuGradeItemDto;
import edu.uni.grademanagement.form.ExcelForm;
import edu.uni.grademanagement.form.InsertStuItmeGradeDtoByExcelForm;
import edu.uni.grademanagement.mapper.StuItemGradeDetailMapper;
import edu.uni.grademanagement.service.*;
import edu.uni.grademanagement.utils.ScoreToPointUtil;
import edu.uni.userBaseInfo1.bean.Student;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 陈汉槟
 * @Create 2019/4/28
 * @Description 成绩明细控制层（所有角色接口）
 * @Since 1.0.0
 */
@Api(value = "StuGradeDetailController", tags = {"成绩明细控制层"})
@RestController
@RequestMapping("/json/gradeManagement")
@Slf4j
public class StuGradeDetailController {

    @Autowired
    private StuGradeItemDetailService stuGradeItemDetailService;

    @Autowired
    private StuGradeItemService stuGradeItemService;

    @Autowired
    private StuGradeMainService stuGradeMainService;

    @Autowired
    private CourseItemService courseItemService;

    @Autowired
    private ScoreToPointUtil scoreToPointUtil;

    @Autowired
    private StudentForGradeService studentService;



    /**
     *
     * @Param [studentMainId, response]
     * @Return:void
     * @Author: 陈汉槟
     * @Date: 2019/5/12 1:10
     * @Description: 根据主表id获取成绩明细表
     * @Modify: 2019/5/19 修改为根据成绩项id获取成绩作业项
     */
    @ApiOperation(value = "根据成绩项id获取成绩明细表", tags = {"获取有效信息", "除教师外所有角色"})
    @GetMapping("/gradeDetail/list/{stuItemGradeId}")
    public void getListByStuItemGradeId(
            @ApiParam(value = "成绩项id", required = true)
            @PathVariable(name = "stuItemGradeId", required = true) Long stuItemGradeId,
            HttpServletResponse response
    ) throws IOException {
        response.setContentType("application/json;charset=utf-8");

            List<StuGradeItemDetailDto> stuGradeItemDetailDtos =
                    stuGradeItemDetailService.selectByStuItemGradeId(stuItemGradeId, null);
            /* todo 优化，逻辑判断，根据成绩项id一定能查到成绩作业项
            if (CollectionUtils.isEmpty(stuGradeItemDetailDtos)) {
                String json =
                        Result.build(ResultType.Failed).
                                appendData("stuGradeItemDetailDtos", null)
                                .convertIntoJSON();
                response.getWriter().write(json);
            }*/

        // todo 缓存根据学校id，教师id，课程id，成绩主表id
        String json = Result.build(ResultType.Success).appendData("stuGradeItemDetailDtos", stuGradeItemDetailDtos).convertIntoJSON();
        response.getWriter().write(json);
    }

    /**
     *
     * @Param [studentMainId, response]
     * @Return:void
     * @Author: 陈汉槟
     * @Date: 2019/5/12 1:10
     * @Description: 根据主表id获取成绩明细表
     * @Modify: 2019/5/19 修改为根据成绩项id获取成绩作业项
     */
    @ApiOperation(value = "根据成绩项id获取成绩明细表", tags = {"获取有效信息", "除教师外所有角色"})
    @GetMapping("/gradeDetail/tec/list/{stuItemGradeId}")
    public void getTecListByStuItemGradeId(
            @ApiParam(value = "成绩项id", required = true)
            @PathVariable(name = "stuItemGradeId", required = true) Long stuItemGradeId,
            HttpServletResponse response
    ) throws IOException {
        response.setContentType("application/json;charset=utf-8");

        List<StuGradeItemDetailDto> stuGradeItemDetailDtos =
                stuGradeItemDetailService.selectByStuItemGradeId(stuItemGradeId, RoleConstant.TEACHER);
            /* todo 优化，逻辑判断，根据成绩项id一定能查到成绩作业项
            if (CollectionUtils.isEmpty(stuGradeItemDetailDtos)) {
                String json =
                        Result.build(ResultType.Failed).
                                appendData("stuGradeItemDetailDtos", null)
                                .convertIntoJSON();
                response.getWriter().write(json);
            }*/

        // todo 缓存根据学校id，教师id，课程id，成绩主表id
        String json = Result.build(ResultType.Success).appendData("stuGradeItemDetailDtos", stuGradeItemDetailDtos).convertIntoJSON();
        response.getWriter().write(json);
    }


    @ApiOperation(value = "根据更新成绩明细表", tags = {"教师角色"})
    @PutMapping("/gradeDetail/tec}")
    public Result updateStuGradeItemDetailDto(
            @ApiParam(value = "成绩明细集合", required = true)
            @RequestBody List<StuItemGradeDetail> stuItemGradeDetails
    ) {

        if (CollectionUtils.isEmpty(stuItemGradeDetails)) {
            return Result.build(ResultType.ParamError);
        }

        Long stuItemGradeId = stuItemGradeDetails.get(0).getStuItemGradeId();
        StuItemGrade stuItemGrade =
                stuGradeItemService.selectByStuItemGradeId(stuItemGradeId);
        if (stuItemGrade.getCache() == GradeConstant.CACHE_INVALID) {
            //todo 后续优化，报错
            return Result.build(ResultType.ParamError);
        }

        Long stuGradeMainId = stuItemGrade.getStuGradeMainId();
        StuGradeMain stuGradeMain =
                stuGradeMainService.selectByStuGradeMainId(stuGradeMainId);
        if (stuGradeMain.getCache() == GradeConstant.CACHE_INVALID) {
            //todo 后续优化，报错
            return Result.build(ResultType.ParamError);
        }
        // 不是缓存状态不能修改作业项成绩
        for (StuItemGradeDetail detail :
                stuItemGradeDetails) {
            StuItemGradeDetail stuItemGradeDetail1 =
                    stuGradeItemDetailService.selectByStuItemGradeDetailId(detail.getId());
            if (stuItemGradeDetail1.getCache() == GradeConstant.CACHE_INVALID) {
                //todo 后续优化，报错
                return Result.build(ResultType.ParamError);
            }
        }
        boolean b =
                stuGradeItemDetailService.updateStuItemGradeDetails(stuItemGradeDetails);
        if (!b) {
            return Result.build(ResultType.Failed);
        }

        // 更新成绩项成绩
        List<StuItemGradeDetail> stuGradeItemDetailList = stuGradeItemDetailService.selectStuItemGradeDetailByStuItemGradeId(
                stuItemGradeId, RoleConstant.TEACHER
        );

        Double stuItemGradeScore = stuGradeItemDetailList.stream()
                .collect(Collectors.averagingDouble(StuItemGradeDetail::getScore));
        stuItemGrade.setScore(stuItemGradeScore);
        boolean b1 = stuGradeItemService.updateStuItemGrade(stuItemGrade);
        if (!b1) {
            return Result.build(ResultType.Failed);
        }

        // 更新成绩主表
        List<StuGradeItemDto> stuGradeItemDtos =
                stuGradeItemService.selectByStuGradeMainId(stuGradeMainId, RoleConstant.TEACHER);
        Double stuGradeMainScore = stuGradeItemDtos.stream()
                .collect(Collectors.averagingDouble(StuGradeItemDto::getScore));
        stuItemGrade.setScore(stuItemGradeScore);
        stuGradeMain.setPoint(scoreToPointUtil.getPoint(stuGradeMainScore));
        boolean b2 = stuGradeMainService.updateStuGradeMain(stuGradeMain);
        if (!b2) {
            return Result.build(ResultType.Failed);
        }
        return Result.build(ResultType.Success);
    }




    /**
     *
     * @Param [form]
     * @Return:edu.uni.bean.Result
     * @Author: 陈汉槟
     * @Date: 2019/5/14 18:31
     * @Description:  录入成绩明细
     * @Modify: 2019/5/19
     */
    @ApiOperation(value = "录入成绩明细", tags = {"教师角色"})
    @PostMapping("/gradeDetail")
    public Result insertStuItmeGradeDtoByExcel(
            @ApiParam(value = "excel成绩录入", required = true)
            @RequestBody InsertStuItmeGradeDtoByExcelForm form
    ) {
        if (form == null) {
            return Result.build(ResultType.ParamError);
        }
        List<Long> stuGradeMainIds = form.getStuGradeMainIds();
        if (CollectionUtils.isEmpty(stuGradeMainIds)) {
            return Result.build(ResultType.ParamError);
        }
        List<StuGradeMain> stuGradeMains = stuGradeMainService.selectStuGradeMainIds(stuGradeMainIds);

        // 不是缓存状态录入会报错
        for (StuGradeMain stuGradeMain :
                stuGradeMains) {
            if (stuGradeMain.getCache() != GradeConstant.CACHE_VALID) {
                return Result.build(ResultType.Failed);
            }

        }


        Long universityId = stuGradeMains.get(0).getUniversityId();
        Long teacherId = stuGradeMains.get(0).getByWho();
        Long courseItemId = form.getCourseItemId();
        Long courseItemDetailId = form.getCourseItemDetailId();
        List<ExcelForm> excelForms = form.getExcelForms();

        // 遍历成绩主表
        for (StuGradeMain stuGradeMain :
                stuGradeMains) {
            Long stuGradeMainId = stuGradeMain.getId();
            // 判断成绩项是否已创建
            StuItemGrade stuItemGrade = stuGradeItemService.selectStuItemGradeByStuGradeMainId(
                    stuGradeMainId, courseItemId
            );
            if (stuItemGrade == null) {
                // 根据学校id，课程成绩项id，教师id，学生主表id创建成绩项
                boolean successStuItemGrade = stuGradeItemService.insertStuGradeItemByExcelContent(
                        universityId, courseItemId, teacherId, stuGradeMainId
                );

                if (!successStuItemGrade) {
                    return Result.build(ResultType.Failed);
                }
                stuItemGrade = stuGradeItemService.selectStuItemGradeByStuGradeMainId(
                        stuGradeMainId, courseItemId
                );
            }

            // 文档内容对应
            for (ExcelForm excelForm :
                    excelForms) {
                String stuNo = excelForm.getStuNo().trim();
                Student student = studentService.selectStudentByStuNo(stuNo);
                if (stuGradeMain.getStudentId().equals(student.getUserId())) {
                    Long stuItemGradeId = stuItemGrade.getId();
                    Double score = excelForm.getScore();
                    String note = excelForm.getNote();
                    // 判断是否存在，用于更新成绩作业项
                    StuItemGradeDetail stuItemGradeDetail = stuGradeItemDetailService.selectByStuItemGradeIdAndCourseItemDetailId(
                            stuItemGradeId, courseItemDetailId
                    );
                    if (stuItemGradeDetail != null) {
                        stuItemGradeDetail.setScore(score);
                        stuItemGradeDetail.setNote(note);
                        // 更新成绩作业项
                        List<StuItemGradeDetail> stuItemGradeDetails =
                                new ArrayList<>();
                        stuItemGradeDetails.add(stuItemGradeDetail);
                        boolean b = stuGradeItemDetailService.updateStuItemGradeDetails(
                                stuItemGradeDetails
                        );
                        if (!b) {
                            // 更新失败 报错
                            return Result.build(ResultType.Failed);
                        }
                    } else {
                        boolean b =
                                stuGradeItemDetailService.insertStuGradeItemByExcelContent(
                                        stuItemGradeId, courseItemDetailId, universityId, teacherId,
                                        score, note
                                );
                        if (!b) {
                            return Result.build(ResultType.Failed);
                        }
                    }
                    break;
                } else {
                    return Result.build(ResultType.Failed);
                }

            }
            // 更新成绩项
            List<StuItemGradeDetail> stuItemGradeDetails =
                    stuGradeItemDetailService.selectStuItemGradeDetailByStuItemGradeId(stuItemGrade.getId(),
                            RoleConstant.TEACHER);
            Double stuItemGradeScore = stuItemGradeDetails.stream()
                    .collect(Collectors.averagingDouble(StuItemGradeDetail::getScore));
            CourseItem courseItem =
                    courseItemService.selectCouseItem(courseItemId);
            // 获取课程成绩项
            stuItemGradeScore = stuItemGradeScore*courseItem.getRate();
            stuItemGrade.setScore(stuItemGradeScore);
            boolean b = stuGradeItemService.updateStuItemGrade(
                    stuItemGrade
            );
            if (!b) {
                return Result.build(ResultType.Failed);
            }

            // 更新成绩主表
            List<StuGradeItemDto> stuGradeItemDtos
                    = stuGradeItemService.selectByStuGradeMainId(stuGradeMainId, RoleConstant.TEACHER);
            Double stuGradeMainScore = stuGradeItemDtos.stream().map(
                    StuGradeItemDto::getScore
            ).reduce(Double::sum).get();
            stuGradeMain.setScore(stuGradeMainScore);
            stuGradeMain.setPoint(scoreToPointUtil.getPoint(stuGradeMainScore));
            stuGradeMain.setDeleted(GradeConstant.RECORD_CACHE);
            stuGradeMain.setState(GradeConstant.MAIN_STATE_SCORING);
            boolean c = stuGradeMainService.updateStuGradeMain(stuGradeMain);
            if (!c) {
                return Result.build(ResultType.Failed);
            }
        }
        return Result.build(ResultType.Success);
    }



}
