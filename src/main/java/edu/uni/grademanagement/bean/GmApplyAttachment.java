package edu.uni.grademanagement.bean;

import java.util.Date;

public class GmApplyAttachment {
    private Long id;

    private String attachment;

    private String reason;

    private Date datetime;

    private Long byWho;

    private Integer deleted;

    private Long gmApplyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment == null ? null : attachment.trim();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
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

    public Long getGmApplyId() {
        return gmApplyId;
    }

    public void setGmApplyId(Long gmApplyId) {
        this.gmApplyId = gmApplyId;
    }
}