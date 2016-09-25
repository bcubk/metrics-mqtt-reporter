package com.bcubk;

import com.codahale.metrics.*;

import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Baris Cubukcuoglu
 */
public class MqttReporter extends ScheduledReporter {
    private static final String REPORTER_TYPE = "mqtt-reporter";

    protected MqttReporter(MetricRegistry registry, String name, MetricFilter filter, TimeUnit rateUnit, TimeUnit durationUnit) {
        super(registry, REPORTER_TYPE, filter, rateUnit, durationUnit);
    }

    /**
     * A builder for {@link MqttReporter} instances. Defaults to not using a prefix, using the
     * default clock, converting rates to events/second, converting durations to milliseconds, and
     * not filtering metrics.
     */
    public static class Builder {
        private final MetricRegistry metricRegistry;
        private Clock clock;
        private String prefix;
        private TimeUnit rateUnit;
        private TimeUnit durationUnit;
        private MetricFilter filter;

        public Builder(MetricRegistry metricRegistry) {
            this.metricRegistry = metricRegistry;
            this.clock = Clock.defaultClock();
            this.prefix = null;
            this.rateUnit = TimeUnit.SECONDS;
            this.durationUnit = TimeUnit.MILLISECONDS;
            this.filter = MetricFilter.ALL;
        }

        /**
         * Use the given {@link Clock} instance for the time.
         *
         * @param clock a {@link Clock} instance
         * @return {@code this}
         */
        public Builder withClock(Clock clock) {
            this.clock = clock;
            return this;
        }

        /**
         * Prefix all metric names with the given string.
         *
         * @param prefix the prefix for all metric names
         * @return {@code this}
         */
        public Builder withPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        /**
         * Convert rates to the given time unit.
         *
         * @param rateUnit a unit of time
         * @return {@code this}
         */
        public Builder convertRatesTo(TimeUnit rateUnit) {
            this.rateUnit = rateUnit;
            return this;
        }

        /**
         * Convert durations to the given time unit.
         *
         * @param durationUnit a unit of time
         * @return {@code this}
         */
        public Builder convertDurationsTo(TimeUnit durationUnit) {
            this.durationUnit = durationUnit;
            return this;
        }

        /**
         * Only report metrics which match the given filter.
         *
         * @param filter a {@link MetricFilter}
         * @return {@code this}
         */
        public Builder filter(MetricFilter filter) {
            this.filter = filter;
            return this;
        }

        public MqttReporter build(MqttSender mqttSender) {
            return new MqttReporter(metricRegistry, prefix, filter, rateUnit, durationUnit);
        }
    }

    public void report(SortedMap<String, Gauge> sortedMap, SortedMap<String, Counter> sortedMap1, SortedMap<String, Histogram> sortedMap2,
                       SortedMap<String, Meter> sortedMap3, SortedMap<String, Timer> sortedMap4) {

    }

    private String format(long n) {
        return Long.toString(n);
    }
}
