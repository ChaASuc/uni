package edu.uni.administrativestructure.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.uni.administrativestructure.ExcelBean.ExcelSubdepartment;
import edu.uni.administrativestructure.bean.DepartmentSubdepartment;
import edu.uni.administrativestructure.bean.DepartmentSubdepartmentExample;
import edu.uni.administrativestructure.bean.Subdepartment;
import edu.uni.administrativestructure.bean.SubdepartmentExample;
import edu.uni.administrativestructure.config.administrativeStructureConfig;
import edu.uni.administrativestructure.mapper.DepartmentSubdepartmentMapper;
import edu.uni.administrativestructure.mapper.SubdepartmentMapper;
import edu.uni.administrativestructure.service.SubdepartmentService;
import edu.uni.administrativestructure.utils.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * author：黄育林
 * create: 2019.4.20
 * modified: 2019.5.17
 * 功能：三级部门实现类
 */
@Service
public class SubdepartmentServiceImpl implements SubdepartmentService {
    @Autowired
    private SubdepartmentMapper subdepartmentMapper;

    @Autowired
    private DepartmentSubdepartmentMapper departmentSubdepartmentMapper;

    @Autowired
    private administrativeStructureConfig globalConfig;

    @Override
    public boolean insert(Subdepartment subdepartment) {
        if(subdepartmentMapper.insert(subdepartment)<=0){
            return false;
        }
        //将记录插入关系表
        DepartmentSubdepartment departmentSubdepartment = new DepartmentSubdepartment();
        departmentSubdepartment.setUniversityId(subdepartment.getUniversityId());
        departmentSubdepartment.setByWho(subdepartment.getByWho());
        departmentSubdepartment.setDatetime(subdepartment.getDatetime());
        departmentSubdepartment.setDeleted(subdepartment.getDeleted());
        departmentSubdepartment.setDepartmentId(subdepartment.getDepartmentId());
        departmentSubdepartment.setSubdepartmentId(subdepartment.getId());
        if(departmentSubdepartmentMapper.insert(departmentSubdepartment)<=0){
            return false;
        }
        else
            return true;
    }

    @Override
    public boolean delete(long id) {
        Subdepartment subdepartment = subdepartmentMapper.selectByPrimaryKey(id);
        //创造查询条件
        DepartmentSubdepartmentExample example = new DepartmentSubdepartmentExample();
        DepartmentSubdepartmentExample.Criteria criteria = example.createCriteria();
        criteria.andSubdepartmentIdEqualTo(id);
        //获取关系表内记录
        List<DepartmentSubdepartment> departmentSubdepartments = departmentSubdepartmentMapper.selectByExample(example);
        DepartmentSubdepartment departmentSubdepartment = departmentSubdepartments.get(0);
        long id1 = departmentSubdepartment.getId();
        //删除关系表内记录
        departmentSubdepartmentMapper.deleteByPrimaryKey(id1);
        if(subdepartmentMapper.deleteByPrimaryKey(id)<=0){
            //无法删除，重新插入关系表记录
            departmentSubdepartmentMapper.insert(departmentSubdepartment);
            return false;
        }else{
            subdepartment.setDeleted(true);
            //删除后插入旧记录，不再插入关系表
            if(subdepartmentMapper.insert(subdepartment)<=0){
                return false;
            }else{
                return true;
            }
        }
    }

    @Override
    public boolean update(Subdepartment subdepartment) {
        //获取旧数据
        long id = subdepartment.getId();
        Subdepartment subdepartment1 = subdepartmentMapper.selectByPrimaryKey(id);
        subdepartment1.setDeleted(true);
        //更新数据
        if(subdepartmentMapper.updateByPrimaryKeySelective(subdepartment)<=0){
            return false;
        }
        //插入旧数据
        if(subdepartmentMapper.insert(subdepartment1)<=0){
            return false;
        }
        //判断对应关系表内容是否发生变化
        if(subdepartment.getDepartmentId()!=subdepartment1.getDepartmentId()){
            //获取关系表原本数据
            long id1 = subdepartment.getId();
            DepartmentSubdepartmentExample example = new DepartmentSubdepartmentExample();
            DepartmentSubdepartmentExample.Criteria criteria = example.createCriteria();
            criteria.andSubdepartmentIdEqualTo(id1);
            List<DepartmentSubdepartment> departmentSubdepartments = departmentSubdepartmentMapper.selectByExample(example);
            DepartmentSubdepartment departmentSubdepartment = departmentSubdepartments.get(0);
            departmentSubdepartment.setDepartmentId(subdepartment.getDepartmentId());
            //更新关系表内数据
            if(departmentSubdepartmentMapper.updateByPrimaryKeySelective(departmentSubdepartment)<=0){
                return false;
            }
            else
                return true;
        }
        else
            return true;
    }

