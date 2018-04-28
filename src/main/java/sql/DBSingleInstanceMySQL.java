package sql;

import java.io.InputStream;
import java.sql.*;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Desciption:
 * Author: JasonHan.
 * Creation time: 2017/04/11 16:26:00.
 * © Copyright 2013-2017, Banksteel Finance.
 */
public class DBSingleInstanceMySQL {
    private static Connection connection = null;
    /*private static Statement statement = null;*/

    /*private static String HOST_IP;
    private static String HOST_PORT;
    private static String USER_ACCOUNT;
    private static String USER_PASSWORD;
    private static String DB_NM;*/
    private static final String CANT_FIND = "can't find";

    /*static {
        createConnection();
    }*/

    public DBSingleInstanceMySQL(String hostIp, String hostPort, String dbNm, String userAcct, String userPwd) {
        createConnection(hostIp, hostPort, dbNm, userAcct, userPwd);
    }

    private void createConnection(String hostIp, String hostPort, String dbNm, String userAcct, String userPwd) {
        String driver = "com.mysql.jdbc.Driver";
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //String url = "jdbc:mysql://"+HOST_IP+":"+HOST_PORT+"/"+DB_NM+"?useUnicode=true&characterEncoding=utf-8";
        String url = "jdbc:mysql://"+hostIp+":"+hostPort+"/"+dbNm+"?useUnicode=true&characterEncoding=utf-8";
        try {
            //connection = DriverManager.getConnection(url, USER_ACCOUNT, USER_PASSWORD);
            connection = DriverManager.getConnection(url, userAcct, userPwd);
            /*statement = connection.createStatement();*/
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            if (connection!=null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
