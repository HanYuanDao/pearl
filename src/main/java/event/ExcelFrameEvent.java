package event;

import handler.Handler;
import scheduler.SchedulerPriorityBlockingQueue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Desciption:
 * Author: jasonhan.
 * Creation time: 2018/4/23 3:15 PM.
 * © Copyright 2013-2018.
 */
public class ExcelFrameEvent extends Event {
    private String nmExcel;
    private int cellNumSheetNm;
    private int cellNumSQL;

    public ExcelFrameEvent(String name, Date doTime, Handler handler, SchedulerPriorityBlockingQueue eventQueue) {
        super(name, doTime, handler, eventQueue);
    }
    public ExcelFrameEvent(String name, int priority, Date doTime, Handler handler, SchedulerPriorityBlockingQueue eventQueue) {
        super(name, priority, doTime, handler, eventQueue);
    }

    public ExcelFrameEvent setNmExcel(String nmExcel) {
        this.nmExcel = nmExcel;
        return this;
    }
    public ExcelFrameEvent setCellNumSheetNm(int cellNumSheetNm) {
        this.cellNumSheetNm = cellNumSheetNm - 1;
        return this;
    }
    public ExcelFrameEvent setCellNumSQL(int cellNumSQL) {
        this.cellNumSQL = cellNumSQL - 1;
        return this;
    }

    public String getNmExcel() {
        return nmExcel;
    }
    public int getCellNumSheetNm() {
        return cellNumSheetNm;
    }
    public int getCellNumSQL() {
        return cellNumSQL;
    }
}
