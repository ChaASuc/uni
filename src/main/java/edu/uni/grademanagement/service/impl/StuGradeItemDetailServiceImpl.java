package edu.uni.grademanagement.service.impl;

import edu.uni.grademanagement.bean.*;
import edu.uni.grademanagement.constants.GradeConstant;
import edu.uni.grademanagement.dto.StuGradeItemDetailDto;
import edu.uni.grademanagement.form.UpdateStuGradeMainForm;
import edu.uni.grademanagement.mapper.CourseItemDetailMapper;
import edu.uni.grademanagement.mapper.StuItemGradeDetailMapper;
import edu.uni.grademanagement.service.StuGradeItemDetailService;
import edu.uni.grademanagement.utils.FileNameUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 陈汉槟
 * @Create 2019/5/8
 * @Description
 * @Since 1.0.0
 */
@Service
@Slf4j
public class StuGradeItemDetailServiceImpl implements StuGradeItemDetailService {


    @Autowired
    private CourseItemDetailMapper courseItemDetailMapper;

    @Autowired
    private StuItemGradeDetailMapper stuItemGradeDetailMapper;

    @Override

    /**
     *
     * @Param [stuItemGradeId]
     * @Return:java.util.List<edu.uni.grademanagement.dto.StuGradeItemDetialDto>
     * @Author: 陈汉槟
     * @Date: 2019/5/8 15:50
     * @Description: 根据学生课程成绩项目id获取
     */
    public List<StuGradeItemDetailDto> selectByStuItemGradeId(
            Long stuItemGradeId, Integer flag) {
        StuItemGradeDetailExample stuItemGradeDetailExample = new StuItemGradeDetailExample();
        StuItemGradeDetailExample.Criteria criteria = stuItemGradeDetailExample.createCriteria()
                .andStuItemGradeIdEqualTo(stuItemGradeId)
                .andDeletedEqualTo(GradeConstant.RECORD_VALID);
        if (flag == null) {
            criteria.andCacheEqualTo(GradeConstant.CACHE_INVALID);
        }
        List<StuItemGradeDetail> stuItemGradeDetails =
                stuItemGradeDetailMapper.selectByExample(stuItemGradeDetailExample);
        if (CollectionUtils.isEmpty(stuItemGradeDetails)) {
            return null;
        }
        List<StuGradeItemDetailDto> stuGradeItemDetailDtos = stuItemGradeDetails.stream().map(
                stuItemGradeDetail -> {
                    StuGradeItemDetailDto stuGradeItemDetailDto = new StuGradeItemDetailDto();
                    BeanUtils.copyProperties(stuItemGradeDetail, stuGradeItemDetailDto);

                    String attachment = stuGradeItemDetailDto.getAttachment();
                    if (attachment != null) {
                        attachment = new FileNameUtil(attachment).getFileName();
                        stuGradeItemDetailDto.setAttachment(attachment);
                    }
                    CourseItemDetail courseItemDetail =
                            courseItemDetailMapper.selectByPrimaryKey(stuItemGradeDetail.getCourseItemDetailId());
                    /* // todo 优化 成绩作业项一定有课程成绩评分项目
                    if (courseItemDetail == null) {
                       // 报错
                    }*/

//                    BeanUtils.copyProperties(courseItemDetail, stuGradeItemDetailDto);
                    stuGradeItemDetailDto.setCourseItemId(courseItemDetail.getCourseItemId());
                    stuGradeItemDetailDto.setNumber(courseItemDetail.getNumber());
                    stuGradeItemDetailDto.setContent(courseItemDetail.getContent());
                    return stuGradeItemDetailDto;
                }
        ).collect(Collectors.toList());
        return stuGradeItemDetailDtos;
    }



    @Override
    public boolean insertStuGradeItemDetailDto(CourseItemDetail courseItemDetail, Long stuGradeMainId) {
        return false;
    }

