package edu.uni.grademanagement.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Author 陈汉槟
 * @Create 2019/6/11
 * @Description
 * @Since 1.0.0
 */
@Mapper
public interface StuAbsenceMapper {

    @Select("select a.status\n" +
            "from am_stu_absence a\n" +
            "inner join ea_curriculum b on a.curriculum_id=b.id\n" +
            "inner join ea_canlendar c on b.canlendar_id=c.id\n" +
            "inner join course d on b.course_id=d.id\n" +
            "where a.student_id=#{a} and c.semester_id=#{b} and b.course_id=#{c};")
    Byte[] staticc(@Param("a") String user, @Param("b") Long semester, @Param("c") Long course);
}
