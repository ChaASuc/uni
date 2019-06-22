//package edu.uni.professionalcourses.service.impl;
//
//import edu.uni.professionalcourses.bean.Course;
//import edu.uni.professionalcourses.mapper.CourseMapper;
//import edu.uni.professionalcourses.ExcelBean.ExcelCourse;
//import edu.uni.professionalcourses.service.ExcelAboutService;
//import edu.uni.professionalcourses.utils.ExcelUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//
//public class ExcelAboutServiceImpl implements ExcelAboutService {
//    @Autowired
//    private CourseMapper courseMapper;
//    @Override
//    public int uploadCourse(String savePath, MultipartFile file) throws IOException {
//        //先保存文件到本地
//        saveFile(savePath,file);
//        int i = 0;
//        //解析excel
//        List<ExcelCourse> data = ExcelUtil.readExcel(file.getInputStream(), ExcelCourse.class);
//        // 插入数据库
//        for(ExcelCourse d: data){
//            //根据学号获得学生id
////            Student student = studentService.selectByStuNo(d.getStudentNo());
////            if(student == null) throw new RuntimeException("没有"+d.getStudentNo()+"这个学生");
////            Long studentId = student.getId();
////            //根据宿舍区、宿舍号、宿舍床位获得床位id
////            Long liveBedId = getLiveBedId(d.getLiveAreaName(),d.getLiveRoom(),d.getBedNumber());
////            if(liveBedId == null) throw new RuntimeException("没有"+d.getLiveAreaName()+d.getLiveRoom()+d.getBedNumber()+"床位");
//            //新增入住信息
//            Course course=new Course();
//            course.setUniversityId(d.getUniversityId());
//            course.setDeleted(false);
//            course.setCategoryId(d.getCategoryId());
//            course.setSpeciesId(d.getSpeciesId());
//            course.setDatetime(new Date());
//            course.setEname(d.getEname());
//            course.setName(d.getName());
//            course.setByWho(1L);
//            course.setCredit(d.getCredit());
//            course.setNumber(d.getNumber());
//            course.setSpecialtyId(d.getSpecialtyId());
//            course.setTeachRequire(d.getTeachRequire());
//            course.setExamModeId(d.getExamModeId());
//            course.setExamTypeId(d.getExamTypeId());
//            course.setExperimentHour(d.getExperimentHour());
//            course.setSyllabusHour(d.getSyllabusHour());
//            course.setIntroduction(d.getIntroduction());
//            course.setHour(d.getHour());
//            course.setTeachGoal(d.getTeachGoal());
//            course.setClassificationId(d.getClassificationId());
//            course.setDepartmentId(d.getDepartmentId());
//
//
//
//
//            if(courseMapper.insert(course)>0){
//                i++;
//            }
//        }
//        return i;
//    }
//
//    private void saveFile(String path, MultipartFile file) throws IOException {
//        System.out.println(path);
//        final String p = "excel";
//        File dir = new File(path,p);
//        if(!dir.exists()){
//            dir.mkdir();
//        }
//        String imgName = UUID.randomUUID().toString()+file.getOriginalFilename();
//        File upload = new File(dir,imgName);
//
////        FileUtils.copyInputStreamToFile(file.getInputStream(),upload);
//    }
//}
