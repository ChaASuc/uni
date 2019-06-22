package edu.uni.grademanagement.form;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author deschen
 * @Create 2019/6/17
 * @Description 学生申请成绩提交表
 * @Since 1.0.0
 */
@Data
public class ApplyForm {

    private Long universityId;
    private Long semesterId;
    private Long studentId;
    private Long courseId;
    private Long courseItemId;
    private Long courseItemDetailId;
    private Long stuItemGradeDetailId;
    private Long byWho;
    private Long sendWho;
    private MultipartFile file;
    private String reason;
}
