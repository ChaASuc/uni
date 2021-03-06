package edu.uni.grademanagement.service.impl;

import edu.uni.educateAffair.bean.Teachingtask;
import edu.uni.grademanagement.bean.StuGradeMain;
import edu.uni.grademanagement.bean.StuGradeMainExample;
import edu.uni.grademanagement.bean.StuItemGrade;
import edu.uni.grademanagement.config.GradeManagementConfig;
import edu.uni.grademanagement.constants.GradeConstant;
import edu.uni.grademanagement.constants.RoleConstant;
import edu.uni.grademanagement.mapper.StuGradeMainMapper;
import edu.uni.grademanagement.mapper.StuItemGradeMapper;
import edu.uni.grademanagement.service.StuGradeItemService;
import edu.uni.grademanagement.service.StuGradeMainService;
import edu.uni.userBaseInfo1.bean.Student;
import edu.uni.userBaseInfo1.bean.StudentExample;
import edu.uni.userBaseInfo1.mapper.StudentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 陈汉槟
 * @Create 2019/4/23
 * @Description
 * @Since 1.0.0
 */

@Service
@Slf4j
// todo
public class StuGradeMainServiceImpl implements StuGradeMainService {


    @Autowired
    private StuGradeMainMapper stuGradeMainMapper;

    @Autowired
    private GradeManagementConfig config;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private StuGradeItemService stuGradeItemService;

    /*根据学生id查询成绩*/
    /*@Override


    *//**
     * 报废
     * @Param [studentId, semesterIds, pageNum]
     * @Return:com.github.pagehelper.PageInfo<edu.uni.grademanagement.bean.StuGradeMain>
     * @Author: 陈汉槟
     * @Date: 2019/5/7 1:41
     * @Description: 根据学生id、学期id集合和页码获取分页学生主表成绩
     *//*
    public PageInfo<StuGradeMain> selectByStudentId(Long studentId, List<Long> semesterIds, int pageNum) {
        StuGradeMainExample example = new StuGradeMainExample();
        StuGradeMainExample.Criteria criteria = example.createCriteria()
                .andStudentIdEqualTo(studentId)
                .andStateGreaterThan(GradeConstant.MAIN_STATE_CACHE)
                .andDeletedEqualTo(GradeConstant.RECORD_VALID);
        if (!CollectionUtils.isEmpty(semesterIds)) {
            criteria.andSemesterIdIn(semesterIds);
        }
        PageHelper.startPage(pageNum, config.getPageSize());
        List<StuGradeMain> stuGradeMains = stuGradeMainMapper.selectByExample(example);
        // todo 测试
        if (CollectionUtils.isEmpty(stuGradeMains)) {
            log.info("【无该学生成绩】 studentId = {}", studentId);
        }
        // todo 测试
        PageInfo<StuGradeMain> gradeMainPage = new PageInfo<>(stuGradeMains);
        return gradeMainPage;
    }*/


    @Override
    /**
     * @Param [studentInfoVOS, pageNum]
     * @Return:java.util.List<edu.uni.grademanagement.bean.StuGradeMain>
     * @Author: 陈汉槟
     * @Date: 2019/4/23 17:09
     * @Description: 根据学校id，教师id,学期id，学生id集合获取学生主表成绩分页
     * @Modify: 2019/5/16 添加缓存查询功能和教师、除学生、教师之外的在校角色区别查询
     */
    public List<StuGradeMain> selectByCurriculum(
            Long universityId, Long teacherId, Long semesterId,
            List<Long> studentIds, Long courseId, Integer flag) {

        // 添加条件
        StuGradeMainExample example = new StuGradeMainExample();
        StuGradeMainExample.Criteria criteria = example.createCriteria()
                .andStudentIdIn(studentIds)
                .andCourseIdEqualTo(courseId)
                .andByWhoEqualTo(teacherId)
                .andSemesterIdEqualTo(semesterId)
                .andUniversityIdEqualTo(universityId)
                .andDeletedEqualTo(GradeConstant.RECORD_VALID);
        // Modify：flag=0 教师 flag=1 代表除学生、教师之外的在校角色
        if (flag == RoleConstant.EMPLOYEE_EXINCLUDE_TEACHER){
            criteria.andStateBetween(GradeConstant.MAIN_STATE_NORMAL, GradeConstant.MAIN_STATE_SECOND_REBUILD)
                    .andCacheEqualTo(GradeConstant.CACHE_INVALID);
        }


        List<StuGradeMain> stuGradeMains = stuGradeMainMapper.selectByExample(example);

        return stuGradeMains;
    }

