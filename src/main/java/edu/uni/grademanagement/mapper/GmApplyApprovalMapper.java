package edu.uni.grademanagement.mapper;

import edu.uni.grademanagement.bean.GmApplyApproval;
import edu.uni.grademanagement.bean.GmApplyApprovalExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GmApplyApprovalMapper {
    long countByExample(GmApplyApprovalExample example);

    int deleteByExample(GmApplyApprovalExample example);

    int deleteByPrimaryKey(Long id);

    int insert(GmApplyApproval record);

    int insertSelective(GmApplyApproval record);

    List<GmApplyApproval> selectByExample(GmApplyApprovalExample example);

    GmApplyApproval selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") GmApplyApproval record, @Param("example") GmApplyApprovalExample example);

    int updateByExample(@Param("record") GmApplyApproval record, @Param("example") GmApplyApprovalExample example);

    int updateByPrimaryKeySelective(GmApplyApproval record);

    int updateByPrimaryKey(GmApplyApproval record);
}