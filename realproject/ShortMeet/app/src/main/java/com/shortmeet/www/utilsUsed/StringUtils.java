package com.shortmeet.www.utilsUsed;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/*
 *  Fly 注：字符串处理     时间格式化
 */
public class StringUtils {
    private static final long ONE_MINUTE = 60;
    private static final long ONE_HOUR = 3600;
    private static final long ONE_DAY = 86400;
    private static final long ONE_MONTH = 2592000;
    private static final long ONE_YEAR = 31104000;


    /**
     * 简单判断是否json
     */
    public static boolean isJson(String str) {

        return str.trim().startsWith("{") && str.trim().endsWith("}");

    }

    /**
     * 获取最大为value的 0.5为单位的数组
     * <p>
     * 最小为  1
     */
    public static String[] getTime(String value) {
        if (value.equals("0")) {
            return new String[0];
        }
        double v = Double.parseDouble(value);
        double time = 1.0;
        String times = "1.0";
        while (time < v) {
            time = time + 0.5;
            times = times + "," + String.valueOf(time);
        }
        return times.split(",");
    }



    /**
     * 時間戳格式化成 yyyy-MM-dd
     */
    public static String strToDate(String milliseconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date(Long.valueOf(milliseconds)));

    }
    public static String strToDateA(String milliseconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        return formatter.format(new Date(Long.valueOf(milliseconds)));
    }

    /**
     * 時間戳格式化成 MM-dd
     */
    public static String strTodate(String milliseconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd");
        return formatter.format(new Date(Long.valueOf(milliseconds + "000")));
    }

    public static String strTodateA(String milliseconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM:dd");
        return formatter.format(new Date(Long.valueOf(milliseconds + "000")));
    }

    /**
     * 時間戳格式化成 yyyy-MM-dd HH:mm
     */
    public static String strToTime(String milliseconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(new Date(Long.valueOf(milliseconds) * 1000));

    }

    /**
     * 時間戳格式化成 yyyy-MM-dd HH:mm
     */
    public static String strToTimeA(String milliseconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(new Date(Long.valueOf(milliseconds)));
    }

    /**
     * yyyy年MM月dd日 HH:mm转换时间戳
     *
     * @param time
     * @return
     */
    public static long getStringToDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime() / 1000;
    }

    /**
     * yyyy-MM-dd HH:mm转换时间戳
     *
     * @param time
     * @return
     */
    public static long getStrToDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime() / 1000;
    }

    /**
     * yyyy-MM-dd HH:mm转换时间戳
     */
    public static long getStrTodate(String time) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }





    /**
     * 距离今天多久
     *
     * @param date
     * @return
     */
    public static String fromToday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        long time = date.getTime() / 1000;
        long now = new Date().getTime() / 1000;
        long ago = now - time;
        if (ago <= ONE_HOUR)
            return ago / ONE_MINUTE + "分钟前";
        else if (ago <= ONE_DAY)
            return ago / ONE_HOUR + "小时" + (ago % ONE_HOUR / ONE_MINUTE)
                    + "分钟前";
        else if (ago <= ONE_DAY * 2)
            return "昨天 " + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                    + calendar.get(Calendar.MINUTE);
        else if (ago <= ONE_DAY * 3)
            return "前天 " + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                    + calendar.get(Calendar.MINUTE);
        else if (ago <= ONE_MONTH) {
            long day = ago / ONE_DAY;
            return day + "天前";
        } else if (ago <= ONE_YEAR) {
            long month = ago / ONE_MONTH;
            long day = ago % ONE_MONTH / ONE_DAY;
            return month + "个月前";
//                    + calendar.get(Calendar.HOUR_OF_DAY) + "点"
//                    + calendar.get(Calendar.MINUTE) + "分";
        } else {
            long year = ago / ONE_YEAR;
            int month = calendar.get(Calendar.MONTH) + 1;// JANUARY which is 0 so month+1
            return year + "年前";
        }

    }

    /**
     * 距离截止日期还有多长时间
     *
     * @param date
     * @return
     */
    public static String fromDeadline(Date date) {
        long deadline = date.getTime() / 1000;
        long now = (new Date().getTime()) / 1000;
        long remain = deadline - now;
        if (remain <= ONE_HOUR)
            return "只剩下" + remain / ONE_MINUTE + "分钟";
        else if (remain <= ONE_DAY)
            return "只剩下" + remain / ONE_HOUR + "小时"
                    + (remain % ONE_HOUR / ONE_MINUTE) + "分钟";
        else {
            long day = remain / ONE_DAY;
            long hour = remain % ONE_DAY / ONE_HOUR;
            long minute = remain % ONE_DAY % ONE_HOUR / ONE_MINUTE;
            return "只剩下" + day + "天" + hour + "小时" + minute + "分钟";
        }
    }

    /**
     * 距离今天的绝对时间
     *
     * @param date
     * @return
     */
    public static String toToday(Date date) {
        long time = date.getTime() / 1000;
        long now = (new Date().getTime()) / 1000;
        long ago = now - time;
        if (ago <= ONE_HOUR)
            return ago / ONE_MINUTE + "分钟";
        else if (ago <= ONE_DAY)
            return ago / ONE_HOUR + "小时" + (ago % ONE_HOUR / ONE_MINUTE) + "分钟";
        else if (ago <= ONE_DAY * 2)
            return "昨天" + (ago - ONE_DAY) / ONE_HOUR + "点" + (ago - ONE_DAY)
                    % ONE_HOUR / ONE_MINUTE + "分";
        else if (ago <= ONE_DAY * 3) {
            long hour = ago - ONE_DAY * 2;
            return "前天" + hour / ONE_HOUR + "点" + hour % ONE_HOUR / ONE_MINUTE
                    + "分";
        } else if (ago <= ONE_MONTH) {
            long day = ago / ONE_DAY;
            long hour = ago % ONE_DAY / ONE_HOUR;
            long minute = ago % ONE_DAY % ONE_HOUR / ONE_MINUTE;
            return day + "天前" + hour + "点" + minute + "分";
        } else if (ago <= ONE_YEAR) {
            long month = ago / ONE_MONTH;
            long day = ago % ONE_MONTH / ONE_DAY;
            long hour = ago % ONE_MONTH % ONE_DAY / ONE_HOUR;
            long minute = ago % ONE_MONTH % ONE_DAY % ONE_HOUR / ONE_MINUTE;
            return month + "个月" + day + "天" + hour + "点" + minute + "分前";
        } else {
            long year = ago / ONE_YEAR;
            long month = ago % ONE_YEAR / ONE_MONTH;
            long day = ago % ONE_YEAR % ONE_MONTH / ONE_DAY;
            return year + "年前" + month + "月" + day + "天";
        }

    }




    /**
     * 根据Unicode编码完美的判断中文汉字和符号
     */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION;
    }

    /**
     * 完整的判断中文汉字和符号
     */
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (char c : ch) {
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证手机号
     */
    public static boolean isMobileNO(String mobiles) {
        String telRegex = "[1][3578]\\d{9}";
        return !TextUtils.isEmpty(mobiles) && mobiles.matches(telRegex);
    }

    /**
     * 判断字符串是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        boolean isNum = true;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c > 58 || c < 47) {
                isNum = false;
                break;
            }
        }
        return isNum;
    }

    /**
     * 解码base64
     */
    private final static String CODE_STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    private final static int ORGINAL_LEN = 8;
    private final static int NEW_LEN = 6;

    public static String decodeBase64(String encodeStr) throws Exception {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < encodeStr.length(); i++) {

            char c = encodeStr.charAt(i);       //把"1tC5sg=="字符串一个个分拆  
            int k = CODE_STR.indexOf(c);        //分拆后的字符在CODE_STR中的位置,从0开始,如果是'=',返回-1  
            if (k != -1) {                        //如果该字符不是'='
                String tmpStr = Integer.toBinaryString(k);
                int n = 0;
                while (tmpStr.length() + n < NEW_LEN) {
                    n++;
                    sb.append("0");
                }
                sb.append(tmpStr);
            }
        }

        /**
         * 8个字节分拆一次，得到总的字符数 
         * 余数是加密的时候补的，舍去 
         */
        int newByteLen = sb.length() / ORGINAL_LEN;

        /**
         * 二进制转成字节数组 
         */
        byte[] b = new byte[newByteLen];
        for (int j = 0; j < newByteLen; j++) {
            b[j] = (byte) Integer.parseInt(sb.substring(j * ORGINAL_LEN, (j + 1) * ORGINAL_LEN), 2);
        }
     /**
      * 字节数组还原成String
      */
      return new String(b, "gb2312");
    }
}
