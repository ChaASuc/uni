package edu.uni.professionalcourses.ExcelBean;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import java.util.Date;

/**
 * author : 黄永佳
 * create : 2019/6/12 0012 22:17
 * modified :
 * 功能 :
 **/
public class ExcelDepartment extends BaseRowModel {


    @ExcelProperty(index = 0)
    private Long universityId;
    @ExcelProperty(index = 1)
    private String name;
    @ExcelProperty(index = 2)
    private String ename;
    @ExcelProperty(index = 3)
    private String telephone;
    @ExcelProperty(index = 4)
    private Long head;
    @ExcelProperty(index = 5)
    private Long officeRoom;
    @ExcelProperty(index = 6)
    private Long universityLeader;

    @ExcelProperty(index = 7)
    private Long byWho;

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
        this.name = name;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

    public Long getUniversityLeader() {
        return universityLeader;
    }

    public void setUniversityLeader(Long universityLeader) {
        this.universityLeader = universityLeader;
    }

    public Long getByWho() {
        return byWho;
    }

    public void setByWho(Long byWho) {
        this.byWho = byWho;
    }
}
