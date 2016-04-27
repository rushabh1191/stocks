package com.rushabh.stocks.helpers;

/**
 * Created by rushabh on 28/04/16.
 */
public class Utils {

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

    public static String round(double d){
        return String.format("%.2f", d);
    }
    public static float calculateFraction(long number, long divisor) {
        long truncate = (number * 10L + (divisor / 2L)) / divisor;
        float fraction = (float) truncate * 0.10F;
        return fraction;
    }
}
