package edu.uni.professionalcourses.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * author : 黄永佳
 * create : 2019/6/10 0010 18:46
 * modified :
 * 功能 :
 **/
public class CourseTeachingSchedule2 {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUniversityId() {
        return universityId;
    }

    public void setUniversityId(Long universityId) {
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

    public Integer getTeachHour() {
        return teachHour;
    }

    public void setTeachHour(Integer teachHour) {
        this.teachHour = teachHour;
    }

    public Integer getExperimentHour() {
        return experimentHour;
    }

    public void setExperimentHour(Integer experimentHour) {
        this.experimentHour = experimentHour;
    }

    public String getHomework() {
        return homework;
    }

    public void setHomework(String homework) {
        this.homework = homework;
    }

    public String getActualProgress() {
        return actualProgress;
    }

    public void setActualProgress(String actualProgress) {
        this.actualProgress = actualProgress;
    }

    public String getOutOfPlanReason() {
        return outOfPlanReason;
    }

    public void setOutOfPlanReason(String outOfPlanReason) {
        this.outOfPlanReason = outOfPlanReason;
    }
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Long getByWho() {
        return byWho;
    }

    public void setByWho(Long byWho) {
        this.byWho = byWho;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public long getCourseId() {
        return CourseId;
    }

    public void setCourseId(long courseId) {
        CourseId = courseId;
    }

    private Long universityId;

    private String week;

    private String chapter;

    private String content;

    private Integer teachHour;

    private Integer experimentHour;

    private String homework;

    private String actualProgress;

    private String outOfPlanReason;

    private Date datetime;

    private Long byWho;

    private Boolean deleted;

    private long CourseId;
}
