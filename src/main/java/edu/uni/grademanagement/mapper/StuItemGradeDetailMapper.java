package edu.uni.grademanagement.mapper;

import edu.uni.grademanagement.bean.StuItemGradeDetail;
import edu.uni.grademanagement.bean.StuItemGradeDetailExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StuItemGradeDetailMapper {
    int countByExample(StuItemGradeDetailExample example);

    int deleteByExample(StuItemGradeDetailExample example);

    int deleteByPrimaryKey(Long id);

    int insert(StuItemGradeDetail record);

    int insertSelective(StuItemGradeDetail record);

    List<StuItemGradeDetail> selectByExample(StuItemGradeDetailExample example);

    StuItemGradeDetail selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") StuItemGradeDetail record, @Param("example") StuItemGradeDetailExample example);

    int updateByExample(@Param("record") StuItemGradeDetail record, @Param("example") StuItemGradeDetailExample example);

    int updateByPrimaryKeySelective(StuItemGradeDetail record);

    int updateByPrimaryKey(StuItemGradeDetail record);
}