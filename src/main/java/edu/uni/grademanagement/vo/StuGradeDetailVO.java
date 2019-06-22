package edu.uni.grademanagement.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uni.grademanagement.dto.StuGradeItemDetailDto;
import edu.uni.grademanagement.dto.StuGradeItemDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author 陈汉槟
 * @Create 2019/4/23
 * @Description
 * @Since 1.0.0
 * @ Modify: 成绩作业表显示格式
 */
@Api(value = "成绩作业对象")
@Data
public class StuGradeDetailVO implements Serializable {


    @ApiModelProperty(value = "学生用户id", name = "studentId",
            notes = "学生用户id", required = true)
    @JsonProperty(value = "studentId", required = true)
    private Long studentId;

    @ApiModelProperty(value = "学生名", name = "studentName",
            notes = "学生名", required = true)
    @JsonProperty(value = "studentName", required = true)
    private String studentName;

    @ApiModelProperty(value = "学生号", name = "studentName",
            notes = "学生号", required = true)
    @JsonProperty(value = "stuNo", required = true)
    private String stuNo;

    @ApiModelProperty(value = "成绩项总评", name = "score", required = true)
    @JsonProperty(value = "score", required = true)
    private Double score;


    @ApiModelProperty(value = "成绩作业项", name = "stuGradeItemDetailDtos", required = true)
    @JsonProperty(value = "stuGradeItemDetailDtos", required = true)
    private List<StuGradeItemDetailDto> stuGradeItemDetailDtos;

}
