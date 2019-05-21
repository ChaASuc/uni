package edu.uni.grademanagement.mapper;

import edu.uni.grademanagement.bean.CourseItem;
import edu.uni.grademanagement.bean.CourseItemDetail;
import edu.uni.grademanagement.bean.CourseItemDetailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CourseItemDetailMapper {
    long countByExample(CourseItemDetailExample example);

    int deleteByExample(CourseItemDetailExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CourseItemDetail record);

    int insertSelective(CourseItemDetail record);

    int insertCourseItemDetailLists(@Param(value="records")
                                            List<CourseItemDetail> courseItemDetailLists);

    List<CourseItemDetail> selectByExample(CourseItemDetailExample example);

    CourseItemDetail selectByPrimaryKey(Long id);

    /*自定义sql*/
    CourseItemDetail selectByCourseItemDetail(CourseItemDetail courseItemDetail);

    int updateByExampleSelective(@Param("record") CourseItemDetail record, @Param("example") CourseItemDetailExample example);

    int updateByExample(@Param("record") CourseItemDetail record, @Param("example") CourseItemDetailExample example);

    int updateByPrimaryKeySelective(CourseItemDetail record);

    int updateByPrimaryKey(CourseItemDetail record);

    int updateDeletedByPrimaryKey(Long id, Integer deleted);
}