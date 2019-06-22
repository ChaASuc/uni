package edu.uni.grademanagement.service.impl;

import edu.uni.grademanagement.mapper.StuAbsenceMapper;
import edu.uni.grademanagement.service.StuAbSenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author 陈汉槟
 * @Create 2019/6/11
 * @Description
 * @Since 1.0.0
 */
@Service
public class StuAbSenceServiceImpl implements StuAbSenceService {

    @Autowired
    private StuAbsenceMapper stuAbsenceMapper;

    @Override
    public String staticc(Long userId, Long semesterId, Long courseId) {
        String user = String.valueOf(userId);
        Byte[] status = stuAbsenceMapper.staticc(user, semesterId, courseId);

        Integer[] count = new Integer[]{0, 0, 0, 0};


        for(Byte item : status){
            if(item == 1){
                count[0]++;
            }else if(item==2){
                count[1]++;
            }else if(item==3){
                count[2]++;
            }else if(item==4){
                count[3]++;
            }
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < count.length; i++) {
            if (i == count.length - 1) {
                sb.append(count[i]);
            }else {

                sb.append(count[i]).append("-");
            }
        }

        return sb.toString();
    }
}
