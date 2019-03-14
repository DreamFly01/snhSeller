package com.snh.snhseller.utils;

import com.mob.tools.utils.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
//        java.util.Date date=new java.util.Date();
        String str=sdf.format(data);
        return str;
    }
    public static String stringToTime(String dateTime){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            String time = df.format(new Date());
            Date parse = df.parse(time);
            Date date = df.parse(dateTime);
            long between = parse.getTime() - date.getTime();
            long day = between / (24 * 60 * 60 * 1000);
            long hour = (between / (60 * 60 * 1000) - day * 24);
            long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            if(day>0){
                if(day>30){
                    if(day/30>12){
                        return (day/30/12+"年");
                    }
                    return (day/30+"月");
                }
                return (day + "天");
            }
            if(hour>0){
                return hour + "小时" ;
            }
            if(min>0){
               return min + "分";
            }
            if(s>0){
               return "刚刚";
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return "";
    }

    public static String secToString(int sec){
        String content = "";
        if(sec/60<1){
            content =  "00小时01分后关闭";
        }
        if(sec/60>1&&sec/60<60){
            String str = sec/60<10?"0"+sec/60:sec/60+"分后关闭";
            content =  "00小时"+str;
        }
        if(sec/3600>=1){
            content =  "01小时00分后关闭";
        }
        return content;
    }
}
