package event;

import access.Main;
import handler.Handler;
import scheduler.SchedulerPriorityBlockingQueue;
import tool.MathHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Desciption:
 * Author: JasonHan.
 * Creation time: 2017/04/25 14:32:00.
 * © Copyright 2013-2017, Banksteel Finance.
 */
public class MakeExcelEvent extends Event {
    private String fileName;
    private String filePath;
    private int totalNumSheet;
    private List<MakeExcelSheet> sheetList = new CopyOnWriteArrayList<>();

    public MakeExcelEvent(String name, Date doTime, Handler handler, SchedulerPriorityBlockingQueue eventQueue) {
        super(name, doTime, handler, eventQueue);
    }

    public MakeExcelEvent(String name, int priority, Date doTime, Handler handler, SchedulerPriorityBlockingQueue eventQueue) {
        super(name, priority, doTime, handler, eventQueue);
    }

    public MakeExcelEvent setTotalNumSheet(int totalNumSheet) {
        this.totalNumSheet = totalNumSheet;
        return this;
    }
    public MakeExcelEvent setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }
    public MakeExcelEvent setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }
    /*public MakeExcelEvent setSheetList(List<MakeExcelSheet> sheetList) {
        this.sheetList = sheetList;
        return this;
    }*/
    public MakeExcelEvent addSheet(String sheetNm, List<List<String>> data) {
        MakeExcelSheet makeExcelSheet = new MakeExcelSheet(sheetNm, data);
        sheetList.add(makeExcelSheet);
        double percent = MathHelper.divide(sheetList.size(), this.totalNumSheet, 4)*100;
        if (sheetList.size() < this.totalNumSheet) {
            putLogInfo(this, "sql callback is " + percent + "%");
        }
        if (sheetList.size() == this.totalNumSheet) {
            putLogInfo(this, "sql callbank is success.");
            Main.eventQueue.insertEvent(this);
        }
        if (sheetList.size() > this.totalNumSheet) {
            putLogInfo(this, "sql callbank is error.");
        }
        /*if (sheetList.size() < (this.totalNumSheet+1)) {
            putLogInfo(this, "sql callback is " + sheetList.size()/this.totalNumSheet + "%");
        }
        if (sheetList.size() == (this.totalNumSheet+1)) {
            putLogInfo(this, "sql callbank is success.");
            Main.eventQueue.insertEvent(this);
        }
        if (sheetList.size() > (this.totalNumSheet+1)) {
            putLogInfo(this, "sql callbank is error.");
        }*/
        return this;
    }

    public String getFileName() {
        return fileName;
    }
    public String getFilePath() {
        return filePath;
    }
    public List<MakeExcelSheet> getSheetList() {
        return sheetList;
    }

    public  class MakeExcelSheet{
        private String sheetNm;
        private List<List<String>> data;

        public MakeExcelSheet(String sheetNm, List<List<String>> data) {
            this.sheetNm = sheetNm;
            this.data = data;
        }

        public String getSheetNm() {
            return sheetNm;
        }

        public List<List<String>> getData() {
            return data;
        }
    }
}
