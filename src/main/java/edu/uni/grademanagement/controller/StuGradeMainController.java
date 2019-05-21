package edu.uni.grademanagement.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.uni.administrativestructure.bean.Class;
import edu.uni.administrativestructure.service.ClassService;
import edu.uni.administrativestructure.service.UniversityService;
import edu.uni.bean.Result;
import edu.uni.bean.ResultType;
import edu.uni.educateAffair.bean.Canlendar;
import edu.uni.educateAffair.bean.Curriculum;
import edu.uni.educateAffair.bean.Semester;
import edu.uni.educateAffair.service.CanlendarService;
import edu.uni.educateAffair.service.CurriculumService;
import edu.uni.educateAffair.service.SemesterService;
import edu.uni.grademanagement.bean.StuGradeMain;
import edu.uni.grademanagement.config.GradeManagementConfig;
import edu.uni.grademanagement.constants.GradeConstant;
import edu.uni.grademanagement.constants.RoleConstant;
import edu.uni.grademanagement.dto.CourseDto;
import edu.uni.grademanagement.dto.StuGradeItemDto;
import edu.uni.grademanagement.form.InsertStuItmeGradeDtoByExcelForm;
import edu.uni.grademanagement.service.CourseDtoService;
import edu.uni.grademanagement.service.StuGradeItemDetailService;
import edu.uni.grademanagement.service.StuGradeItemService;
import edu.uni.grademanagement.service.StuGradeMainService;
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
@Api(value = "成绩主表控制层", tags = {"成绩主表控制层"})
@RestController
@RequestMapping("/json/gradeManagement/gradeMain")
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
        public static final String StuList_CacheNamePrefix = "gm_stuGradeMain_stuList_%s";
        // 根据学期id和课程id和班级id和页码存储学生主表成绩
        // gm_stuGradeMain_tecList_{semesterId}_{courseId}_{classId}_{pageNum}
        public static final String TecList_CacheName = "gm_stuGradeMain_tecList_%s_%s_%s_%s";
        // 根据学期id和课程id和班级id和页码存储学生主表成绩
        // gm_stuGradeMain_List_{semesterId}_{courseId}_{teacherId}_{classId}_{pageNum}
        public static final String ListAll_CacheName = "gm_stuGradeMain_tecList_%s_%s_%s_%s_%s";
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
    @ApiOperation(value = "学生获取主表", tags = {"获取有效信息", "学生角色", "可用"}, notes = "可有可无参数")
    @GetMapping("/stuList/{studentId}")
    public void getStuList(
            @ApiParam(value = "学生id", required = false)
            @PathVariable Long studentId,
            HttpServletResponse response
    ) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        // 1 todo 获取redis缓存key 留
//        String cacheName = String.format(CacheNameHelper.StuList_CacheNamePrefix, studentId);

        // 2 todo 根据key获取缓存 留
//        String stuJson = redisCache.get(cacheName);
        String stuJson = "";  // todo 缓存写入就删掉
        // 3 todo 判断是否存在 留
