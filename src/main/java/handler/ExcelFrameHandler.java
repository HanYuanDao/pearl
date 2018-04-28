package handler;

import access.Main;
import event.Event;
import event.ExcelFrameEvent;
import event.ExecuteSQLEvent;
import event.MakeExcelEvent;
import exception.EventQueueException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import scheduler.SchedulerPriorityBlockingQueue;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Desciption:
 * Author: jasonhan.
 * Creation time: 2018/4/23 3:09 PM.
 * © Copyright 2013-2018.
 */
public class ExcelFrameHandler extends Handler {

    private ExcelFrameHandler() {}

    /**
     * 配合getInstance方法，实现安全的多线程环境下的单实例创建。
     * 类级的内部类，只有被调用的时候才会被加载，从而实现了延迟加载。
     */
    private static class ExcelFrameHandlerHolder {
        private static ExcelFrameHandler EXCEL_FRAME_HOLDER_INSTANCE = new ExcelFrameHandler();
    }

    public static ExcelFrameHandler getInstance() {
        return ExcelFrameHandler.ExcelFrameHandlerHolder.EXCEL_FRAME_HOLDER_INSTANCE;
    }

    @Override
    public int handle(Event event) {
        ExcelFrameEvent excelFrameEvent = (ExcelFrameEvent) event;
        String nmExcel = excelFrameEvent.getNmExcel();
        if (null == nmExcel) {
            putLogError(event, " is null");
        }

        Workbook workbook = null;
        try {
            workbook = loadExcel(nmExcel);
        } catch (EventQueueException e) {
            putLogError(event, nmExcel + " " + e.getExcMsg());
        } catch (IOException e) {
            putLogError(event, "Excel is inexistence！！！！");
        }

        if (null != workbook) {
            int sheetNum = workbook.getNumberOfSheets();
            for (int i = 0; i < sheetNum; i++) {
                Sheet sheet = workbook.getSheetAt(i);

                String prodExcelNm = sheet.getSheetName();
                MakeExcelEvent makeExcelEvent =
                        new MakeExcelEvent(
                                "",
                                new Date(),
                                MakeExcelHandler.getInstance(),
                                Main.eventQueue
                        ).setFileName(prodExcelNm)
                         .setFilePath(System.getProperty("user.dir"))
                         .setTotalNumSheet(sheet.getLastRowNum() + 1);
                List<String> prodExcelSheetNmList = new ArrayList<>();
                List<String> prodExcelSqlList = new ArrayList<>();
                for (Row rowStep : sheet) {
                    String sheetNm = rowStep.getCell(excelFrameEvent.getCellNumSheetNm()).getStringCellValue();
                    String sql = rowStep.getCell(excelFrameEvent.getCellNumSQL()).getStringCellValue();
                    prodExcelSheetNmList.add(sheetNm==null ? "":sheetNm);
                    prodExcelSqlList.add(sql == null ? "":sql);

                    if (StringUtils.isNotEmpty(sheetNm) && StringUtils.isNotEmpty(sql)) {
                        ExecuteSQLEvent executeSQLEvent = new ExecuteSQLEvent("", new Date(), ExecuteSQLHandler.getInstance(), Main.eventQueue, makeExcelEvent)
                                .setSql(sql).setSheetNm(sheetNm);
                        Main.eventQueue.insertEvent(executeSQLEvent);
                    } else {
                        putLogError(event, "make executeSQLEvent is fail."+"prodExcelNm:"+prodExcelNm+","+"sheetNm:"+sheetNm+","+"sql:"+sql);
                    }
                }
            }
        } else {
            putLogError(event, "workbook is null.");
        }

        return 0;
    }

    @Override
    protected int createEvent(SchedulerPriorityBlockingQueue eventQueue, Object obj) {
        System.out.println(Main.eventQueue);
        return 0;
    }

    @Override
    protected int dealEvent(SchedulerPriorityBlockingQueue eventQueue, Object obj) {
        return 0;
    }

    public Workbook loadExcel(String nmFile) throws IOException, EventQueueException {
        String[] filePathArr = nmFile.split("[.]");
        if (filePathArr.length < 2) {
            throw new EventQueueException("", "excel's path is error");
        }
        String tailStr = filePathArr[1];
        if (tailStr.equals("xls")) {
            return loadHSSFExcel(nmFile);
        } else {
            return loadXSSFExcel(nmFile);
        }
    }

    private HSSFWorkbook loadHSSFExcel(String nmFile) throws IOException {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        ArrayList<String> result = new ArrayList<String>();

        if (null != nmFile) {
            InputStream inputStream = getInputStream(nmFile);
            hssfWorkbook = new HSSFWorkbook(inputStream);
            if (null!=inputStream) {
                inputStream.close();
            }
        }

        return hssfWorkbook;
    }

    private XSSFWorkbook loadXSSFExcel(String nmFile) throws IOException {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        ArrayList<String> result = new ArrayList<String>();

        if (null != nmFile) {
            InputStream inputStream = getInputStream(nmFile);
            xssfWorkbook = new XSSFWorkbook(inputStream);
            if (null!=inputStream) {
                inputStream.close();
            }
        }

        return xssfWorkbook;
    }

    private InputStream getInputStream(String nmFile) throws FileNotFoundException {
        //return this.getClass().getClassLoader().getResourceAsStream(nmFile);
        return new FileInputStream(nmFile);
    }

    public static ArrayList<HashMap> loadValueFromExcel(Workbook workbook, int sheetId) {
        ArrayList<HashMap> result = new ArrayList<>();

        Sheet sheet = workbook.getSheetAt(sheetId);
        for (Row rowStep : sheet) {
            HashMap<String, Object> step = new HashMap();

            int column = 0;
            for (Cell cellStep : rowStep) {
                switch (cellStep.getCellType()) {
                    case HSSFCell.CELL_TYPE_BLANK:
                        step.put(String.valueOf(column++), "");
                        break;
                    case HSSFCell.CELL_TYPE_BOOLEAN:
                        step.put(String.valueOf(column++), cellStep.getBooleanCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_ERROR:
                        step.put(String.valueOf(column++), cellStep.getErrorCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_FORMULA:
                        step.put(String.valueOf(column++), cellStep.getCellFormula());
                        break;
                    case HSSFCell.CELL_TYPE_NUMERIC:
                        step.put(String.valueOf(column++), cellStep.getNumericCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_STRING:
                        step.put(String.valueOf(column++), cellStep.getStringCellValue());
                        break;
                    default:
                        break;
                }
            }
            result.add(step);
        }

        return result;
    }
}