    @Override
    public Subdepartment select(long id) {
        return subdepartmentMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Subdepartment> selectLikeName(String name) {
        //筛选出有效记录
        SubdepartmentExample example = new SubdepartmentExample();
        SubdepartmentExample.Criteria criteria = example.createCriteria();
        criteria.andNameLike("%"+name+"%").andDeletedEqualTo(false);
        List<Subdepartment> subdepartments = subdepartmentMapper.selectByExample(example);
        return subdepartments;
    }

    @Override
    public PageInfo<Subdepartment> selectPage(int pageNum) {
        PageHelper.startPage(pageNum, globalConfig.getPageSize());    // 开启分页查询，第一次切仅第一次查询时生效
        //筛选条件
        SubdepartmentExample example = new SubdepartmentExample();
        SubdepartmentExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo(false);
        //筛选有效记录
        List<Subdepartment> subdepartments = subdepartmentMapper.selectByExample(example);
        if(subdepartments != null)
            return new PageInfo<>(subdepartments);
        else
            return null;
    }

    @Override
    public PageInfo<Subdepartment> selectPageByDepartment(int pageNum, long departmentId) {
        PageHelper.startPage(pageNum, globalConfig.getPageSize());    // 开启分页查询，第一次切仅第一次查询时生效
        // 创建查询条件
        SubdepartmentExample example = new SubdepartmentExample();
        SubdepartmentExample.Criteria criteria = example.createCriteria();
        criteria.andDepartmentIdEqualTo(departmentId).andDeletedEqualTo(false);
        // 根据条件查询
        List<Subdepartment> subdepartments = subdepartmentMapper.selectByExample(example);
        if(subdepartments != null)
            return new PageInfo<>(subdepartments);
        else
            return null;
    }

    @Override
    public List<Subdepartment> selectAll() {
        //筛选有效记录
        SubdepartmentExample example = new SubdepartmentExample();
        SubdepartmentExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo(false);
        // 根据条件查询
        List<Subdepartment> subdepartments = subdepartmentMapper.selectByExample(example);
        return subdepartments;
    }

    @Override
    public PageInfo<Subdepartment> selectPageByUniversity(int pageNum, long universityId) {
        PageHelper.startPage(pageNum, globalConfig.getPageSize());    // 开启分页查询，第一次切仅第一次查询时生效
        // 创建查询条件
        SubdepartmentExample example = new SubdepartmentExample();
        SubdepartmentExample.Criteria criteria = example.createCriteria();
        criteria.andUniversityIdEqualTo(universityId).andDeletedEqualTo(false);
        // 根据条件查询
        List<Subdepartment> subdepartments = subdepartmentMapper.selectByExample(example);
        if(subdepartments != null)
            return new PageInfo<>(subdepartments);
        else
            return null;
    }

    /**
     * excel导入
     * @param savePath
     * @param file
     * @return
     * @throws IOException
     */
    @Override
    public int uploadSubdepartment(String savePath, MultipartFile file) throws IOException {
        //先保存文件到本地
        saveFile(savePath,file);
        int i = 0;
        //解析excel
        List<ExcelSubdepartment> data = ExcelUtil.readExcel(file.getInputStream(), ExcelSubdepartment.class);
        // 插入数据库
        for(ExcelSubdepartment s: data){
            Subdepartment subdepartment= new Subdepartment();
            subdepartment.setUniversityId(s.getUniversityId());
            subdepartment.setDepartmentId(s.getDepartmentId());
            subdepartment.setName(s.getName());
            subdepartment.setEname(s.getEname());
            subdepartment.setTelephone(s.getTelephone());
            subdepartment.setHead(s.getHead());
            subdepartment.setOfficeRoom(s.getOfficeRoom());
            subdepartment.setDatetime(new Date());
            subdepartment.setByWho(1L);
            subdepartment.setDeleted(false);
            if(subdepartmentMapper.insert(subdepartment)>0){
                i++;
            }
        }
        return i;
    }

    /**
     * 保存文件
     * @param path
     * @param file
     * @throws IOException
     */
    private void saveFile(String path, MultipartFile file) throws IOException {
        System.out.println(path);
        final String p = "excel";
        File dir = new File(path,p);
        if(!dir.exists()){
            dir.mkdir();
        }
        String imgName = UUID.randomUUID().toString()+file.getOriginalFilename();
        File upload = new File(dir,imgName);
//        FileUtils.copyInputStreamToFile(file.getInputStream(),upload);
    }

}
