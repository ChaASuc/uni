package edu.uni.grademanagement.service.impl;

import edu.uni.educateAffair.bean.CurriculumExample;
import edu.uni.educateAffair.bean.Teachingtask;
import edu.uni.educateAffair.bean.TeachingtaskExample;
import edu.uni.educateAffair.mapper.CurriculumMapper;
import edu.uni.educateAffair.mapper.TeachingtaskMapper;
import edu.uni.grademanagement.service.CurriculumForGradeService;
import edu.uni.professionalcourses.bean.Course;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 陈汉槟
 * @Create 2019/6/4
 * @Description
 * @Since 1.0.0
 */
@Service
@Slf4j
public class CurriculumForGradeServiceImpl implements CurriculumForGradeService {

    @Autowired
    private CurriculumMapper curriculumMapper;

    @Autowired
    private TeachingtaskMapper teachingtaskMapper;


    @Override
    public List<Long> selectCourseIdBySemeterId(Long semesterId) {
        TeachingtaskExample teachingtaskExample = new TeachingtaskExample();
        teachingtaskExample.createCriteria()
                .andSemesterIdEqualTo(semesterId)
                .andDeletedEqualTo(false);
        List<Teachingtask> teachingtasks =
                teachingtaskMapper.selectByExample(teachingtaskExample);
        List<Long> courseIds = teachingtasks.stream().map(
                teachingtask -> {
                    return teachingtask.getCourseId();
                }
        ).distinct().collect(Collectors.toList());
        return courseIds;
    }

    @Override
    public List<Long> selectCourseId() {
        TeachingtaskExample teachingtaskExample = new TeachingtaskExample();
        teachingtaskExample.createCriteria()
                .andDeletedEqualTo(false);
        List<Teachingtask> teachingtasks =
                teachingtaskMapper.selectByExample(teachingtaskExample);
        List<Long> courseIds = teachingtasks.stream().map(
                teachingtask -> {
                    return teachingtask.getCourseId();
                }
        ).distinct().collect(Collectors.toList());
        return courseIds;
    }
}
