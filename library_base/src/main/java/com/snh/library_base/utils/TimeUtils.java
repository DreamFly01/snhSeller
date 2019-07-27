package com.snh.library_base.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/6/3<p>
 * <p>changeTime：2019/6/3<p>
 * <p>version：1<p>
 */
public class TimeUtils {

    private static Calendar getCalendar() {
        long currenTime = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();//
        c.setTimeInMillis(currenTime);
        return c;
    }

    private static Calendar getCalendar(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");
        Calendar c = Calendar.getInstance();//
        try {
            Date date1 = df.parse(date);
            c.setTime(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c;
    }

    public static int getYear() {
        return getCalendar().get(Calendar.YEAR); // 获取当前年份
    }

    public static int getYear(String date) {
        return getCalendar(date).get(Calendar.YEAR);
    }

    public static int getMonth() {
        return getCalendar().get(Calendar.MONTH);// 获取当前月份
    }

    public static int getMonth(String date) {
        return getCalendar(date).get(Calendar.MONTH);
    }

    public static int getDay() {
        return getCalendar().get(Calendar.DAY_OF_MONTH);// 获取当日期
    }

    public static int getDay(String date) {
        return getCalendar(date).get(Calendar.DAY_OF_MONTH);
    }

    public static int getHour(){
        return getCalendar().get(Calendar.HOUR_OF_DAY);
    }
    public static int getHour(String date){
        return getCalendar(date).get(Calendar.HOUR_OF_DAY);
    }
    public static boolean compareData(String dataStr1, String dataStr2) {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd/hh");
        try {
            Date data1 = df.parse(dataStr1);
            Date date2 = df.parse(dataStr2);
            if (data1.getTime() > date2.getTime()) {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static String getDataString(Date data, String format) {
        if (null == data) {
            return "";
        }
        if (StrUtils.isEmpty(format)) {
            format = "yyyy-MM-dd HH";
        }
        String str;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            str = sdf.format(data);
        } catch (Exception e) {
            return data.toString();
        }
        return str;
    }
}
