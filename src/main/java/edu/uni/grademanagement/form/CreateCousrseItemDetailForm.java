package edu.uni.grademanagement.form;

import edu.uni.grademanagement.bean.CourseItem;
import edu.uni.grademanagement.bean.CourseItemDetail;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author 陈汉槟
 * @Create 2019/5/9
 * @Description
 * @Since 1.0.0
 */
@Data
public class CreateCousrseItemDetailForm implements Serializable {

    private List<CourseItemDetail> courseItemDetails;

    private List<Long> stuGradeMainIds;
}
