package edu.uni.professionalcourses.ExcelBean;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

/**
 * author : 黄永佳
 * create : 2019/6/13 0013 15:06
 * modified :
 * 功能 :
 **/
public class ExcelCourseTeachingSchedule extends BaseRowModel {
    @ExcelProperty(index = 0)
    private long universityId;
    @ExcelProperty(index = 1)
    private String week;
    @ExcelProperty(index = 2)
    private String chapter;
    @ExcelProperty(index = 3)
    private String content;
    @ExcelProperty(index = 4)
    private int teach_hour;
    @ExcelProperty(index = 5)
    private int experiment_hour;
    @ExcelProperty(index = 6)
    private String homework;
    @ExcelProperty(index = 7)
    private String actual_progress;
    @ExcelProperty(index = 8)
    private String out_of_plan_reason;

    public long getUniversityId() {
        return universityId;
    }

    public void setUniversityId(long universityId) {
        this.universityId = universityId;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
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

    public int getTeach_hour() {
        return teach_hour;
    }

    public void setTeach_hour(int teach_hour) {
        this.teach_hour = teach_hour;
    }

    public int getExperiment_hour() {
        return experiment_hour;
    }

    public void setExperiment_hour(int experiment_hour) {
        this.experiment_hour = experiment_hour;
    }

    public String getHomework() {
        return homework;
    }

    public void setHomework(String homework) {
        this.homework = homework;
    }

    public String getActual_progress() {
        return actual_progress;
    }

    public void setActual_progress(String actual_progress) {
        this.actual_progress = actual_progress;
    }

    public String getOut_of_plan_reason() {
        return out_of_plan_reason;
    }

    public void setOut_of_plan_reason(String out_of_plan_reason) {
        this.out_of_plan_reason = out_of_plan_reason;
    }
}
