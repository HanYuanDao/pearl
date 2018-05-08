package tool;

import java.io.UnsupportedEncodingException;

/**
 * Desciption:
 * Author: JasonHan.
 * Creation time: 2017/04/26 11:03:00.
 * © Copyright 2013-2018, Banksteel Finance.
 */
public class StringHelper {

    /** 8 位 UCS 转换格式 */
    public static final String UTF_8 = "UTF-8";

    /** 中文超大字符集 */
    public static final String GBK = "GBK";

    private StringHelper() {}

    public static String toGBK(String source) throws UnsupportedEncodingException {
        return changeCharset(source, GBK);
    }

    /**
     * 字符串编码转换的实现方法
     * @param str  待转换编码的字符串
     * @param newCharset 目标编码
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String changeCharset(String str, String newCharset)
            throws UnsupportedEncodingException {
        if (str != null) {
            //用默认字符编码解码字符串。
            byte[] bs = str.getBytes();
            //用新的字符编码生成字符串
            return new String(bs, newCharset);
        }
        return null;
    }

    /**
     * 截取一段字符中已{@code startStr}开头以{@code endStr}结尾的字符串，不包含开头结尾。
     *
     * @param source     被截取的代码
     * @param startStr   截取位置的起始字符串
     * @param endStr     截取位置的终止字符串
     *
     * @author: jasonhan.
     * @creation time: 2018/05/08 17:25:37.
     */
    public static String cutOutString(String source, String startStr, String endStr) {
        if (null != source) {
            int startIndex = 0;
            int endIndex = source.length();

            if (null != startStr) {
                startIndex = source.indexOf(startStr);
                startIndex = startIndex>=0?(startIndex+startStr.length()):0;
            }
            if (null != endStr) {
//                endIndex = source.indexOf(endStr);
                endIndex = source.indexOf(endStr, startIndex);
                endIndex = endIndex>=0?(endIndex):startIndex;
            }

            if (startIndex <= endIndex) {
                return source.subSequence(startIndex, endIndex).toString();
            }
        }
        return "";
    }

}
