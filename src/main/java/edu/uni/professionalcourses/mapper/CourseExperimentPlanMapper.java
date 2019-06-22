package edu.uni.professionalcourses.mapper;

import edu.uni.professionalcourses.bean.CourseExperimentPlan;
import edu.uni.professionalcourses.bean.CourseExperimentPlanExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CourseExperimentPlanMapper {
    int countByExample(CourseExperimentPlanExample example);

    int deleteByExample(CourseExperimentPlanExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CourseExperimentPlan record);

    int insertSelective(CourseExperimentPlan record);

    List<CourseExperimentPlan> selectByExample(CourseExperimentPlanExample example);

    CourseExperimentPlan selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CourseExperimentPlan record, @Param("example") CourseExperimentPlanExample example);

    int updateByExample(@Param("record") CourseExperimentPlan record, @Param("example") CourseExperimentPlanExample example);

    int updateByPrimaryKeySelective(CourseExperimentPlan record);

    int updateByPrimaryKey(CourseExperimentPlan record);
}