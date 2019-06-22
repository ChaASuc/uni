package edu.uni.professionalcourses.ExcelBean;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

/**
 * author : 黄永佳
 * create : 2019/6/13 0013 15:55
 * modified :
 * 功能 :
 **/
public class ExcelLearningProcessSemesterAllocation extends BaseRowModel {
    @ExcelProperty(index = 0)
    private long university;
    @ExcelProperty(index = 1)
    private long learningProcessId;
    @ExcelProperty(index = 2)
    private String credits;
    @ExcelProperty(index = 3)
    private String period;
    @ExcelProperty(index = 4)
    private String semester;

    public long getUniversity() {
        return university;
    }

    public void setUniversity(long university) {
        this.university = university;
    }

    public long getLearningProcessId() {
        return learningProcessId;
    }

    public void setLearningProcessId(long learningProcessId) {
        this.learningProcessId = learningProcessId;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
