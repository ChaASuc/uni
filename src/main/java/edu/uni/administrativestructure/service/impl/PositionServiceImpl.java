package edu.uni.administrativestructure.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.uni.administrativestructure.ExcelBean.ExcelPosition;
import edu.uni.administrativestructure.bean.Position;
import edu.uni.administrativestructure.bean.PositionExample;
import edu.uni.administrativestructure.config.administrativeStructureConfig;
import edu.uni.administrativestructure.mapper.PositionMapper;
import edu.uni.administrativestructure.service.PositionService;
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
 * 功能：岗位信息实现类
 */
@Service
public class PositionServiceImpl implements PositionService {
    @Autowired
    private PositionMapper positionMapper;

    @Autowired
    private administrativeStructureConfig globalConfig;

    @Override
    public boolean insert(Position position) {
        return positionMapper.insert(position) > 0 ? true : false;
    }

    @Override
    public boolean delete(long id) {
        //获取旧数据
        Position position = positionMapper.selectByPrimaryKey(id);
        position.setDeleted(true);
        //删除记录
        if(positionMapper.deleteByPrimaryKey(id)<=0){
            return false;
        }
        //删除后插入旧的记录
        if(positionMapper.insert(position)<=0){
            return false;
        }
        else
            return true;
    }

    @Override
    public boolean update(Position position) {
        //获取修改前的数据
        long id = position.getId();
        Position position1 = positionMapper.selectByPrimaryKey(id);
        position1.setDeleted(true);
        //修改数据
        if(positionMapper.updateByPrimaryKeySelective(position)<=0){
            return false;
        }
        //修改后插入旧的记录
        if(positionMapper.insert(position1)<=0){
            return false;
        }
        else
            return true;
    }

    @Override
    public Position select(long id) {
        return positionMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Position> selectLikeName(String name) {
        //筛选出有效记录
        PositionExample example = new PositionExample();
        PositionExample.Criteria criteria = example.createCriteria();
        criteria.andNameLike("%"+name+"%").andDeletedEqualTo(false);
        List<Position> positions = positionMapper.selectByExample(example);
        return positions;
    }

    @Override
    public PageInfo<Position> selectPage(int pageNum) {
        PageHelper.startPage(pageNum, globalConfig.getPageSize());    // 开启分页查询，第一次切仅第一次查询时生效
        // 创建查询条件
        PositionExample example = new PositionExample();
        PositionExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo(false);
        // 根据条件查询
        List<Position> positions = positionMapper.selectByExample(example);
        if(positions != null)
            return new PageInfo<>(positions);
        else
            return null;
    }

    @Override
    public List<Position> selectAll() {
        // 创建查询条件
        PositionExample example = new PositionExample();
        PositionExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo(false);
        return positionMapper.selectByExample(example);
    }

    @Override
    public PageInfo<Position> selectPageByUniversity(int pageNum, long universityId) {
        PageHelper.startPage(pageNum, globalConfig.getPageSize());    // 开启分页查询，第一次切仅第一次查询时生效
        // 创建查询条件
        PositionExample example = new PositionExample();
        PositionExample.Criteria criteria = example.createCriteria();
        criteria.andUniversityIdEqualTo(universityId).andDeletedEqualTo(false);
        // 根据条件查询
        List<Position> positions = positionMapper.selectByExample(example);
        if(positions != null)
            return new PageInfo<>(positions);
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
    public int uploadposition(String savePath, MultipartFile file) throws IOException {
        //先保存文件到本地
        saveFile(savePath,file);
        int i = 0;
        //解析excel
        List<ExcelPosition> data = ExcelUtil.readExcel(file.getInputStream(), ExcelPosition.class);
        // 插入数据库
        for(ExcelPosition p: data){
            Position position= new Position();
            position.setUniversityId(p.getUniversityId());
            position.setName(p.getName());
            position.setEname(p.getEname());
            position.setDatetime(new Date());
            position.setByWho(1L);
            position.setDeleted(false);
            if(positionMapper.insert(position)>0){
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
