package edu.uni.administrativestructure.ExcelBean;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * author:黄育林
 * create:2019.5.7
 * modified:2019.6.13
 * 功能：二级部门实体类导入
 */
public class ExcelSubdepartment extends BaseRowModel {
    //    主id
//    @ExcelProperty(value = "二级部门id",index = 0)
    private Long id;
    //    学校id
    @ExcelProperty(value = "学校id",index = 1)
    private Long universityId;
    //    部门id
    @ExcelProperty(value = "部门id",index = 2)
    private Long departmentId;
    //    二级部门名称
    @ExcelProperty(value = "二级部门名称",index = 3)
    private String name;
    //    部门英文名
    @ExcelProperty(value = "部门英文名称",index = 4)
    private String ename;
    //    电话
    @ExcelProperty(value = "电话",index = 5)
    private String telephone;
    //    领导人
    @ExcelProperty(value = "领导人",index = 6)
    private Long head;
    //    领导办公室
    @ExcelProperty(value = "领导办公室",index = 7)
    private Long officeRoom;
    //    录入日期
    private Date datetime;
    //    录入人
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

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public Long getHead() {
        return head;
    }

    public void setHead(Long head) {
        this.head = head;
    }

    public Long getOfficeRoom() {
        return officeRoom;
    }

    public void setOfficeRoom(Long officeRoom) {
        this.officeRoom = officeRoom;
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