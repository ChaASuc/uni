package edu.uni.professionalcourses.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.uni.professionalcourses.bean.LearningProcessSemesterAllocation;
import edu.uni.professionalcourses.bean.LearningProcessSemesterAllocationExample;
import edu.uni.professionalcourses.config.ProfessionalCoursesConfig;
import edu.uni.professionalcourses.mapper.LearningProcessSemesterAllocationMapper;
import edu.uni.professionalcourses.service.LearningProcessSemesterAllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author : 黄永佳
 * create : 2019/4/29 0029 16:46
 * modified :
 * 功能 :实现LearningProcessSemesterAllocationService接口
 **/
@Service
public class LearningProcessSemesterAllocationServiceImpl implements LearningProcessSemesterAllocationService {

    @Autowired
    private LearningProcessSemesterAllocationMapper learningProcessSemesterAllocationMapper;
    @Autowired
    private ProfessionalCoursesConfig globalConfig;
//    增加
    @Override
    public boolean insert(LearningProcessSemesterAllocation learningProcessSemesterAllocation) {
        return learningProcessSemesterAllocationMapper.insert(learningProcessSemesterAllocation) > 0 ? true : false;
    }
//删除
    @Override
    public boolean delete(long id) {
        LearningProcessSemesterAllocation learningProcessSemesterAllocation=learningProcessSemesterAllocationMapper.selectByPrimaryKey(id);
        if(learningProcessSemesterAllocation!=null){
            learningProcessSemesterAllocation.setDeleted(true);
            learningProcessSemesterAllocationMapper.updateByPrimaryKeySelective(learningProcessSemesterAllocation);
            return true;
        }
        else {
            return false;
        }
    }
//修改
    @Override
    public boolean update(LearningProcessSemesterAllocation learningProcessSemesterAllocation) {
        LearningProcessSemesterAllocation learningProcessSemesterAllocation1= new LearningProcessSemesterAllocation();
        learningProcessSemesterAllocation1 = learningProcessSemesterAllocationMapper.selectByPrimaryKey(learningProcessSemesterAllocation.getId());
        learningProcessSemesterAllocation1.setDeleted(true);
        if (learningProcessSemesterAllocationMapper.updateByPrimaryKeySelective(learningProcessSemesterAllocation) <= 0) {
            return false;
        }
        if (learningProcessSemesterAllocationMapper.insert(learningProcessSemesterAllocation1) <= 0) {
            return false;
        }
        return true;
    }
//根据id查询
    @Override
    public LearningProcessSemesterAllocation select(long id) {
        return learningProcessSemesterAllocationMapper.selectByPrimaryKey(id);    }
//分页查询所有
    @Override
    public PageInfo<LearningProcessSemesterAllocation> selectPage(int pageNum) {
        PageHelper.startPage(pageNum, globalConfig.getPageSize());    // 开启分页查询，第一次切仅第一次查询时生效
        LearningProcessSemesterAllocationExample example = new LearningProcessSemesterAllocationExample();
        LearningProcessSemesterAllocationExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo(false);
        List<LearningProcessSemesterAllocation> learningProcessSemesterAllocations = learningProcessSemesterAllocationMapper.selectByExample(example);   //  无条件查找
        if(learningProcessSemesterAllocations != null)
            return new PageInfo<>(learningProcessSemesterAllocations);
        else
            return null;
    }
//根据学校id分页查询
    @Override
    public PageInfo<LearningProcessSemesterAllocation> selectPageByUniversity(int pageNum, long universityId) {
        PageHelper.startPage(pageNum, globalConfig.getPageSize());    // 开启分页查询，第一次切仅第一次查询时生效
        // 创建查询条件
        LearningProcessSemesterAllocationExample example = new LearningProcessSemesterAllocationExample();
        LearningProcessSemesterAllocationExample.Criteria criteria = example.createCriteria();
        criteria.andUniversityIdEqualTo(universityId).andDeletedEqualTo(false);
        // 根据条件查询
        List<LearningProcessSemesterAllocation> learningProcessSemesterAllocations = learningProcessSemesterAllocationMapper.selectByExample(example);
        if(learningProcessSemesterAllocations != null)
            return new PageInfo<>(learningProcessSemesterAllocations);
        else
            return null;
    }
//根据专业学习计划表id分页查询
    @Override
    public PageInfo<LearningProcessSemesterAllocation> selectPageByLearningprocess(int pageNum, long learningprocessId) {
        PageHelper.startPage(pageNum, globalConfig.getPageSize());    // 开启分页查询，第一次切仅第一次查询时生效
        // 创建查询条件
        LearningProcessSemesterAllocationExample example = new LearningProcessSemesterAllocationExample();
        LearningProcessSemesterAllocationExample.Criteria criteria = example.createCriteria();
        criteria.andLearningProcessIdEqualTo(learningprocessId).andDeletedEqualTo(false);
        // 根据条件查询
        List<LearningProcessSemesterAllocation> learningProcessSemesterAllocations= learningProcessSemesterAllocationMapper.selectByExample(example);
        if(learningProcessSemesterAllocations != null)
            return new PageInfo<>(learningProcessSemesterAllocations);
        else
            return null;
    }

    @Override
    public List<LearningProcessSemesterAllocation> selectAll() {
        return learningProcessSemesterAllocationMapper.selectByExample(null);
    }
}