    @Override
    public boolean deletedUpdateStuGradeItem(List<StuItemGrade> stuItemGrades) {
        return false;
    }

    @Override

    /**
     *
     * @Param [stuItemGradeDetail]
     * @Return:boolean
     * @Author: 陈汉槟
     * @Date: 2019/5/9 22:35
     * @Description: 插入学生作业项
     */
    @Transactional
    public boolean insertStuItemGradeDetail(StuItemGradeDetail stuItemGradeDetail) {
        int i = stuItemGradeDetailMapper.insertSelective(stuItemGradeDetail);
        if (i == 0) {
            throw new RuntimeException("成绩项明细创建失败");
        }
        return true;
    }


    @Override

    /**
     *
     * @Param [stuItemGradeid, courseItemDetailId]
     * @Return:boolean
     * @Author: 陈汉槟
     * @Date: 2019/5/10 1:21
     * @Description: 根据学生成绩项id何课程评分项id获取作业项目，并删除
     */
    @Transactional
    public boolean deleteStuItemGradeDetailByStuItemGradeIdAndCourseItemDetailId(Long stuItemGradeid, Long courseItemDetailId) {
        StuItemGradeDetailExample stuItemGradeDetailExample = new StuItemGradeDetailExample();
        stuItemGradeDetailExample.createCriteria()
                .andStuItemGradeIdEqualTo(stuItemGradeid)
                .andCourseItemDetailIdEqualTo(courseItemDetailId)
                .andDeletedEqualTo(GradeConstant.RECORD_VALID);
        List<StuItemGradeDetail> stuItemGradeDetails = stuItemGradeDetailMapper.selectByExample(stuItemGradeDetailExample);
        for (StuItemGradeDetail stuItemGradeDetail:
        stuItemGradeDetails) {
            stuItemGradeDetail.setDeleted(GradeConstant.RECORD_INVALID);
            int update = stuItemGradeDetailMapper.updateByPrimaryKey(stuItemGradeDetail);
            if (update == 0) {
                return false;
            }
        }
        return true;
    }

    @Override

    /**
     *
     * @Param [stuItemGradeDetails]
     * @Return:boolean
     * @Author: 陈汉槟
     * @Date: 2019/5/12 1:31
     * @Description: 根据学生成绩项评分项集合更新
     */
    @Transactional
    public boolean updateStuItemGradeDetails(List<StuItemGradeDetail> stuItemGradeDetails) throws SQLException {
        for (StuItemGradeDetail stuItemGradeDetail :
                stuItemGradeDetails) {
            int update = stuItemGradeDetailMapper.updateByPrimaryKeySelective(stuItemGradeDetail);
            if (update == 0) {
                throw new SQLException("成绩项更新失败");
            }
        }
        return true;
    }

    @Override

    /**
     *
     * @Param [form]
     * @Return:boolean
     * @Author: 陈汉槟
     * @Date: 2019/5/11 11:15
     * @Description: 根据UpdateStuGradeMainForm里原学生成绩项id集合与缓存学生成绩项id集合
     */
    @Transactional
    public boolean insertStuItemGradeDetailsByUpdateStuGradeMainForm(UpdateStuGradeMainForm form) {

        List<Long> stuItemGradeIds = form.getStuItemGradeIds();
        List<Long> cacheStuItemGradeIds = form.getCacheStuItemGradeIds();
        for (int i = 0; i < stuItemGradeIds.size(); i++) {
            Long stuItemGradeId = stuItemGradeIds.get(i);
            Long cacheStuItemGradeId = cacheStuItemGradeIds.get(i);
            StuItemGradeDetailExample stuItemGradeDetailExample =
                    new StuItemGradeDetailExample();
            stuItemGradeDetailExample.createCriteria()
                    .andStuItemGradeIdEqualTo(stuItemGradeId)
                    .andDeletedEqualTo(GradeConstant.RECORD_VALID);
            List<StuItemGradeDetail> stuItemGradeDetails =
                    stuItemGradeDetailMapper.selectByExample(stuItemGradeDetailExample);
            for (StuItemGradeDetail stuItemGradeDetail :
                    stuItemGradeDetails) {
                stuItemGradeDetail.setId(null);
                stuItemGradeDetail.setStuItemGradeId(cacheStuItemGradeId);
                int i1 = stuItemGradeDetailMapper.insertSelective(
                        stuItemGradeDetail
                );
                if (i1 == 0) {
                    // todo 插入错误 优化：抛出异常
                    return false;
                }
            }
        }
        return true;
    }

