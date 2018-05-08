package handler;

import event.Event;
import event.MakeExcelEvent;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import scheduler.SchedulerPriorityBlockingQueue;
import tool.ExcelHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
/**
 * Desciption:
 * Author: JasonHan.
 * Creation time: 2017/03/28 16:19:00.
 * © Copyright 2013-2018, Banksteel Finance.
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
            ExcelHelper.createSheet(workbook, step.getSheetNm(), step.getData());
        }

        try {
            ExcelHelper.export(workbook, makeExcelEvent.getFilePath(), makeExcelEvent.getFileName()+".xls");
        } catch (IOException e) {
            putLogError(event, "export is error");
        }

        return 0;
    }
}
