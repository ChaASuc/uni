package edu.uni.grademanagement.utils;

import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @Author 陈汉槟
 * @Create 2019/5/12
 * @Description
 * @Since 1.0.0
 */
@Component
public class ScoreToPointUtil {

    public static Double getPoint(Double score) {
        if (score >= 90) {
            return 4d;
        }
        if (score < 60) {
            return 0d;
        }

        double point = 4 - (90 - score) * 0.1;
        return point;
    }
}
