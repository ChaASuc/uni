package edu.uni.administrativestructure.service;

import com.github.pagehelper.PageInfo;
import edu.uni.administrativestructure.bean.DepartmentSubdepartment;

import java.util.List;
/**
 author:黄永佳
 create:2019.4.25
 modified:null
 功能:创建DepartmentService接口
 **/
public interface DepartmentSubdepartmentService {
    /**
     * 新增一级部门二级部门关系
     * @param departmentSubdepartment
     * @return
     */
    boolean insert(DepartmentSubdepartment departmentSubdepartment);

    /**
     * 删除一级部门二级部门关系
     * @param id
     * @return
     */
    boolean delete(long id);

    /**
     * 修改一级部门二级部门关系
     * @param
     * @return
     */
    boolean update(DepartmentSubdepartment departmentSubdepartment);

    /**
     * 查询一级部门二级部门关系详情
     * @param id
     * @return
     */
    DepartmentSubdepartment select(long id);

    /**
     * 分页查询所有一二级部门关系
     * @param pageNum
     * @return
     */
    PageInfo<DepartmentSubdepartment> selectPage(int pageNum);

    /**
     * 分学校分页查询一二级部门关系
     * @param universityId
     * @return
     */
    List<DepartmentSubdepartment> selectPageByUniversity(long universityId);

    /**
     * 分一级部门分页查询部门关系
     * @param departmentId
     * @return
     */
    List<DepartmentSubdepartment> selectPageByDepartment(long departmentId);

    /**
     * 分二级部门分页查询部门关系
     * @param subdepartmentId
     * @return
     */
    List<DepartmentSubdepartment> selectPageBySubdepartment(long subdepartmentId);

    /**
     * 查找所有部门关系
     * @return
     */
    List<DepartmentSubdepartment> selectAll();
}
