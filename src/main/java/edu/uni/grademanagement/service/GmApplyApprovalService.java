package edu.uni.grademanagement.service;

import edu.uni.grademanagement.bean.GmApplyApproval;

import java.util.List;

/**
 * @Author deschen
 * @Create 2019/6/17
 * @Description
 * @Since 1.0.0
 */
public interface GmApplyApprovalService {

    void insertGmApplyApproval(
            Long gmApplyId, Long sendWho
    );

    GmApplyApproval selectGmApplyApproval(Long gmApplyId);

    List<GmApplyApproval> selectGmApplyApprovalByByWho(
            Long byWho
    );

    void updateGmApplyApproval(GmApplyApproval gmApplyApproval);
}
