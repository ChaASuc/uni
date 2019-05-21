package edu.uni.grademanagement.form;

import edu.uni.grademanagement.bean.CourseItem;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.List;

/**
 * @Author 陈汉槟
 * @Create 2019/5/9
 * @Description
 * @Since 1.0.0
 */
@Data
public class CreateCousrseItemForm implements Serializable {

    private List<CourseItem> courseItems;

    private List<Long> stuGradeMainIds;
}
