package edu.uni.grademanagement.dto;

import edu.uni.grademanagement.constants.GradeConstant;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author 陈汉槟
 * @Create 2019/4/16
 * @Description 成绩各项中单个DTO
 * @Since 1.0.0
 */
@Data
public class StuGradeItemDto implements Serializable{

    private Long id;

    private Long universityId;

    private Long stuGradeMainId;

    private Long courseItemId;

    private Double score;

    private String note;

    private Long byWho;

    private Long courseId;

    private Integer name;

    private Double rate;

    private Integer count;

    private Integer deleted;

    private Integer cache;


    /*单项中每次作业*/
    private List<StuGradeItemDetailDto> stuGradeItemDetailDtos;

}
