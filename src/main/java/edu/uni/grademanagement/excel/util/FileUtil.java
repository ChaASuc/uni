package edu.uni.grademanagement.excel.util;

import java.io.InputStream;

public class FileUtil {

    public static InputStream getResourcesFileInputStream(String fileName) {
        System.out.println(Thread.currentThread().getContextClassLoader().getResource("" + fileName).getPath());
        return Thread.currentThread().getContextClassLoader().getResourceAsStream("" + fileName);
    }
}
