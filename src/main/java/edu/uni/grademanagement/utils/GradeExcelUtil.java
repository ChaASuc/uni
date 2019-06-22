package edu.uni.grademanagement.utils;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.apache.poi.poifs.filesystem.FileMagic;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * @Author 陈汉槟
 * @Create 2019/5/2
 * @Description 成绩excel文件录入校验工具
 * @Since 1.0.0
 */
public final class GradeExcelUtil {

    static class ExcelListener<T extends BaseRowModel> extends AnalysisEventListener<T>{
        private final List<T> rows = new ArrayList<>();

        @Override
        public void invoke(T object, AnalysisContext context) {
            rows.add(object);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
        }

        public List<T> getRows() {
            return rows;
        }
    }
    /**
     * 从Excel中读取文件，读取的文件是一个DTO类，该类必须继承BaseRowModel
     * 具体实例参考 ： MemberMarketDto.java
     * 参考：https://github.com/alibaba/easyexcel
     * 字符流必须支持标记，FileInputStream 不支持标记，可以使用BufferedInputStream 代替
     * BufferedInputStream bis = new BufferedInputStream(new FileInputStream(...));
     */
    public static <T extends BaseRowModel> List<T> readExcel(final InputStream inputStream, final Class<? extends BaseRowModel> clazz) {
        if (null == inputStream) {
            throw new NullPointerException("the inputStream is null!");
        }
        ExcelListener<T> listener = new ExcelListener<>();
        // 这里因为EasyExcel-1.1.1版本的bug，所以需要选用下面这个标记已经过期的版本
        ExcelReader reader = new ExcelReader(inputStream,null,listener);
        reader.read(new com.alibaba.excel.metadata.Sheet(1, 2, clazz));

        return listener.getRows();
    }


    public static void writeExcel(String filePath, List<List<Object>> list) throws FileNotFoundException {
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        OutputStream out = new FileOutputStream(filePath);
        ExcelWriter writer = EasyExcelFactory.getWriter(out);
        //写第一个sheet, sheet1  数据全是List<String> 无模型映射关系
        Sheet sheet1 = new Sheet(1, 2);
        sheet1.setSheetName("第一个sheet");

        //设置列宽 设置每列的宽度
        Map columnWidth = new HashMap();
        columnWidth.put(0,10000);columnWidth.put(1,40000);columnWidth.put(2,10000);columnWidth.put(3,10000);
        sheet1.setColumnWidthMap(columnWidth);
        sheet1.setHead(createGradeListStringHead());
        //or 设置自适应宽度
//        sheet1.setAutoWidth(Boolean.TRUE);
        writer.write1(list, sheet1);
    }


