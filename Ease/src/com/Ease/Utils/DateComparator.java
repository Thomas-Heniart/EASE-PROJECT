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

    private static final long millisecondsInDay = 86400000;
    private final static Long millisecondsInMonth = new Long("2629746000");

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

    public static boolean isEqualsAfter(Date date, int numberOfDays) {
        long time = new Date().getTime() - date.getTime();
        return time >= (numberOfDays * millisecondsInDay) && time <= ((numberOfDays + 1) * millisecondsInDay);
    }

    public static boolean isInDays(Date date, int numberOfDays) {
        System.out.println("Free trial end: " + date.getTime());
        System.out.println("Now: " + new Date().getTime());
        long time = date.getTime() - new Date().getTime();
        return time >= (numberOfDays * millisecondsInDay) && time < -((numberOfDays + 1) * millisecondsInDay);
    }
}
