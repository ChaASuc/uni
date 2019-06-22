package edu.uni.grademanagement.excel.listen;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import edu.uni.grademanagement.excel.model.ReadModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelListener extends AnalysisEventListener {


    private List<Object>  data = new ArrayList<Object>();

    // 收集错误信息，哪行那列,报什么错误
    private Map<Map<Integer, Integer>, String> map = new HashMap<Map<Integer, Integer>, String>();

    // 记录行数，用于记录报错行
    private int error_row;

    @Override
    public void invoke(Object object, AnalysisContext context) {
        // 每读取一行记录就加一
        error_row++;
        System.out.println(context.getCurrentSheet());
//        data.add(object);
//        System.out.println("【excel工具】" + data.toString());
//        if (data.size() >= 100) {
            doSomething(object);
//            data = new ArrayList<Object>();
//        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        doSomething();
    }
    public void doSomething(){
        for (Object o:data) {
            ReadModel model = (ReadModel) o;
            System.out.println(model);
        }
    }

    public void doSomething(Object o){
        ReadModel model = (ReadModel) o;

        System.out.println(model);
    }
}
