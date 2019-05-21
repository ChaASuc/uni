package edu.uni.grademanagement.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
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
 * @ Modify: 成绩主表显示格式
 */
@Api(value = "主表成绩对象", description = "显示主表成绩")
@Data
public class StuGradeVO implements Serializable {

    @ApiModelProperty(value = "主表成绩id", name = "id", notes = "主表成绩id", required = true)
    @JsonProperty(value = "id", required = true)
    private Long id;

    @ApiModelProperty(value = "学校id", name = "universityId",
            notes = "学校id", required = true)
    @JsonProperty(value = "universityId", required = true)
    private Long universityId;

    @ApiModelProperty(value = "学校名字", name = "universityName",
            notes = "学校名字", required = true)
    @JsonProperty(value = "universityName", required = true)
    private String universityName;

    @ApiModelProperty(value = "学期id", name = "semesterId",
            notes = "学期id", required = true)
    @JsonProperty(value = "semesterId", required = true)
    private Long semesterId;

    @ApiModelProperty(value = "学期名", name = "semesterName",
            notes = "学期名", required = true)
    @JsonProperty(value = "semesterName", required = true)
    private String semesterName;

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

    @ApiModelProperty(value = "班级id", name = "classId",
            notes = "班级id", required = true)
    @JsonProperty(value = "classId", required = true)
    private Long classId;

    @ApiModelProperty(value = "班级名", name = "className",
            notes = "班级名", required = true)
    @JsonProperty(value = "className", required = true)
    private String className;

    @ApiModelProperty(value = "课程id", name = "courseId",
            notes = "课程id", required = true)
    @JsonProperty(value = "courseId", required = true)
    private Long courseId;

    @ApiModelProperty(value = "课程编号", name = "number",
            notes = "课程编号", required = true)
    private String number;

    @ApiModelProperty(value = "课程名", name = "name",
            notes = "课程名", required = true)
    private String courseName;

    @ApiModelProperty(value = "课程类别", name = "categoryId",
            notes = "课程类别", required = true)
    private Long categoryId;

    @ApiModelProperty(value = "课程类别", name = "categoryName",
            notes = "课程类别", required = true)
    private String categoryName;

    @ApiModelProperty(value = "课程种类id", name = "speciesId",
            notes = "课程种类", required = true)
    private Long speciesId;

    @ApiModelProperty(value = "课程种类", name = "speciesName",
            notes = "课程种类", required = true)
    private String speciesName;

    @ApiModelProperty(value = "分数", name = "score",
            notes = "分数", required = true)
    @JsonProperty(value = "score", required = true)
    private Double score;

    @ApiModelProperty(value = "绩点", name = "point",
            notes = "绩点", required = true)
    @JsonProperty(value = "point", required = true)
    private Double point;

    @ApiModelProperty(value = "状态", name = "state",
            notes = "成绩状态(0未评分 1 缓存 2正常 3第一次重修 4第二次重修)", required = true)
    @JsonProperty(value = "state", required = true)
    private Integer state;

    @ApiModelProperty(value = "写入者id", name = "byWho",
            notes = "写入者id", required = true)
    @JsonProperty(value = "teacherId", required = true)
    private Long byWho;

    @ApiModelProperty(value = "写入者名", name = "who",
            notes = "写入者名", required = true)
    @JsonProperty(value = "who", required = true)
    private String who;

    @ApiModelProperty(value = "创建时间", name = "datetime",
            notes = "创建时间",required = true)
    @JsonProperty(value = "datetime", required = true)
    private Date datetime;

    @ApiModelProperty(value = "是否有效", name = "deleted",
            notes = "是否有效",required = true)
    @JsonProperty(value = "deleted", required = true)
    private Integer deleted;

    @ApiModelProperty(value = "是否缓存", name = "cache",
            notes = "是否缓存",required = true)
    @JsonProperty(value = "cache", required = true)
    private Integer cache;

    @ApiModelProperty(value = "成绩项", name = "stuGradeItemDtos", required = true)
    @JsonProperty(value = "stuGradeItemDtos", required = true)
    private List<StuGradeItemDto> stuGradeItemDtos;

}
