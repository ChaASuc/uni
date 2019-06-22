package edu.uni.professionalcourses.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.uni.professionalcourses.bean.SpecialtyCourse;
import edu.uni.professionalcourses.bean.SpecialtyCourseExample;
import edu.uni.professionalcourses.config.ProfessionalCoursesConfig;
import edu.uni.professionalcourses.mapper.SpecialtyCourseMapper;
import edu.uni.professionalcourses.service.SpecialtyCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author : 黄永佳
 * create : 2019/4/29 0029 11:10
 * modified :
 * 功能 :SpecialtyCourseService接口的实现类
 **/
@Service
public class SpecialtyCourseServiceImpl implements SpecialtyCourseService {
    @Autowired
    private SpecialtyCourseMapper specialtyCourseMapper;
    @Autowired
    private ProfessionalCoursesConfig globalConfig;
//    新增专业课程信息
    @Override
    public boolean insert(SpecialtyCourse specialtyCourse) {
        return specialtyCourseMapper.insert(specialtyCourse) > 0 ? true : false;
    }
//  删除专业课程信息
    @Override
    public boolean delete(long id) {
        SpecialtyCourse specialtyCourse=specialtyCourseMapper.selectByPrimaryKey(id);
        if(specialtyCourse!=null){
            specialtyCourse.setDeleted(true);
            specialtyCourseMapper.updateByPrimaryKeySelective(specialtyCourse);
            return true;
        }
        else {
            return false;
        }
    }
//  修改专业课程信息
    @Override
    public boolean update(SpecialtyCourse specialtyCourse) {
        SpecialtyCourse specialtyCourse1= new SpecialtyCourse();
        specialtyCourse1= specialtyCourseMapper.selectByPrimaryKey(specialtyCourse.getId());
        specialtyCourse1.setDeleted(true);
        if (specialtyCourseMapper.updateByPrimaryKeySelective(specialtyCourse) <= 0) {
            return false;
        }
        if (specialtyCourseMapper.insert(specialtyCourse1) <= 0) {
            return false;
        }
        return true;    }
//查询专业课程信息
    @Override
    public SpecialtyCourse select(long id) {
        return specialtyCourseMapper.selectByPrimaryKey(id);
    }
//分页查询所有专业课程信息
    @Override
    public PageInfo<SpecialtyCourse> selectPage(int pageNum) {
        PageHelper.startPage(pageNum, globalConfig.getPageSize());    // 开启分页查询，第一次切仅第一次查询时生效
        SpecialtyCourseExample example = new SpecialtyCourseExample();
        SpecialtyCourseExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo(false);
        List<SpecialtyCourse> specialtyCourses = specialtyCourseMapper.selectByExample(example);   //  无条件查找
        if(specialtyCourses != null)
            return new PageInfo<>(specialtyCourses);
        else
            return null;
    }
//分学校id分页查询所有专业课程信息
    @Override
    public PageInfo<SpecialtyCourse> selectPageByUniversity(int pageNum, long universityId) {
        PageHelper.startPage(pageNum, globalConfig.getPageSize());    // 开启分页查询，第一次切仅第一次查询时生效
        // 创建查询条件
        SpecialtyCourseExample example = new SpecialtyCourseExample();
        SpecialtyCourseExample.Criteria criteria = example.createCriteria();
        criteria.andUniversityIdEqualTo(universityId).andDeletedEqualTo(false);
        // 根据条件查询
        List<SpecialtyCourse> specialtyCourses = specialtyCourseMapper.selectByExample(example);
        if(specialtyCourses != null)
            return new PageInfo<>(specialtyCourses);
        else
            return null;
    }
//分专业分页查询专业课程信息
    @Override
    public PageInfo<SpecialtyCourse> selectPageBySpecialty(int pageNum, long specialtyId) {
        PageHelper.startPage(pageNum, globalConfig.getPageSize());    // 开启分页查询，第一次切仅第一次查询时生效
        // 创建查询条件
        SpecialtyCourseExample example = new SpecialtyCourseExample();
        SpecialtyCourseExample.Criteria criteria = example.createCriteria();
        criteria.andSpecialtyIdEqualTo(specialtyId).andDeletedEqualTo(false);
        // 根据条件查询
        List<SpecialtyCourse> specialtyCourses = specialtyCourseMapper.selectByExample(example);
        if(specialtyCourses != null)
            return new PageInfo<>(specialtyCourses);
        else
            return null;
    }
//分课程分页查询专业课程信息
    @Override
    public PageInfo<SpecialtyCourse> selectPageByCourse(int pageNum, long courseId) {
        PageHelper.startPage(pageNum, globalConfig.getPageSize());    // 开启分页查询，第一次切仅第一次查询时生效
        // 创建查询条件
        SpecialtyCourseExample example = new SpecialtyCourseExample();
        SpecialtyCourseExample.Criteria criteria = example.createCriteria();
        criteria.andCourseIdEqualTo(courseId).andDeletedEqualTo(false);
        // 根据条件查询
        List<SpecialtyCourse> specialtyCourses = specialtyCourseMapper.selectByExample(example);
        if(specialtyCourses != null)
            return new PageInfo<>(specialtyCourses);
        else
            return null;
    }

    @Override
    public List<SpecialtyCourse> selectAll() {
        return specialtyCourseMapper.selectByExample(null);
    }
    @Override
    public List<SpecialtyCourse> selectBySpecialtyId(long specialty) {
        SpecialtyCourseExample example = new SpecialtyCourseExample();
        SpecialtyCourseExample.Criteria criteria = example.createCriteria();
        criteria.andSpecialtyIdEqualTo(specialty).andDeletedEqualTo(false);
        return specialtyCourseMapper.selectByExample(example);
    }
}
