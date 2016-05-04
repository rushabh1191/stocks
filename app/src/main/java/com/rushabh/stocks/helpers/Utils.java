package com.rushabh.stocks.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by rushabh on 28/04/16.
 */
public class Utils {

    final static String TIME_FORMAT = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'";

    public static String truncateNumber(double floatNumber) {
        long million = 1000000L;
        long billion = 1000000000L;
        long trillion = 1000000000000L;
        long number = Math.round(floatNumber);
        if ((number >= million) && (number < billion)) {
            float fraction = calculateFraction(number, million);
            return Float.toString(fraction) + " Million";
        } else if ((number >= billion) && (number < trillion)) {
            float fraction = calculateFraction(number, billion);
            return Float.toString(fraction) + " Billion";
        }
        return Long.toString(number);
    }

    public static String convertTime(String dateTime){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-mm-dd'T'hh:mm:ss'Z'");
        try {
            Date date=format.parse(dateTime);
            SimpleDateFormat f1=new SimpleDateFormat("dd/mm/yyyy hh:mm a");
            return  f1.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return  dateTime;
        }
    }

    public static String convertUTCToTime(String utcTime) {

        SimpleDateFormat ft = new SimpleDateFormat("DD MMMM dd, HH:mm:ss 'UTC-04:00' yyyy", Locale.US);
        Date t= null;
        try {
            t = ft.parse(utcTime);
            ft.applyPattern("dd MMM, yyy, hh:mm:ss");
            return  ft.format(t);
        } catch (ParseException e) {
            e.printStackTrace();
            return  utcTime;
        }


    }
    public static String round(double d){
        return String.format("%.2f", d);
    }
    public static float calculateFraction(long number, long divisor) {
        long truncate = (number * 10L + (divisor / 2L)) / divisor;
        float fraction = (float) truncate * 0.10F;
        return fraction;
    }
}
