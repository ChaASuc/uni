package edu.uni.professionalcourses.ExcelBean;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

/**
 * author : 黄永佳
 * create : 2019/6/13 0013 15:22
 * modified :
 * 功能 :
 **/
public class ExcelExperimentSettingContent extends BaseRowModel {
    @ExcelProperty(index = 0)
    private long universityId;
    @ExcelProperty(index = 1)
    private long courseExperiment_description_id;
    @ExcelProperty(index = 2)
    private String name;
    @ExcelProperty(index = 3)
    private String content;
    @ExcelProperty(index = 4)
    private int  hour;
    @ExcelProperty(index = 5)
    private int group_number;
    @ExcelProperty(index = 6)
    private String type;
    @ExcelProperty(index = 7)
    private String ExperimentCategory;
    @ExcelProperty(index = 8)
    private String establish_requirement;

    public long getUniversityId() {
        return universityId;
    }

    public void setUniversityId(long universityId) {
        this.universityId = universityId;
    }

    public long getCourseExperiment_description_id() {
        return courseExperiment_description_id;
    }

    public void setCourseExperiment_description_id(long courseExperiment_description_id) {
        this.courseExperiment_description_id = courseExperiment_description_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getGroup_number() {
        return group_number;
    }

    public void setGroup_number(int group_number) {
        this.group_number = group_number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExperimentCategory() {
        return ExperimentCategory;
    }

    public void setExperimentCategory(String experimentCategory) {
        ExperimentCategory = experimentCategory;
    }

    public String getEstablish_requirement() {
        return establish_requirement;
    }

    public void setEstablish_requirement(String establish_requirement) {
        this.establish_requirement = establish_requirement;
    }
}
