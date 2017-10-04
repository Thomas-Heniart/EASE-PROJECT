package com.Ease.Utils;

import java.util.Date;

/**
 * Created by thomas on 06/07/2017.
 */
public class DateComparator {
    private static DateComparator ourInstance = new DateComparator();

    public static DateComparator getInstance() {
        return ourInstance;
    }

    public final static long millisecondsInDay = 86400000;
    public final static Long millisecondsInMonth = new Long("2629746000");

    public static boolean isOutdated(Date date, int numberOfDays) {
        long now = new Date().getTime();
        return (now - date.getTime()) > (numberOfDays * millisecondsInDay);
    }

    private DateComparator() {

    }

    public static boolean isOutdated(Date date, int numberOfMonth, int numberOfDays) {
        long now = new Date().getTime();
        return (now - date.getTime()) > (numberOfMonth * millisecondsInMonth + numberOfDays * millisecondsInDay);
    }

    public static boolean isInDays(Date date, int numberOfDays) {
        long time = date.getTime() - new Date().getTime();
        return time <= (numberOfDays * millisecondsInDay) && time >= ((numberOfDays - 1) * millisecondsInDay);
    }
}
