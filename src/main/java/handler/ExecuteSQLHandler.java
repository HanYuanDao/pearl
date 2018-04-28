package handler;

import access.Main;
import event.Event;
import event.ExecuteSQLEvent;
import scheduler.SchedulerPriorityBlockingQueue;
import sql.DBSingleInstanceMySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Desciption:
 * Author: jasonhan.
 * Creation time: 2018/4/23 6:30 PM.
 * © Copyright 2013-2018.
 */
public class ExecuteSQLHandler extends Handler {

    private ExecuteSQLHandler() {}

    /**
     * 配合getInstance方法，实现安全的多线程环境下的单实例创建。
     * 类级的内部类，只有被调用的时候才会被加载，从而实现了延迟加载。
     */
    private static class ExecuteSQLHandlerHolder {
        private static ExecuteSQLHandler HANDLER_EXECUTE_SQL_HOLDER_INSTANCE = new ExecuteSQLHandler();
    }

    public static ExecuteSQLHandler getInstance() {
        return ExecuteSQLHandler.ExecuteSQLHandlerHolder.HANDLER_EXECUTE_SQL_HOLDER_INSTANCE;
    }

    @Override
    public int handle(Event event) {
        ExecuteSQLEvent executeSQLEvent = (ExecuteSQLEvent) event;
        String sql = executeSQLEvent.getSql();
        try {
            Connection connection = DBSingleInstanceMySQL.getConnection();

            if (null == connection) {
                putLogDebug(event, "connection is null");
                Main.eventQueue.insertEvent(event);
            } else {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                List<List<String>> data = resultSet2List(resultSet);
                executeSQLEvent.getParentEvent().addSheet(executeSQLEvent.getSheetNm(), data);
            }
        } catch (SQLException e) {
            executeSQLEvent.getParentEvent().addSheet(executeSQLEvent.getSheetNm(), null);
            putLogError(event, executeSQLEvent.getSheetNm() + "" + executeSQLEvent.getSql());
        }
        return 0;
    }

    @Override
    protected int createEvent(SchedulerPriorityBlockingQueue eventQueue, Object obj) {
        return 0;
    }

    @Override
    protected int dealEvent(SchedulerPriorityBlockingQueue eventQueue, Object obj) {
        return 0;
    }

    private static List<List<String>> resultSet2List(ResultSet resultSet) throws SQLException {
        List<List<String>> result = new ArrayList<>();
        if (null == resultSet) {
            return null;
        }

        // column head
        ResultSetMetaData rsMetaData = resultSet.getMetaData();
        int numColumn = rsMetaData.getColumnCount();
        List<String> title = new ArrayList<>();
        for (int i = 0; i < numColumn; i++) {
            title.add(rsMetaData.getColumnName(i+1));
        }
        result.add(title);
        // column data
        while (resultSet.next()) {
            List<String> rowData = new ArrayList<>();
            for (int i = 0; i < numColumn; i++) {
                rowData.add(resultSet.getString(i+1));
            }
            result.add(rowData);
        }
        return result;
    }
}
