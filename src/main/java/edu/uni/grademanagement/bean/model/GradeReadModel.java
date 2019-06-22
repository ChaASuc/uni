package edu.uni.grademanagement.bean.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author 陈汉槟
 * @Create 2019/6/8
 * @Description 成绩excel读取实体类
 * @Since 1.0.0
 */
public class GradeReadModel extends BaseRowModel {

    @ExcelProperty(value = "学号", index = 0)
    private String stuNo;  //学号

    @ExcelProperty(value = "姓名", index = 1)
    private String stuName;  //姓名

    @ExcelProperty(value = "序号", index = 2)
    private Integer no;  // 序号 （第几次作业）

    @ExcelProperty(value = "成绩", index = 3)
    private Double score;  // 成绩

    @ExcelProperty(value = "评语", index = 4)
    private String note;  // 成绩

    @ExcelProperty(value = "课程编号",index = 5)
    private String courseNo;  // 课程编号

    @ExcelProperty(value = "课程名称",index = 6)
    private String courseName;   // 课程名称

    @ExcelProperty(value = "班级编号",index = 7)
    private String classNo;  // 班级编号

    @ExcelProperty(value = "班级名称", index = 8)
    private String className;   // 班级名称

//    @ExcelProperty(value = "状态", index = 9)
    private String status;   // 这条数据的情况

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getClassNo() {
        return classNo;
    }

    public void setClassNo(String classNo) {
        this.classNo = classNo;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
