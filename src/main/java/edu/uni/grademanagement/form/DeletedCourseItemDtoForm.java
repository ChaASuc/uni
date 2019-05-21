package edu.uni.grademanagement.form;

import edu.uni.grademanagement.dto.CourseItemDto;
import lombok.Data;

import java.util.List;

/**
 * @Author 陈汉槟
 * @Create 2019/5/10
 * @Description
 * @Since 1.0.0
 */
@Data
public class DeletedCourseItemDtoForm {

    private List<CourseItemDto> courseItemDtos;

    private List<Long> stuGradeMainIds;
}
