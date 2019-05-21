package edu.uni.grademanagement.constants;

/**
 * @Author 陈汉槟
 * @Create 2019/5/5
 * @Description
 * @Since 1.0.0
 */
public interface GradeConstant {

    Integer RECORD_VALID = 0;

    Integer RECORD_INVALID = 1;

    Integer RECORD_CACHE = 2;

    Integer CACHE_VALID = 0;

    Integer CACHE_INVALID = 1;

    Integer MAIN_STATE_NOSCORE = 0;

    Integer MAIN_STATE_SCORING = 1;

    Integer MAIN_STATE_NORMAL = 2;

    Integer MAIN_STATE_FIRST_REBUILD = 3;

    Integer MAIN_STATE_SECOND_REBUILD = 4;

}
