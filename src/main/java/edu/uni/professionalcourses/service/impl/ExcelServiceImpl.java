package edu.uni.professionalcourses.service.impl;

import edu.uni.administrativestructure.bean.Class;
import edu.uni.administrativestructure.bean.Department;
import edu.uni.administrativestructure.mapper.ClassMapper;
import edu.uni.administrativestructure.mapper.DepartmentMapper;
import edu.uni.professionalcourses.ExcelBean.*;
import edu.uni.professionalcourses.bean.*;
import edu.uni.professionalcourses.controller.LearningProcessSemesterAllocationController;
import edu.uni.professionalcourses.mapper.*;
import edu.uni.professionalcourses.service.ExcelService;
import edu.uni.professionalcourses.service.ExperimentSettingContentService;
import edu.uni.professionalcourses.service.LearningProcessService;
import edu.uni.professionalcourses.utils.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * author : 黄永佳
 * create : 2019/6/12 0012 22:08
 * modified :
 * 功能 :
 **/
@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private CourseSyllabusDescriptionMapper courseSyllabusDescriptionMapper;
    @Autowired
    private CourseTeachingScheduleMapper courseTeachingScheduleMapper;
    @Autowired
    private ExperimentSettingContentMapper experimentSettingContentMapper;
    @Autowired
    private LearningProcessMapper learningProcessMapper;
    @Autowired
    private ClassMapper classMapper;
    @Autowired
    private LearningProcessSemesterAllocationMapper learningProcessSemesterAllocationMapper;
    @Override
    public int uploadDepartment(String savePath, MultipartFile file) throws IOException {
        //先保存文件到本地
        saveFile(savePath,file);
        int i = 0;
        //解析excel
        List<ExcelDepartment> data = ExcelUtil.readExcel(file.getInputStream(), ExcelDepartment.class);
        // 插入数据库
        for(ExcelDepartment d: data){
            //根据学号获得学生id
//            Student student = studentService.selectByStuNo(d.getStudentNo());
//            if(student == null) throw new RuntimeException("没有"+d.getStudentNo()+"这个学生");
//            Long studentId = student.getId();
//            //根据宿舍区、宿舍号、宿舍床位获得床位id
//            Long liveBedId = getLiveBedId(d.getLiveAreaName(),d.getLiveRoom(),d.getBedNumber());
//            if(liveBedId == null) throw new RuntimeException("没有"+d.getLiveAreaName()+d.getLiveRoom()+d.getBedNumber()+"床位");
            //新增入住信息
            Department department= new Department();
            department.setUniversityId(d.getUniversityId());
            department.setName(d.getName());
            department.setDeleted(false);
            department.setByWho(1L);
            department.setDatetime(new Date());
            department.setEname(d.getEname());
            department.setTelephone(d.getTelephone());
            department.setHead(d.getHead());
            department.setOfficeRoom(d.getOfficeRoom());
            department.setUniversityLeader(d.getUniversityLeader());

            if(departmentMapper.insert(department)>0){
                i++;
            }
        }
        return i;
    }

    @Override
    public int uploadCourse_syllabus_description(String savePath, MultipartFile file) throws IOException {
        //先保存文件到本地
        saveFile(savePath,file);
        int i = 0;
        //解析excel
        List<ExcelCourse_syllabus_description> data = ExcelUtil.readExcel(file.getInputStream(), ExcelCourse_syllabus_description.class);
        // 插入数据库
        for(ExcelCourse_syllabus_description d: data){
            //根据学号获得学生id
//            Student student = studentService.selectByStuNo(d.getStudentNo());
//            if(student == null) throw new RuntimeException("没有"+d.getStudentNo()+"这个学生");
//            Long studentId = student.getId();
//            //根据宿舍区、宿舍号、宿舍床位获得床位id
//            Long liveBedId = getLiveBedId(d.getLiveAreaName(),d.getLiveRoom(),d.getBedNumber());
//            if(liveBedId == null) throw new RuntimeException("没有"+d.getLiveAreaName()+d.getLiveRoom()+d.getBedNumber()+"床位");
            //新增入住信息
            CourseSyllabusDescription courseSyllabusDescription= new CourseSyllabusDescription();
            courseSyllabusDescription.setUniversityId(d.getUniversityId());
            courseSyllabusDescription.setCourseSyllabusId(d.getCourse_syllabus_id());
            courseSyllabusDescription.setDeleted(false);
            courseSyllabusDescription.setByWho(1L);
            courseSyllabusDescription.setDatetime(new Date());
            courseSyllabusDescription.setChapter(d.getChapter());
            courseSyllabusDescription.setContent(d.getContent());
            courseSyllabusDescription.setTeachingHour(d.getTeaching_hour());
            courseSyllabusDescription.setAssessmentRequirement(d.getAssessment_requirement());
            courseSyllabusDescription.setCourseBookId(d.getCourse_book_id());
            courseSyllabusDescription.setInstruction(d.getInstruction());
            if(courseSyllabusDescriptionMapper.insert(courseSyllabusDescription)>0){
                i++;
            }
        }
        return i;
    }

    @Override
    public int uploadCourseTeachingSchedule(String savePath, MultipartFile file) throws IOException {
        saveFile(savePath,file);
        int i = 0;
        //解析excel
        List<ExcelCourseTeachingSchedule> data = ExcelUtil.readExcel(file.getInputStream(), ExcelCourseTeachingSchedule.class);
        // 插入数据库
        for(ExcelCourseTeachingSchedule d: data){

            CourseTeachingSchedule courseTeachingSchedule= new CourseTeachingSchedule();
            courseTeachingSchedule.setUniversityId(d.getUniversityId());
            courseTeachingSchedule.setWeek(d.getWeek());
            courseTeachingSchedule.setDeleted(false);
            courseTeachingSchedule.setByWho(1L);
            courseTeachingSchedule.setDatetime(new Date());
            courseTeachingSchedule.setChapter(d.getChapter());
            courseTeachingSchedule.setContent(d.getContent());
            courseTeachingSchedule.setTeachHour(d.getTeach_hour());
            courseTeachingSchedule.setExperimentHour(d.getExperiment_hour());
            courseTeachingSchedule.setHomework(d.getHomework());
            courseTeachingSchedule.setActualProgress(d.getActual_progress());
            courseTeachingSchedule.setOutOfPlanReason(d.getOut_of_plan_reason());

            if(courseTeachingScheduleMapper.insert(courseTeachingSchedule)>0){
                i++;
            }
        }
        return i;
    }

    @Override
    public int uploadExperimentSettingContent(String savePath, MultipartFile file) throws IOException {
        saveFile(savePath,file);
        int i = 0;
        //解析excel
        List<ExcelExperimentSettingContent> data = ExcelUtil.readExcel(file.getInputStream(), ExcelExperimentSettingContent.class);
        // 插入数据库
        for(ExcelExperimentSettingContent d: data){

            ExperimentSettingContent experimentSettingContent= new ExperimentSettingContent();
            experimentSettingContent.setUniversityId(d.getUniversityId());
            experimentSettingContent.setCourseExperimentDescriptionId(d.getCourseExperiment_description_id());
            experimentSettingContent.setDeleted(false);
            experimentSettingContent.setByWho(1L);
            experimentSettingContent.setDatetime(new Date());
            experimentSettingContent.setName(d.getName());
            experimentSettingContent.setContent(d.getContent());
            experimentSettingContent.setHour(d.getHour());
            experimentSettingContent.setGroupNumber(d.getGroup_number());
            experimentSettingContent.setType(d.getType());
            experimentSettingContent.setExperimentCategory(d.getExperimentCategory());
            experimentSettingContent.setEstablishRequirement(d.getEstablish_requirement());

            if(experimentSettingContentMapper.insert(experimentSettingContent)>0){
                i++;
            }
        }
        return i;
    }

    @Override
    public int uploadLearningProcess(String savePath, MultipartFile file) throws IOException {
        saveFile(savePath,file);
        int i = 0;
        //解析excel
        List<ExcelLearningProcess> data = ExcelUtil.readExcel(file.getInputStream(), ExcelLearningProcess.class);
        // 插入数据库
        for(ExcelLearningProcess d: data){

            LearningProcess learningProcess= new LearningProcess();
            learningProcess.setUniversityId(d.getUniversityId());
            learningProcess.setSpecialtyId(d.getSpecialtyId());
            learningProcess.setDeleted(false);
            learningProcess.setByWho(1L);
            learningProcess.setDatetime(new Date());
            learningProcess.setTerm(d.getTerm());
            learningProcess.setCourseId(d.getCourseId());
            learningProcess.setNote(d.getNote());


            if(learningProcessMapper.insert(learningProcess)>0){
                i++;
            }
        }
        return i;
    }

    @Override
    public int uploadLearningProcessSemesterAllocation(String savePath, MultipartFile file) throws IOException {
        saveFile(savePath,file);
        int i = 0;
        //解析excel
        List<ExcelLearningProcessSemesterAllocation> data = ExcelUtil.readExcel(file.getInputStream(), ExcelLearningProcessSemesterAllocation.class);
        // 插入数据库
        for(ExcelLearningProcessSemesterAllocation d: data){

            LearningProcessSemesterAllocation learningProcessSemesterAllocation= new LearningProcessSemesterAllocation();
            learningProcessSemesterAllocation.setUniversityId(d.getUniversity());
            learningProcessSemesterAllocation.setLearningProcessId(d.getLearningProcessId());
            learningProcessSemesterAllocation.setDeleted(false);
            learningProcessSemesterAllocation.setByWho(1L);
            learningProcessSemesterAllocation.setDatetime(new Date());
            learningProcessSemesterAllocation.setCredits(d.getCredits());
            learningProcessSemesterAllocation.setPeriod(d.getPeriod());
            learningProcessSemesterAllocation.setSemester(d.getSemester());


            if(learningProcessSemesterAllocationMapper.insert(learningProcessSemesterAllocation)>0){
                i++;
            }
        }
        return i;
    }

    @Override
    public int uploadClass(String savePath, MultipartFile file) throws IOException {
        saveFile(savePath,file);
        int i = 0;
        //解析excel
        List<ExcelClass> data = ExcelUtil.readExcel(file.getInputStream(), ExcelClass.class);
        // 插入数据库
        for(ExcelClass d: data){
            Class classes=new Class();
            classes.setUniversityId(d.getUniversityId());
            classes.setDepartmentId(d.getDepartmentId());
            classes.setDeleted(false);
            classes.setByWho(1L);
            classes.setDatetime(new Date());
            classes.setName(d.getName());
            classes.setEname(d.getEname());
            classes.setSpecialtyId(d.getSpecialtyId());
            classes.setCode(d.getCode());
            classes.setCyear(d.getCyear());
            classes.setCmonth(d.getCmonth());
            classes.setClength(d.getClength());
            classes.setCover(d.getCover());
            classes.setHeadteacher(d.getHeadteacher());
            if(classMapper.insert(classes)>0){
                i++;
            }
        }
        return i;
    }


    private void saveFile(String path, MultipartFile file) throws IOException {
        System.out.println(path);
        final String p = "excel";
        File dir = new File(path,p);
        if(!dir.exists()){
            dir.mkdir();
        }
        String imgName = UUID.randomUUID().toString()+file.getOriginalFilename();
        File upload = new File(dir,imgName);

//        FileUtils.copyInputStreamToFile(file.getInputStream(),upload);
    }
}
