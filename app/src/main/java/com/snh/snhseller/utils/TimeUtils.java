package com.snh.snhseller.utils;

import com.mob.tools.utils.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/5<p>
 * <p>changeTime：2019/3/5<p>
 * <p>version：1<p>
 */
public class TimeUtils {

    public static String getDateString(long milliseconds) {
        return getDateTimeString(milliseconds, "yyyy-MM-dd");
    }
    public static String getDateTimeString(long milliseconds, String format) {
        Date date = new Date(milliseconds);
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        return formatter.format(date);
    }

    public static String getDataString(Date data){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date=new java.util.Date();
        String str=sdf.format(date);
        return str;
    }
}
