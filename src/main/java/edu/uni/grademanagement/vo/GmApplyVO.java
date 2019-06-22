package edu.uni.grademanagement.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uni.grademanagement.bean.GmApply;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author deschen
 * @Create 2019/6/17
 * @Description 申请情况显示
 * @Since 1.0.0
 */
@Data
public class GmApplyVO extends GmApply {

    private String universityName;

    private String semesterName;

    private String studentName;

    private String courseName;

    private Integer courseItemName;

    private String courseItemDetailName;

    private Double oldScore;

    private Double newScore;

    private String byWhoName;

    private String sendWhoName;

    private Long gmApplyApprovalId;

    private Integer applicationStatus;

    private String reply;
}
