package com.Ease.Metrics;

public class MetricsFactory {
    private static MetricsFactory ourInstance = new MetricsFactory();

    public static MetricsFactory getInstance() {
        return ourInstance;
    }

    private MetricsFactory() {
    }
}
