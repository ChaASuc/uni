package edu.uni.grademanagement.form;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author 陈汉槟
 * @Create 2019/5/12
 * @Description
 * @Since 1.0.0
 */
@Data
public class InsertStuItmeGradeDtoByExcelForm implements Serializable {

    private Long courseItemId;

    private Long courseItemDetailId;

    private List<Long> stuGradeMainIds;

    private List<ExcelForm> excelForms;


}
