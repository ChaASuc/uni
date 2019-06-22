package edu.uni.grademanagement.utils;

import edu.uni.grademanagement.service.AdministrativestructureService;
import edu.uni.grademanagement.service.CourseForGradeService;
import edu.uni.grademanagement.service.StudentForGradeService;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class GradeDataUtil {

    @Autowired
    private StudentForGradeService studentForGradeService;

    @Autowired
    private CourseForGradeService courseForGradeService;

    @Autowired
    private AdministrativestructureService administrativestructureService;


    public List<List<Object>> createGradeListObject() {

        List<List<Object>> object = new ArrayList<List<Object>>();
        for (int i = 0; i < 1000; i++) {
            List<Object> da = new ArrayList<Object>();
            da.add("字符串"+i);
            da.add(Long.valueOf(187837834l+i));
            da.add(Integer.valueOf(2233+i));
            da.add(Double.valueOf(2233.00+i));
            da.add(Float.valueOf(2233.0f+i));
            da.add(new Date());
            da.add(new BigDecimal("3434343433554545"+i));
            da.add(Short.valueOf((short)i));
            object.add(da);
        }
        return object;
    }

    public List<List<String>> createGradeListStringHead(){

        List<List<String>> head = new ArrayList<List<String>>();
        List<String> headCoulumn1 = new ArrayList<String>();
        List<String> headCoulumn2 = new ArrayList<String>();
        List<String> headCoulumn3 = new ArrayList<String>();
        List<String> headCoulumn4 = new ArrayList<String>();
        List<String> headCoulumn5 = new ArrayList<String>();
        List<String> headCoulumn6 = new ArrayList<String>();
        List<String> headCoulumn7 = new ArrayList<String>();
        List<String> headCoulumn8 = new ArrayList<String>();
        List<String> headCoulumn9 = new ArrayList<String>();

        headCoulumn1.add("学号");
        headCoulumn2.add("姓名");
        headCoulumn3.add("序号");
        headCoulumn4.add("成绩");
        headCoulumn5.add("评语");
        headCoulumn6.add("课程编号");
        headCoulumn7.add("课程名称");
        headCoulumn8.add("班级编号");
        headCoulumn9.add("班级");

        head.add(headCoulumn1);
        head.add(headCoulumn2);
        head.add(headCoulumn3);
        head.add(headCoulumn4);
        head.add(headCoulumn5);
        head.add(headCoulumn6);
        head.add(headCoulumn7);
        head.add(headCoulumn8);
        head.add(headCoulumn9);

        return head;
    }


}
