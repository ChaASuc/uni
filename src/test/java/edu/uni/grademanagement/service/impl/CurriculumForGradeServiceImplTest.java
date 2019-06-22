package edu.uni.grademanagement.service.impl;

import edu.uni.grademanagement.service.CurriculumForGradeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CurriculumForGradeServiceImplTest {

    @Autowired
    private CurriculumForGradeService service;

    @Test
    public void selectCourseIdBySemeterId() {
        List<Long> longs =
                service.selectCourseIdBySemeterId(40L);
        System.out.println(longs.size());
    }
}