    @Override

    /**
     *
     * @Param [id, courseItemDetailId, universityId, teacherId]
     * @Return:boolean
     * @Author: 陈汉槟
     * @Date: 2019/5/12 18:39
     * @Description: 根据学校id，课程评分项id，教师id，成绩项idd 创建成绩作业项
     */
    public boolean insertStuGradeItemByExcelContent(Long stuItemGradeId, Long courseItemDetailId, Long universityId,
                                                    Long teacherId, double score, String note) throws SQLException {
        StuItemGradeDetail stuItemGradeDetail = new StuItemGradeDetail();
        stuItemGradeDetail.setUniversityId(universityId);
        stuItemGradeDetail.setCourseItemDetailId(courseItemDetailId);
        stuItemGradeDetail.setByWho(teacherId);
        stuItemGradeDetail.setStuItemGradeId(stuItemGradeId);
        stuItemGradeDetail.setScore(score);
        stuItemGradeDetail.setNote(note);
        int i = stuItemGradeDetailMapper.insertSelective(stuItemGradeDetail);
        if (i != 0) {
            return true;
        }
        throw new SQLException("成绩作业项插入失败");
    }

    @Override

    /**
     *
     * @Param [stuItemGradeId, courseItemDetailId]
     * @Return:edu.uni.grademanagement.bean.StuItemGradeDetail
     * @Author: 陈汉槟
     * @Date: 2019/5/12 18:54
     * @Description: 根据成绩项id，课程评分项id获取成绩作业项
     */
    public StuItemGradeDetail selectByStuItemGradeIdAndCourseItemDetailId(
            Long stuItemGradeId, Long courseItemDetailId, Integer cache) {
        StuItemGradeDetailExample stuItemGradeDetailExample =
                new StuItemGradeDetailExample();
        StuItemGradeDetailExample.Criteria criteria = stuItemGradeDetailExample.createCriteria()
                .andStuItemGradeIdEqualTo(stuItemGradeId)
                .andCourseItemDetailIdEqualTo(courseItemDetailId)
                .andDeletedEqualTo(GradeConstant.RECORD_VALID);
        if (cache != null) {
            criteria.andCacheEqualTo(cache);
        }
        List<StuItemGradeDetail> stuItemGradeDetails =
                stuItemGradeDetailMapper.selectByExample(stuItemGradeDetailExample);

        if (CollectionUtils.isEmpty(stuItemGradeDetails)) {
            return null;
        }
        return stuItemGradeDetails.get(0);
    }

    @Override

    /**
     *
     * @Param [stuItemGradeId]
     * @Return:java.util.List<edu.uni.grademanagement.bean.StuItemGradeDetail>
     * @Author: 陈汉槟
     * @Date: 2019/5/12 21:21
     * @Description: 根据成绩项目id获取所有成绩评分项
     * @Modify: 添加缓存功能和角色区分功能
     */
    public List<StuItemGradeDetail> selectStuItemGradeDetailByStuItemGradeId(Long stuItemGradeId,
                                                                             Integer flag) {
        StuItemGradeDetailExample stuItemGradeDetailExample = new StuItemGradeDetailExample();
        StuItemGradeDetailExample.Criteria criteria = stuItemGradeDetailExample.createCriteria()
                .andStuItemGradeIdEqualTo(stuItemGradeId)
                .andDeletedEqualTo(GradeConstant.RECORD_VALID);
        if (flag == null) {
            criteria.andCacheEqualTo(GradeConstant.CACHE_INVALID);
        }
        List<StuItemGradeDetail> stuItemGradeDetails =
                stuItemGradeDetailMapper.selectByExample(stuItemGradeDetailExample);

        return stuItemGradeDetails;
    }

