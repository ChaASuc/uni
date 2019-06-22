package edu.uni.grademanagement.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.uni.administrativestructure.bean.Class;
import edu.uni.administrativestructure.bean.Department;
import edu.uni.administrativestructure.service.ClassService;
import edu.uni.administrativestructure.service.DepartmentService;
import edu.uni.administrativestructure.service.UniversityService;
import edu.uni.bean.Result;
import edu.uni.bean.ResultType;
import edu.uni.educateAffair.bean.Teachingtask;
import edu.uni.educateAffair.service.CanlendarService;
import edu.uni.educateAffair.service.CurriculumService;
import edu.uni.educateAffair.service.SemesterService;
import edu.uni.grademanagement.bean.StuGradeMain;
import edu.uni.grademanagement.config.GradeManagementConfig;
import edu.uni.grademanagement.constants.GradeConstant;
import edu.uni.grademanagement.constants.RoleConstant;
import edu.uni.grademanagement.dto.CourseDto;
import edu.uni.grademanagement.dto.StuGradeItemDto;
import edu.uni.grademanagement.service.*;
import edu.uni.grademanagement.vo.*;
import edu.uni.professionalcourses.bean.Course;
import edu.uni.userBaseInfo1.bean.Student;
import edu.uni.userBaseInfo1.bean.User;
import edu.uni.userBaseInfo1.service.EmployeeService;
import edu.uni.userBaseInfo1.service.StudentService;
import edu.uni.userBaseInfo1.service.UserService;
import edu.uni.utils.JsonUtils;
import edu.uni.utils.RedisCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javafx.scene.CacheHint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 陈汉槟
 * @Create 2019/4/25
 * @Description  成绩主表控制层角色接口
 * @Since 1.0.0
 */
@Api(description = "成绩主表控制层")
@RestController
@RequestMapping("/json/gradeManagement/gradeMain")
@Slf4j
public class StuGradeMainController {

    @Autowired
    private StuGradeMainService stuGradeMainService;

    @Autowired
    private StuGradeItemService stuGradeItemService;

    @Autowired
    private StuGradeItemDetailService stuGradeItemDetailService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    @Autowired
    private UniversityService universityService;

    @Autowired
    private SemesterService semesterService;

    @Autowired
    private ClassService classService;

    @Autowired
    private CourseDtoService courseDtoService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CurriculumService curriculumService;

    @Autowired
    private CanlendarService canlendarService;

    @Autowired
    private CourseForGradeService courseForGradeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EducateAffairService educateAffairService;

    @Autowired
    private AdministrativestructureService administrativestructureService;

    @Autowired
    private StuAbSenceService stuAbSenceService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private GradeManagementConfig config;

    /**
     * 内部类，专门用来管理每个get方法所对应缓存的名称。
     */
    static class CacheNameHelper {
        // 根据学生id和学期id和页码存储学生主表成绩
        // gm_stuGradeMain_stuList_{studentId}_{fromSemesterId}_{toSemesterId}_{pageNum}
//        public static final String StuList_CacheNamePrefix = "gm_stuGradeMain_stuList_%s_%s_%s_%s";
        // 根据学生id存储学生主表成绩
        // gm_stuGradeMain_tecList_{semesterId}_{studentId}
        public static final String StuList_CacheNamePrefix = "gm_stuGradeMain_stuList_%s_%s";
        // 根据学期集合课程集合班级集合教师集合院级集合分页获取课表
        // gm_stuGradeMain_tecList_{semesterIds}_{courseIds}_{classIds}_{teacherIds}_{departmentIds}_{pageNum}
        public static final String Curriculum_CacheNamePrefix =
                "gm_stuGradeMain_curriculum_%s_%s_%s_%s_%s_%s";
        // 根据教师学校id学期id课程id和班级id教师id存储学生主表成绩
        // gm_stuGradeMain_tecList_{universityId}_{semesterId}_{courseId}_{classId}_{teacherId}
        public static final String TecList_CacheName = "gm_stuGradeMain_tecList_%s_%s_%s_%s_%s";
//        根据非任课老师学校id学期id课程id和班级id教师id存储学生主表成绩
        // gm_stuGradeMain_tecList_{universityId}_{semesterId}_{teacherId}_{courseId}_{classId}
        public static final String EmployeeList_CacheName = "gm_stuGradeMain_semployeeList_%s_%s_%s_%s_%s";
        // 根据成绩主表
        // gm_stuGradeMain_List_*
        public static final String ListAll_CacheName = "gm_stuGradeMain_*";
        // 根据学期id和课程id和班级id存储学生考勤
        // gm_stuGradeMain_stuAbsence_{semesterId}_{courseId}_{teacherId}_{classId}_{pageNum}
        public static final String StuAbsence_CachePrefix = "gm_stuGradeMain_stuAbsence_%s_%s_%s";
        // 根据学校id存储课程
        // gm_stuGradeMain_course_{universityId}
        public static final String Course_CachePrefix = "gm_stuGradeMain_course_%s";
    }

