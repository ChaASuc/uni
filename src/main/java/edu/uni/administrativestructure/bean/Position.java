package edu.uni.administrativestructure.bean;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * author:黄育林
 * create:2019.5.7
 * modified:2019.5.9
 * 功能：岗位信息实体类
 */
public class Position extends BaseRowModel {
    //    主id
    @ExcelProperty(value = "岗位信息id",index = 0)
    private Long id;
    //    学校id
    @ExcelProperty(value = "学校id",index = 1)
    private Long universityId;
    //    岗位名称
    @ExcelProperty(value = "岗位名称",index = 2)
    private String name;
    //    岗位英文名
    @ExcelProperty(value = "岗位英文名",index = 3)
    private String ename;
    //    录入日期
    private Date datetime;
    //    修改日期
    private Long byWho;
    //    是否有效
    private Boolean deleted;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename == null ? null : ename.trim();
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
}