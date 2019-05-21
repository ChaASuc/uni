package edu.uni.grademanagement.dto;

import edu.uni.grademanagement.bean.CourseItemDetail;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author 陈汉槟
 * @Create 2019/5/5
 * @Description
 * @Since 1.0.0
 */
@Data
public class CourseItemDto implements Serializable {

    private Long id;

    private Long universityId;

    private Long courseId;

    private Integer name;

    private Double rate;

    private Integer count;

    private Date datetime;

    private Long byWho;

    private Integer deleted;

    private List<CourseItemDetail> courseItemDetails;
}
