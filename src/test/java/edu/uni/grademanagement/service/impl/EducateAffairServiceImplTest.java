package edu.uni.grademanagement.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.uni.educateAffair.bean.Teachingtask;
import edu.uni.educateAffair.mapper.TeachingtaskMapper;
import edu.uni.grademanagement.service.EducateAffairService;
import edu.uni.utils.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import springfox.documentation.spring.web.json.Json;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EducateAffairServiceImplTest {

    @Autowired
    private EducateAffairService educateAffairService;

    @Test
    public void selectTeachingTaskByIds() throws JsonProcessingException {
        List<Long> semesterIds = new ArrayList<>();
//        semesterIds.add(40L);
//        semesterIds.add(39L);
        List<Long> teacherIds = new ArrayList<>();
//        teacherIds.add(1962L);
//        teacherIds.add(1967L);
        List<Long> classIds = new ArrayList<>();
//        classIds.add(27L);
        List<Long> courseIds = new ArrayList<>();
//        courseIds.add(16L);
        List<Long> departmentIds = new ArrayList<>();
        departmentIds.add(14L);
        PageHelper.startPage(1, 30);
        List<Teachingtask> teachingtasks = educateAffairService.selectTeachingTaskByIds(
                semesterIds, teacherIds, courseIds, classIds, departmentIds);
        PageInfo<Teachingtask> teachingtaskPageInfo =
                new PageInfo<>(teachingtasks);
        System.out.println(JsonUtils.obj2String(teachingtaskPageInfo));
    }


}