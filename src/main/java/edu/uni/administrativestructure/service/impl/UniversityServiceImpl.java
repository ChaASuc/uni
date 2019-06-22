package edu.uni.administrativestructure.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.uni.administrativestructure.ExcelBean.ExcelUniversity;
import edu.uni.administrativestructure.bean.University;
import edu.uni.administrativestructure.bean.UniversityExample;
import edu.uni.administrativestructure.config.administrativeStructureConfig;
import edu.uni.administrativestructure.mapper.UniversityMapper;
import edu.uni.administrativestructure.service.UniversityService;
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
 * modified: 2019.6.13
 * 功能：学校接口实现类
 */
@Service
public class UniversityServiceImpl implements UniversityService {
    @Autowired
    private UniversityMapper universityMapper;

    @Autowired
    private administrativeStructureConfig globalConfig;

    /**
     * 保存学校
     * @param university
     * @return
     */
    @Override
    public boolean insert(University university) {
        return universityMapper.insert(university) > 0 ? true : false;
    }

    /**
     * 删除学校
     * @param id
     * @return
     */
    @Override
    public boolean delete(long id) {
        //获取删除前的数据
        University university1 = universityMapper.selectByPrimaryKey(id);
        university1.setDeleted(true);
        //判断能否删除
        if(universityMapper.deleteByPrimaryKey(id)<=0){
            return false;
        }else{
            //删除后插入作废记录
            if(universityMapper.insert(university1)<=0){
                return false;
            }
            else
                return true;
        }
    }

    /**
     * 更新学校
     * @param university
     * @return
     */
    public boolean update(University university){
        //获取修改前的数据
        long id1 = university.getId();
        University university1 = universityMapper.selectByPrimaryKey(id1);
        university1.setDeleted(true);
        //修改数据
        if(universityMapper.updateByPrimaryKeySelective(university)<=0){
            return false;
        }
        //修改后插入修改前的数据
        if(universityMapper.insert(university1)<=0){
            return false;
        }
        else
            return true;
    }

    /**
     * 查找学校
     * @param id
     * @return
     */
    @Override
    public University select(long id) {
        return universityMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据名称模糊查找学校
     * @return
     */
    @Override
    public List<University> selectLikeName(String name) {
        //筛选出有效记录
        UniversityExample example = new UniversityExample();
        UniversityExample.Criteria criteria = example.createCriteria();
        criteria.andNameLike("%"+name+"%").andDeletedEqualTo(false);
        List<University> universities = universityMapper.selectByExample(example);
        return universities;
    }

    /**
     * 查找所有学校
     * @return
     */
    @Override
    public List<University> selectAll() {
        //筛选出有效记录
        UniversityExample example = new UniversityExample();
        UniversityExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo(false);
        List<University> universities = universityMapper.selectByExample(example);
        return universities;
    }

    /**
     * 分页查找所有学校
     * @return
     */
    @Override
    public PageInfo<University> selectPage(int pageNum) {
        PageHelper.startPage(pageNum, globalConfig.getPageSize());    // 开启分页查询，第一次切仅第一次查询时生效
        //筛选出有效记录
        UniversityExample example = new UniversityExample();
        UniversityExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo(false);
        List<University> universities = universityMapper.selectByExample(example);
        if(universities != null)
            return new PageInfo<>(universities);
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
    public int uploadUniversity(String savePath, MultipartFile file) throws IOException {
        //先保存文件到本地
        saveFile(savePath,file);
        int i = 0;
        //解析excel
        List<ExcelUniversity> data = ExcelUtil.readExcel(file.getInputStream(), ExcelUniversity.class);
        // 插入数据库
        for(ExcelUniversity u: data){
            University university= new University();
            university.setUnitNumber(u.getUnitNumber());
            university.setSocialTrustCode(u.getSocialTrustCode());
            university.setCertificationCode(u.getCertificationCode());
            university.setEnterpriseCode(u.getEnterpriseCode());
            university.setName(u.getName());
            university.setEname(u.getEname());
            university.setStatus(u.getStatus());
            university.setFundingSources(u.getFundingSources());
            university.setEstablishDate(u.getEstablishDate());
            university.setHostedBy(u.getHostedBy());
            university.setAdminiBy(u.getAdminiBy());
            university.setInitialFunding(u.getInitialFunding());
            university.setCertificationBeginDate(u.getCertificationBeginDate());
            university.setCertificationEndDate(u.getCertificationEndDate());
            university.setTelephone(u.getTelephone());
            university.setAddress(u.getAddress());
            university.setDatetime(new Date());
            university.setByWho(1L);
            university.setDeleted(false);
            if(universityMapper.insert(university)>0){
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
