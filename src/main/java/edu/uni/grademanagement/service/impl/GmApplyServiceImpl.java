package edu.uni.grademanagement.service.impl;

import edu.uni.grademanagement.bean.GmApply;
import edu.uni.grademanagement.bean.GmApplyExample;
import edu.uni.grademanagement.constants.GradeConstant;

import edu.uni.grademanagement.constants.RoleConstant;
import edu.uni.grademanagement.mapper.GmApplyMapper;
import edu.uni.grademanagement.service.GmApplyApprovalService;
import edu.uni.grademanagement.service.GmApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author deschen
 * @Create 2019/6/17
 * @Description
 * @Since 1.0.0
 */
@Service
public class GmApplyServiceImpl implements GmApplyService {

    @Autowired
    private GmApplyMapper gmApplyMapper;

    @Autowired
    private GmApplyApprovalService gmApplyApprovalService;

    @Override
    @Transactional
    public void insertGmApply(GmApply gmApply) {
        int success = gmApplyMapper.insertSelective(gmApply);
        if (success == 0) {
            throw new RuntimeException("创建申请失败");
        }

        Long gmapplyId = null;
        gmapplyId = selectNewGmApply(gmApply);

        gmApplyApprovalService.insertGmApplyApproval(
                gmapplyId, gmApply.getSendWho());

    }

    @Override
    public GmApply selectGmApplyById(Long gmApplyId) {
        GmApply gmApply =
                gmApplyMapper.selectByPrimaryKey(gmApplyId);
        return gmApply;
    }

    /**
     *
     * @param byWho
     * @param universityId
     * @param semesterId
     * @param courseId
     * @param flag  // 用于申请还是审核
     * @return
     */
    @Override
    public List<GmApply> selectGmApplyList(
            Long who, Long universityId, Long semesterId, Long courseId, Integer flag) {
        GmApplyExample gmApplyExample = new GmApplyExample();
        gmApplyExample.setOrderByClause("DATETIME DESC");
        GmApplyExample.Criteria criteria = gmApplyExample.createCriteria()
                .andDeletedEqualTo(GradeConstant.RECORD_VALID);

        if (flag.equals(GradeConstant.APPLY)) {
            // 用于申请
            criteria.andByWhoEqualTo(who);
        }else {
            // 用于审核
            criteria.andSendWhoEqualTo(who);
        }

        if (universityId != null) {
            criteria.andUniversityIdEqualTo(universityId);
        }
        if (semesterId != null) {
            criteria.andSemesterIdEqualTo(semesterId);
        }
        if (courseId != null) {
            criteria.andCourseIdEqualTo(courseId);
        }
        List<GmApply> gmApplies =
                gmApplyMapper.selectByExample(gmApplyExample);
        return gmApplies;
    }

    @Override
    public Long selectNewGmApply(GmApply gmApply) {

        GmApplyExample gmApplyExample =
                new GmApplyExample();
        gmApplyExample.setOrderByClause("DATETIME DESC");
        gmApplyExample.createCriteria()
                .andUniversityIdEqualTo(gmApply.getUniversityId())
                .andSemesterIdEqualTo(gmApply.getSemesterId())
                .andCourseIdEqualTo(gmApply.getCourseId())
                .andStudentIdEqualTo(gmApply.getStudentId())
                .andByWhoEqualTo(gmApply.getByWho())
                .andSendWhoEqualTo(gmApply.getSendWho())
                .andDeletedEqualTo(GradeConstant.RECORD_VALID);
        List<GmApply> gmApplies =
                gmApplyMapper.selectByExample(gmApplyExample);
        GmApply newGmApply = gmApplies.get(0);
        if (newGmApply == null) {
            throw new RuntimeException("申请不存在");
        }
        return newGmApply.getId();
    }


}