    @Override

    /**
     * 废弃
     * @Param [universityId, teacherId, semesterId, courseId]
     * @Return:java.util.List<edu.uni.grademanagement.bean.StuGradeMain>
     * @Author: 陈汉槟
     * @Date: 2019/5/9 16:48
     * @Description: 根据学院id、学期id和教师id何课程id获取学生主表成绩集合
     */
    public List<StuGradeMain> selectStuGradeMainByIds(Long universityId, Long teacherId, Long semesterId, Long courseId) {
        StuGradeMainExample stuGradeMainExample = new StuGradeMainExample();
        stuGradeMainExample.createCriteria()
                .andUniversityIdEqualTo(universityId)
                .andSemesterIdEqualTo(semesterId)
                .andCourseIdEqualTo(courseId)
                .andByWhoEqualTo(teacherId)
                .andDeletedEqualTo(GradeConstant.RECORD_VALID);
        return stuGradeMainMapper.selectByExample(stuGradeMainExample);
    }

    @Override

    /**
     *
     * @Param [stuGradeMainIds]
     * @Return:java.util.List<java.lang.Long>
     * @Author: 陈汉槟
     * @Date: 2019/5/9 16:48
     * @Description: 根据成绩主表id集合获取学生主表成绩集合
     */
    public List<StuGradeMain> selectStuGradeMainIds(List<Long> stuGradeMainIds) {
        StuGradeMainExample stuGradeMainExample = new StuGradeMainExample();
        stuGradeMainExample.createCriteria()
                .andIdIn(stuGradeMainIds);
        List<StuGradeMain> stuGradeMainss =
                stuGradeMainMapper.selectByExample(stuGradeMainExample);
        /*List<StuGradeMain> stuGradeMains = stuGradeMainIds.stream().map(
                stuGradeMainId -> {
                    StuGradeMain stuGradeMain = stuGradeMainMapper.selectByPrimaryKey(stuGradeMainId);
                    return stuGradeMain;
                }
        ).collect(Collectors.toList());*/
        return stuGradeMainss;
    }

    @Override

    /**
     *
     * @Param [universityId, teacherId, semesterId, studentIds, courseId, pageNum]
     * @Return:boolean
     * @Author: 陈汉槟
     * @Date: 2019/5/8 14:24
     * @Description: 根据学校id，教师id,学期id，学生id集合创建学生主表成绩
     * @Modify: 2019/5/15 添加缓存字段功能
     */
    @Transactional
    public boolean insertStuGradeMainByIds(Long universityId, Long teacherId, Long semesterId,
                                           List<Long> studentIds, Long courseId, Integer state) {
        List<StuGradeMain> stuGradeMains = studentIds.stream().map(
                studentid -> {
                    StuGradeMain stuGradeMain = new StuGradeMain();
                    stuGradeMain.setUniversityId(universityId);
                    stuGradeMain.setStudentId(studentid);
                    stuGradeMain.setSemesterId(semesterId);
                    stuGradeMain.setCourseId(courseId);
                    stuGradeMain.setByWho(teacherId);
                    stuGradeMain.setState(state);
                    stuGradeMain.setDeleted(GradeConstant.RECORD_VALID);
                    int success = stuGradeMainMapper.insertSelective(stuGradeMain);
                    if (success == 0) {
                        throw new RuntimeException("成绩主表更新失败");
                    }
                    return stuGradeMain;
                }
        ).collect(Collectors.toList());

        return true;
    }


    @Override

    /**
     *
     * @Param [studentId, deleted]
     * @Return:java.util.List<java.lang.Long>
     * @Author: 陈汉槟
     * @Date: 2019/5/7 1:22
     *@Description: 获取该学生所有学期id
     */
    public List<Long> selectDistinctSemesterId(Long studentId, Integer deleted) {
        /*判断获取的学期id是有效的还是无效的*/
        List<Long> semesterIds = new ArrayList<>();
        if (deleted == null || deleted == GradeConstant.RECORD_VALID) {
            semesterIds = stuGradeMainMapper.selectDistinctSemesterId(studentId, GradeConstant.RECORD_VALID);
        } else {
            semesterIds = selectDistinctSemesterId(studentId, deleted);
        }
        return semesterIds;
    }

    @Override

