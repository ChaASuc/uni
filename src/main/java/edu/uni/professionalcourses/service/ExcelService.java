package edu.uni.professionalcourses.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * author : 黄永佳
 * create : 2019/6/12 0012 22:05
 * modified :
 * 功能 :
 **/
public interface ExcelService {
    int uploadDepartment(String savePath, MultipartFile file) throws IOException;
    int uploadCourse_syllabus_description(String savePath, MultipartFile file) throws IOException;
    int uploadCourseTeachingSchedule(String savePath, MultipartFile file) throws IOException;
    int uploadExperimentSettingContent(String savePath, MultipartFile file) throws IOException;
    int uploadLearningProcess(String savePath, MultipartFile file) throws IOException;
    int uploadLearningProcessSemesterAllocation(String savePath, MultipartFile file) throws IOException;
    int uploadClass(String savePath, MultipartFile file) throws IOException;
}
