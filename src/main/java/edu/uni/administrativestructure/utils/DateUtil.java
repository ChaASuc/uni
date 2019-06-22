package edu.uni.administrativestructure.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtil {
    private static String DEFAULT_PATTERN="yyyy-MM-dd HH:mm:ss";

    public static Date string2date(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_PATTERN);
        return format.parse(date);
    }

    public static String date2string(Date date){
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_PATTERN);
        return format.format(date);
    }
    public static String now(String pattern){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
    public static String now(){
        return now(DEFAULT_PATTERN);
    }
}
