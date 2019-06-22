package edu.uni.professionalcourses.service;

import com.github.pagehelper.PageInfo;
import edu.uni.professionalcourses.bean.ExperimentSettingContent;

import java.util.List;

/**
 * author : 黄永佳
 * create : 2019/5/5 0005 17:03
 * modified :
 * 功能 :创建ExperimentSettingContent接口
 **/
public interface ExperimentSettingContentService {
    /**
     * 新增
     * @param experimentSettingContent
     * @return
     */
    boolean insert(ExperimentSettingContent experimentSettingContent);

    /**
     * 删除
     * @param id
     * @return
     */
    boolean delete(long id);

    /**
     * 修改
     * @param
     * @return
     */
    boolean update(ExperimentSettingContent experimentSettingContent);

    /**
     * 查询
     * @param id
     * @return
     */
    ExperimentSettingContent select(long id);

    /**
     * 分页查询所有
     * @param pageNum
     * @return
     */
    PageInfo<ExperimentSettingContent> selectPage(int pageNum);
    /**
     * 分学校分页查询
     * @param pageNum
     * @param universityId
     * @return
     */
    PageInfo<ExperimentSettingContent> selectPageByUniversity(int pageNum, long universityId);
    /**
     * 分理论教学大纲分页查询
     * @param pageNum
     * @param course_experiment_description_Id
     * @return
     */
    PageInfo<ExperimentSettingContent> selectPageByCourseexperimentdescriptionId(int pageNum, long course_experiment_description_Id);
    /**
     * 查找所有
     * @return
     */
    List<ExperimentSettingContent> selectAll();
}