    /**
     * 根据输入流，判断为xls还是xlsx，该方法原本存在于easyexcel 1.1.0 的ExcelTypeEnum中。
     */
    public static ExcelTypeEnum valueOf(InputStream inputStream) {
        try {
            FileMagic fileMagic = FileMagic.valueOf(inputStream);
            if (FileMagic.OLE2.equals(fileMagic)) {
                return ExcelTypeEnum.XLS;
            }
            if (FileMagic.OOXML.equals(fileMagic)) {
                return ExcelTypeEnum.XLSX;
            }
            throw new IllegalArgumentException("excelTypeEnum can not null");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // 表头
    public static  List<List<String>> createGradeListStringHead(){

        List<List<String>> head = new ArrayList<List<String>>();
        List<String> headCoulumn1 = new ArrayList<String>();
        List<String> headCoulumn2 = new ArrayList<String>();
        List<String> headCoulumn3 = new ArrayList<String>();
        List<String> headCoulumn4 = new ArrayList<String>();
        List<String> headCoulumn5 = new ArrayList<String>();
        List<String> headCoulumn6 = new ArrayList<String>();
        List<String> headCoulumn7 = new ArrayList<String>();
        List<String> headCoulumn8 = new ArrayList<String>();

        /*headCoulumn1.add("说明：（1）总成绩可以不填写，如果是百分制,导入时总成绩是由系统按照该课程设置的比例和导入的分项成绩计算出来，如果是二级制或者五级制则按照总成绩导入\n" +
                "（2）若成绩方式选择二级制，可以在总成绩中填写（合格 或 不合格），也可以添加分数\n" +
                "（3）若成绩方式选择五级制，可以在总成绩中填写（不及格、及格、中等、良好 或 优秀），也可以添加分数\n" +
                "（4）平时成绩、实验成绩、期中成绩和期末成绩为原始成绩（即未折算前的成绩）\n" +
                "\n" +
                "注意：此单元格不能删除，否则会出现缺漏     平时成绩x10% 实验成绩x90% ");*/
        headCoulumn1.add("学号");

        /*headCoulumn2.add("说明：（1）总成绩可以不填写，如果是百分制,导入时总成绩是由系统按照该课程设置的比例和导入的分项成绩计算出来，如果是二级制或者五级制则按照总成绩导入\n" +
                "（2）若成绩方式选择二级制，可以在总成绩中填写（合格 或 不合格），也可以添加分数\n" +
                "（3）若成绩方式选择五级制，可以在总成绩中填写（不及格、及格、中等、良好 或 优秀），也可以添加分数\n" +
                "（4）平时成绩、实验成绩、期中成绩和期末成绩为原始成绩（即未折算前的成绩）\n" +
                "\n" +
                "注意：此单元格不能删除，否则会出现缺漏     平时成绩x10% 实验成绩x90% ");*/
        headCoulumn2.add("姓名");

        /*headCoulumn3.add("说明：（1）总成绩可以不填写，如果是百分制,导入时总成绩是由系统按照该课程设置的比例和导入的分项成绩计算出来，如果是二级制或者五级制则按照总成绩导入\n" +
                "（2）若成绩方式选择二级制，可以在总成绩中填写（合格 或 不合格），也可以添加分数\n" +
                "（3）若成绩方式选择五级制，可以在总成绩中填写（不及格、及格、中等、良好 或 优秀），也可以添加分数\n" +
                "（4）平时成绩、实验成绩、期中成绩和期末成绩为原始成绩（即未折算前的成绩）\n" +
                "\n" +
                "注意：此单元格不能删除，否则会出现缺漏     平时成绩x10% 实验成绩x90% ");*/
        headCoulumn3.add("序号");

        /*headCoulumn4.add("说明：（1）总成绩可以不填写，如果是百分制,导入时总成绩是由系统按照该课程设置的比例和导入的分项成绩计算出来，如果是二级制或者五级制则按照总成绩导入\n" +
                "（2）若成绩方式选择二级制，可以在总成绩中填写（合格 或 不合格），也可以添加分数\n" +
                "（3）若成绩方式选择五级制，可以在总成绩中填写（不及格、及格、中等、良好 或 优秀），也可以添加分数\n" +
                "（4）平时成绩、实验成绩、期中成绩和期末成绩为原始成绩（即未折算前的成绩）\n" +
                "\n" +
                "注意：此单元格不能删除，否则会出现缺漏     平时成绩x10% 实验成绩x90% ");*/
        headCoulumn4.add("成绩");

        /*headCoulumn5.add("说明：（1）总成绩可以不填写，如果是百分制,导入时总成绩是由系统按照该课程设置的比例和导入的分项成绩计算出来，如果是二级制或者五级制则按照总成绩导入\n" +
                "（2）若成绩方式选择二级制，可以在总成绩中填写（合格 或 不合格），也可以添加分数\n" +
                "（3）若成绩方式选择五级制，可以在总成绩中填写（不及格、及格、中等、良好 或 优秀），也可以添加分数\n" +
                "（4）平时成绩、实验成绩、期中成绩和期末成绩为原始成绩（即未折算前的成绩）\n" +
                "\n" +
                "注意：此单元格不能删除，否则会出现缺漏     平时成绩x10% 实验成绩x90% ");*/
        headCoulumn5.add("课程编号");

        /*headCoulumn6.add("说明：（1）总成绩可以不填写，如果是百分制,导入时总成绩是由系统按照该课程设置的比例和导入的分项成绩计算出来，如果是二级制或者五级制则按照总成绩导入\n" +
                "（2）若成绩方式选择二级制，可以在总成绩中填写（合格 或 不合格），也可以添加分数\n" +
                "（3）若成绩方式选择五级制，可以在总成绩中填写（不及格、及格、中等、良好 或 优秀），也可以添加分数\n" +
                "（4）平时成绩、实验成绩、期中成绩和期末成绩为原始成绩（即未折算前的成绩）\n" +
                "\n" +
                "注意：此单元格不能删除，否则会出现缺漏     平时成绩x10% 实验成绩x90% ")
        ;*/headCoulumn6.add("课程名称");

        /*headCoulumn7.add("说明：（1）总成绩可以不填写，如果是百分制,导入时总成绩是由系统按照该课程设置的比例和导入的分项成绩计算出来，如果是二级制或者五级制则按照总成绩导入\n" +
                "（2）若成绩方式选择二级制，可以在总成绩中填写（合格 或 不合格），也可以添加分数\n" +
                "（3）若成绩方式选择五级制，可以在总成绩中填写（不及格、及格、中等、良好 或 优秀），也可以添加分数\n" +
                "（4）平时成绩、实验成绩、期中成绩和期末成绩为原始成绩（即未折算前的成绩）\n" +
                "\n" +
                "注意：此单元格不能删除，否则会出现缺漏     平时成绩x10% 实验成绩x90% ");*/
        headCoulumn7.add("班级编号");

        /*headCoulumn8.add("说明：（1）总成绩可以不填写，如果是百分制,导入时总成绩是由系统按照该课程设置的比例和导入的分项成绩计算出来，如果是二级制或者五级制则按照总成绩导入\n" +
                "（2）若成绩方式选择二级制，可以在总成绩中填写（合格 或 不合格），也可以添加分数\n" +
                "（3）若成绩方式选择五级制，可以在总成绩中填写（不及格、及格、中等、良好 或 优秀），也可以添加分数\n" +
                "（4）平时成绩、实验成绩、期中成绩和期末成绩为原始成绩（即未折算前的成绩）\n" +
                "\n" +
                "注意：此单元格不能删除，否则会出现缺漏     平时成绩x10% 实验成绩x90% ");*/
        headCoulumn8.add("班级");

        head.add(headCoulumn1);
        head.add(headCoulumn2);
        head.add(headCoulumn3);
        head.add(headCoulumn4);
        head.add(headCoulumn5);
        head.add(headCoulumn6);
        head.add(headCoulumn7);
        head.add(headCoulumn8);

        return head;
    }


}


