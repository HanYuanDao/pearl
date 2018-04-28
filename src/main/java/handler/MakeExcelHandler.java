package handler;

import event.Event;
import event.MakeExcelEvent;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import scheduler.SchedulerPriorityBlockingQueue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
/**
 * Desciption:
 * Author: JasonHan.
 * Creation time: 2017/03/28 16:19:00.
 * © Copyright 2013-2017, Banksteel Finance.
 */
public class MakeExcelHandler extends Handler{

    private MakeExcelHandler() {}

    /**
     * 配合getInstance方法，实现安全的多线程环境下的单实例创建。
     * 类级的内部类，只有被调用的时候才会被加载，从而实现了延迟加载。
     */
    private static class MakeExcelHandlerHolder {
        private static MakeExcelHandler MAKE_EXCEL_HANDLER_HOLDER_INSTANCE = new MakeExcelHandler();
    }

    public static MakeExcelHandler getInstance() {
        return MakeExcelHandlerHolder.MAKE_EXCEL_HANDLER_HOLDER_INSTANCE;
    }

    @Override
    public int handle(Event event) {
        MakeExcelEvent makeExcelEvent = (MakeExcelEvent) event;

        Workbook workbook = new HSSFWorkbook();
        for (MakeExcelEvent.MakeExcelSheet step : makeExcelEvent.getSheetList()) {
            cteateSheet(workbook, step.getSheetNm(), step.getData());
        }

        try {
            export(workbook, makeExcelEvent.getFilePath(), makeExcelEvent.getFileName()+".xls");
        } catch (IOException e) {
            putLogError(event, "export is error");
        }

        return 0;
    }

    @Override
    protected int dealEvent(SchedulerPriorityBlockingQueue eventQueue, Object obj) {
        return 0;
    }

    @Override
    protected int createEvent(SchedulerPriorityBlockingQueue eventQueue, Object obj) {
        return 0;
    }

    public static String cutOutString(String source, String startStr, String endStr) {
        if (null != source) {
            int startIndex = 0;
            int endIndex = source.length();

            if (null != startStr) {
                startIndex = source.indexOf(startStr);
                startIndex = startIndex>=0?(startIndex+1):0;
            }
            if (null != endStr) {
//                endIndex = source.indexOf(endStr);
                endIndex = source.indexOf(endStr, startIndex);
                endIndex = endIndex>=0?(endIndex):startIndex;
            }

            if (startIndex <= endIndex) {
                return source.subSequence(startIndex, endIndex).toString();
            }
        }
        return "";
    }

    public static ArrayList<Workbook> createWorkBookList(List<Map<String, Object>> list, String []keys, String columnNames[]) {
        ArrayList<Workbook> workbookList = new ArrayList<>();
        int listSize = list.size();
        int thresholdValue = 50000;
        int groupSize = list.size()/thresholdValue;
        for (int i = 0; i <= groupSize; i++) {
            List<Map<String, Object>> listNew = new ArrayList<Map<String, Object>>();
            for (int j = 0; j < thresholdValue; j++) {
                if ((j+i*thresholdValue) < listSize) {
                    Map<String, Object> step = list.get(j+(i*thresholdValue));
//                    String line = String.valueOf(j+1);
//                    step.put("formula", "IF(S"+line+"=\"\",\"\",CONCATENATE($AA$1,\" VALUES('\",\"德邦物流报价.xlsx\",\"',\",\"now()\",\",'\",\"DEPPON\",\"','\",\"德邦物流\",\"','\",\"德邦物流股份有限公司\",\"','\",\"DEPPON\",\"','\",\"DEPPON\",\"','\",\"001\",\"','\",\"中国\",\"','\",B"+line+",\"','\",A"+line+",\"','\",D"+line+",\"','\",C"+line+",\"','\",F"+line+",\"','\",E"+line+",\"','\",\"001\",\"','\",\"中国\",\"','\",H"+line+",\"','\",G"+line+",\"','\",J"+line+",\"','\",I"+line+",\"','\",L"+line+",\"','\",K"+line+",\"','\",Z"+line+",\"','\",O"+line+",\"','\",P"+line+",\"','\",\"1145.200.325 \",\"','\",\"公斤\",\"','\",Q"+line+",\"','\",R"+line+",\"','\",\"1145.210.300\",\"','\",\"立方米\",\"','\",S"+line+",\"','\",T"+line+",\"','\",U"+line+",\"','\",V"+line+",\"','\",W"+line+",\"','\",M"+line+",\"','\",N"+line+",\"','\",X"+line+",\"','\",Y"+line+",\"','\",\"\",\"',\",\"now()\",\",'\",\"hanzhe\",\"','\",\"1100.200\",\"','\",\"1.0.0\",\"');\"))");
                    listNew.add(step);
                } else {
                    break;
                }
            }
            Workbook wb = createWorkBook(listNew, keys, columnNames);
            workbookList.add(wb);
        }
        return workbookList;
    }

    public static Workbook createWorkBook(List<Map<String, Object>> list, String []keys, String columnNames[]) {
        // 创建excel工作簿
        Workbook wb = new HSSFWorkbook();
        // 创建第一个sheet（页），并命名
        Sheet sheet = wb.createSheet("物流报价");
        // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
        for(int i=0;i<keys.length;i++){
            sheet.setColumnWidth( i, (357 * 15));
        }

        // 创建第一行
        Row row = sheet.createRow(0);

        // 创建两种单元格格式
        CellStyle cs = wb.createCellStyle();
        CellStyle cs2 = wb.createCellStyle();

        // 创建两种字体
        Font f = wb.createFont();
        Font f2 = wb.createFont();

        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short) 10);
        f.setColor(IndexedColors.BLACK.getIndex());
        f.setBoldweight(Font.BOLDWEIGHT_BOLD);

        // 创建第二种字体样式（用于值）
        f2.setFontHeightInPoints((short) 10);
        f2.setColor(IndexedColors.BLACK.getIndex());

