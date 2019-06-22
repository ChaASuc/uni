package edu.uni.administrativestructure.service;

import com.github.pagehelper.PageInfo;
import edu.uni.administrativestructure.bean.Class;

import java.util.List;

/**
 * author : 黄永佳
 * create : 2019/4/26 0026 12:27
 * modified :
 * 功能 :创建class表的接口
 **/
public interface ClassService {
    /**
     * 新增班级信息
     * @param class_1
     * @return
     */
    boolean insert(Class class_1);

    /**
     * 删除班级信息
     * @param id
     * @return
     */
    boolean delete(long id);

    /**
     * 修改班级信息
     * @param
     * @return
     */
    boolean update(Class class_1);

    /**
     * 查询一级部门二级部门关系详情
     * @param id
     * @return
     */
    Class select(long id);

    /**
     * 分页查询所有一二级部门关系
     * @param pageNum
     * @return
     */
    PageInfo<Class> selectPage(int pageNum);

    /**
     * 分学校查询一二级部门关系
     * @param universityId
     * @return
     */
    List<Class> selectPageByUniversity(long universityId);

    /**
     * 分一级部门查询部门关系
     * @param departmentId
     * @return
     */
    List<Class> selectPageByDepartment(long departmentId);

    /**
     * 分二级部门分页查询部门关系
     * @param pageNum
     * @param specialtyId
     * @return
     */
    PageInfo<Class> selectPageBySpecialty(int pageNum, long specialtyId);

    //    遍历所有
    List<Class> selectAll();
    /*
    @param code
    @return
    根据code查找班级
    */
    Class selectByCode(String code);
    //根据专业id查班级
    List<Class> selectSpecilty(long specialty_id);
    /*
     * 根据部门名称模糊查询记录
     * */
    List<Class> selectLikeName(String name);

}
