package access;

import event.Event;
import event.InitEvent;
import handler.InitHandler;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import scheduler.EventThreadPool;
import scheduler.SchedulerPriorityBlockingQueue;

import java.util.Date;

/**
 * Desciption:
 * Author: JasonHan.
 * Creation time: 2017/04/09 12:29:00.
 * © Copyright 2013-2018, Banksteel Finance.
 */
public class Main {
    private static Logger logger = Logger.getLogger(Main.class);
//    private static Logger logger = LogManager.getLogger(Main.class);

    /**
     * 轮询事件队列的休息时间
     */
    private static final int SLEEP_TIME = 100;
    /**
     * 存放事件的队列
     */
    public static final SchedulerPriorityBlockingQueue<Event> eventQueue = new SchedulerPriorityBlockingQueue<>();
    /**
     * 处理事件的线程池
     */
    private static final EventThreadPool eventThreadPool = EventThreadPool.getThreadPool();

    private static void init() {
        /**
         * 设置主线程优先级为最高优先级
         */
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        /**
         * 创建事件队列和处理事件的线程池
         */
        /*eventQueue = new SchedulerPriorityBlockingQueue<>();
        eventThreadPool = EventThreadPool.getThreadPool();*/



        /**
         * 插入第一个（批）事件到事件队列中
         */
        //TODO 这里根据不同项目的需求写入不同的事件
        InitEvent initEvent = new InitEvent("", new Date(), InitHandler.getInstance(), eventQueue);
        eventQueue.insertEvent(initEvent);
    }

    public static void main(String[] myArgs) {
        BasicConfigurator.configure();
        //PropertyConfigurator.configure("classpath:log4j.properties");
        //PropertyConfigurator.configure(this.getClass().getClassLoader().getResource("log4j.properties"));
        //Properties props = new Properties();
//        try {
            //props.load(Main.class.getClassLoader().getResourceAsStream("log4j.properties"));
            //PropertyConfigurator.configure(props);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        init();
        Date nowTime = new Date();
        Date eventDoTime = new Date();
        Event event;

        while (true) {
            /**
             * 查看正在处理的任务数量、正在等待的事件、已经执行且结束了的任务数量、分配到这个线程池的任务数量
             */
            logger.info(String.format("activeCount:%-8d,waitCount:%-8d,completedTaskCount:%-8d,taskCount:%-8d.\n",
                    eventThreadPool.getActiveCount(),
                    eventThreadPool.getTaskCount()-eventThreadPool.getCompletedTaskCount(),
                    eventThreadPool.getCompletedTaskCount(),
                    eventThreadPool.getTaskCount()));

            if (eventQueue.size() > 0) {
                event = eventQueue.getEvent();

                nowTime.setTime(System.currentTimeMillis());
                eventDoTime.setTime(event.getDoTime().getTime());

                if ((null!=eventDoTime)&&(eventDoTime.compareTo(nowTime)<0)) {
                    eventThreadPool.execute(event);
                    eventQueue.removeEvent();
                }
            }
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
