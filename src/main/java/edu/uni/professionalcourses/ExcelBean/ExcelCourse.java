package edu.uni.professionalcourses.ExcelBean;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import java.util.Date;

public class ExcelCourse extends BaseRowModel {

    @ExcelProperty(index = 0)
    private Long universityId;

    @ExcelProperty(index = 1)
    private String number;

    @ExcelProperty(index = 2)
    private String name;

    @ExcelProperty(index = 3)
    private String ename;

    @ExcelProperty(index = 4)
    private Long departmentId;

    @ExcelProperty(index = 5)
    private Long specialtyId;

    @ExcelProperty(index = 6)
    private Long speciesId;

    @ExcelProperty(index = 7)
    private Long categoryId;

    @ExcelProperty(index = 8)
    private Long classificationId;

    @ExcelProperty(index = 9)
    private Long examTypeId;

    @ExcelProperty(index = 10)
    private Long examModeId;

    @ExcelProperty(index = 11)
    private Integer hour;

    @ExcelProperty(index = 12)
    private Float credit;

    @ExcelProperty(index = 13)
    private Integer syllabusHour;

    @ExcelProperty(index = 14)
    private Integer experimentHour;

    @ExcelProperty(index = 15)
    private String introduction;

    @ExcelProperty(index = 16)
    private String teachGoal;

    @ExcelProperty(index = 17)
    private String teachRequire;

    @ExcelProperty(index = 18)
    private Date datetime;

    @ExcelProperty(index = 19)
    private Long byWho;

    @ExcelProperty(index = 20)
    private Boolean deleted;

    public Long getUniversityId() {
        return universityId;
    }

    public void setUniversityId(Long universityId) {
        this.universityId = universityId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(Long specialtyId) {
        this.specialtyId = specialtyId;
    }

    public Long getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(Long speciesId) {
        this.speciesId = speciesId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(Long classificationId) {
        this.classificationId = classificationId;
    }

    public Long getExamTypeId() {
        return examTypeId;
    }

    public void setExamTypeId(Long examTypeId) {
        this.examTypeId = examTypeId;
    }

    public Long getExamModeId() {
        return examModeId;
    }

    public void setExamModeId(Long examModeId) {
        this.examModeId = examModeId;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Float getCredit() {
        return credit;
    }

    public void setCredit(Float credit) {
        this.credit = credit;
    }

    public Integer getSyllabusHour() {
        return syllabusHour;
    }

    public void setSyllabusHour(Integer syllabusHour) {
        this.syllabusHour = syllabusHour;
    }

    public Integer getExperimentHour() {
        return experimentHour;
    }

    public void setExperimentHour(Integer experimentHour) {
        this.experimentHour = experimentHour;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getTeachGoal() {
        return teachGoal;
    }

    public void setTeachGoal(String teachGoal) {
        this.teachGoal = teachGoal;
    }

    public String getTeachRequire() {
        return teachRequire;
    }

    public void setTeachRequire(String teachRequire) {
        this.teachRequire = teachRequire;
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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