//        Font f3=wb.createFont();
//        f3.setFontHeightInPoints((short) 10);
//        f3.setColor(IndexedColors.RED.getIndex());

        // 设置第一种单元格的样式（用于列名）
        cs.setFont(f);
        cs.setBorderLeft(CellStyle.BORDER_THIN);
        cs.setBorderRight(CellStyle.BORDER_THIN);
        cs.setBorderTop(CellStyle.BORDER_THIN);
        cs.setBorderBottom(CellStyle.BORDER_THIN);
        cs.setAlignment(CellStyle.ALIGN_CENTER);

        // 设置第二种单元格的样式（用于值）
        cs2.setFont(f2);
        cs2.setBorderLeft(CellStyle.BORDER_THIN);
        cs2.setBorderRight(CellStyle.BORDER_THIN);
        cs2.setBorderTop(CellStyle.BORDER_THIN);
        cs2.setBorderBottom(CellStyle.BORDER_THIN);
        cs2.setAlignment(CellStyle.ALIGN_CENTER);

        //设置列名
        for(int i=0;i<columnNames.length;i++){
            Cell cell = row.createCell(i);
            cell.setCellValue(columnNames[i]);

            cell.setCellStyle(cs);
        }

        //设置每行每列的值
        for (int i = 1; i < list.size(); i++) {
            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
            Row row1 = sheet.createRow( i);sheet.setForceFormulaRecalculation(true);
            // 在row行上创建一个方格
            for(int j=0;j<keys.length;j++){
                Cell cell = row1.createCell(j);
                cell.setCellStyle(cs2);
                /*if (j != (keys.length-1)) {
                    cell.setCellValue(list.get(i).get(keys[j]) == null?" ": list.get(i).get(keys[j]).toString());
                } else {
                    cell.setCellFormula(list.get(i).get(keys[j]) == null?" ": list.get(i).get(keys[j]).toString());
                }*/

                cell.setCellValue(list.get(i).get(keys[j]) == null?" ": list.get(i).get(keys[j]).toString());
            }
        }
        return wb;
    }

    public static Workbook createWorkBook() {
        return new HSSFWorkbook();
    }

    private Sheet cteateSheet(Workbook workbook, String sheetNm, List<List<String>> data) {
        Sheet sheetResult = workbook.createSheet(sheetNm);
        // If data is null or empty, Don't need to continue
        if (null == data || data.size() == 0 || data.get(0).size() == 0) {
            return sheetResult;
        }

        // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
        /*for(int i=0;i<keys.length;i++){
            sheet.setColumnWidth( i, (357 * 15));
        }*/

        // 创建第一行
        Row row = sheetResult.createRow(0);

        // 创建两种单元格格式
        CellStyle cs = workbook.createCellStyle();
        CellStyle cs2 = workbook.createCellStyle();

        // 创建两种字体
        Font f = workbook.createFont();
        Font f2 = workbook.createFont();

        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short) 10);
        f.setColor(IndexedColors.BLACK.getIndex());
        f.setBoldweight(Font.BOLDWEIGHT_BOLD);

        // 创建第二种字体样式（用于值）
        f2.setFontHeightInPoints((short) 10);
        f2.setColor(IndexedColors.BLACK.getIndex());

        /*Font f3=wb.createFont();
        f3.setFontHeightInPoints((short) 10);
        f3.setColor(IndexedColors.RED.getIndex());*/

        // 设置第一种单元格的样式（用于列名）
        cs.setFont(f);
        cs.setBorderLeft(CellStyle.BORDER_THIN);
        cs.setBorderRight(CellStyle.BORDER_THIN);
        cs.setBorderTop(CellStyle.BORDER_THIN);
        cs.setBorderBottom(CellStyle.BORDER_THIN);
        cs.setAlignment(CellStyle.ALIGN_CENTER);

        // 设置第二种单元格的样式（用于值）
        cs2.setFont(f2);
        cs2.setBorderLeft(CellStyle.BORDER_THIN);
        cs2.setBorderRight(CellStyle.BORDER_THIN);
        cs2.setBorderTop(CellStyle.BORDER_THIN);
        cs2.setBorderBottom(CellStyle.BORDER_THIN);
        cs2.setAlignment(CellStyle.ALIGN_CENTER);

        //设置列头
        int titleLength = data.get(0).size();
        for(int i = 0; i < titleLength; i++){
            Cell cell = row.createCell(i);
            cell.setCellValue(data.get(0).get(i));
            cell.setCellStyle(cs);
        }

        //设置每行每列的值
        int dataLineNum = data.size();
        for (int i = 1; i < dataLineNum; i++) {
            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
            Row rowStep = sheetResult.createRow(i);
            sheetResult.setForceFormulaRecalculation(true);
            // 在row行上创建一个方格
            int dataColNum = data.get(i).size();
            for(int j = 0; j < dataColNum; j++){
                Cell cell = rowStep.createCell(j);
                cell.setCellStyle(cs2);
                cell.setCellValue(data.get(i).get(j) == null ? "" : data.get(i).get(j));
            }
        }
        return sheetResult;
    }

    private void export(Workbook workbook, String filePath, String fileNm) throws IOException {
        File targetFolder = new File(filePath);
        if (!targetFolder.exists()) {
            targetFolder.mkdirs();
        }
        FileOutputStream fout =
                new FileOutputStream(filePath + "/" + fileNm);
        workbook.write(fout);
        fout.close();
    }
}
