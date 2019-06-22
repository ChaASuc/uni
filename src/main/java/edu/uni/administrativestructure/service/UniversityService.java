package edu.uni.administrativestructure.service;

import com.github.pagehelper.PageInfo;
import edu.uni.administrativestructure.bean.University;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * author：黄育林
 * create: 2019.4.20
 * modified:2019.6.13
 * 功能：学校接口
 */
public interface UniversityService {
    /**
     * 保存学校
     * @param university
     * @return
     */
    boolean insert(University university);

    /**
     * 删除学校
     * @param id
     * @return
     */
    boolean delete(long id);

    /**
     * 更新学校
     * @param university
     * @return
     */
    boolean update(University university);

    /**
     * 查找学校
     * @param id
     * @return
     */
    University select(long id);

    /**
     * 根据名称模糊查找学校
     * @param
     * @return
     */
    List<University> selectLikeName(String name);

    /**
     * 查找所有学校
     * @return
     */
    List<University> selectAll();

    /**
     * 分页查询所有学校
     * @param pageNum
     * @return
     */
    PageInfo<University> selectPage(int pageNum);

    /**
     * Excel导入
     */
    int uploadUniversity(String savePath, MultipartFile file) throws IOException;
}
