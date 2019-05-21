package edu.uni.grademanagement.mapper;


import edu.uni.grademanagement.bean.StuItemGrade;
import edu.uni.grademanagement.bean.StuItemGradeExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StuItemGradeMapper {
    int countByExample(StuItemGradeExample example);

    int deleteByExample(StuItemGradeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(StuItemGrade record);

    int insertSelective(StuItemGrade record);

    int insertStuItemGrades(@Param("records")List<StuItemGrade> records);

    List<StuItemGrade> selectByExample(StuItemGradeExample example);

    StuItemGrade selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") StuItemGrade record, @Param("example") StuItemGradeExample example);

    int updateByExample(@Param("record") StuItemGrade record, @Param("example") StuItemGradeExample example);

    int updateByPrimaryKeySelective(StuItemGrade record);

    int updateByPrimaryKey(StuItemGrade record);
}