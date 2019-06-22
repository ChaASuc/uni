package edu.uni.professionalcourses.ExcelBean;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

/**
 * author : 黄永佳
 * create : 2019/6/13 0013 14:53
 * modified :
 * 功能 :
 **/
public class ExcelCourse_syllabus_description extends BaseRowModel {
    @ExcelProperty(index = 0)
    private Long universityId;
    @ExcelProperty(index=1)
    private long course_syllabus_id;
    @ExcelProperty(index = 2)
    private String chapter;
    @ExcelProperty(index = 3)
    private String content;
    @ExcelProperty(index = 4)
    private int teaching_hour;
    @ExcelProperty(index = 5)
    private String assessment_requirement;
    @ExcelProperty(index = 6)
    private long course_book_id;
    @ExcelProperty(index = 7)
    private String instruction;

    public Long getUniversityId() {
        return universityId;
    }

    public void setUniversityId(Long universityId) {
        this.universityId = universityId;
    }

    public long getCourse_syllabus_id() {
        return course_syllabus_id;
    }

    public void setCourse_syllabus_id(long course_syllabus_id) {
        this.course_syllabus_id = course_syllabus_id;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTeaching_hour() {
        return teaching_hour;
    }

    public void setTeaching_hour(int teaching_hour) {
        this.teaching_hour = teaching_hour;
    }

    public String getAssessment_requirement() {
        return assessment_requirement;
    }

    public void setAssessment_requirement(String assessment_requirement) {
        this.assessment_requirement = assessment_requirement;
    }

    public long getCourse_book_id() {
        return course_book_id;
    }

    public void setCourse_book_id(long course_book_id) {
        this.course_book_id = course_book_id;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
