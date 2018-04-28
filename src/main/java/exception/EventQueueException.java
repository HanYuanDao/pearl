package exception;

/**
 * Desciption:
 * Author: jasonhan.
 * Creation time: 2018/4/23 3:40 PM.
 * © Copyright 2013-2018.
 */
public class EventQueueException extends Exception{
    private String excCode;
    private String excMsg;

    public EventQueueException(String code, String msg) {
        this.excCode = code;
        this.excMsg = msg;
    }

    public String getExcMsg() {
        return excMsg;
    }
}
