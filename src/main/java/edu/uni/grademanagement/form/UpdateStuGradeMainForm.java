package edu.uni.grademanagement.form;

import lombok.Data;

import java.util.List;

/**
 * @Author 陈汉槟
 * @Create 2019/5/12
 * @Description
 * @Since 1.0.0
 */
@Data
public class UpdateStuGradeMainForm {

    private List<Long> stuItemGradeIds;

    private List<Long> cacheStuItemGradeIds;
}
