package edu.uni.grademanagement.controller;

import com.alibaba.excel.EasyExcelFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import edu.uni.administrativestructure.bean.Class;
import edu.uni.bean.Result;
import edu.uni.bean.ResultType;
import edu.uni.educateAffair.bean.Semester;
import edu.uni.educateAffair.service.SemesterService;

import edu.uni.grademanagement.bean.CourseItem;

import edu.uni.grademanagement.bean.StuGradeMain;
import edu.uni.grademanagement.bean.StuItemGrade;
import edu.uni.grademanagement.bean.StuItemGradeDetail;
import edu.uni.grademanagement.bean.model.GradeReadModel;
import edu.uni.grademanagement.config.GradeManagementConfig;
import edu.uni.grademanagement.constants.GradeConstant;
import edu.uni.grademanagement.constants.RoleConstant;
import edu.uni.grademanagement.dto.StuGradeItemDetailDto;
import edu.uni.grademanagement.dto.StuGradeItemDto;
import edu.uni.grademanagement.form.*;
import edu.uni.grademanagement.service.*;
import edu.uni.grademanagement.utils.FileNameUtil;
import edu.uni.grademanagement.utils.ScoreToPointUtil;
import edu.uni.grademanagement.vo.StuGradeDetailVO;
import edu.uni.professionalcourses.bean.Course;
import edu.uni.userBaseInfo1.bean.Student;
import edu.uni.userBaseInfo1.bean.User;
import edu.uni.userBaseInfo1.service.StudentService;
import edu.uni.userBaseInfo1.service.UserService;
import edu.uni.grademanagement.utils.FileUtil;
import edu.uni.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 陈汉槟
 * @Create 2019/4/28
 * @Description 成绩明细控制层（所有角色接口）
 * @Since 1.0.0
 */
