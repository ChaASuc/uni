package edu.uni.grademanagement.enums;

/**
 * @Author 陈汉槟
 * @Create 2019/4/16
 * @Description 成绩模块枚举
 * @Since 1.0.0
 */
public enum GradeEnum implements GlobalEnum {

    GRADE_STATE_NO_SCORE(0, "未评分"),
    GRADE_STATE_NORMAL(1, "正常"),
    GRADE_STATE_FIRST_REBUILD(2, "第一次重修"),
    GRADE_STATE_SECOND_REBUILD(3, "第二次重修"),

    /*报错信息*/
    GRADE_ERROR_NOT_EXIST(101, "该课程学生成绩不存在"),
    ;


    private Integer code;

    private String message;

    GradeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
