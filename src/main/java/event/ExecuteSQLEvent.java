package event;

import handler.Handler;
import scheduler.SchedulerPriorityBlockingQueue;

import java.sql.Connection;
import java.util.Date;

/**
 * Desciption:
 * Author: jasonhan.
 * Creation time: 2018/4/23 6:33 PM.
 * © Copyright 2013-2018.
 */
public class ExecuteSQLEvent extends Event {
    private MakeExcelEvent parentEvent;
    private String sheetNm;
    private String sql;

    public ExecuteSQLEvent(String name, Date doTime, Handler handler, SchedulerPriorityBlockingQueue eventQueue, MakeExcelEvent parentEvent) {
        super(name, doTime, handler, eventQueue);
        this.parentEvent = parentEvent;
    }
    public ExecuteSQLEvent(String name, int priority, Date doTime, Handler handler, SchedulerPriorityBlockingQueue eventQueue, MakeExcelEvent parentEvent) {
        super(name, priority, doTime, handler, eventQueue);
        this.parentEvent = parentEvent;
    }

    public ExecuteSQLEvent setSheetNm(String sheetNm) {
        this.sheetNm = sheetNm;
        return this;
    }
    public ExecuteSQLEvent setSql(String sql) {
        this.sql = sql;
        return this;
    }

    public String getSheetNm() {
        return sheetNm;
    }
    public String getSql() {
        return sql;
    }
    public MakeExcelEvent getParentEvent() {
        return parentEvent;
    }
}