    /**
     *
     * @Param [studentId]
     * @Return:java.util.List<edu.uni.grademanagement.bean.StuGradeMain>
     * @Author: 陈汉槟
     * @Date: 2019/5/10 21:19
     * @Description: 根据学生id获取该学生所有成绩并按学期id降序排列
     * @Modify: 2019/5/16 添加缓存功能,和学期查找
     */
    public List<StuGradeMain> selectStugradeMainByStudentId(Long studentId, Long semesterId) {
        StuGradeMainExample stuGradeMainExample = new StuGradeMainExample();
        stuGradeMainExample.setOrderByClause("SEMESTER_ID DESC");
        stuGradeMainExample.createCriteria()
                .andStudentIdEqualTo(studentId)
                .andStateBetween(GradeConstant.MAIN_STATE_NORMAL, GradeConstant.MAIN_STATE_SECOND_REBUILD)
                .andDeletedEqualTo(GradeConstant.RECORD_VALID)
                .andCacheEqualTo(GradeConstant.CACHE_INVALID)
                .andSemesterIdEqualTo(semesterId);
        List<StuGradeMain> stuGradeMains =
                stuGradeMainMapper.selectByExample(stuGradeMainExample);
        return stuGradeMains;
    }

    /*@Override

    *//**
     * 报废
     * @Param [stuGradeMainId]
     * @Return:java.lang.Boolean
     * @Author: 陈汉槟
     * @Date: 2019/5/11 10:11
     * @Description: 根据学生id创建学生主表
     *//*
    public Long insertStuGradeMainById(Long stuGradeMainId) {

        StuGradeMain stuGradeMain = stuGradeMainMapper.selectByPrimaryKey(stuGradeMainId);
        // 创建缓存数据，用于更新成绩
        stuGradeMain.setId(null);
        stuGradeMain.setState(GradeConstant.MAIN_STATE_CACHE);
        int i = stuGradeMainMapper.insertSelective(stuGradeMain);
        if (i == 0) {

            // todo 插入报错，后续优化：抛出异常
            return -1L;
        }
        // 获取创建成绩主表的id
        StuGradeMainExample stuGradeMainExample = new StuGradeMainExample();
        stuGradeMainExample.createCriteria()
                .andStateEqualTo(stuGradeMain.getState())
                .andUniversityIdEqualTo(stuGradeMain.getUniversityId())
                .andSemesterIdEqualTo(stuGradeMain.getSemesterId())
                .andByWhoEqualTo(stuGradeMain.getByWho())
                .andStudentIdEqualTo(stuGradeMain.getStudentId())
                .andCourseIdEqualTo(stuGradeMain.getCourseId())
                .andDeletedEqualTo(GradeConstant.RECORD_VALID);
        List<StuGradeMain> stuGradeMains = stuGradeMainMapper.selectByExample(stuGradeMainExample);
        if (stuGradeMains.size() == 1) {
            return stuGradeMains.get(0).getId();
        }
        // todo 获取是多个数据，不符条件，后续优化：报错异常
        return -1L;
    }*/

    @Override
    public void updateStuGradeMainStateToCacheInvalid(Long stuGradeMainId) {
        StuGradeMain stuGradeMain = new StuGradeMain();
        stuGradeMain.setId(stuGradeMainId);
        stuGradeMain.setCache(GradeConstant.CACHE_INVALID);
        stuGradeMain.setState(GradeConstant.MAIN_STATE_NORMAL);
        int success = stuGradeMainMapper.updateByPrimaryKeySelective(stuGradeMain);
        if (success == 0) {
            throw new RuntimeException("成绩主表更新失败");
        }
    }

    @Override

    /**
     *
     * @Param [stuGradeMain]
     * @Return:boolean
     * @Author: 陈汉槟
     * @Date: 2019/5/12 21:34
     * @Description: 根据成绩主表实体类更新成绩主表
     */
    public boolean updateStuGradeMain(StuGradeMain stuGradeMain) {
        int update = stuGradeMainMapper.updateByPrimaryKey(stuGradeMain);
        if (update != 0) {
            return true;
        }
        return false;
    }

    @Override

    /**
     *
     * @Param [stuGradeMainId]
     * @Return:edu.uni.grademanagement.bean.StuGradeMain
     * @Author: 陈汉槟
     * @Date: 2019/5/21 14:14
     * @Description: 根据成绩主表id查询成绩主表
     */
    public StuGradeMain selectByStuGradeMainId(Long stuGradeMainId) {
        StuGradeMain stuGradeMain = stuGradeMainMapper.selectByPrimaryKey(stuGradeMainId);
        return stuGradeMain;
    }