@Api(description = "成绩明细控制层")
@RestController
@RequestMapping("/json/gradeManagement/gradeDetail")
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
    private StudentForGradeService studentForGradeService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseDtoService courseDtoService;

    @Autowired
    private AdministrativestructureService administrativestructureService;

    @Autowired
    private SemesterService semesterService;

    @Autowired
    private CourseForGradeService courseForGradeService;

    @Autowired
    private GradeManagementConfig config;


    /**
     *
     * @Param [studentMainId, response]
     * @Return:void
     * @Author: 陈汉槟
     * @Date: 2019/5/12 1:10
     * @Description: 根据主表id获取成绩明细表
     * @Modify: 2019/5/19 修改为根据成绩项id获取成绩作业项
     */
    @ApiOperation(value = "根据成绩项id获取成绩明细表", notes = "非任课教师、班主任等角色")
    @GetMapping("/list/{stuItemGradeId}")
    public void getGradeDetailListByStuItemGradeId(
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
    @ApiOperation(value = "根据成绩项id获取成绩明细表", notes = "除教师外所有角色")
    @GetMapping("/tec/list/{stuItemGradeId}")
    public void getGradeDetailTecListByStuItemGradeId(
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

    @ApiOperation(value = "根据成绩主表id集合和课程成绩项目id获取成绩作业表", notes="任课教师外角色")
    @GetMapping("/tec/list")
    public void getGradeDetailTecList(
//            @ApiParam(value = "成绩项id", required = true)
            @RequestParam Long universityId,
            @RequestParam Long semesterId,
            @RequestParam Long courseId,
            @RequestParam Long classId,
            @RequestParam Long teacherId,
            @RequestParam Long courseItemId,
            HttpServletResponse response
    ) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        Integer flag = RoleConstant.TEACHER;
        String json = getJson(
                universityId, semesterId, courseId,classId,
                teacherId, courseItemId, flag);
        response.getWriter().write(json);
    }

    @ApiOperation(value = "根据成绩主表id集合和课程成绩项目id获取成绩作业表", notes = "非任课教师角色、班主任等")
    @GetMapping("/list")
    public void getGradeDetailList(
            @RequestParam Long universityId,
            @RequestParam Long semesterId,
            @RequestParam Long courseId,
            @RequestParam Long classId,
            @RequestParam Long teacherId,
            @RequestParam Long courseItemId,
            HttpServletResponse response
    ) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        Integer flag = RoleConstant.EMPLOYEE_EXINCLUDE_TEACHER;
        String json = getJson(
                universityId, semesterId, courseId,classId,
                teacherId, courseItemId, flag);
        response.getWriter().write(json);
    }

   /* private String getJson(GradeDetailTecForm form, Integer flag) throws JsonProcessingException {
        List<Long> stuGradeMainIds = form.getStuGradeMainIds();
        Long courseItemId = form.getCourseItemId();
        List<StuGradeDetailVO> stuGradeDetailVOS = stuGradeMainIds.stream().map(
                stuGradeMainId -> {
                    StuGradeDetailVO stuGradeDetailVO = new StuGradeDetailVO();
                    StuGradeMain stuGradeMain =
                            stuGradeMainService.selectByStuGradeMainId(stuGradeMainId);
                    Long studentId = stuGradeMain.getStudentId();
                    List<Student> studentList =
                            studentService.selectByUserId(studentId);
                    Student student = studentList.get(0);
                    stuGradeDetailVO.setStudentId(studentId);
                    stuGradeDetailVO.setStuNo(student.getStuNo());
                    User user = userService.selectUserById(studentId);
                    stuGradeDetailVO.setStudentName(user.getUserName());
                    StuItemGrade stuItemGrade =
                            stuGradeItemService.selectStuItemGradeByStuGradeMainIdAndCourseItemId(stuGradeMainId, courseItemId);
                    stuGradeDetailVO.setScore(stuItemGrade.getScore());
                    List<StuGradeItemDetailDto> stuGradeItemDetailDtos =
                            stuGradeItemDetailService.selectByStuItemGradeId(stuItemGrade.getId(), flag);
                    stuGradeDetailVO.setStuGradeItemDetailDtos(stuGradeItemDetailDtos);
                    return stuGradeDetailVO;
                }
        ).collect(Collectors.toList());
//        List<StuGradeItemDetailDto> stuGradeItemDetailDtos =
//                stuGradeItemDetailService.selectByStuItemGradeId(stuItemGradeId, RoleConstant.TEACHER);
            // todo 优化，逻辑判断，根据成绩项id一定能查到成绩作业项
            if (CollectionUtils.isEmpty(stuGradeItemDetailDtos)) {
                String json =
                        Result.build(ResultType.Failed).
                                appendData("stuGradeItemDetailDtos", null)
                                .convertIntoJSON();
                response.getWriter().write(json);
            }

        // todo 缓存根据学校id，教师id，课程id，成绩主表id
        return Result.build(ResultType.Success).appendData("stuGradeDetailVOS", stuGradeDetailVOS).convertIntoJSON();
    }*/

    private String getJson(
            Long universityId, Long semesterId, Long courseId,
            Long classId, Long teacherId, Long courseItemId, Integer flag) throws JsonProcessingException {
        List<Student> studentList =
                studentService.selectByClassId(classId);
        List<Long> studentIds = studentList.stream().map(
                Student::getUserId
        ).collect(Collectors.toList());
        List<StuGradeMain> stuGradeMains = stuGradeMainService.selectByCurriculum(
                universityId, teacherId, semesterId, studentIds, courseId, flag
        );
        List<StuGradeDetailVO> stuGradeDetailVOS = stuGradeMains.stream().map(
                stuGradeMain -> {
                    StuGradeDetailVO stuGradeDetailVO = new StuGradeDetailVO();
                    Long studentId = stuGradeMain.getStudentId();
                    List<Student> studentList1 =
                            studentService.selectByUserId(studentId);
                    Student student = studentList1.get(0);
                    User user =
                            userService.selectUserById(studentId);
                    stuGradeDetailVO.setStudentId(studentId);
                    stuGradeDetailVO.setStuNo(student.getStuNo());
                    stuGradeDetailVO.setStudentName(user.getUserName());

                    Long stuGradeMainId = stuGradeMain.getId();
                    StuItemGrade stuItemGrade =
                            stuGradeItemService.selectStuItemGradeByStuGradeMainIdAndCourseItemId(stuGradeMainId, courseItemId);
                    stuGradeDetailVO.setScore(stuItemGrade.getScore());

                    List<StuGradeItemDetailDto> stuGradeItemDetailDtos =
                            stuGradeItemDetailService.selectByStuItemGradeId(stuItemGrade.getId(), flag);
                    stuGradeDetailVO.setStuGradeItemDetailDtos(stuGradeItemDetailDtos);
                    return stuGradeDetailVO;
                }
        ).collect(Collectors.toList());
//        List<StuGradeItemDetailDto> stuGradeItemDetailDtos =
//                stuGradeItemDetailService.selectByStuItemGradeId(stuItemGradeId, RoleConstant.TEACHER);
            /* todo 优化，逻辑判断，根据成绩项id一定能查到成绩作业项
            if (CollectionUtils.isEmpty(stuGradeItemDetailDtos)) {
                String json =
                        Result.build(ResultType.Failed).
                                appendData("stuGradeItemDetailDtos", null)
                                .convertIntoJSON();
                response.getWriter().write(json);
            }*/

        // todo 缓存根据学校id，教师id，课程id，成绩主表id
        return Result.build(ResultType.Success).appendData("stuGradeDetailVOS", stuGradeDetailVOS).convertIntoJSON();
    }


    @ApiOperation(value = "根据更新成绩明细表", notes = "任课教师角色")
    @PutMapping("/tec")
    public Result updateStuGradeItemDetailDto(
            @ApiParam(value = "成绩明细集合", required = true)
            @RequestBody List<StuItemGradeDetail> stuItemGradeDetails
    ) throws SQLException {

        if (CollectionUtils.isEmpty(stuItemGradeDetails)) {
            return Result.build(ResultType.ParamError);
        }

        Long stuItemGradeId = stuItemGradeDetails.get(0).getStuItemGradeId();
        StuItemGrade stuItemGrade =
                stuGradeItemService.selectByStuItemGradeId(stuItemGradeId);
        CourseItem item =
                courseItemService.selectCouseItem(stuItemGrade.getCourseItemId());
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
        stuItemGrade.setScore(stuItemGradeScore*item.getRate());
        boolean b1 = stuGradeItemService.updateStuItemGrade(stuItemGrade);
        if (!b1) {
            return Result.build(ResultType.Failed);
        }

        // 更新成绩主表
        List<StuGradeItemDto> stuGradeItemDtos =
                stuGradeItemService.selectByStuGradeMainId(stuGradeMainId, RoleConstant.TEACHER);
        Double stuGradeMainScore = stuGradeItemDtos.stream()
                .collect(Collectors.summingDouble(StuGradeItemDto::getScore));
        stuGradeMain.setScore(stuGradeMainScore);
        stuGradeMain.setPoint(scoreToPointUtil.getPoint(stuGradeMainScore));
        boolean b2 = stuGradeMainService.updateStuGradeMain(stuGradeMain);
        if (!b2) {
            return Result.build(ResultType.Failed);
        }
        return Result.build(ResultType.Success);
    }


    @ApiOperation(value = "导入成绩excel文件并校验", notes = "任课教师角色")
    @PostMapping("/tec/excel")
    public void upload(
            @RequestParam("file") MultipartFile file,
            HttpServletResponse response
    ) throws IOException {

        InputStream inputStream = file.getInputStream();
        com.alibaba.excel.metadata.Sheet sheet = new com.alibaba.excel.metadata.Sheet(1, 2, GradeReadModel.class);
        List<Object> datas = EasyExcelFactory.read(inputStream, sheet);

        System.out.println(JsonUtils.obj2String(datas));

        List<GradeReadModel> gradeReadModels = datas.stream().map(
                data -> {
                    GradeReadModel gradeReadModel = (GradeReadModel) data;
                    return gradeReadModel;
                }
        ).collect(Collectors.toList());

        gradeReadModels.stream().forEach(
                gradeReadModel -> {
                    String stuStatus = studentForGradeService.selectStudentByStuNoAndStudentName(
                            gradeReadModel.getStuNo(), gradeReadModel.getStuName()
                    );

                    String courseStatus = courseDtoService.selectCourseByCourseNoAndCourseName(
                            gradeReadModel.getCourseNo(), gradeReadModel.getCourseName()
                    );

                    String classStatus = administrativestructureService.selectClassByClassNoAndClassName(
                            gradeReadModel.getClassNo(), gradeReadModel.getClassName()
                    );

                    String socreStatus = "";
                    if (gradeReadModel.getScore() < 0) {
                        socreStatus = "成绩不能小于0";
                    }
                    String status = stuStatus  + courseStatus + classStatus + socreStatus;
                    gradeReadModel.setStatus(status);
                }
        );
        String json = Result.build(ResultType.Success).appendData("gradeReadModels", gradeReadModels).convertIntoJSON();
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }

    @ApiOperation(value = "导出成绩excel文件", notes = "任课教师角色")
    @GetMapping("/tec/excel")
    public void download(
            @RequestParam(name = "semesterId") Long semesterId,
            @RequestParam(name = "classId") Long classId,
            @RequestParam(name = "courseId") Long courseId,
            @RequestParam(name = "teacherId") Long teacherId,
            @RequestParam(name = "universityId") Long universityId,
            HttpServletResponse response

    ) throws IOException {

        Semester semester =
                semesterService.selectById(semesterId);
        String semesterName = semester.getName();

        Class aClass =
                administrativestructureService.selectClassByClassId(classId);
        String className = aClass.getName();

        Course course =
                courseForGradeService.selectCourseByCourseId(courseId);

        String courseName = course.getName();
        String fileName = semesterName + className + courseName + "成绩表.xlsx";

        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("成绩表");

        String[] headers = {"学号", "姓名", "成绩", "课程编号", "课程名称", "班级编号", "班级"};


        for (int i = 0; i < 2; i++) {
            Row row = sheet.createRow(i);
            for (int j = 0; j < headers.length; j++) {
                Cell cell = row.createCell(j);
                if (i == 1) {
                    // 表单第二行添加列名
                    cell.setCellValue(headers[j]);
                }
            }
        }
        // 第一行内容
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.length - 1));
        sheet.getRow(0).getCell(0).setCellValue("说明：（1）总成绩可以不填写，如果是百分制,导入时总成绩是由系统按照该课程设置的比例和导入的分项成绩计算出来，如果是二级制或者五级制则按照总成绩导入\n" +
                "（2）若成绩方式选择二级制，可以在总成绩中填写（合格 或 不合格），也可以添加分数\n" +
                "（3）若成绩方式选择五级制，可以在总成绩中填写（不及格、及格、中等、良好 或 优秀），也可以添加分数\n" +
                "（4）平时成绩、实验成绩、期中成绩和期末成绩为原始成绩（即未折算前的成绩）");


        //在表中存放查询到的数据放入对应的列
        List<StuGradeMain> stuGradeMains = stuGradeMainService.selectStuGradeMainByIds(
                universityId, teacherId, semesterId, courseId
        );

        int rowNum = 2;
        for (StuGradeMain stuGradeMain :
                stuGradeMains) {

            Row row1 = sheet.createRow(rowNum);

            List<Student> studentList =
                    studentService.selectByUserId(stuGradeMain.getStudentId());

            User user =
                    userService.selectUserById(stuGradeMain.getStudentId());
            row1.createCell(0).setCellValue(studentList.get(0).getStuNo());
            row1.createCell(1).setCellValue(user.getUserName());
//            row1.createCell(2).setCellValue(no);
            row1.createCell(2).setCellValue(stuGradeMain.getScore());
//            row1.createCell(4).setCellValue("");
            row1.createCell(3).setCellValue(course.getNumber());
            row1.createCell(4).setCellValue(course.getName());
            row1.createCell(5).setCellValue(aClass.getCode());
            row1.createCell(6).setCellValue(aClass.getName());
            rowNum++;
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "iso-8859-1"));
        response.flushBuffer();
        workbook.write(response.getOutputStream());
    }


    @ApiOperation(value = "导出成绩作业项目，用于录入成绩测试", notes = "测试用的")
    @GetMapping("/test/excel")
    public void downloadTest(
            @RequestParam(name = "semesterId") Long semesterId,
            @RequestParam(name = "classId") Long classId,
            @RequestParam(name = "courseId") Long courseId,
            @RequestParam(name = "teacherId") Long teacherId,
            @RequestParam(name = "universityId") Long universityId,
            @RequestParam(name = "no") Integer no,
            @RequestParam(name = "note") String note,
            @RequestParam(name = "score") Double score,
            HttpServletResponse response

    ) throws IOException {

        Semester semester =
                semesterService.selectById(semesterId);
        String semesterName = semester.getName();

        Class aClass =
                administrativestructureService.selectClassByClassId(classId);
        String className = aClass.getName();

        Course course =
                courseForGradeService.selectCourseByCourseId(courseId);

        String courseName = course.getName();
        String fileName = semesterName + className + courseName + "成绩表.xlsx";

        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("成绩表");

        String[] headers = {"学号", "姓名", "序号", "成绩", "评语", "课程编号", "课程名称", "班级编号", "班级"};


        for (int i = 0; i < 2; i++) {
            Row row = sheet.createRow(i);
            for (int j = 0; j < headers.length; j++) {
                Cell cell = row.createCell(j);
                if (i == 1) {
                    // 表单第二行添加列名
                    cell.setCellValue(headers[j]);
                }
            }
        }
        // 第一行内容
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.length - 1));
        sheet.getRow(0).getCell(0).setCellValue("说明：（1）总成绩可以不填写，如果是百分制,导入时总成绩是由系统按照该课程设置的比例和导入的分项成绩计算出来，如果是二级制或者五级制则按照总成绩导入\n" +
                "（2）若成绩方式选择二级制，可以在总成绩中填写（合格 或 不合格），也可以添加分数\n" +
                "（3）若成绩方式选择五级制，可以在总成绩中填写（不及格、及格、中等、良好 或 优秀），也可以添加分数\n" +
                "（4）平时成绩、实验成绩、期中成绩和期末成绩为原始成绩（即未折算前的成绩）");


        List<Student> studentList = studentService.selectByClassId(classId);

        int rowNum = 2;
        for (Student student :
                studentList) {

            Row row1 = sheet.createRow(rowNum);

//            Long studentId = stuGradeMain.getStudentId();
//            Student student = studentService.selectById(studentId);
            User user = userService.selectUserById(student.getUserId());
            row1.createCell(0).setCellValue(student.getStuNo());
            row1.createCell(1).setCellValue(user.getUserName());
            row1.createCell(2).setCellValue(no);
            row1.createCell(3).setCellValue(score);
            row1.createCell(4).setCellValue(note);
            row1.createCell(5).setCellValue(course.getNumber());
            row1.createCell(6).setCellValue(course.getName());
            row1.createCell(7).setCellValue(aClass.getCode());
            row1.createCell(8).setCellValue(aClass.getName());
            rowNum++;
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "iso-8859-1"));
        response.flushBuffer();
        workbook.write(response.getOutputStream());
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
    @ApiOperation(value = "录入成绩明细", notes = "任课教师角色")
    @PostMapping("")
    public Result insertStuItmeGradeDtoByExcel(
            @ApiParam(value = "excel成绩录入", required = true)
            @RequestBody ExcelDtoForm form
            ) throws SQLException {
        if (form == null) {
            return Result.build(ResultType.ParamError);
        }
        Long universityId = form.getUniversityId();
        Long teacherId = form.getTeacherId();
        Long courseItemId = form.getCourseItemId();
        Long courseItemDetailId = form.getCourseItemDetailId();
        Long courseId = form.getCourseId();
        Long classId = form.getClassId();
        Long semesterId = form.getSemesterId();

        List<Student> studentList =
                studentService.selectByClassId(classId);
        List<Long> studentIds = studentList.stream().map(
                student -> {
                    return student.getUserId();
                }
        ).collect(Collectors.toList());
        List<StuGradeMain> stuGradeMains = stuGradeMainService.selectByCurriculum(
                universityId, teacherId, semesterId, studentIds, courseId, RoleConstant.TEACHER
        );

        // 不是缓存状态录入会报错
        for (StuGradeMain stuGradeMain :
                stuGradeMains) {
            if (stuGradeMain.getCache() != GradeConstant.CACHE_VALID) {
                throw new SQLException("非缓存状态不能修改成绩");
            }

        }

        // 校验excel内容
        int flag = 0;  // 用于判断是否有问题
        List<GradeReadModel> gradeReadModels1 = form.getGradeReadModels();
        for (GradeReadModel gradeReadModel:
             gradeReadModels1) {
            String stuStatus = studentForGradeService.selectStudentByStuNoAndStudentName(
                    gradeReadModel.getStuNo(), gradeReadModel.getStuName()
            );

            String courseStatus = courseDtoService.selectCourseByCourseNoAndCourseName(
                    gradeReadModel.getCourseNo(), gradeReadModel.getCourseName()
            );

            String classStatus = administrativestructureService.selectClassByClassNoAndClassName(
                    gradeReadModel.getClassNo(), gradeReadModel.getClassName()
            );

            String socreStatus = "";
            if (gradeReadModel.getScore() < 0) {
                socreStatus = "成绩不能小于0";
            }
            String status = stuStatus + courseStatus + classStatus + socreStatus;
            if (!status.trim().equals("")) {
                flag = 1;
            }
            gradeReadModel.setStatus(status);
        }
        if (flag == 1) {
            return Result.build(ResultType.Failed).appendData("gradeReadModels", gradeReadModels1);
        }


        List<GradeReadModel> gradeReadModels = form.getGradeReadModels();

        // 遍历成绩主表
        for (StuGradeMain stuGradeMain :
                stuGradeMains) {
            Long stuGradeMainId = stuGradeMain.getId();
            // 判断成绩项是否已创建
            StuItemGrade stuItemGrade = stuGradeItemService.selectStuItemGradeByStuGradeMainIdAndCourseItemId(
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
                stuItemGrade = stuGradeItemService.selectStuItemGradeByStuGradeMainIdAndCourseItemId(
                        stuGradeMainId, courseItemId
                );
            }

            // 文档内容对应
            Iterator<GradeReadModel> iterator = gradeReadModels.iterator();
            while (iterator.hasNext()) {
                GradeReadModel grade = iterator.next();
                String stuNo = grade.getStuNo().trim();
                Student student = studentForGradeService.selectStudentByStuNo(stuNo);
                if (stuGradeMain.getStudentId().equals(student.getUserId())) {
                    Long stuItemGradeId = stuItemGrade.getId();
                    Double score = grade.getScore();
                    String note = grade.getNote();
                    // 判断是否存在，用于更新成绩作业项
                    StuItemGradeDetail stuItemGradeDetail = stuGradeItemDetailService.selectByStuItemGradeIdAndCourseItemDetailId(
                            stuItemGradeId, courseItemDetailId, null
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
                    iterator.remove();
                    break;
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
            stuGradeMain.setState(GradeConstant.MAIN_STATE_SCORING);
            boolean c = stuGradeMainService.updateStuGradeMain(stuGradeMain);
            if (!c) {
                return Result.build(ResultType.Failed);
            }
        }
        return Result.build(ResultType.Success);
    }
/*
    *//**
     *
     * @Param [form]
     * @Return:edu.uni.bean.Result
     * @Author: 陈汉槟
     * @Date: 2019/5/14 18:31
     * @Description:  录入成绩明细
     * @Modify: 2019/5/19
     *//*
    @ApiOperation(value = "录入成绩明细", notes = "任课教师角色")
    @PostMapping("")
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
            StuItemGrade stuItemGrade = stuGradeItemService.selectStuItemGradeByStuGradeMainIdAndCourseItemId(
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
                stuItemGrade = stuGradeItemService.selectStuItemGradeByStuGradeMainIdAndCourseItemId(
                        stuGradeMainId, courseItemId
                );
            }

            // 文档内容对应
            for (ExcelForm excelForm :
                    excelForms) {
                String stuNo = excelForm.getStuNo().trim();
                Student student = studentForGradeService.selectStudentByStuNo(stuNo);
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
    }*/

    @ApiOperation(value="文件上传")
    @PostMapping("/tec/upload/{stuItemGradeDetailId}")
    public Result uploadFile(
            @PathVariable Long stuItemGradeDetailId,
            @RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Result.build(ResultType.ParamError);
        }
        StuItemGradeDetail stuItemGradeDetail1 =
                stuGradeItemDetailService.selectByStuItemGradeDetailId(stuItemGradeDetailId);
        if (stuItemGradeDetail1.getCache() != GradeConstant.CACHE_VALID) {
            return Result.build(ResultType.Failed);
        }
        FileUtil fileUtil = new FileUtil(config.getAbsoluteExcelDir());
        String filePath;
        try {
            // 文件现存的路径+文件名 用于存储到数据库
            filePath = fileUtil.uploadFile(file);
        } catch (IOException e) {
            return Result.build(ResultType.Failed);
        }
        log.info("成功");
        StuItemGradeDetail stuItemGradeDetail = new StuItemGradeDetail();
        stuItemGradeDetail.setId(stuItemGradeDetailId);
        stuItemGradeDetail.setAttachment(filePath);
        stuGradeItemDetailService.updateStuItemGradeDetail(stuItemGradeDetail);
        return Result.build(ResultType.Success);
    }

    @ApiOperation(value = "附件下载")
    @GetMapping("/download/{stuItemGradeDetailId}")
    public void download(
            @PathVariable Long stuItemGradeDetailId,
            HttpServletResponse response) {
        StuItemGradeDetail stuItemGradeDetail =
                stuGradeItemDetailService.selectByStuItemGradeDetailId(stuItemGradeDetailId);
        String filePath = stuItemGradeDetail.getAttachment();
        FileUtil fileUtil = new FileUtil();
        try {
            // filePath 可以从数据库获取，这里为了方便直接输入路径
            fileUtil.downloadFile(filePath, response);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "根据成绩明细项id成绩明细")
    @GetMapping("/tec/{stuItemGradeDetailId}")
    public void getStuItemGradeDetail(
            @PathVariable Long stuItemGradeDetailId,
            HttpServletResponse response) throws IOException {
        StuItemGradeDetail stuItemGradeDetail =
                stuGradeItemDetailService.selectByStuItemGradeDetailId(stuItemGradeDetailId);
        String attachment = stuItemGradeDetail.getAttachment();
        attachment = new FileNameUtil(attachment).getFileName();
        stuItemGradeDetail.setAttachment(attachment);
        response.setContentType("application/json;charset=utf-8");
        String json = Result.build(ResultType.Success).appendData("stuItemGradeDetail", stuItemGradeDetail).convertIntoJSON();
        response.getWriter().write(json);
    }



    public static void main(String[] args) throws JsonProcessingException {

        List<GradeReadModel> gradeReadModels = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            GradeReadModel gradeReadModel = new GradeReadModel();
            gradeReadModel.setNo(i);
            gradeReadModels.add(gradeReadModel);
        }


        for (int i = 0; i < 10; i++) {
            Iterator<GradeReadModel> iterator = gradeReadModels.iterator();
            while (iterator.hasNext()) {
                GradeReadModel next = iterator.next();
                System.out.println(JsonUtils.obj2String(next));
                System.out.println();
                if (next.getNo() == i) {
                    System.out.println(i);
                    iterator.remove();
                    break;
                }
            }
            /*for (int j = 0; j < gradeReadModels.size(); j++) {
                GradeReadModel gradeReadModel = gradeReadModels.get(j);
                System.out.println(JsonUtils.obj2String(gradeReadModel));
                if (gradeReadModel.getNo() == i) {
                    System.out.println(i);
                    gradeReadModels.remove(j);
                    break;
                }
            }*/
        }

    }

}
