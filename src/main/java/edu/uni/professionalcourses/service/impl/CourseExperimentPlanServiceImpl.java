package edu.uni.professionalcourses.service.impl;

import edu.uni.professionalcourses.bean.CourseExperimentPlan;
import edu.uni.professionalcourses.bean.CourseExperimentPlanExample;
import edu.uni.professionalcourses.mapper.CourseExperimentPlanMapper;
import edu.uni.professionalcourses.service.CourseExperimentPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author : 黄永佳
 * create : 2019/6/9 0009 21:44
 * modified :
 * 功能 :
 **/
@Service
public class CourseExperimentPlanServiceImpl implements CourseExperimentPlanService {
    @Autowired
    private CourseExperimentPlanMapper courseExperimentPlanMapper;

    @Override
    public boolean insert(CourseExperimentPlan courseExperimentPlan) {
        return courseExperimentPlanMapper.insert(courseExperimentPlan) > 0 ? true : false ;
    }

    @Override
    public boolean delete(long id) {
        CourseExperimentPlan courseExperimentPlan=new CourseExperimentPlan();
        courseExperimentPlan=courseExperimentPlanMapper.selectByPrimaryKey(id);
        courseExperimentPlan.setDeleted(true);


        return courseExperimentPlanMapper.updateByPrimaryKeySelective(courseExperimentPlan) > 0 ? true : false;
    }

    @Override
    public boolean update(CourseExperimentPlan courseExperimentPlan) {
        CourseExperimentPlan courseExperimentPlan1=new CourseExperimentPlan();
        courseExperimentPlan1=courseExperimentPlanMapper.selectByPrimaryKey(courseExperimentPlan.getId());
        courseExperimentPlan1.setDeleted(true);
        courseExperimentPlanMapper.insert(courseExperimentPlan1);

        return courseExperimentPlanMapper.updateByPrimaryKeySelective(courseExperimentPlan) > 0 ? true : false;
    }

    @Override
    public CourseExperimentPlan select(long id) {
        CourseExperimentPlan courseExperimentPlan=new CourseExperimentPlan();
        courseExperimentPlan=courseExperimentPlanMapper.selectByPrimaryKey(id);

        if (courseExperimentPlan.getDeleted()==false){
            return courseExperimentPlan;
        }else {
            return null;
        }
    }

    @Override
    public List<CourseExperimentPlan> selectAll() {
        return courseExperimentPlanMapper.selectByExample(null);
    }
    @Override
    public List<CourseExperimentPlan> selectByCourseId(long course_id) {
        CourseExperimentPlanExample example = new CourseExperimentPlanExample();
        CourseExperimentPlanExample.Criteria criteria = example.createCriteria();
        criteria.andCourseIdEqualTo(course_id).andDeletedEqualTo(false);
        return courseExperimentPlanMapper.selectByExample(example);
    }
}
