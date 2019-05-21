package edu.uni.grademanagement.service.impl;

import edu.uni.grademanagement.bean.*;
import edu.uni.grademanagement.constants.GradeConstant;
import edu.uni.grademanagement.constants.RoleConstant;
import edu.uni.grademanagement.dto.StuGradeItemDto;
import edu.uni.grademanagement.form.UpdateStuGradeMainForm;
import edu.uni.grademanagement.mapper.CourseItemMapper;
import edu.uni.grademanagement.mapper.StuItemGradeMapper;
import edu.uni.grademanagement.service.StuGradeItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 陈汉槟
 * @Create 2019/5/1
 * @Description
 * @Since 1.0.0
 */
@Service
@Slf4j
public class StuGradeItemServiceImpl implements StuGradeItemService {

    @Autowired
    private CourseItemMapper courseItemMapper;

    @Autowired
    private StuItemGradeMapper stuItemGradeMapper;



    @Override
    /**
     *
     * @Param [stuGradeMainId]
     * @Return:java.util.List<edu.uni.grademanagement.dto.StuGradeItemDto>
     * @Author: 陈汉槟
     * @Date: 2019/5/5 22:56
     * @Description: 根据学生主表id获取课程成绩项明细
     */
    public List<StuGradeItemDto> selectByStuGradeMainId(Long stuGradeMainId,
                                                        Integer flag) {
        StuItemGradeExample stuItemGradeExample = new StuItemGradeExample();
        StuItemGradeExample.Criteria criteria = stuItemGradeExample.createCriteria()
                .andStuGradeMainIdEqualTo(stuGradeMainId)
                .andDeletedEqualTo(GradeConstant.RECORD_VALID);
        if (flag == null) {
            criteria.andCacheEqualTo(GradeConstant.CACHE_INVALID);
        }
        List<StuItemGrade> stuItemGrades = stuItemGradeMapper.selectByExample(stuItemGradeExample);
        if (CollectionUtils.isEmpty(stuItemGrades)) {
            return null;
        }
        List<StuGradeItemDto> stuGradeItemDtos = stuItemGrades.stream().map(
                stuItemGrade -> {
                    StuGradeItemDto stuGradeItemDto = new StuGradeItemDto();
                    CourseItem courseItem = courseItemMapper.selectByPrimaryKey(stuItemGrade.getCourseItemId());
                    BeanUtils.copyProperties(courseItem, stuGradeItemDto);
                    BeanUtils.copyProperties(stuItemGrade, stuGradeItemDto);
                    return stuGradeItemDto;
                }
        ).collect(Collectors.toList());
        return stuGradeItemDtos;
    }

    @Override

    /**
     *
     * @Param [courseItem, stuGradeMains]
     * @Return:int
     * @Author: 陈汉槟
     * @Date: 2019/5/8 15:26
     * @Description: 根据课程成绩项何成绩id何成绩集合创建StuGradeItemDto
     */
    @Transactional
    public boolean insertStuGradeItemDto(CourseItem courseItem, List<StuGradeMain> stuGradeMains) {

        List<StuItemGrade> stuItemGrades = stuGradeMains.stream().map(
                stuGradeMain -> {
                    StuItemGrade stuItemGrade = new StuItemGrade();
                    stuItemGrade.setByWho(courseItem.getByWho());
                    stuItemGrade.setCourseItemId(courseItem.getId());
                    stuItemGrade.setStuGradeMainId(stuGradeMain.getId());
                    stuItemGrade.setUniversityId(courseItem.getUniversityId());
                    return stuItemGrade;
                }
        ).collect(Collectors.toList());
        int insert_stuItemGrades = stuItemGradeMapper.insertStuItemGrades(stuItemGrades);
        if (insert_stuItemGrades != stuItemGrades.size()) {
            log.info("【创建stuItemGrade集合】失败，stuitemGrades = {}",
                    stuItemGrades);
            // todo 报错
            return false;
        }
        return true;
    }

    @Override

    /**
     *
     * @Param [stuItemGrades]
     * @Return:boolean
     * @Author: 陈汉槟
     * @Date: 2019/5/10 1:04
     * @Description:
     */
    public boolean deletedUpdateStuGradeItem(List<StuItemGrade> stuItemGrades) {
        return false;
    }

    @Override

    /**
     *
     * @Param [stuGradeMainId, courseItemId]
     * @Return:edu.uni.grademanagement.bean.StuItemGrade
     * @Author: 陈汉槟
     * @Date: 2019/5/9 22:27
     * @Description: 根据成绩主表id何课程成绩项id获取成绩项
     */
    public StuItemGrade selectStuItemGradeByStuGradeMainId(Long stuGradeMainId, Long courseItemId) {
        StuItemGradeExample stuItemGradeExample = new StuItemGradeExample();
        stuItemGradeExample.createCriteria()
                .andStuGradeMainIdEqualTo(stuGradeMainId)
                .andCourseItemIdEqualTo(courseItemId)
                .andDeletedEqualTo(GradeConstant.RECORD_VALID);
        List<StuItemGrade> stuItemGrades = stuItemGradeMapper.selectByExample(stuItemGradeExample);
        if (CollectionUtils.isEmpty(stuItemGrades)) {
            return null;
        }
        return stuItemGrades.get(0);
    }

    @Override

