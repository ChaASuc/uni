package edu.uni.professionalcourses.service;

import com.github.pagehelper.PageInfo;
import edu.uni.professionalcourses.bean.SpecialtyCourse;

import java.util.List;

/**
 * author : 黄永佳
 * create : 2019/4/29 0029 11:02
 * modified :
 * 功能 :专业课程表的接口
 **/
public interface SpecialtyCourseService {
    /**
     * 新增专业课程信息
     * @param specialtyCourse
     * @return
     */
    boolean insert(SpecialtyCourse specialtyCourse);

    /**
     * 删除专业课程信息
     * @param id
     * @return
     */
    boolean delete(long id);

    /**
     * 修改专业课程信息
     * @param
     * @return
     */
    boolean update(SpecialtyCourse specialtyCourse);

    /**
     * 查询专业课程信息
     * @param id
     * @return
     */
    SpecialtyCourse select(long id);

    /**
     * 分页查询所有专业课程信息
     * @param pageNum
     * @return
     */
    PageInfo<SpecialtyCourse> selectPage(int pageNum);
    /**
     * 分学校分页查询专业课程
     * @param pageNum
     * @param universityId
     * @return
     */
    PageInfo<SpecialtyCourse> selectPageByUniversity(int pageNum, long universityId);
    /**
     * 分专业分页查询专业课程
     * @param pageNum
     * @param specialtyId
     * @return
     */
    PageInfo<SpecialtyCourse> selectPageBySpecialty(int pageNum, long specialtyId);
    /**
     * 分课程分页查询专业课程
     * @param pageNum
     * @param courseId
     * @return
     */
    PageInfo<SpecialtyCourse> selectPageByCourse(int pageNum, long courseId);
    /**
     * 查找所有
     * @return
     */
    List<SpecialtyCourse> selectAll();
    List<SpecialtyCourse> selectBySpecialtyId(long specialtyId);
}
