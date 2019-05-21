package edu.uni.grademanagement.bean;

import java.util.Date;

public class GmApply {
    private Long id;

    private Long universityId;

    private Long approvalMainId;

    private Long semesterId;

    private Long studentId;

    private Long courseId;

    private Long stuMainGradeId;

    private Long byWho;

    private Long sendWho;

    private Date datetime;

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

    public Long getApprovalMainId() {
        return approvalMainId;
    }

    public void setApprovalMainId(Long approvalMainId) {
        this.approvalMainId = approvalMainId;
    }

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getStuMainGradeId() {
        return stuMainGradeId;
    }

    public void setStuMainGradeId(Long stuMainGradeId) {
        this.stuMainGradeId = stuMainGradeId;
    }

    public Long getByWho() {
        return byWho;
    }

    public void setByWho(Long byWho) {
        this.byWho = byWho;
    }

    public Long getSendWho() {
        return sendWho;
    }

    public void setSendWho(Long sendWho) {
        this.sendWho = sendWho;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}