    @Override
    public StuItemGradeDetail selectByStuItemGradeDetailId(Long stuItemGradeDetailId) {
        StuItemGradeDetail stuItemGradeDetail =
                stuItemGradeDetailMapper.selectByPrimaryKey(stuItemGradeDetailId);
        return stuItemGradeDetail;
    }

    @Override

    /**
     * @Param: [stuItemGradeIds]
     * @Return:void
     * @Author: 陈汉槟
     * @Date: 2019/6/4 13:07
     * @Description: 更新成绩作业项目
     */
    @Transactional
    public void updateStuGradeItemDetailStateToCacheInvalid(List<Long> stuItemGradeIds) {
        StuItemGradeDetail stuItemGradeDetail = new StuItemGradeDetail();
        stuItemGradeDetail.setCache(GradeConstant.CACHE_INVALID);
        StuItemGradeDetailExample stuItemGradeDetailExample = new StuItemGradeDetailExample();
        stuItemGradeDetailExample.createCriteria().andStuItemGradeIdIn(stuItemGradeIds);
        int success =
                stuItemGradeDetailMapper.updateByExampleSelective(stuItemGradeDetail, stuItemGradeDetailExample);
        if (success == 0) {
            throw new RuntimeException("成绩作业项目更新失败");
        }
    }

    @Override

    /**
     * @Param: [stuItemGradeDetail]
     * @Return:void
     * @Author: 陈汉槟
     * @Date: 2019/6/4 13:41
     * @Description: 更新成绩作业项目
     */
    public void updateStuItemGradeDetail(StuItemGradeDetail stuItemGradeDetail) {
        int success = stuItemGradeDetailMapper.updateByPrimaryKeySelective(stuItemGradeDetail);
        if (success == 0) {
            throw new RuntimeException("成绩作业项目附件更新失败");
        }
    }

    @Override
    public void rebulidStuGradeItemDetail(
            Long stuItemGradeId, Long rebulidStuItemGradeId, Integer teacher) {
        StuItemGradeDetailExample stuItemGradeDetailExample =
                new StuItemGradeDetailExample();
        stuItemGradeDetailExample.createCriteria()
                .andStuItemGradeIdEqualTo(stuItemGradeId)
                .andDeletedEqualTo(GradeConstant.RECORD_VALID);
        List<StuItemGradeDetail> stuItemGradeDetails =
                stuItemGradeDetailMapper.selectByExample(stuItemGradeDetailExample);
        stuItemGradeDetails.stream().forEach(
                stuItemGradeDetail -> {
                    stuItemGradeDetail.setId(null);
                    stuItemGradeDetail.setAttachment(null);
                    stuItemGradeDetail.setCache(GradeConstant.CACHE_VALID);
                    stuItemGradeDetail.setNote(null);
                    stuItemGradeDetail.setScore(0D);
                    stuItemGradeDetail.setStuItemGradeId(rebulidStuItemGradeId);
                    int success = stuItemGradeDetailMapper.insertSelective(stuItemGradeDetail);
                    if (success == 0) {
                        throw new RuntimeException("重修成绩明细创建失败");
                    }
                }
        );
    }

//    @Override

    /**
     *
     * @Param [courseItem, stuGradeMainId]
     * @Return:boolean
     * @Author: 陈汉槟
     * @Date: 2019/5/8 16:04
     * @Description:
     */
    public boolean insertStuGradeItemDto(CourseItemDetail courseItemDetail, Long stuGradeMainId) {

        return false;
    }

}
