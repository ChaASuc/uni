package edu.uni.example.controller;

import com.github.pagehelper.PageInfo;
import edu.uni.bean.Result;
import edu.uni.bean.ResultType;
import edu.uni.config.GlobalConfig;
import edu.uni.utils.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author deschen
 * @Create 2019/5/21
 * @Description
 * @Since 1.0.0
 */
@Api
@RestController
@RequestMapping("/example/file")
@Slf4j
public class FileExampleController {

    @Autowired
    private GlobalConfig config;

    @ApiOperation(value="文件上传", notes = "")
    @PostMapping("/upload")
    public Result uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Result.build(ResultType.ParamError);
        }
        FileUtil fileUtil = new FileUtil(config.getUploadRootDir());
        String filePath;
        try {
            filePath = fileUtil.uploadFile(file);
        } catch (IOException e) {
            return Result.build(ResultType.Failed);
        }
        log.info("成功");
        return Result.build(ResultType.Success);
    }

    @ApiOperation(value = "文件下载", notes = "")
    @GetMapping("/download")
    public void download(
            @ApiParam(name = "filePath")
            @RequestParam String filePath,
            HttpServletResponse response) {
        FileUtil fileUtil = new FileUtil(config.getUploadRootDir());
        fileUtil.downloadFile(filePath, response);
    }

}
