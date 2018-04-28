package event;

import handler.Handler;
import scheduler.SchedulerPriorityBlockingQueue;

import java.util.Date;

/**
 * Desciption:
 * Author: jasonhan.
 * Creation time: 2018/4/25 2:51 PM.
 * © Copyright 2013-2018.
 */
public class InitEvent extends Event {
    public static final String PROP_KEY_DB_MYSQL_CONNECT = "dbMySQLConnet";

    public InitEvent(String name, Date doTime, Handler handler, SchedulerPriorityBlockingQueue eventQueue) {
        super(name, doTime, handler, eventQueue);
    }

    public InitEvent(String name, int priority, Date doTime, Handler handler, SchedulerPriorityBlockingQueue eventQueue) {
        super(name, priority, doTime, handler, eventQueue);
    }
}
