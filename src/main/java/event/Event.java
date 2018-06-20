package event;

import handler.Handler;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import scheduler.SchedulerPriorityBlockingQueue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Desciption: 处理流程中的最小颗粒,用于装载、传递数据。应该是尽可能简单的某一类工作。
 *             执行完毕之后应该在事件列表中插入后续的事件（eg:返回该事件处理结
 *             果或处理该事件所得结果的事件）。
 * Author: JasonHan.
 * Creation time: 2017/03/28 11:12:00.
 * © Copyright 2013-2018, Banksteel Finance.
 */
public abstract class Event implements Comparable<Event>, Runnable {
    private static Logger logger = Logger.getLogger(Event.class);
//    private static Logger logger = LogManager.getLogger(Handler.class);

    private static final String LOGGER_HEAD = "EVENT_NM:";
    private static final String LOGGER_BODY = "EVENT_CONTENT:";

    /**
     * The maximum priority that a event can have.
     */
    public static final int MAX_PRIORITY = 10;
    /**
     * The default priority that is assigned to a event.
     */
    public static final int NORM_PRIORITY = 5;
    /**
     * The minimum priority that a event can have.
     */
    public static final int MIN_PRIORITY = 1;

    /**
     * The code of the event
     */
    private String name;
    /**
     * The priority of the event
     * TODO 此优先级表示在线程池中执行时的优先级，是否需要还应该被讨论
     */
    private int priority;
    /**
     * The execution time of the event
     */
    private Date doTime;
    /**
     * The processing method of the event
     */
    private Handler handler;
    /**
     * the event queue
     */
    private SchedulerPriorityBlockingQueue eventQueue;

    /**
     * Allocates a new {@code Event} object.
     *
     * @param  doTime
     *         {@link #doTime} is used to specify the execution time.
     *         If the event queue have more than one event in the main program a polling(sleep) time
     *         or old event still in the event queue,
     *         the event will not be executed on time.
     * @param  handler
     *         {@link #handler} is used to specify the processing method.
     */
    public Event(String name, Date doTime, Handler handler, SchedulerPriorityBlockingQueue eventQueue){
        this.name = name;
        this.priority = NORM_PRIORITY;
        this.doTime = doTime;
        this.handler = handler;
        this.eventQueue = eventQueue;
    }
    public Event(String name, int priority, Date doTime, Handler handler, SchedulerPriorityBlockingQueue eventQueue){
        this.name = name;
        this.doTime = doTime;
        this.handler = handler;
        this.eventQueue = eventQueue;
        this.setPriority(priority);
    }

    public String getName() {
        return name;
    }

    public void setPriority(int priority) {
        if (priority > MAX_PRIORITY) {
            priority = MAX_PRIORITY;
        } else if (priority < MIN_PRIORITY) {
            priority = MIN_PRIORITY;
        }
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public Date getDoTime() {
        return doTime;
    }

    public Handler getHandler() {
        return handler;
    }

    public SchedulerPriorityBlockingQueue getEventQueue() {
        return eventQueue;
    }

    /**
     * 事件处理应该将事件包含的待处理数据传递给对应的处理方法类的对象。
     */
    public void run() {
        try {
            getHandler().handle(this);
        } catch (Exception e) {
            putLogError(this, "handle显示中未处理异常：" + e.getMessage());
        }
    }

    protected void putLogInfo(Event event, String msg) {
        logger.info(LOGGER_HEAD +
                event.getName() +
                System.getProperty("line.separator") +
                LOGGER_BODY +
                event.toString() +
                System.getProperty("line.separator") +
                "InfoMsg:" +
                msg);
    }
    protected void putLogError(Event event, String msg) {
        logger.error(LOGGER_HEAD +
                event.getName() +
                System.getProperty("line.separator") +
                LOGGER_BODY +
                event.toString() +
                System.getProperty("line.separator") +
                "ErrorMsg:" +
                msg);
    }
    protected void putLogDebug(Event event, String msg) {
        logger.debug(LOGGER_HEAD +
                event.getName() +
                System.getProperty("line.separator") +
                LOGGER_BODY +
                event.toString() +
                System.getProperty("line.separator") +
                "DebugMsg:" +
                msg);
    }

    @Override
    public String toString() {
        String eventNm = this.getClass().getName();
        Field[] f = this.getClass().getDeclaredFields();
        List<String> eventPropList = new ArrayList<>();
        for(Field fieldStep : f){
            fieldStep.setAccessible(true);
            try {
                eventPropList.add(fieldStep.getName() + ":" + fieldStep.get(this));
            } catch (IllegalAccessException e) {
                eventPropList.add(fieldStep.getName() + ":" + "undefind");
            }
        }
        return eventNm + "{" + StringUtils.join(eventPropList.toArray(), ",") + "}";
    }

    /**
     * 为创造一个有序的事件队列而编写的排序原则，实现Comparable接口重
     * 写compareTo方法。目前的逻辑是：事件执行事件越靠前则优先级越高。
     *
     * @param event
     * @return
     */
    @Override
    public int compareTo(Event event) {
        //TODO 除了事件的执行事件还应该将事件的优先级考虑进来
        return (this.getDoTime().getTime() > event.getDoTime().getTime()) ?
                1 : (this.getDoTime().getTime()==event.getDoTime().getTime()) ?
                0 : -1;
    }
}