//        if (stuJson == null) {

            // 5.1 根据学生id和学期id获取学生成绩主表
            // todo 向学期模块获取当前学期
            Long semesterId = 40L;
            List<StuGradeMain> stuGradeMains =
                    stuGradeMainService.selectStugradeMainByStudentId(studentId,semesterId);
            if (CollectionUtils.isEmpty(stuGradeMains)) {
                response.getWriter().write(JsonUtils.obj2String(null));
            }

            // 5.2 todo 向其他模快获取值
            // 同个学生
            List<Student> students =
                    studentService.selectByUserId(studentId);
            Student student = students.get(0);
            User user = userService.selectUserById(studentId);

            String studentName = user.getUserName();
            String stuNo = student.getStuNo();

            Long classId = student.getClassId();
            Class classEntity = classService.select(classId);
            String className = classEntity.getName();

            List<StuGradeVO> stuGradeVOS = stuGradeMains.stream().map(
                    stuGradeMain -> {
                        StuGradeVO stuGradeVO = new StuGradeVO();
                        BeanUtils.copyProperties(stuGradeMain, stuGradeVO);

                        /*//  根据学院id获取学院信息 todo university没数据
                        University university =
                                universityService.select(stuGradeMain.getUniversityId());
                        stuGradeVO.setUniversityName(university.getName());*/

                        // 根据用户模块获取学生信息
                        stuGradeVO.setStudentId(studentId);
                        stuGradeVO.setStudentName(studentName);
                        stuGradeVO.setStuNo(stuNo);
                        stuGradeVO.setClassId(classId);
                        stuGradeVO.setClassName(className);

                        // 根据学期模块获取学期
                        Semester semester =
                                semesterService.selectById(stuGradeVO.getSemesterId());
                        stuGradeVO.setSemesterName(semester.getName());

                        // 根据课程id获取课程信息
                        CourseDto courseDto =
                                null;
                        try {
                            courseDto = courseDtoService.selectByCourseId(stuGradeVO.getCourseId());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        stuGradeVO.setSpeciesId(courseDto.getSpeciesId());
                        stuGradeVO.setCategoryId(courseDto.getCategoryId());
                        stuGradeVO.setCategoryName(courseDto.getCategoryName());
                        stuGradeVO.setSpeciesName(courseDto.getSpeciesName());
                        stuGradeVO.setNumber(courseDto.getNumber());
                        stuGradeVO.setCourseName(courseDto.getName());

                        // 根据教师id获取教师名字
                        User teacher = userService.selectUserById(stuGradeMain.getByWho());
                        String teacherName = teacher.getUserName();
                        stuGradeVO.setWho(teacherName);

                        List<StuGradeItemDto> stuGradeItemDtos = stuGradeItemService.selectByStuGradeMainId(
                                stuGradeMain.getId(), null
                        );
                        stuGradeVO.setStuGradeItemDtos(stuGradeItemDtos);
                        return stuGradeVO;
                    }
            ).collect(Collectors.toList());
            // 5.4 转换json字符串
            stuJson = JsonUtils.obj2String(stuGradeVOS);
            // 5.5  todo 写入缓存 留
//            redisCache.set(cacheName, stuJson);
//        }

        // 6 返回值
        response.getWriter().write(stuJson);
//    }
}
//

    /**
     *
     * @Param [teacherId, gradeFilter, response]
     * @Return:void
     * @Author: 陈汉槟
     * @Date: 2019/5/19 18:32
     * @Description:  教师所教学生成绩主表 // todo 未加入缓存
     */
    @ApiOperation(value = "教师所教学生成绩主表分组表", tags = {"获取有效信息", "教师角色"}, notes = "可有可无参数,，为空代表今年学期")
    @GetMapping("/curriculum/{teacherId}")
    public void getCurriculumByTeacherId(
            @ApiParam(value = "教师id", required = true)
            @PathVariable Long teacherId,
            @ApiParam(value = "高级过滤条件", required = false)
            @RequestBody(required = false) GradeFilter gradeFilter,
            HttpServletResponse response
    ) throws IOException {
        String json = null;
        Integer flag = RoleConstant.TEACHER;
        json = getJson(teacherId, gradeFilter, flag, response);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }

    /**
     *
     * @Param [teacherId, gradeFilter, response]
     * @Return:void
     * @Author: 陈汉槟
     * @Date: 2019/5/19 18:32
     * @Description:  除教师外在职角色查看教师所教学生成绩主表 // todo 未加入缓存
     */
    @ApiOperation(value = "教师所教学生成绩主表分组表", tags = {"获取有效信息", "院长，班主任"}, notes = "可有可无参数,，为空代表今年学期")
    @GetMapping("/curriculum")
    public void getCurriculum(
            @ApiParam(value = "高级过滤条件", required = false)
            @RequestBody(required = false) GradeFilter gradeFilter,
            HttpServletResponse response
    ) throws IOException {
        String json = null;
        Integer flag = RoleConstant.EMPLOYEE_EXINCLUDE_TEACHER;
        json = getJson(null, gradeFilter, flag, response);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }



    /**
     *
     * @Param
     * @Return:
     * @Author: deschen
     * @Date: 2019/5/19 20:50
     * @Description: 封装教师角色和除教师角色外在职角色查看成绩功能方法
     */
    private String getJson(Long teacherId, GradeFilter gradeFilter,
                           Integer flag, HttpServletResponse response) throws JsonProcessingException {
        String json;
        response.setContentType("application/json;charset=utf-8");
        List<Long> semesterIds = new ArrayList<>();
        List<Long> teacherIds = new ArrayList<>();
        List<Long> courseIds = new ArrayList<>();
        List<Long> classIds = new ArrayList<>();
        Integer pageNum = 0;
        if (gradeFilter == null) {
           // todo 学期模块获取默认当前学期
            Long semesterId = 40L;
            semesterIds.add(semesterId);
            // 教师id存在
            if (teacherId != null) {
                teacherIds.add(teacherId);
            }
        }else {
            semesterIds = gradeFilter.getSemesterIds();
            teacherIds = gradeFilter.getTeacherIds();
            courseIds = gradeFilter.getCourseIds();
            classIds = gradeFilter.getClassIds();
            pageNum = gradeFilter.getPageNum() - 1;
        }

        PageHelper.startPage(pageNum, config.getPageSize());
        // 根据默认条件或筛选条件获取获取课表信息
        List<Curriculum> curriculumList = curriculumService.selectCurriculumByCondition(
                semesterIds, teacherIds, courseIds, classIds
        );


        // 判空
        if (CollectionUtils.isEmpty(curriculumList)) {
            json = Result.build(ResultType.Success).appendData("stuGradeCurriculumVOPageInfo", null).convertIntoJSON();
        }else {
            // 根据课表信息组装成前端需要的数据
            List<StuGradeCurriculumVO> stuGradeCurriculumVOS = curriculumList.stream().map(
                    curriculum -> {
                        StuGradeCurriculumVO stuGradeCurriculumVO =
                                new StuGradeCurriculumVO();
                        Long universityId = curriculum.getUniversityId();
                        Long classId = curriculum.getClassId();
                        Long courseId = curriculum.getCourseId();
                        Long canlendarId = curriculum.getCanlendarId();
                        // 获取学校信息 university没数据
                        /*stuGradeCurriculumVO.setUniversityId(universityId);
                        University university =
                                universityService.select(universityId);
                        if (university == null) {
                            stuGradeCurriculumVO.setUniversityName(null);
                        } else {
                            stuGradeCurriculumVO.setUniversityName(university.getName());
                        }*/
                        // 获取学期信息
                        Canlendar canlendar =
                                canlendarService.selectByCanlendarId(canlendarId);
                        if (canlendar == null) {
                            stuGradeCurriculumVO.setSemesterId(null);
                            stuGradeCurriculumVO.setSemesterName(null);
                        } else {
                            Semester semester =
                                    semesterService.selectById(canlendar.getSemesterId());
                            stuGradeCurriculumVO.setSemesterId(canlendar.getSemesterId());
                            if (semester == null) {
                                stuGradeCurriculumVO.setSemesterName(null);
                            } else {
                                stuGradeCurriculumVO.setSemesterName(semester.getName());
                            }
                        }
                        // 获取课程信息
                        Course course = null;
                        stuGradeCurriculumVO.setCourseId(courseId);
                        try {
                            course =
                                    courseDtoService.selectCourseByCourseId(courseId);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (course == null) {
                            stuGradeCurriculumVO.setCourseName(null);
                            stuGradeCurriculumVO.setCourseNumber(null);
                        } else {
                            stuGradeCurriculumVO.setCourseName(course.getName());
                            stuGradeCurriculumVO.setCourseNumber(course.getNumber());
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
                            studentList
                                    = studentService.selectByClassId(aClass.getId());
                            stuGradeCurriculumVO.setClassStudentNumber(studentList.size());
                        }
                        List<Long> studentIds = studentList.stream().map(
                                student -> {
                                    return student.getUserId();
                                }
                        ).collect(Collectors.toList());
                        // 获取考试情况人数
                        List<StuGradeMain> stuGradeMains = stuGradeMainService.selectByCurriculum(
                                universityId, teacherId, stuGradeCurriculumVO.getSemesterId(),
                                studentIds, courseId, RoleConstant.EMPLOYEE_EXINCLUDE_TEACHER
                        );

                        // 判空
                        if (CollectionUtils.isEmpty(stuGradeMains)) {
                            stuGradeCurriculumVO.setStuGradeSuccessNumber(0);
                            stuGradeCurriculumVO.setStuGradeFailNumber(0);
                        } else {
                            // 查看成绩主表是否以评完分，无则为0
                            List<StuGradeMain> stuGradeMainCaches = stuGradeMains.stream().filter(
                                    stuGradeMain -> stuGradeMain.getCache() == GradeConstant.CACHE_VALID
                            ).collect(Collectors.toList());
                            if (stuGradeMainCaches.size() > 0) {
                                stuGradeCurriculumVO.setStuGradeSuccessNumber(0);
                                stuGradeCurriculumVO.setStuGradeFailNumber(0);
                            }else {
                                //   steam流通过和不通过人数
                                List<StuGradeMain> stuGradeMainSuccessList = stuGradeMains.stream().filter(
                                        stuGradeMain -> {
                                            return stuGradeMain.getScore() >= 60;
                                        }
                                ).collect(Collectors.toList());
                                List<StuGradeMain> stuGradeMainFailList = stuGradeMains.stream().filter(
                                        stuGradeMain -> stuGradeMain.getScore() < 60
                                ).collect(Collectors.toList());
                                stuGradeCurriculumVO.setStuGradeSuccessNumber(stuGradeMainSuccessList.size());
                                stuGradeCurriculumVO.setStuGradeFailNumber(stuGradeMainFailList.size());
                            }

                        }

                        // 获取教师信息
                        User teacher
                                = userService.selectUserById(curriculum.getEmployeeId());
                        stuGradeCurriculumVO.setTeacherId(teacherId);
                        stuGradeCurriculumVO.setTeacherName(teacher.getUserName());
                        return stuGradeCurriculumVO;
                    }
            ).collect(Collectors.toList());


            PageInfo<StuGradeCurriculumVO> stuGradeCurriculumVOPageInfo
                    = new PageInfo<>(stuGradeCurriculumVOS);
            json = Result.build(ResultType.Success).appendData("stuGradeCurriculumVOPageInfo", stuGradeCurriculumVOPageInfo).convertIntoJSON();
        }
        return json;
    }
//
     //todo 未完成加缓存

    /**
     * @Param [universityId, semesterId, teacherId, courseId, classId, pageNum, response]
     * @Return:void
     * @Author: 杨冠钦，陈汉槟
     * @Date: 2019/4/25 0:56
     * @Description: 根据学校id，学期id，教师id,课程id，班级id，页码获取分页的成绩主表信息
     * @Modify: 2019/5/16 添加缓存查询功能和教师、除学生、教师之外的在校角色区别查询
     */
    @ApiOperation(value = "教师所教学生成绩主表", tags = {"教师角色"})
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
    @ApiOperation(value = "教师所教学生成绩主表", tags = {"教务员", "院长、班主任角色"})
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
     * @Author: deschen
     * @Date: 2019/5/19 20:52
     * @Description: 封装教师角色和除教师角色外在职角色查看成绩主表功能方法
     * @Modify: 修改成绩主表显示格式
     */
    private void SetReponse(Long universityId, Long semesterId, Long teacherId,
                            Long courseId, Long classId, int roleFlag, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");


//        // 2 todo 获取redis缓存key
//        String cacheName = null;

        // 3 todo 获取缓存数据
//        String json = redisCache.get(cacheName);
        String json = null;
        if (json == null) {
            // 3.1 根据班级模块班级id获取学生集合
            List<Student> students = studentService.selectByClassId(classId);
            List<Long> studentIds = students.stream().map(
                    student -> {
                        return student.getUserId();
                    }
            ).collect(Collectors.toList());
            // 3.2 根据学校id，学期id，教师id，课程id，学生id集合获取成绩主表
            List<StuGradeMain> stuGradeMains = stuGradeMainService.selectByCurriculum(
                    universityId, teacherId, semesterId, studentIds, courseId, roleFlag
            );


            List<StuGradeVO> stuGradeVOS = stuGradeMains.stream().map(
                    stuGradeMain -> {
                        StuGradeVO stuGradeVO = new StuGradeVO();
                        BeanUtils.copyProperties(stuGradeMain, stuGradeVO);

                       /* //  根据学院id获取学院信息 todo university表没数据
                        University university =
                                universityService.select(stuGradeMain.getUniversityId());
                        stuGradeVO.setUniversityName(university.getName());*/

                        // 根据用户模块获取学生信息
                        List<Student> studentList =
                                studentService.selectByUserId(stuGradeMain.getStudentId());
                        Student student = studentList.get(0);
                        Long studentUserId = student.getUserId();
                        User user = userService.selectUserById(studentUserId);
                        String studentName = user.getUserName();
                        String stuNo = student.getStuNo();
                        Long studentClassId = student.getClassId();
                        // 班级信息
                        Class classEntity = classService.select(classId);
                        String className = classEntity.getName();
                        //教师信息
                        User teacher = userService.selectUserById(stuGradeMain.getByWho());
                        String teacherName = teacher.getUserName();
                        stuGradeVO.setStudentId(studentUserId);
                        stuGradeVO.setStudentName(studentName);
                        stuGradeVO.setStuNo(stuNo);
                        stuGradeVO.setClassId(studentClassId);
                        stuGradeVO.setClassName(className);
                        stuGradeVO.setWho(teacherName);

                        // 根据学期模块获取学期
                        Semester semester =
                                semesterService.selectById(stuGradeVO.getSemesterId());
                        stuGradeVO.setSemesterName(semester.getName());

                        // 根据课程id获取课程信息
                        CourseDto courseDto = null;
                        try {
                            courseDto = courseDtoService.selectByCourseId(stuGradeVO.getCourseId());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        stuGradeVO.setSpeciesId(courseDto.getSpeciesId());
                        stuGradeVO.setCategoryId(courseDto.getCategoryId());
                        stuGradeVO.setCategoryName(courseDto.getCategoryName());
                        stuGradeVO.setSpeciesName(courseDto.getSpeciesName());
                        stuGradeVO.setNumber(courseDto.getNumber());
                        stuGradeVO.setCourseName(courseDto.getName());
                        return stuGradeVO;
                    }
            ).collect(Collectors.toList());


            json = Result.build(ResultType.Success).appendData("stuGradeVOS", stuGradeVOS).convertIntoJSON();
//            redisCache.set(cacheName, json);
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
    @ApiOperation(value = "创建学生成绩主表,主要用于查询成绩不存在，就创建", tags = {"教师角色"})
    @PostMapping("/tec")
    public Result createStuGradeMain(
            @ApiParam(value = "学院Id", required = true)
            @RequestParam(name = "universityId", required = true) Long universityId,
            @ApiParam(value = "学期Id", required = true)
            @RequestParam(name = "semesterId", required = true) Long semesterId,
            @ApiParam(value = "课程id", required = true)
            @RequestParam(name = "courseId", required = true) Long courseId,
            @ApiParam(value = "班级id", required = true)
            @RequestParam(name = "classId", required = true) Long classId,
            @ApiParam(value = "教师id", required = true)
            @RequestParam(name = "teacherId", required = true) Long teacherId
    ) {
        List<Student> students = studentService.selectByClassId(classId);
        List<Long> studentIds = students.stream().map(
                student -> {
                    return student.getUserId();
                }
        ).collect(Collectors.toList());
        boolean success = stuGradeMainService.insertStuGradeMainByIds(
                universityId, teacherId, semesterId, studentIds, courseId
        );
        if (success) {
            // todo 删除缓存
//            redisCache.deleteByPaterm(CourseItemController.CacheNameHelper.List_All_CacheNamePrefix);
            return Result.build(ResultType.Success);
        } else {
            return Result.build(ResultType.Failed);
        }
    }

    /*
    @ApiOperation(value = "最终提交成绩", tags = {"教师角色"})
    @PutMapping("/tec/list")
    public Result updateStuGradeMain(
            @ApiParam(value = "成绩主表id集合", required = true)
            @RequestBody List<Long> stuGradeMainIds
    ) {
        if (CollectionUtils.isEmpty(stuGradeMainIds)) {
            return Result.build(ResultType.ParamError);
        }

        stuGradeMainIds.stream().forEach(
                stuGradeMainId -> {
                    StuGradeMain stuGradeMain =
                            stuGradeMainService.selectByStuGradeMainId(stuGradeMainId);
                    stuGradeMain.setCache(GradeConstant.CACHE_INVALID);
                    stuGradeMainService.updateStuGradeMain(stuGradeMain);
                }
        );
    }*/


}
