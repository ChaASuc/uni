package edu.uni.grademanagement.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author 陈汉槟
 * @Create 2019/4/23
 * @Description 成绩各项中单个作业DTO
 * @Since 1.0.0
 */
@Data
public class StuGradeItemDetailDto implements Serializable{

    private Long id;

    private Long universityId;

    private Long stuItemGradeId;

    private Long courseItemDetailId;

    private String attachment;

    private Double score;

    private String note;

    private Long courseItemId;

    private Integer number;

    private String content;

    private Integer deleted;

    private Integer cache;
}
