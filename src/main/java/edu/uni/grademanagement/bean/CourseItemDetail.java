package edu.uni.grademanagement.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uni.grademanagement.constants.GradeConstant;

import java.util.Date;

public class CourseItemDetail {
    private Long id;

    private Long universityId;

    private Long courseItemId;

    private Integer number;

    private String content;

    private Date datetime;

    @JsonProperty("teacherId")
    private Long byWho;

    private Integer deleted;

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

    public Long getCourseItemId() {
        return courseItemId;
    }

    public void setCourseItemId(Long courseItemId) {
        this.courseItemId = courseItemId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

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

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}