package edu.uni.utils;

import edu.uni.config.GlobalConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * @Author deschen
 * @Create 2019/5/21
 * @Description 文件上传下载工具类
 * @Since 1.0.0
 */
@Data
@Slf4j
public class FileUtil {

    private String uploadRootDir;

    private String filePrefix;


    /*设置存储路径，后缀默认*/
    public FileUtil(String uploadRootDir) {
        this.uploadRootDir = uploadRootDir;
        this.filePrefix = UUID.randomUUID().toString();
    }

    /*设置存储路径和后缀*/
    public FileUtil(String uploadRootDir, String filePrefix) {
        this.uploadRootDir = uploadRootDir;
        this.filePrefix = filePrefix;
    }

    /*存储文件并返回文件路径名*/
    public String uploadFile(MultipartFile multipartFile) throws IOException {

        String fIlePath = getFilePath(multipartFile);
        File file = new File(fIlePath);
        //如果没有files文件夹，则创建
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        //文件写入指定路径
        multipartFile.transferTo(file);
        log.info("文件上传成功");
        return fIlePath;
    }

    // 下载文件
    public void downloadFile(
            String filePath, HttpServletResponse response){
        File file = new File(filePath);
        if (file.exists()) {
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                log.info("文件下载成功");
                System.out.println("111");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    // 生成文件路径名
    private String getFilePath(MultipartFile file) {
        // 获取文件的后缀名
        String originalFilename = file.getOriginalFilename();
        String suffixName =
                originalFilename.substring(originalFilename.lastIndexOf("."));
        log.info("【文件工具】后缀, suffixName = {}", suffixName);
        // 文件路径名 liunx下中文路径，图片显示问题
        String path = uploadRootDir + "//" + filePrefix + suffixName;
        return path;
    }

}