    // todo 未完成需要其他模块数据
    /**
     *
     * @Param [studentId, response]
     * @Return:void
     * @Author: 陈汉槟
     * @Date: 2019/4/26 0:53
     * @Description: 根据学生id查找所有成绩主表信息
     * @Modify: 2019/5/17 添加缓存功能，改变数据格式输出，修改成绩主表显示格式
     */
    @ApiOperation(value = "学生获取主表", notes = "学生角色")
    @GetMapping("/stuList/{studentId}")
    public void getStuList(
            @ApiParam(value = "学生id", required = false)
            @PathVariable Long studentId,
            @RequestParam(required = false) Long semesterId,
            HttpServletResponse response
    ) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        // 1 获取redis缓存key
        //  获取当前学期
        if (semesterId == null) {
            semesterId = 40L;
        }
        String cacheName = String.format(CacheNameHelper.StuList_CacheNamePrefix,
                semesterId, studentId);

        // 2 根据key获取缓存
        String json = redisCache.get(cacheName);
//        String json = null;
        // 3 判断是否存在
        if (json == null) {


            List<StuGradeMain> stuGradeMains =
                    stuGradeMainService.selectStugradeMainByStudentId(studentId,semesterId);
            if (CollectionUtils.isEmpty(stuGradeMains)) {
                json = Result.build(ResultType.Success).appendData("stuGradeVOS", null).convertIntoJSON();
                response.getWriter().write(json);
            }

            // 5.2 向其他模快获取值
            // 同个学生
            Student student = studentService.selectById(studentId);

            User user = userService.selectUserById(student.getUserId());

            String studentName = user.getUserName();
            String stuNo = student.getStuNo();

            Long classId = student.getClassId();
            Class classEntity = classService.select(classId);
            String className = classEntity.getName();
            String classCode = classEntity.getCode();
            Department department = departmentService.select(classEntity.getDepartmentId());
            String departmentName = department.getName();
        String specialtyName = "";

        List<StuGradeVO> stuGradeVOS = stuGradeMains.stream().map(
                    stuGradeMain -> {
                        StuGradeVO stuGradeVO = new StuGradeVO();
                        BeanUtils.copyProperties(stuGradeMain, stuGradeVO);

                        // 根据用户模块获取学生信息
                        stuGradeVO.setStudentId(studentId);
                        stuGradeVO.setStudentName(studentName);
                        stuGradeVO.setStuNo(stuNo);
                        stuGradeVO.setClassId(classId);
                        stuGradeVO.setClassName(className);
                        stuGradeVO.setClassCode(classCode);
                        stuGradeVO.setSpecialtyName(specialtyName);
                        stuGradeVO.setDepartmentName(departmentName);

                        // 根据课程id获取课程信息
                        CourseDto courseDto =
                                 courseDtoService.selectByCourseId(stuGradeVO.getCourseId());

                        stuGradeVO.setSpeciesId(courseDto.getSpeciesId());
                        stuGradeVO.setCategoryId(courseDto.getCategoryId());
                        stuGradeVO.setCategoryName(courseDto.getCategoryName());
                        stuGradeVO.setSpeciesName(courseDto.getSpeciesName());
                        stuGradeVO.setCourseNumber(courseDto.getNumber());
                        stuGradeVO.setCourseName(courseDto.getName());
                        stuGradeVO.setHour(courseDto.getHour());
                        stuGradeVO.setCredit(courseDto.getCredit());

                        List<StuGradeItemDto> stuGradeItemDtos = stuGradeItemService.selectByStuGradeMainId(
                                stuGradeMain.getId(), null
                        );
                        stuGradeVO.setStuGradeItemDtos(stuGradeItemDtos);
                        return stuGradeVO;
                    }
            ).collect(Collectors.toList());
            // 5.4 转换json字符串
            json = Result.build(ResultType.Success).appendData("stuGradeVOS", stuGradeVOS).convertIntoJSON();
            // 5.5   写入缓存 留
            redisCache.set(cacheName, json);
        }

