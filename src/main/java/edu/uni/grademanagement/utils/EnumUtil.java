package edu.uni.grademanagement.utils;

import edu.uni.grademanagement.enums.GlobalEnum;

/**
 * @Author 陈汉槟
 * @Create 2019/4/23
 * @Description
 * @Since 1.0.0
 */
// todo 不用
public class EnumUtil {

    public static <T extends GlobalEnum> T getByCode(Integer code, Class<T> enumClass) {
        for (T each : enumClass.getEnumConstants()) {
            if (code.equals(each.getCode())) {
                return each;
            }
        }
        return null;
    }
}
