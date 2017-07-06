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

    public static boolean isOutdated(Date date, int numberOfDays) {
        long now = new Date().getTime();
        return (now - date.getTime()) > (numberOfDays * millisecondsInDay);
    }

    private DateComparator() {

    }
}
