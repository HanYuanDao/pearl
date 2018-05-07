package handler;

import access.Main;
import event.Event;
import event.ExcelFrameEvent;
import org.apache.commons.lang3.StringUtils;
import scheduler.SchedulerPriorityBlockingQueue;
import sql.DBSingleInstanceMySQL;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Desciption:
 * Author: jasonhan.
 * Creation time: 2018/4/23 2:13 PM.
 * © Copyright 2013-2018.
 */
public class InitHandler extends Handler {

    private static final String NM_PROPERTIES = "config.properties";
    private static final String KEY_DB_HOST_IP = "mysql.db.host.ip";
    private static final String KEY_DB_HOST_PROT = "mysql.db.host.port";
    private static final String KEY_DB_NM = "mysql.db.nm";
    private static final String KEY_DB_ACCOUNT = "mysql.db.account";
    private static final String KEY_DB_PASSWORD = "mysql.db.password";
    private static final String NM_EXCEL = "fileName";
    private static final String COL_NUM_TASK_NM = "columnNumTaskNm";
    private static final String COL_NUM_SQL = "columnNumSql";

    /**
     * 配合getInstance方法，实现安全的多线程环境下的单实例创建。
     * 类级的内部类，只有被调用的时候才会被加载，从而实现了延迟加载。
     */
    private static class InitHandlerHolder {
        private static InitHandler HANDLER_INIT_HOLDER_INSTANCE = new InitHandler();
    }

    public static InitHandler getInstance() {
        return InitHandler.InitHandlerHolder.HANDLER_INIT_HOLDER_INSTANCE;
    }

    @Override
    public int handle(Event event) {
        Map<String, String> configKeyValuePair = null;


        try {
            configKeyValuePair = getProperties(NM_PROPERTIES);
        } catch (IOException e) {
            putLogError(event, "get config.properties is error");
        }

        boolean isOkInit = true;
        if (configKeyValuePair != null) {
            if (!checkValue(configKeyValuePair.get(KEY_DB_HOST_IP))) {
                putLogInfo(event, KEY_DB_HOST_IP + " is null in properties!");
                isOkInit = false;
            }
            if (!checkValue(configKeyValuePair.get(KEY_DB_HOST_PROT))) {
                putLogInfo(event, KEY_DB_HOST_PROT + " is null in properties!");
                isOkInit = false;
            }
            if (!checkValue(configKeyValuePair.get(KEY_DB_NM))) {
                putLogInfo(event, KEY_DB_NM + " is null in properties!");
                isOkInit = false;
            }
            if (!checkValue(configKeyValuePair.get(KEY_DB_ACCOUNT))) {
                putLogInfo(event, KEY_DB_ACCOUNT + " is null in properties!");
                isOkInit = false;
            }
            if (!checkValue(configKeyValuePair.get(KEY_DB_PASSWORD))) {
                putLogInfo(event, KEY_DB_PASSWORD + " is null in properties!");
                isOkInit = false;
            }
            if (!checkValue(configKeyValuePair.get(NM_EXCEL))) {
                putLogInfo(event, NM_EXCEL + " is null in properties!");
                isOkInit = false;
            }
            if (!checkValue(configKeyValuePair.get(COL_NUM_TASK_NM))) {
                putLogInfo(event, COL_NUM_TASK_NM + " is null in properties!");
                isOkInit = false;
            }
            if (!checkValue(configKeyValuePair.get(COL_NUM_SQL))) {
                putLogInfo(event, COL_NUM_SQL + " is null in properties!");
                isOkInit = false;
            }

            if (isOkInit) {
                putLogInfo(event, "verify proerties is true please.");
                DBSingleInstanceMySQL dbSingleInstanceMySQL =
                        new DBSingleInstanceMySQL(
                                configKeyValuePair.get(KEY_DB_HOST_IP),
                                configKeyValuePair.get(KEY_DB_HOST_PROT),
                                configKeyValuePair.get(KEY_DB_NM),
                                configKeyValuePair.get(KEY_DB_ACCOUNT),
                                configKeyValuePair.get(KEY_DB_PASSWORD)
                                );
                ExcelFrameEvent excelFrameEvent =
                        new ExcelFrameEvent(
                                "",
                                new Date(),
                                ExcelFrameHandler.getInstance(),
                                Main.eventQueue
                        ).setNmExcel(configKeyValuePair.get(NM_EXCEL))
                         .setCellNumSheetNm(Integer.valueOf(configKeyValuePair.get(COL_NUM_TASK_NM)))
                         .setCellNumSQL(Integer.valueOf(configKeyValuePair.get(COL_NUM_SQL)));
                Main.eventQueue.insertEvent(excelFrameEvent);
            } else {
                putLogInfo(event, "verify proerties is false please.");
            }
        }

        return 0;
    }

    private boolean checkValue(Object source) {
        if (null == source) {
            return false;
        }
        if (StringUtils.isEmpty(source.toString())) {
            return false;
        }
        return true;
    }

    public Map<String, String> getProperties(String nmProperties) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(nmProperties);

        Map<String, String> propertiesKeyValuePair = new HashMap<>();

        Properties properties = new Properties();
        properties.load(inputStream);
        inputStream.close();

        //遍历配置文件方法一
        Enumeration en = properties.propertyNames();
        while (en.hasMoreElements()) {
            String key = en.nextElement().toString();
            String value = properties.getProperty(key);
            propertiesKeyValuePair.put(key, value);
        }
        //遍历配置文件方法二
//        Set keys = props.keySet();
//        for (Interator it = keys.iterator(); it.hasNext();){
//            String k = it.next();
//        }

        return propertiesKeyValuePair;
    }
}
