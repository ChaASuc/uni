package edu.uni.grademanagement.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.List;

/**
 * @Author 陈汉槟
 * @Create 2019/5/7
 * @Description
 * @Since 1.0.0
 */
@Data
public class GradeFilter implements Serializable {

    private List<Long> semesterIds;

    private List<Long> courseIds;

    private List<Long> classIds;

    private List<Long> teacherIds;

    private Integer pageNum;
}