        // 6 返回值
        response.getWriter().write(json);
//    }
}
/**
     *
     * @Param [teacherId, gradeFilter, response]
     * @Return:void
     * @Author: 陈汉槟
     * @Date: 2019/5/19 18:32
     * @Description:  除教师外在职角色查看教师所教学生成绩主表 // todo 未加入缓存
     */
    @ApiOperation(value = "教师所教学生成绩主表分组表", notes = "除学生外等角色")
    @PostMapping("/curriculum")
    public void getCurriculum(
            @ApiParam(value = "高级过滤条件", required = false)
            @RequestBody(required = false) GradeFilter gradeFilter,
            HttpServletResponse response
    ) throws IOException {
        String cacheName = String.format(CacheNameHelper.Curriculum_CacheNamePrefix,
                JsonUtils.obj2String(gradeFilter.getSemesterIds()),
                JsonUtils.obj2String(gradeFilter.getCourseIds()),
                JsonUtils.obj2String(gradeFilter.getClassIds()),
                JsonUtils.obj2String(gradeFilter.getTeacherIds()),
                JsonUtils.obj2String(gradeFilter.getDepartmentIds()),
                gradeFilter.getPageNum() != null ? gradeFilter.getPageNum(): 0);

        // 2 根据key获取缓存
        String json = redisCache.get(cacheName);
//        String json = null;
        // 3 判断是否存在
        if (json == null) {
            List<Long> semesterIds = new ArrayList<>();
            List<Long> teacherIds = new ArrayList<>();
            List<Long> courseIds = new ArrayList<>();
            List<Long> classIds = new ArrayList<>();
            List<Long> departmentIds = new ArrayList<>();
            Integer pageNum = 0;
            if (gradeFilter == null) {
                // todo 学期模块获取默认当前学期
                Long semesterId = 40L;
                semesterIds.add(semesterId);
            }else {
                semesterIds = gradeFilter.getSemesterIds();
                teacherIds = gradeFilter.getTeacherIds();
                courseIds = gradeFilter.getCourseIds();
                classIds = gradeFilter.getClassIds();
                departmentIds = gradeFilter.getDepartmentIds();
                if (gradeFilter.getPageNum() != null) {

                    pageNum = gradeFilter.getPageNum();
                }
            }

            PageHelper.startPage(pageNum, config.getPageSize());
            // 根据默认条件或筛选条件获取获取课表信息
            List<Teachingtask> teachingtasks = educateAffairService.selectTeachingTaskByIds(
                    semesterIds, teacherIds, courseIds, classIds, departmentIds
            );

            // 根据课表信息组装成前端需要的数据
            List<StuGradeCurriculumVO> stuGradeCurriculumVOS = teachingtasks.stream().map(
                    teachingtask -> {
                        StuGradeCurriculumVO stuGradeCurriculumVO =
                                new StuGradeCurriculumVO();
                        Long universityId = teachingtask.getUniversityId();
                        Long classId = teachingtask.getClassId();
                        Long courseId = teachingtask.getCourseId();
                        Long semesterId = teachingtask.getSemesterId();
                        Long byWho = teachingtask.getWorkerId();
                        // 获取学校信息 university没数据
                        stuGradeCurriculumVO.setUniversityId(universityId);

                        stuGradeCurriculumVO.setSemesterId(semesterId);

                        // 获取课程信息
                        CourseDto courseDto = null;
                        stuGradeCurriculumVO.setCourseId(courseId);

                        courseDto =
                                courseDtoService.selectByCourseId(courseId);

                        if (courseDto == null) {
                            stuGradeCurriculumVO.setCourseName(null);
                            stuGradeCurriculumVO.setCourseNumber(null);
                        } else {
                            stuGradeCurriculumVO.setCourseName(courseDto.getName());
                            stuGradeCurriculumVO.setCourseNumber(courseDto.getNumber());
                            stuGradeCurriculumVO.setSpeciesId(courseDto.getSpeciesId());
                            stuGradeCurriculumVO.setSpeciesName(courseDto.getSpeciesName());
                            stuGradeCurriculumVO.setCategoryId(courseDto.getCategoryId());
                            stuGradeCurriculumVO.setCategoryName(courseDto.getCategoryName());
                        }
                        // 获取班级信息
                        stuGradeCurriculumVO.setClassId(classId);
                        Class aClass = classService.select(classId);
                        List<Student> studentList = new ArrayList<>();
                        if (aClass == null) {
                            stuGradeCurriculumVO.setClassCode(null);
                            stuGradeCurriculumVO.setClassName(null);
                            stuGradeCurriculumVO.setClassStudentNumber(0);
                        } else {
                            stuGradeCurriculumVO.setClassCode(aClass.getCode());
                            stuGradeCurriculumVO.setClassName(aClass.getName());
                            studentList = studentService.selectByClassId(classId);
                            stuGradeCurriculumVO.setClassStudentNumber(studentList.size());
                        }

                        if (studentList.size() == 0) {
                            stuGradeCurriculumVO.setStuGradeSuccessNumber(null);
                            stuGradeCurriculumVO.setStuGradeFailNumber(null);
                            stuGradeCurriculumVO.setTeacherId(byWho);
                            return stuGradeCurriculumVO;
                        }

                        List<Long> studentIds = studentList.stream().map(
                                student -> {
                                    return student.getUserId();
                                }
                        ).collect(Collectors.toList());

                        // 获取考试情况人数
                        List<StuGradeMain> stuGradeMains = stuGradeMainService.selectByCurriculum(
                                universityId, byWho, stuGradeCurriculumVO.getSemesterId(),
                                studentIds, courseId, RoleConstant.EMPLOYEE_EXINCLUDE_TEACHER
                        );

                        // 判空
                        if (CollectionUtils.isEmpty(stuGradeMains)) {
                            stuGradeCurriculumVO.setStuGradeSuccessNumber(null);
                            stuGradeCurriculumVO.setStuGradeFailNumber(null);
                        } else {
//
                            //   steam流通过和不通过人数
                            List<StuGradeMain> stuGradeMainSuccessList = stuGradeMains.stream().filter(
                                    stuGradeMain -> {
                                        return stuGradeMain.getScore() >= 60;
                                    }
                            ).collect(Collectors.toList());
                            int successNumber = stuGradeMainSuccessList.size();
                            stuGradeCurriculumVO.setStuGradeSuccessNumber(successNumber);
                            Integer studentNumber = stuGradeCurriculumVO.getClassStudentNumber();
                            stuGradeCurriculumVO.setStuGradeFailNumber(studentNumber - successNumber);

                        }

                        // 获取教师信息
                        stuGradeCurriculumVO.setTeacherId(byWho);
                        return stuGradeCurriculumVO;
                    }
            ).collect(Collectors.toList());


            PageInfo<StuGradeCurriculumVO> stuGradeCurriculumVOPageInfo
                    = new PageInfo<>(stuGradeCurriculumVOS);
            json = Result.build(ResultType.Success).appendData("stuGradeCurriculumVOPageInfo", stuGradeCurriculumVOPageInfo).convertIntoJSON();
            redisCache.set(cacheName, json);
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }

    /**
     * @Param [universityId, semesterId, teacherId, courseId, classId, pageNum, response]
     * @Return:void
     * @Author: 杨冠钦，陈汉槟
     * @Date: 2019/4/25 0:56
     * @Description: 根据学校id，学期id，教师id,课程id，班级id，页码获取分页的成绩主表信息
     * @Modify: 2019/5/16 添加缓存查询功能和教师、除学生、教师之外的在校角色区别查询
     */
    @ApiOperation(value = "教师所教学生成绩主表", notes = "任课教师角色")
    @GetMapping("/tec/list")
    public void getTecList(
            @ApiParam(value = "学校id", required = true)
            @RequestParam(name = "universityId", required = true) Long universityId,
            @ApiParam(value = "学期id", required = true)
            @RequestParam(name = "semesterId", required = true) Long semesterId,
            @ApiParam(value = "教师id", required = true)
            @RequestParam(name = "teacherId", required = true) Long teacherId,
            @ApiParam(value = "课程id", required = true)
            @RequestParam(name = "courseId", required = true) Long courseId,
            @ApiParam(value = "班级id", required = true)
            @RequestParam(name = "classId", required = true) Long classId,
            HttpServletResponse response
    ) throws IOException {
        // 角色标志，以此不同查询
        int roleFlag = RoleConstant.TEACHER;
        SetReponse(universityId, semesterId, teacherId, courseId, classId,
                roleFlag,response);
    }

    /**
     * @Param [universityId, semesterId, teacherId, courseId, classId, pageNum, response]
     * @Return:void
     * @Author: 杨冠钦，陈汉槟
     * @Date: 2019/4/25 0:56
     * @Description: 根据学校id，学期id，教师id,课程id，班级id，页码获取分页的成绩主表信息
     * @Modify: 2019/5/17 添加缓存查询功能和教师、除学生、教师之外的在校角色区别查询
     */
    @ApiOperation(value = "教师所教学生成绩主表", notes = "非任课教师、教务员、班主任")
    @GetMapping("/employee/list")
    public void getEmployeeList(
            @ApiParam(value = "学校id", required = true)
            @RequestParam(name = "universityId", required = true) Long universityId,
            @ApiParam(value = "学期id", required = true)
            @RequestParam(name = "semesterId", required = true) Long semesterId,
            @ApiParam(value = "教师id", required = true)
            @RequestParam(name = "teacherId", required = true) Long teacherId,
            @ApiParam(value = "课程id", required = true)
            @RequestParam(name = "courseId", required = true) Long courseId,
            @ApiParam(value = "班级id", required = true)
            @RequestParam(name = "classId", required = true) Long classId,
            HttpServletResponse response
    ) throws IOException {
        // 角色标志，以此不同查询
        int roleFlag = RoleConstant.EMPLOYEE_EXINCLUDE_TEACHER;
        SetReponse(universityId, semesterId, teacherId, courseId, classId,
                roleFlag,response);
    }


    /**
     *
     * @Param
     * @Return:
     * @Author: 陈汉槟
     * @Date: 2019/5/19 20:52
     * @Description: 封装教师角色和除教师角色外在职角色查看成绩主表功能方法
     * @Modify: 修改成绩主表显示格式
     */
    private void SetReponse(Long universityId, Long semesterId, Long teacherId,
                            Long courseId, Long classId, int roleFlag, HttpServletResponse response) throws IOException {


//        // 2 获取redis缓存key
        String cacheName;
        if (roleFlag == RoleConstant.TEACHER) {
             cacheName = String.format(CacheNameHelper.TecList_CacheName,
                    universityId, semesterId, courseId, classId, teacherId);
        }else {
            cacheName = String.format(CacheNameHelper.EmployeeList_CacheName,
                    universityId, semesterId, courseId, classId, teacherId);
        }

        // 3 获取缓存数据
        String json = redisCache.get(cacheName);
//        String json = null;
        if (json == null) {
            // 3.1 根据班级模块班级id获取学生集合
            List<Student> students = studentService.selectByClassId(classId);
            List<GradeVO> stuGradeVOS = new ArrayList<>();
            if (!CollectionUtils.isEmpty(students)){
                List<Long> studentIds = students.stream().map(
                        student -> {
                            return student.getUserId();
                        }
                ).collect(Collectors.toList());
                // 3.2 根据学校id，学期id，教师id，课程id，学生id集合获取成绩主表
                List<StuGradeMain> stuGradeMains = stuGradeMainService.selectByCurriculum(
                        universityId, teacherId, semesterId, studentIds, courseId, roleFlag
                );


                // 班级
                Class aClass = classService.select(classId);

                // 课程
                Course course = courseForGradeService.selectCourseByCourseId(courseId);

                stuGradeVOS = stuGradeMains.stream().map(
                        stuGradeMain -> {
                            GradeVO stuGradeVO = new GradeVO();
                            BeanUtils.copyProperties(stuGradeMain, stuGradeVO);

                            Student student = studentService.selectById(stuGradeMain.getStudentId());
                            Long studentUserId = student.getUserId();
                            User user = userService.selectUserById(studentUserId);
                            stuGradeVO.setStuNo(student.getStuNo());
                            stuGradeVO.setStudentName(user.getUserName());

                            // 班级信息
                            stuGradeVO.setClassId(classId);
                            stuGradeVO.setClassName(aClass.getName());
                            stuGradeVO.setClassCode(aClass.getCode());

                            stuGradeVO.setCourseName(course.getName());
                            stuGradeVO.setCourseNumber(course.getNumber());
                            // 成绩项
                            List<StuGradeItemDto> stuGradeItemDtos =
                                    stuGradeItemService.selectByStuGradeMainId(stuGradeMain.getId(), roleFlag);
                            stuGradeVO.setStuGradeItemDtos(stuGradeItemDtos);
                            return stuGradeVO;
                        }
                ).collect(Collectors.toList());
            }


            json = Result.build(ResultType.Success).appendData("stuGradeVOS", stuGradeVOS).convertIntoJSON();
            redisCache.set(cacheName, json);
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }

    /**
     *
     * @Param [universityId, semesterId, courseId, classId, teacherId]
     * @Return:edu.uni.bean.Result
     * @Author: 陈汉槟
     * @Date: 2019/4/28 9:58
     * @Description:  根据学校id，学期id，教师id,课程id，班级id创建成绩主表信息
     */
    @ApiOperation(value = "创建学生成绩主表,课程组长创建完课程项", notes = "课程组长")
    @PostMapping("/tec/list")
    public Result createStuGradeMains(
            @ApiParam(value = "学院Id", required = true)
            @RequestParam(name = "universityId", required = true) Long universityId,
            @ApiParam(value = "学期Id", required = true)
            @RequestParam(name = "semesterId", required = true) Long semesterId,
            @ApiParam(value = "课程id", required = true)
            @RequestParam(name = "courseId", required = true) Long courseId
    ) {
        List<Teachingtask> teachingtasks = educateAffairService.selectTeachingTaskById(
                universityId, semesterId, courseId
        );

        List<StuGradeMain> stuGradeMains = stuGradeMainService.selectByUniversityIdAndCourseIdAndSemesterId(
                universityId, semesterId, courseId
        );

        if (!CollectionUtils.isEmpty(stuGradeMains)) {
            throw new RuntimeException("成绩主表已存在");
        }

        boolean success = stuGradeMainService.insertStuGradeMainByIdsAndTeachingTasks(
                universityId, semesterId, courseId, teachingtasks, GradeConstant.MAIN_STATE_NOSCORE
        );
        if (success) {
            redisCache.delete(CacheNameHelper.ListAll_CacheName);
            return Result.build(ResultType.Success);
        } else {
            return Result.build(ResultType.Failed);
        }
    }



    @ApiOperation(value = "最终提交成绩", notes = "教师角色")
    @PutMapping("/tec/list")
    public Result updateStuGradeMain(
            @ApiParam(value = "成绩主表id集合", required = true)
            @RequestBody List<Long> stuGradeMainIds
    ) {
        if (CollectionUtils.isEmpty(stuGradeMainIds)) {
            return Result.build(ResultType.ParamError);
        }

        List<StuGradeMain> stuGradeMains =
                stuGradeMainService.selectStuGradeMainIds(stuGradeMainIds);
        stuGradeMains.stream().forEach(
                stuGradeMain -> {
                    if (stuGradeMain.getCache() == GradeConstant.CACHE_INVALID) {
                        throw new RuntimeException("成绩已提交");
                    }
                }
        );

        stuGradeMainIds.stream().forEach(
                stuGradeMainId -> {
                    stuGradeMainService.updateStuGradeMainStateToCacheInvalid(stuGradeMainId);
                    List<StuGradeItemDto> stuGradeItemDtos =
                            stuGradeItemService.selectByStuGradeMainId(stuGradeMainId, RoleConstant.TEACHER);
                    stuGradeItemService.updateStuItemGradeStateToCacheInvalid(stuGradeMainId);
                    List<Long> stuItemGradeIds =
                            stuGradeItemDtos.stream().map(stuGradeItemDto -> stuGradeItemDto.getId()).collect(Collectors.toList());
                    stuGradeItemDetailService.updateStuGradeItemDetailStateToCacheInvalid(stuItemGradeIds);
                }
        );
        redisCache.delete(CacheNameHelper.ListAll_CacheName);
        return Result.build(ResultType.Success);
    }

    @ApiOperation(value = "获取某学期某班级某课的考勤记录", notes = "任课教师角色")
    @GetMapping("/tec/stuAbsence")
    public void getStuAbsence(
            @ApiParam(value = "学期Id", required = true)
            @RequestParam(name = "semesterId", required = true) Long semesterId,
            @ApiParam(value = "课程id", required = true)
            @RequestParam(name = "courseId", required = true) Long courseId,
            @ApiParam(value = "班级id", required = true)
            @RequestParam(name = "classId", required = true) Long classId,
            HttpServletResponse response
    ) throws IOException {
        String cacheName = String.format(
                CacheNameHelper.StuAbsence_CachePrefix,
                semesterId, courseId, classId
        );
        response.setContentType("application/json;charset=utf-8");
        String json = redisCache.get(cacheName);
        if (json == null) {
            List<Student> studentList =
                    studentService.selectByClassId(classId);
            List<String> stuAbsences = studentList.stream().map(
                    student -> {
                        String staticc = stuAbSenceService.staticc(student.getUserId(),
                                semesterId, courseId);
                        return staticc;
                    }
            ).collect(Collectors.toList());
            json = Result.build(ResultType.Success).appendData("stuAbsences", stuAbsences).convertIntoJSON();
            redisCache.set(cacheName, json);
        }

        response.getWriter().write(json);
    }

    @ApiOperation(value = "创建成绩，用于重修的", notes = "任课教师角色")
    @PostMapping("/tec/rebuild")
    public Result  rebuildStuGradeMain(
            @ApiParam(value = "stuGradeMainId", required = true)
            @RequestParam(name = "stuGradeMainId", required = true) Long stuGradeMainId,
            @ApiParam(value = "成绩状态", required = true)
            @RequestParam(name = "state", required = true) Integer state
    ) {
        StuGradeMain stuGradeMain =
                stuGradeMainService.selectByStuGradeMainId(stuGradeMainId);
        if (stuGradeMain.getState() < GradeConstant.MAIN_STATE_NORMAL) {
            throw new RuntimeException("成绩未提交");
        }
        stuGradeMainService.rebulidStuGradeMainsSelective(
                stuGradeMainId, state
        );
        redisCache.delete(CacheNameHelper.ListAll_CacheName);
        return Result.build(ResultType.Success);
    }

    @ApiOperation(value = "根据学校获取课程", notes = "所有角色")
    @GetMapping("/courses/{universityId}")
    public void getCourses(
            @ApiParam(value = "学校id", required = true)
            @PathVariable Long universityId,
            HttpServletResponse response
    ) throws IOException {
        String cacheName = String.format(
                CacheNameHelper.Course_CachePrefix,
                universityId
        );
        String json = redisCache.get(cacheName);
        if (json == null) {
            List<Course> courses =
                    courseDtoService.selectByUniversityId(universityId);
            response.setContentType("application/json;charset=utf-8");
            json = Result.build(ResultType.Success).appendData("courses", courses).convertIntoJSON();
            redisCache.set(cacheName, json);
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }

}
