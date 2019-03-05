package com.snh.snhseller.utils;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/4<p>
 * <p>changeTime：2019/3/4<p>
 * <p>version：1<p>
 */
public class DistanceUtils {
    private final static double EARTH_RADIUS = 6378.137;
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static double getDistance(double lat1, double lng1, double lat2,
                                     double lng2) {
//        LogUtil.d("LocationUtil", "lat1:" + lat1 + "lng1:" + lng1 + "lat2:" + lat2 + "lng2:" + lng2);
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        // 如果有一方等于零，直接返回0
        if (radLat1 == 0 || radLat2 == 0) {
            return 0;
        }
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        s = s * 1000;//单位：米
        return s;
    }

    /**
     * 距离格式化
     *
     * @param distance 以千米为单位
     * @return
     */
    public static String distanceKMFormat(double distance) {
        return distance > 1000 ? (distance/1000 + "Km") : (distance + "m");
    }


    /**
     * 距离只保留两位小数
     * @param distance 以米为单位
     * @return
     */
    public static String distanceFormat(double distance) {
        String str;
        double value = distance;
        if (distance >= 1000) {
            value = value / 1000;
            str = "KM";
        } else {
            str = "M";
        }
        return String.format("%.2f",value);
    }

}
