package edu.uni.grademanagement.mapper;

import edu.uni.grademanagement.bean.GmApplyAttachment;
import edu.uni.grademanagement.bean.GmApplyAttachmentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GmApplyAttachmentMapper {
    long countByExample(GmApplyAttachmentExample example);

    int deleteByExample(GmApplyAttachmentExample example);

    int deleteByPrimaryKey(Long id);

    int insert(GmApplyAttachment record);

    int insertSelective(GmApplyAttachment record);

    List<GmApplyAttachment> selectByExample(GmApplyAttachmentExample example);

    GmApplyAttachment selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") GmApplyAttachment record, @Param("example") GmApplyAttachmentExample example);

    int updateByExample(@Param("record") GmApplyAttachment record, @Param("example") GmApplyAttachmentExample example);

    int updateByPrimaryKeySelective(GmApplyAttachment record);

    int updateByPrimaryKey(GmApplyAttachment record);
}