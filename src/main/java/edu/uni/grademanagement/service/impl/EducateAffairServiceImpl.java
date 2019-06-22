package edu.uni.grademanagement.service.impl;

import edu.uni.administrativestructure.bean.Class;
import edu.uni.administrativestructure.bean.ClassExample;
import edu.uni.administrativestructure.bean.Classmate;
import edu.uni.administrativestructure.mapper.ClassMapper;
import edu.uni.educateAffair.bean.Teachingtask;
import edu.uni.educateAffair.bean.TeachingtaskExample;
import edu.uni.educateAffair.mapper.TeachingtaskMapper;
import edu.uni.grademanagement.service.EducateAffairService;
import org.junit.internal.Classes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 陈汉槟
 * @Create 2019/6/12
 * @Description
 * @Since 1.0.0
 */
@Service
public class EducateAffairServiceImpl implements EducateAffairService {

    @Autowired
    private TeachingtaskMapper teachingtaskMapper;

    @Autowired
    private ClassMapper classMapper;


    @Override
    public List<Teachingtask> selectTeachingTaskByIds(
            List<Long> semesterIds, List<Long> teacherIds,
            List<Long> courseIds, List<Long> classIds, List<Long> departmentIds) {

        TeachingtaskExample teachingtaskExample =
                new TeachingtaskExample();
        TeachingtaskExample.Criteria criteria =
                teachingtaskExample.createCriteria();
        if (!CollectionUtils.isEmpty(semesterIds)) {
            criteria.andSemesterIdIn(semesterIds);
        }

        if (!CollectionUtils.isEmpty(teacherIds)) {
            criteria.andWorkerIdIn(teacherIds);
        }

        if (!CollectionUtils.isEmpty(courseIds)) {
            criteria.andCourseIdIn(courseIds);
        }

        if (!CollectionUtils.isEmpty(classIds)) {
            criteria.andClassIdIn(classIds);
        }else {
            if (!CollectionUtils.isEmpty(departmentIds)) {
                ClassExample classExample = new ClassExample();
                classExample.createCriteria().
                        andDepartmentIdIn(departmentIds)
                        .andDeletedEqualTo(false);
                List<Class> classes =
                        classMapper.selectByExample(classExample);
                List<Long> classIdList =
                        classes.stream().map(Class::getId).collect(Collectors.toList());
                criteria.andClassIdIn(classIdList);
            }
        }

        criteria.andDeletedEqualTo(false);

        List<Teachingtask> teachingtasks =
                teachingtaskMapper.selectByExample(teachingtaskExample);
        return teachingtasks;
    }

    @Override
    public List<Long> selectCourseByTeachingTaskBySemesterId(Long semesterId) {
        TeachingtaskExample teachingtaskExample =
                new TeachingtaskExample();
        teachingtaskExample.createCriteria()
                .andSemesterIdEqualTo(semesterId)
                .andDeletedEqualTo(false);
        List<Teachingtask> teachingtasks =
                teachingtaskMapper.selectByExample(teachingtaskExample);
        List<Long> courseIds = teachingtasks.stream().map(Teachingtask::getCourseId).
                distinct().collect(Collectors.toList());
        return courseIds;
    }

    @Override
    public List<Teachingtask> selectTeachingTaskById(Long universityId, Long semesterId, Long courseId) {
        TeachingtaskExample teachingtaskExample = new TeachingtaskExample();
        teachingtaskExample.createCriteria()
                .andUniversityIdEqualTo(universityId)
                .andSemesterIdEqualTo(semesterId)
                .andCourseIdEqualTo(courseId)
                .andDeletedEqualTo(false);
        List<Teachingtask> teachingtasks =
                teachingtaskMapper.selectByExample(teachingtaskExample);

        return teachingtasks;
    }
}
