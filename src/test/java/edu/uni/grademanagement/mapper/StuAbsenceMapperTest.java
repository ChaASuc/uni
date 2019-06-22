package edu.uni.grademanagement.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.uni.utils.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StuAbsenceMapperTest {

    @Autowired
    private StuAbsenceMapper stuAbsenceMapper;

    @Test
    public void staticc() throws JsonProcessingException {

        Byte[] status = stuAbsenceMapper.staticc("618", 40L, 45L);

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
        System.out.println(JsonUtils.obj2String(count));
    }


}