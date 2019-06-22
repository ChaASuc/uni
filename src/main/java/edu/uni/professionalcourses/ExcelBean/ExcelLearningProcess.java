package edu.uni.professionalcourses.ExcelBean;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

/**
 * author : 黄永佳
 * create : 2019/6/13 0013 15:41
 * modified :
 * 功能 :
 **/
public class ExcelLearningProcess extends BaseRowModel {
    @ExcelProperty(index = 0)
    private long universityId;
    @ExcelProperty(index = 1)
    private long SpecialtyId;
    @ExcelProperty(index = 2)
    private long CourseId;
    @ExcelProperty(index = 3)
    private int term;
    @ExcelProperty(index = 4)
    private String note;

    public long getUniversityId() {
        return universityId;
    }

    public void setUniversityId(long universityId) {
        this.universityId = universityId;
    }

    public long getSpecialtyId() {
        return SpecialtyId;
    }

    public void setSpecialtyId(long specialtyId) {
        SpecialtyId = specialtyId;
    }

    public long getCourseId() {
        return CourseId;
    }

    public void setCourseId(long courseId) {
        CourseId = courseId;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
