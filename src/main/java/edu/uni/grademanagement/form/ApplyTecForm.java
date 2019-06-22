package edu.uni.grademanagement.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author deschen
 * @Create 2019/6/17
 * @Description 学生申请成绩提交表
 * @Since 1.0.0
 */
@Data
public class ApplyTecForm extends ApplyForm{

    private Double score;
}
