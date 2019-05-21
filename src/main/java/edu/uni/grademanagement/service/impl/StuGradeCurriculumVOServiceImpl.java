package edu.uni.grademanagement.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.uni.administrativestructure.mapper.ClassMapper;
import edu.uni.educateAffair.bean.Curriculum;
import edu.uni.educateAffair.bean.Semester;
import edu.uni.educateAffair.mapper.CurriculumMapper;
import edu.uni.grademanagement.config.GradeManagementConfig;
import edu.uni.grademanagement.service.StuGradeSemesterSerice;
import edu.uni.grademanagement.vo.GradeFilter;
import edu.uni.grademanagement.vo.StuGradeCurriculumVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 陈汉槟
 * @Create 2019/5/19
 * @Description
 * @Since 1.0.0
 */
public class StuGradeCurriculumVOServiceImpl implements StuGradeSemesterSerice {

    @Autowired
    private CurriculumMapper curriculumMapper;

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private GradeManagementConfig config;

    @Override
    public Semester selectByNow(Date now) {
        return null;
    }

    /*根据过滤器和页码获取分页信息*//*
    @Override
    public PageInfo<StuGradeCurriculumVO> selectPageByGradeFilter(
            GradeFilter gradeFilter, Integer pageNum) {

        *//*分页信息*//*
        PageHelper.startPage(pageNum, config.getPageSize());

        *//*根据锅炉其获取课表信息*//*
        Map<String, List<Long>> curriculumMap = new HashMap<String,List<Long>>();
        curriculumMap.put("semesterId",gradeFilter.getSemesterIds());
        curriculumMap.put("employeeId",gradeFilter.getTeacherIds());
        curriculumMap.put("courseId",gradeFilter.getCourseIds());
        curriculumMap.put("classId",gradeFilter.getClassIds());

        List<Curriculum> curriculumMsgList =
                curriculumMapper.getCurriculumMsgList(curriculumMap);

        *//*根据课表信息组成学生成绩+课表*//*
        curriculumMsgList.stream()
                .filter(
                        curriculum -> {
                            Long classId = curriculum.getClassId();

                        }
                )
        return null;
    }*/
}