    @Override
    @Transactional
    public boolean insertStuGradeMainByIdsAndTeachingTasks(Long universityId,
                                   Long semesterId, Long courseId, List<Teachingtask> teachingtasks,
                                                           Integer state) {

        teachingtasks.stream().forEach(
                teachingtask -> {
                    Long classId = teachingtask.getClassId();
                    Long teacherId = teachingtask.getWorkerId();
                    StudentExample studentExample = new StudentExample();
                    studentExample.createCriteria()
                            .andClassIdEqualTo(classId)
                            .andDeletedEqualTo(false);
                    List<Student> studentList =
                            studentMapper.selectByExample(studentExample);
//                    List<Long> studentIds =
//                            studentList.stream().map(Student::getUserId).collect(Collectors.toList());
                    List<StuGradeMain> stuGradeMains = studentList.stream().map(
                            student -> {
                                Long studentId = student.getUserId();
                                StuGradeMain stuGradeMain = new StuGradeMain();
                                stuGradeMain.setUniversityId(universityId);
                                stuGradeMain.setSemesterId(semesterId);
                                stuGradeMain.setCourseId(courseId);
                                stuGradeMain.setStudentId(studentId);
                                stuGradeMain.setByWho(teacherId);
                                return stuGradeMain;
                            }
                    ).collect(Collectors.toList());

                    int success = stuGradeMainMapper.insertStuGradeMains(stuGradeMains);
                    if (success == 0) {
                        throw new RuntimeException("成绩主表创建失败");
                    }

                }

        );
        return true;
    }

    @Override
    public List<StuGradeMain> selectByUniversityIdAndCourseIdAndSemesterId(Long universityId, Long semesterId, Long courseId) {
        StuGradeMainExample stuGradeMainExample = new StuGradeMainExample();
        stuGradeMainExample.createCriteria()
                .andUniversityIdEqualTo(universityId)
                .andSemesterIdEqualTo(semesterId)
                .andCourseIdEqualTo(courseId)
                .andDeletedEqualTo(GradeConstant.RECORD_VALID);
        List<StuGradeMain> stuGradeMains =
                stuGradeMainMapper.selectByExample(stuGradeMainExample);
        return stuGradeMains;
    }

    @Override
    @Transactional
    public void rebulidStuGradeMainsSelective(
            Long stuGradeMainId, Integer state) {
        StuGradeMain stuGradeMain =
                stuGradeMainMapper.selectByPrimaryKey(stuGradeMainId);
        stuGradeMain.setId(null);
        stuGradeMain.setScore(0d);
        stuGradeMain.setPoint(0d);
        stuGradeMain.setState(state);
        stuGradeMain.setCache(GradeConstant.CACHE_VALID);
        int insertSuccess =
                stuGradeMainMapper.insertSelective(stuGradeMain);
        if (insertSuccess == 0) {
            throw new RuntimeException("重修成绩创建失败");
        }

        Long universityId = stuGradeMain.getUniversityId();
        Long studentId = stuGradeMain.getStudentId();
        Long semesterId = stuGradeMain.getSemesterId();
        Long courseId = stuGradeMain.getCourseId();
        Long byWho = stuGradeMain.getByWho();

        StuGradeMainExample stuGradeMainExample = new StuGradeMainExample();
        stuGradeMainExample.createCriteria()
                .andUniversityIdEqualTo(universityId)
                .andSemesterIdEqualTo(semesterId)
                .andCourseIdEqualTo(courseId)
                .andStudentIdEqualTo(studentId)
                .andByWhoEqualTo(byWho)
                .andStateEqualTo(state)
                .andDeletedEqualTo(GradeConstant.RECORD_VALID);

        List<StuGradeMain> stuGradeMains =
                stuGradeMainMapper.selectByExample(stuGradeMainExample);
        if (CollectionUtils.isEmpty(stuGradeMains) || stuGradeMains.size() != 1) {
            throw new RuntimeException("重修成绩创建失败");
        }

        Long rebulidStuGradeMainId = stuGradeMains.get(0).getId();

        stuGradeItemService.rebulidStuGradeItemSelective(
                stuGradeMainId, rebulidStuGradeMainId, RoleConstant.TEACHER);

    }
}
