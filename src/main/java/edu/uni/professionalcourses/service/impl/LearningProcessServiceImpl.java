package edu.uni.professionalcourses.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.uni.professionalcourses.bean.LearningProcess;
import edu.uni.professionalcourses.bean.LearningProcessExample;
import edu.uni.professionalcourses.config.ProfessionalCoursesConfig;
import edu.uni.professionalcourses.mapper.LearningProcessMapper;
import edu.uni.professionalcourses.service.LearningProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author : 黄永佳
 * create : 2019/4/30 0030 15:42
 * modified :
 * 功能 :实现LearningProcessService接口
 **/
@Service
public class LearningProcessServiceImpl implements LearningProcessService {
    @Autowired
    private LearningProcessMapper learningProcessMapper;
    @Autowired
    private ProfessionalCoursesConfig globalConfig;
//    在表中插入新数据
    @Override
    public boolean insert(LearningProcess learningProcess) {
        return learningProcessMapper.insert(learningProcess) > 0 ? true : false;

    }
//根据id删除数据
    @Override
    public boolean delete(long id) {
        LearningProcess learningProcess=learningProcessMapper.selectByPrimaryKey(id);
        if(learningProcess!=null){
            learningProcess.setDeleted(true);
            learningProcessMapper.updateByPrimaryKeySelective(learningProcess);
            return true;
        }
        else {
            return false;
        }
    }
//修改
    @Override
    public boolean update(LearningProcess learningProcess) {
        LearningProcess learningProcess1= new LearningProcess();
        learningProcess1= learningProcessMapper.selectByPrimaryKey(learningProcess.getId());
        learningProcess1.setDeleted(true);
        if (learningProcessMapper.updateByPrimaryKeySelective(learningProcess) <= 0) {
            return false;
        }
        if (learningProcessMapper.insert(learningProcess1) <= 0) {
            return false;
        }
        return true;
    }
//查询
    @Override
    public LearningProcess select(long id) {
        return learningProcessMapper.selectByPrimaryKey(id);
    }
//分页查询
    @Override
    public PageInfo<LearningProcess> selectPage(int pageNum) {
        PageHelper.startPage(pageNum, globalConfig.getPageSize());    // 开启分页查询，第一次切仅第一次查询时生效
        LearningProcessExample example = new LearningProcessExample();
        LearningProcessExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo(false);
        List<LearningProcess> learningProcesses= learningProcessMapper.selectByExample(example);   //  无条件查找课程
        if(learningProcesses != null)
            return new PageInfo<>(learningProcesses);
        else
            return null;
    }
//根据学校id分页查询
    @Override
    public PageInfo<LearningProcess> selectPageByUniversity(int pageNum, long universityId) {
        PageHelper.startPage(pageNum, globalConfig.getPageSize());    // 开启分页查询，第一次切仅第一次查询时生效
        // 创建查询条件
        LearningProcessExample example = new LearningProcessExample();
        LearningProcessExample.Criteria criteria = example.createCriteria();
        criteria.andUniversityIdEqualTo(universityId).andDeletedEqualTo(false);
        // 根据条件查询
        List<LearningProcess> specialtyCourses = learningProcessMapper.selectByExample(example);
        if(specialtyCourses != null)
            return new PageInfo<>(specialtyCourses);
        else
            return null;
    }
//根据专业id分页查询
    @Override
    public PageInfo<LearningProcess> selectPageBySpecialty(int pageNum, long specialtyid) {
        PageHelper.startPage(pageNum, globalConfig.getPageSize());    // 开启分页查询，第一次切仅第一次查询时生效
        // 创建查询条件
        LearningProcessExample example = new LearningProcessExample();
        LearningProcessExample.Criteria criteria = example.createCriteria();
        criteria.andSpecialtyIdEqualTo(specialtyid).andDeletedEqualTo(false);
        // 根据条件查询
        List<LearningProcess> learningProcesses = learningProcessMapper.selectByExample(example);
        if(learningProcesses != null)
            return new PageInfo<>(learningProcesses);
        else
            return null;
    }
//根据课程id分页查询
    @Override
    public PageInfo<LearningProcess> selectPageByCourse(int pageNum, long courseid) {
        PageHelper.startPage(pageNum, globalConfig.getPageSize());    // 开启分页查询，第一次切仅第一次查询时生效
        // 创建查询条件
        LearningProcessExample example = new LearningProcessExample();
        LearningProcessExample.Criteria criteria = example.createCriteria();
        criteria.andCourseIdEqualTo(courseid).andDeletedEqualTo(false);
        // 根据条件查询
        List<LearningProcess> specialtyCourses = learningProcessMapper.selectByExample(example);
        if(specialtyCourses != null)
            return new PageInfo<>(specialtyCourses);
        else
            return null;
    }

    @Override
    public List<LearningProcess> selectAll() {
        return learningProcessMapper.selectByExample(null);
    }

}
