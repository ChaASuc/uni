package edu.uni.professionalcourses.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.uni.professionalcourses.bean.CourseTeachingPlan;
import edu.uni.professionalcourses.bean.CourseTeachingPlanExample;
import edu.uni.professionalcourses.config.ProfessionalCoursesConfig;
import edu.uni.professionalcourses.mapper.CourseTeachingPlanMapper;
import edu.uni.professionalcourses.service.CourseTeachingPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author : 黄永佳
 * create : 2019/4/30 0030 16:36
 * modified :
 * 功能 :
 **/
@Service
public class CourseTeachingPlanServiceImpl implements CourseTeachingPlanService {
@Autowired
private CourseTeachingPlanMapper courseTeachingPlanMapper;
@Autowired
private ProfessionalCoursesConfig globalConfig;
//插入新数据
    @Override
    public boolean insert(CourseTeachingPlan courseTeachingPlan) {
        return courseTeachingPlanMapper.insert(courseTeachingPlan) > 0 ? true : false;
    }
//删除数据
    @Override
    public boolean delete(long id) {
        CourseTeachingPlan courseTeachingPlan=courseTeachingPlanMapper.selectByPrimaryKey(id);
        if(courseTeachingPlan!=null){
            courseTeachingPlan.setDeleted(true);
            courseTeachingPlanMapper.updateByPrimaryKeySelective(courseTeachingPlan);
            return true;
        }
        else {
            return false;
        }
    }
//修改
    @Override
    public boolean update(CourseTeachingPlan courseTeachingPlan) {
        CourseTeachingPlan courseTeachingPlan1=new CourseTeachingPlan();
        courseTeachingPlan1=courseTeachingPlanMapper.selectByPrimaryKey(courseTeachingPlan.getId());
        courseTeachingPlan1.setDeleted(true);
        if(courseTeachingPlanMapper.updateByPrimaryKeySelective(courseTeachingPlan)<=0){
            return false;
        }
        if(courseTeachingPlanMapper.insert(courseTeachingPlan1)<=0){
            return false;
        }
        return true;
    }
//查询
    @Override
    public CourseTeachingPlan select(long id) {
        return courseTeachingPlanMapper.selectByPrimaryKey(id);    }

//分页查询所有
    @Override
    public PageInfo<CourseTeachingPlan> selectPage(int pageNum) {
        PageHelper.startPage(pageNum, globalConfig.getPageSize());    // 开启分页查询，第一次切仅第一次查询时生效
        CourseTeachingPlanExample example = new CourseTeachingPlanExample();
        CourseTeachingPlanExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo(false);
        List<CourseTeachingPlan> courseTeachingPlans = courseTeachingPlanMapper.selectByExample(example);   //  无条件查找
        if(courseTeachingPlans != null)
            return new PageInfo<>(courseTeachingPlans);
        else
            return null;
    }
//根据学校id分页查询所有
    @Override
    public PageInfo<CourseTeachingPlan> selectPageByUniversity(int pageNum, long universityId) {
        PageHelper.startPage(pageNum, globalConfig.getPageSize());    // 开启分页查询，第一次切仅第一次查询时生效
        // 创建查询条件
        CourseTeachingPlanExample example = new CourseTeachingPlanExample();
        CourseTeachingPlanExample.Criteria criteria = example.createCriteria();
        criteria.andUniversityIdEqualTo(universityId).andDeletedEqualTo(false);
        // 根据条件查询
        List<CourseTeachingPlan> courseTeachingPlans = courseTeachingPlanMapper.selectByExample(example);
        if(courseTeachingPlans != null)
            return new PageInfo<>(courseTeachingPlans);
        else
            return null;
    }
//根据课程分页查询所有
    @Override
    public PageInfo<CourseTeachingPlan> selectPageByCourse(int pageNum, long courseId) {
        PageHelper.startPage(pageNum, globalConfig.getPageSize());    // 开启分页查询，第一次切仅第一次查询时生效
        // 创建查询条件
        CourseTeachingPlanExample example = new CourseTeachingPlanExample();
        CourseTeachingPlanExample.Criteria criteria = example.createCriteria();
        criteria.andCourseIdEqualTo(courseId).andDeletedEqualTo(false);
        // 根据条件查询
        List<CourseTeachingPlan> courseTeachingPlans = courseTeachingPlanMapper.selectByExample(example);
        if(courseTeachingPlans != null)
            return new PageInfo<>(courseTeachingPlans);
        else
            return null;
    }
//根据教学计划表id分页查询所有
    @Override
    public PageInfo<CourseTeachingPlan> selectPageByCourseteachingschedule(int pageNum, long courseteachingscheduleId) {
        PageHelper.startPage(pageNum, globalConfig.getPageSize());    // 开启分页查询，第一次切仅第一次查询时生效
        // 创建查询条件
        CourseTeachingPlanExample example = new CourseTeachingPlanExample();
        CourseTeachingPlanExample.Criteria criteria = example.createCriteria();
        criteria.andCourseTeachingScheduleIdEqualTo(courseteachingscheduleId).andDeletedEqualTo(false);
        // 根据条件查询
        List<CourseTeachingPlan> courseTeachingPlans = courseTeachingPlanMapper.selectByExample(example);
        if(courseTeachingPlans != null)
            return new PageInfo<>(courseTeachingPlans);
        else
            return null;    }
////根据实验计划分页查询所有
//    @Override
//    public PageInfo<CourseTeachingPlan> selectPageByCourseexperimentschedule(int pageNum, long courseexperimentscheduleId) {
//        PageHelper.startPage(pageNum, globalConfig.getPageSize());    // 开启分页查询，第一次切仅第一次查询时生效
//        // 创建查询条件
//        CourseTeachingPlanExample example = new CourseTeachingPlanExample();
//        CourseTeachingPlanExample.Criteria criteria = example.createCriteria();
//        criteria.andCourseExperimentScheduleIdEqualTo(courseexperimentscheduleId).andDeletedEqualTo(false);
//        // 根据条件查询
//        List<CourseTeachingPlan> courseTeachingPlans = courseTeachingPlanMapper.selectByExample(example);
//        if(courseTeachingPlans != null)
//            return new PageInfo<>(courseTeachingPlans);
//        else
//            return null;        }

    @Override
    public List<CourseTeachingPlan> selectAll() {
        return courseTeachingPlanMapper.selectByExample(null);
    }

    @Override
    public List<CourseTeachingPlan> selectByCourseId(long course_id) {
        CourseTeachingPlanExample example = new CourseTeachingPlanExample();
        CourseTeachingPlanExample.Criteria criteria = example.createCriteria();
        criteria.andCourseIdEqualTo(course_id).andDeletedEqualTo(false);
        return courseTeachingPlanMapper.selectByExample(example);
    }
}
