package edu.uni.grademanagement.form;

import com.alibaba.excel.ExcelReader;
import edu.uni.educateAffair.service.SemesterService;
import edu.uni.grademanagement.bean.model.GradeReadModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author 陈汉槟
 * @Create 2019/6/9
 * @Description
 * @Since 1.0.0
 */
@Data
public class ExcelDtoForm implements Serializable {

    private Long universityId;

    private Long courseItemId;

    private Long courseItemDetailId;

    private Long teacherId;

    private Long classId;

    private Long semesterId;

    private Long courseId;

    private List<GradeReadModel> GradeReadModels;

}