    /**
     *
     * @Param [stuGradeMainId, id]
     * @Return:boolean
     * @Author: 陈汉槟
     * @Date: 2019/5/10 1:09
     * @Description: 根据成绩主表id何课程成绩项id获取成绩项，删除
     */
    @Transactional
    public boolean deleteStuItemGradeByStuGradeMainIdAndCourseItemId(Long stuGradeMainId, Long id) {
        StuItemGradeExample stuItemGradeExample = new StuItemGradeExample();
        stuItemGradeExample.createCriteria()
                .andStuGradeMainIdEqualTo(stuGradeMainId)
                .andCourseItemIdEqualTo(id)
                .andDeletedEqualTo(GradeConstant.RECORD_VALID);
        List<StuItemGrade> stuItemGrades = stuItemGradeMapper.selectByExample(stuItemGradeExample);
        for (StuItemGrade stuItemGrade:
        stuItemGrades) {
            stuItemGrade.setDeleted(GradeConstant.RECORD_INVALID);
            int update = stuItemGradeMapper.updateByPrimaryKey(stuItemGrade);
            if (update == 0) {
                return false;
            }
        }
        return true;
    }

    @Override

    /**
     *
     * @Param [stuItemGrade]
     * @Return:boolean
     * @Author: 陈汉槟
     * @Date: 2019/5/10 1:23
     * @Description: 根据成绩项更新信息
     */
    public boolean updateStuItemGrade(StuItemGrade stuItemGrade) {
        int update = stuItemGradeMapper.updateByPrimaryKeySelective(stuItemGrade);
        if (update == 0) {
            return false;
        }
        return true;
    }



    /**
     *
     * @Param [stuGradeMainId]
     * @Return:java.lang.Long
     * @Author: 陈汉槟
     * @Date: 2019/5/11 10:28
     * @Description 根据原学生id查询成绩项，并插入到新的成绩主表id的成绩项中
     */
    @Override
    @Transactional
    public UpdateStuGradeMainForm insertStuItemGradeByStuGradeMainId(Long stuGradeMainId, Long cacheStuGradeMainId) {


        // 根据主表id获取其成绩项目
        StuItemGradeExample stuItemGradeExample = new StuItemGradeExample();
        stuItemGradeExample.createCriteria()
                .andStuGradeMainIdEqualTo(stuGradeMainId)
                .andDeletedEqualTo(GradeConstant.RECORD_VALID);
        List<StuItemGrade> stuItemGrades = stuItemGradeMapper.selectByExample(stuItemGradeExample);
        // 获取成绩项的id集合
        List<Long> stuItemGradeIds = stuItemGrades.stream().map(
                stuItemGrade -> {
                    return stuItemGrade.getId();
                }
        ).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(stuItemGrades)) {
            log.info("【修改已经提交的成绩】拷贝成绩项失败, stuGradeMainId = {}",
                    stuGradeMainId);
            // todo 报错处理 优化：异常
        }

        // 拷贝成绩项
        List<Long> cacheStuItemGradeIds = stuItemGrades.stream().map(
                stuItemGrade -> {
                    stuItemGrade.setId(null);
                    stuItemGrade.setStuGradeMainId(cacheStuGradeMainId);
                    int i = stuItemGradeMapper.insertSelective(stuItemGrade);
                    if (i == 0) {
                        // todo 插入报错，优化：抛异常
                    }
                    StuItemGradeExample stuItemGradeExample1 =
                            new StuItemGradeExample();
                    stuItemGradeExample1.createCriteria()
                            .andDeletedEqualTo(stuItemGrade.getDeleted())
                            .andStuGradeMainIdEqualTo(cacheStuGradeMainId)
                            .andUniversityIdEqualTo(stuItemGrade.getUniversityId())
                            .andCourseItemIdEqualTo(stuItemGrade.getCourseItemId())
                            .andByWhoEqualTo(stuItemGrade.getByWho());
                    List<StuItemGrade> stuItemGradesCache =
                            stuItemGradeMapper.selectByExample(stuItemGradeExample1);

                    if (CollectionUtils.isEmpty(stuItemGradesCache) || stuItemGradesCache.size() != 1) {
                        // todo 只能查询一个，优化：抛异常
                    }

                    return stuItemGradesCache.get(0).getId();
                }
        ).collect(Collectors.toList());
        UpdateStuGradeMainForm updateStuGradeMainForm = new UpdateStuGradeMainForm();
        updateStuGradeMainForm.setStuItemGradeIds(stuItemGradeIds);
        updateStuGradeMainForm.setCacheStuItemGradeIds(cacheStuItemGradeIds);
        return updateStuGradeMainForm;
    }

    @Override

    /**
     *
     * @Param [universityId, courseItemId, teacherId, stuGradeMainId]
     * @Author: 陈汉槟
     * @Date: 2019/5/12 18:05
     * @Description: 根据学校id，课程成绩项id，教师id，学生主表id 创建成绩项
     */
    public boolean insertStuGradeItemByExcelContent(
            Long universityId, Long courseItemId, Long teacherId, Long stuGradeMainId) {
        StuItemGrade stuItemGrade = new StuItemGrade();
        stuItemGrade.setUniversityId(universityId);
        stuItemGrade.setStuGradeMainId(stuGradeMainId);
        stuItemGrade.setCourseItemId(courseItemId);
        stuItemGrade.setByWho(teacherId);
        int i = stuItemGradeMapper.insertSelective(stuItemGrade);
        if (i == 0) {
            // todo 插入错误，优化：异常
            return false;
        }
        return true;
    }

    @Override

    /**
     *
     * @Param [stuItemGradeId]
     * @Return:edu.uni.grademanagement.bean.StuItemGrade
     * @Author: deschen
     * @Date: 2019/5/21 13:56
     * @Description: 根据成绩项id获取成绩项
     */
    public StuItemGrade selectByStuItemGradeId(Long stuItemGradeId) {
        StuItemGrade stuItemGrade = stuItemGradeMapper.selectByPrimaryKey(stuItemGradeId);
        return stuItemGrade;
    }


}
