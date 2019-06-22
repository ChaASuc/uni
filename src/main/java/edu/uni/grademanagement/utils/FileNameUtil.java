package edu.uni.grademanagement.utils;

import lombok.Data;

/**
 * @Author deschen
 * @Create 2019/6/17
 * @Description
 * @Since 1.0.0
 */
@Data
public class FileNameUtil {

    private String filePath;

    public FileNameUtil(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {

        int headIndex = filePath.lastIndexOf("/");
        int middleIndex = filePath.lastIndexOf("_");
        int tailIndex = filePath.lastIndexOf(".");
        filePath = filePath.substring(headIndex + 1, middleIndex)
                + filePath.substring(tailIndex);
        return filePath;
    }
}
