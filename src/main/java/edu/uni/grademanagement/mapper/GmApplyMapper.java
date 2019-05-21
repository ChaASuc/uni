package edu.uni.grademanagement.mapper;

import edu.uni.grademanagement.bean.GmApply;
import edu.uni.grademanagement.bean.GmApplyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GmApplyMapper {
    long countByExample(GmApplyExample example);

    int deleteByExample(GmApplyExample example);

    int deleteByPrimaryKey(Long id);

    int insert(GmApply record);

    int insertSelective(GmApply record);

    List<GmApply> selectByExample(GmApplyExample example);

    GmApply selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") GmApply record, @Param("example") GmApplyExample example);

    int updateByExample(@Param("record") GmApply record, @Param("example") GmApplyExample example);

    int updateByPrimaryKeySelective(GmApply record);

    int updateByPrimaryKey(GmApply record);
}