package edu.uni.professionalcourses.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ExcelAboutService {
    int uploadCourse(String savePath, MultipartFile file) throws IOException;
}
