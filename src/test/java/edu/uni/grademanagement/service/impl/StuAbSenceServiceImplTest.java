package edu.uni.grademanagement.service.impl;

import edu.uni.grademanagement.service.StuAbSenceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class StuAbSenceServiceImplTest {

    @Autowired
    private StuAbSenceService stuAbSenceService;

    @Test
    public void staticc() {
        String staticc = stuAbSenceService.staticc(
                618L, 40L, 45L
        );
        System.out.println(staticc);
    }
}