package edu.uni.grademanagement.service;

import edu.uni.grademanagement.bean.GmApply;

import java.util.List;

/**
 * @Author 陈汉槟
 * @Create 2019/6/16
 * @Description 申请事务层
 * @Since 1.0.0
 */
public interface GmApplyService {

    void insertGmApply(GmApply gmApply);

    GmApply selectGmApplyById(Long gmApplyId);

    List<GmApply> selectGmApplyList(
            Long who, Long universityId,
            Long semesterId, Long courseId,
            Integer flag
    );

    Long selectNewGmApply(GmApply gmApply);
}
