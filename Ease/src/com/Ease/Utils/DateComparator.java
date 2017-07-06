package com.Ease.Utils;

/**
 * Created by thomas on 06/07/2017.
 */
public class DateComparator {
    private static DateComparator ourInstance = new DateComparator();

    public static DateComparator getInstance() {
        return ourInstance;
    }

    private DateComparator() {
    }
}
