package com.rushabh.stocks.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        String result = null;
        SimpleDateFormat sdf = new SimpleDateFormat(
                TIME_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy hh:mm a");
        df.setTimeZone(TimeZone.getTimeZone("IST"));
        try {
            Date date = sdf.parse(utcTime);
            result = df.format(date);
            return result;
        } catch (ParseException e) {
            e.printStackTrace();
            return utcTime;
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
