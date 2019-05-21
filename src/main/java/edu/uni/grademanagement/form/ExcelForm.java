package edu.uni.grademanagement.form;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author 陈汉槟
 * @Create 2019/5/12
 * @Description
 * @Since 1.0.0
 */
@Data
public class ExcelForm implements Serializable {


    private Integer courseNumber;

    private String courseName;

    private String stuNo;

    private Double score;

    private String note;
}
