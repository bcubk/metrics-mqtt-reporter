package com.bcubk;

import com.codahale.metrics.*;

import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Baris Cubukcuoglu
 */
public class MqttReporter extends ScheduledReporter {
    private static final String REPORTER_TYPE ="mqtt-reporter";

    protected MqttReporter(MetricRegistry registry, String name, MetricFilter filter, TimeUnit rateUnit, TimeUnit durationUnit) {
        super(registry, REPORTER_TYPE, filter, rateUnit, durationUnit);
    }

    public void report(SortedMap<String, Gauge> sortedMap, SortedMap<String, Counter> sortedMap1, SortedMap<String, Histogram> sortedMap2,
                       SortedMap<String, Meter> sortedMap3, SortedMap<String, Timer> sortedMap4) {

    }
}
