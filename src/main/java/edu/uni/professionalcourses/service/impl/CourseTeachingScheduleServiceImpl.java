package edu.uni.professionalcourses.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.uni.professionalcourses.bean.CourseTeachingPlan;
import edu.uni.professionalcourses.bean.CourseTeachingSchedule;
import edu.uni.professionalcourses.bean.CourseTeachingScheduleExample;
import edu.uni.professionalcourses.config.ProfessionalCoursesConfig;
import edu.uni.professionalcourses.mapper.CourseTeachingPlanMapper;
import edu.uni.professionalcourses.mapper.CourseTeachingScheduleMapper;
import edu.uni.professionalcourses.service.CourseTeachingScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author : 黄永佳
 * create : 2019/5/5 0005 16:35
 * modified :
 * 功能 :实现 CourseTeachingScheduleService接口
 **/
@Service
public class CourseTeachingScheduleServiceImpl implements CourseTeachingScheduleService {
    @Autowired
    private CourseTeachingScheduleMapper courseTeachingScheduleMapper;
    @Autowired
    private CourseTeachingPlanMapper courseTeachingPlanMapper;
    @Autowired
    private ProfessionalCoursesConfig globalConfig;
//    增加
    @Override
    public boolean insert(CourseTeachingSchedule courseTeachingSchedule,long courseId) {
       if(courseTeachingScheduleMapper.insert(courseTeachingSchedule)<0){
           return false;
       }
        CourseTeachingPlan courseTeachingPlan=new CourseTeachingPlan();
       System.out.println(courseTeachingSchedule.getId());
       courseTeachingPlan.setByWho(courseTeachingSchedule.getByWho());
       courseTeachingPlan.setDeleted(courseTeachingSchedule.getDeleted());
       courseTeachingPlan.setCourseTeachingScheduleId(courseTeachingSchedule.getId());
       courseTeachingPlan.setUniversityId(courseTeachingSchedule.getUniversityId());
       courseTeachingPlan.setDatetime(courseTeachingSchedule.getDatetime());
       courseTeachingPlan.setCourseId(courseId);
       return courseTeachingPlanMapper.insert(courseTeachingPlan) > 0 ? true : false;

    }
//删除
    @Override
    public boolean delete(long id) {
        CourseTeachingSchedule courseTeachingSchedule=courseTeachingScheduleMapper.selectByPrimaryKey(id);
        if(courseTeachingSchedule!=null){
            courseTeachingSchedule.setDeleted(true);
            courseTeachingScheduleMapper.updateByPrimaryKeySelective(courseTeachingSchedule);
            return true;
        }
        else {
            return false;
        }
    }
//修改
    @Override
    public boolean update(CourseTeachingSchedule courseTeachingSchedule) {

        CourseTeachingSchedule courseTeachingSchedule1=new CourseTeachingSchedule();
        courseTeachingSchedule1=courseTeachingScheduleMapper.selectByPrimaryKey(courseTeachingSchedule.getId());
        courseTeachingSchedule1.setDeleted(true);
        if(courseTeachingScheduleMapper.updateByPrimaryKeySelective(courseTeachingSchedule)<=0){
            return false;
        }
        if(courseTeachingScheduleMapper.insert(courseTeachingSchedule1)<=0){
            return false;
        }
        return true;
    }
//查询
    @Override
    public CourseTeachingSchedule select(long id) {
        return courseTeachingScheduleMapper.selectByPrimaryKey(id);
    }
//分页查询
    @Override
    public PageInfo<CourseTeachingSchedule> selectPage(int pageNum) {
        PageHelper.startPage(pageNum, globalConfig.getPageSize());    // 开启分页查询，第一次切仅第一次查询时生效
        CourseTeachingScheduleExample example = new CourseTeachingScheduleExample();
        CourseTeachingScheduleExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo(false);
        List<CourseTeachingSchedule> courseTeachingSchedules = courseTeachingScheduleMapper.selectByExample(example);   //  无条件查找
        if(courseTeachingSchedules != null)
            return new PageInfo<>(courseTeachingSchedules);
        else
            return null;
    }
//根据学校id分页查询
    @Override
    public PageInfo<CourseTeachingSchedule> selectPageByUniversity(int pageNum, long universityId) {
        PageHelper.startPage(pageNum, globalConfig.getPageSize());    // 开启分页查询，第一次切仅第一次查询时生效
        // 创建查询条件
        CourseTeachingScheduleExample example = new CourseTeachingScheduleExample();
        CourseTeachingScheduleExample.Criteria criteria = example.createCriteria();
        criteria.andUniversityIdEqualTo(universityId).andDeletedEqualTo(false);
        // 根据条件查询
        List<CourseTeachingSchedule> courseTeachingSchedules= courseTeachingScheduleMapper.selectByExample(example);
        if(courseTeachingSchedules != null)
            return new PageInfo<>(courseTeachingSchedules);
        else
            return null;    }

    @Override
    public List<CourseTeachingSchedule> selectAll() {
        return courseTeachingScheduleMapper.selectByExample(null);
    }
}
