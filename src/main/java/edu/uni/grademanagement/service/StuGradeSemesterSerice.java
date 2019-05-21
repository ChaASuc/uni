package edu.uni.grademanagement.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import edu.uni.educateAffair.bean.Semester;
import edu.uni.grademanagement.vo.GradeFilter;
import edu.uni.grademanagement.vo.StuGradeCurriculumVO;

import java.util.Date;

/**
 * @Author 陈汉槟
 * @Create 2019/5/14
 * @Description 获取学生成绩及课程信息
 * @Since 1.0.0
 */
public interface StuGradeSemesterSerice {

//    /*根据过滤器和页码获取分页信息*/
//    PageInfo<StuGradeCurriculumVO> selectPageByGradeFilter(
//            GradeFilter gradeFilter, Integer pageNum
//    );

    /*根据当前时间获取学期*/
    Semester selectByNow(Date now);
}
