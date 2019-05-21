package edu.uni.grademanagement.mapper;

import edu.uni.grademanagement.bean.StuGradeMain;
import edu.uni.grademanagement.bean.StuGradeMainExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StuGradeMainMapper {
    int countByExample(StuGradeMainExample example);

    int deleteByExample(StuGradeMainExample example);

    int deleteByPrimaryKey(Long id);

    int insert(StuGradeMain record);

    int insertSelective(StuGradeMain record);

    /*自定义sql*/
    @Select("SELECT DISTINCT semester_id FROM stu_grade_main WHERE student_id = #{studentId} AND deleted = #{deleted}")
    List<Long> selectDistinctSemesterId(Long studentId, Integer deleted);

    int insertStuGradeMains(@Param("records")List<StuGradeMain> records);

    List<StuGradeMain> selectByExample(StuGradeMainExample example);

    StuGradeMain selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") StuGradeMain record, @Param("example") StuGradeMainExample example);

    int updateByExample(@Param("record") StuGradeMain record, @Param("example") StuGradeMainExample example);

    int updateByPrimaryKeySelective(StuGradeMain record);

    int updateByPrimaryKey(StuGradeMain record);
}