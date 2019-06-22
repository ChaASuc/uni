package edu.uni.professionalcourses.ExcelBean;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import java.util.Date;

/**
 * author : 黄永佳
 * create : 2019/6/13 0013 16:08
 * modified :
 * 功能 :
 **/
public class ExcelClass extends BaseRowModel {

    @ExcelProperty(index = 0)
    private Long universityId;
    @ExcelProperty(index = 1)
    private Long departmentId;

    @ExcelProperty(index = 2)
    private String name;

    @ExcelProperty(index = 3)
    private String ename;

    @ExcelProperty(index = 4)
    private Long specialtyId;

    @ExcelProperty(index = 5)
    private String code;

    @ExcelProperty(index = 6)
    private Integer cyear;

    @ExcelProperty(index = 7)
    private Integer cmonth;

    @ExcelProperty(index = 8)
    private Integer clength;

    @ExcelProperty(index = 9)
    private Boolean cover;

    @ExcelProperty(index =10)
    private Long headteacher;

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
        this.name = name;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public Long getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(Long specialtyId) {
        this.specialtyId = specialtyId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getCyear() {
        return cyear;
    }

    public void setCyear(Integer cyear) {
        this.cyear = cyear;
    }

    public Integer getCmonth() {
        return cmonth;
    }

    public void setCmonth(Integer cmonth) {
        this.cmonth = cmonth;
    }

    public Integer getClength() {
        return clength;
    }

    public void setClength(Integer clength) {
        this.clength = clength;
    }

    public Boolean getCover() {
        return cover;
    }

    public void setCover(Boolean cover) {
        this.cover = cover;
    }

    public Long getHeadteacher() {
        return headteacher;
    }

    public void setHeadteacher(Long headteacher) {
        this.headteacher = headteacher;
    }
}
