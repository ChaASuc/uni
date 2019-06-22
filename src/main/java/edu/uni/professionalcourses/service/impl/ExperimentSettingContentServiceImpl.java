package edu.uni.professionalcourses.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.uni.professionalcourses.bean.ExperimentSettingContent;
import edu.uni.professionalcourses.bean.ExperimentSettingContentExample;
import edu.uni.professionalcourses.config.ProfessionalCoursesConfig;
import edu.uni.professionalcourses.mapper.ExperimentSettingContentMapper;
import edu.uni.professionalcourses.service.ExperimentSettingContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author : 黄永佳
 * create : 2019/5/5 0005 17:08
 * modified :
 * 功能 :实现ExperimentSettingContentService接口
 **/
@Service
public class ExperimentSettingContentServiceImpl implements ExperimentSettingContentService {
    @Autowired
    private ExperimentSettingContentMapper experimentSettingContentMapper;
    @Autowired
    private ProfessionalCoursesConfig globalConfig;
//    增加
    @Override
    public boolean insert(ExperimentSettingContent experimentSettingContent) {
        return experimentSettingContentMapper.insert(experimentSettingContent) > 0 ? true : false;
    }
//删除
    @Override
    public boolean delete(long id) {
        ExperimentSettingContent experimentSettingContent=experimentSettingContentMapper.selectByPrimaryKey(id);
        if(experimentSettingContent!=null){
            experimentSettingContent.setDeleted(true);
            experimentSettingContentMapper.updateByPrimaryKeySelective(experimentSettingContent);
            return true;
        }
        else {
            return false;
        }
//        return experimentSettingContentMapper.deleteByPrimaryKey(id) > 0 ? true : false;
    }
//修改
    @Override
    public boolean update(ExperimentSettingContent experimentSettingContent) {
        ExperimentSettingContent experimentSettingContent1 = new ExperimentSettingContent();
        experimentSettingContent1 = experimentSettingContentMapper.selectByPrimaryKey(experimentSettingContent.getId());
        experimentSettingContent1.setDeleted(true);
        if (experimentSettingContentMapper.updateByPrimaryKeySelective(experimentSettingContent) <= 0) {
            return false;
        }
        if (experimentSettingContentMapper.insert(experimentSettingContent1) <= 0) {
            return false;
        }
        return true;
    }
//查询
    @Override
    public ExperimentSettingContent select(long id) {
        return experimentSettingContentMapper.selectByPrimaryKey(id);    }
//分页查询所有
    @Override
    public PageInfo<ExperimentSettingContent> selectPage(int pageNum) {
        PageHelper.startPage(pageNum, globalConfig.getPageSize());    // 开启分页查询，第一次切仅第一次查询时生效
        ExperimentSettingContentExample example = new ExperimentSettingContentExample();
        ExperimentSettingContentExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo(false);
        List<ExperimentSettingContent> experimentSettingContents = experimentSettingContentMapper.selectByExample(example);   //  无条件查找
        if(experimentSettingContents != null)
            return new PageInfo<>(experimentSettingContents);
        else
            return null;
    }
//根据学校id分页查询
    @Override
    public PageInfo<ExperimentSettingContent> selectPageByUniversity(int pageNum, long universityId) {
        PageHelper.startPage(pageNum, globalConfig.getPageSize());    // 开启分页查询，第一次切仅第一次查询时生效
        // 创建查询条件
        ExperimentSettingContentExample example = new ExperimentSettingContentExample();
        ExperimentSettingContentExample.Criteria criteria = example.createCriteria();
        criteria.andUniversityIdEqualTo(universityId).andDeletedEqualTo(false);
        // 根据条件查询
        List<ExperimentSettingContent> experimentSettingContents= experimentSettingContentMapper.selectByExample(example);
        if(experimentSettingContents != null)
            return new PageInfo<>(experimentSettingContents);
        else
            return null;
    }
//根据course_experiment_description_Id分页查询
    @Override
    public PageInfo<ExperimentSettingContent> selectPageByCourseexperimentdescriptionId(int pageNum, long course_experiment_description_Id) {
        PageHelper.startPage(pageNum, globalConfig.getPageSize());    // 开启分页查询，第一次切仅第一次查询时生效
        // 创建查询条件
        ExperimentSettingContentExample example = new ExperimentSettingContentExample();
        ExperimentSettingContentExample.Criteria criteria = example.createCriteria();
        criteria.andCourseExperimentDescriptionIdEqualTo(course_experiment_description_Id).andDeletedEqualTo(false);
        // 根据条件查询
        List<ExperimentSettingContent> experimentSettingContents= experimentSettingContentMapper.selectByExample(example);
        if(experimentSettingContents != null)
            return new PageInfo<>(experimentSettingContents);
        else
            return null;
    }

    @Override
    public List<ExperimentSettingContent> selectAll() {
        return experimentSettingContentMapper.selectByExample(null);
    }
}
