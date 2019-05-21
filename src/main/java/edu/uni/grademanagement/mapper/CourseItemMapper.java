package edu.uni.grademanagement.mapper;

import edu.uni.grademanagement.bean.CourseItem;
import edu.uni.grademanagement.bean.CourseItemExample;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;

public interface CourseItemMapper {
    long countByExample(CourseItemExample example);

    int deleteByExample(CourseItemExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CourseItem record);

    int insertCourseItemLists(@Param(value="courseItemLists") List<CourseItem> courseItemLists);

    int insertSelective(CourseItem record);

    List<CourseItem> selectByExample(CourseItemExample example);

    CourseItem selectByPrimaryKey(Long id);

    /*自定义sql*/
    CourseItem selectByCourseItem(@Param("record") CourseItem courseItem);

    int updateByExampleSelective(@Param("record") CourseItem record, @Param("example") CourseItemExample example);

    int updateByExample(@Param("record") CourseItem record, @Param("example") CourseItemExample example);

    int updateByPrimaryKeySelective(CourseItem record);

    int updateByPrimaryKey(CourseItem record);

    int updateListByPrimaryKeySelective(@Param(value="courseItemLists") List<CourseItem> courseItemLists);
}