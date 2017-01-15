/*
 * Copyright 2016 Baris Cubukcuoglu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bcubk;

import com.codahale.metrics.*;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Baris Cubukcuoglu
 */
public class MqttReporter extends ScheduledReporter {
		private static final String REPORTER_TYPE = "mqtt-reporter";

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

				private Builder(final MetricRegistry metricRegistry) {
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
				public Builder withClock(final Clock clock) {
						this.clock = clock;
						return this;
				}

				/**
				 * Prefix all metric names with the given string.
				 *
				 * @param prefix the prefix for all metric names
				 * @return {@code this}
				 */
				public Builder withPrefix(final String prefix) {
						this.prefix = prefix;
						return this;
				}

				/**
				 * Convert rates to the given time unit.
				 *
				 * @param rateUnit a unit of time
				 * @return {@code this}
				 */
				public Builder convertRatesTo(final TimeUnit rateUnit) {
						this.rateUnit = rateUnit;
						return this;
				}

				/**
				 * Convert durations to the given time unit.
				 *
				 * @param durationUnit a unit of time
				 * @return {@code this}
				 */
				public Builder convertDurationsTo(final TimeUnit durationUnit) {
						this.durationUnit = durationUnit;
						return this;
				}

				/**
				 * Only report metrics which match the given filter.
				 *
				 * @param filter a {@link MetricFilter}
				 * @return {@code this}
				 */
				public Builder filter(final MetricFilter filter) {
						this.filter = filter;
						return this;
				}

				public MqttReporter build(final MqttSender mqttSender) {
						return new MqttReporter(metricRegistry, filter, rateUnit,
								durationUnit, mqttSender, clock, prefix);
				}
		}

		private static final Logger LOGGER = LoggerFactory.getLogger(MqttReporter.class);

		private final MqttSender mqttSender;
		private final Clock clock;
		private final String prefix;

		private MqttReporter(final MetricRegistry registry, final MetricFilter filter,
				final TimeUnit rateUnit, final TimeUnit durationUnit,
				final MqttSender mqttSender, final Clock clock, final String prefix) {
				super(registry, REPORTER_TYPE, filter, rateUnit, durationUnit);
				this.mqttSender = mqttSender;
				this.clock = clock;
				this.prefix = prefix;
		}

		public void report(final SortedMap<String, Gauge> gauges,
				final SortedMap<String, Counter> counters,
				final SortedMap<String, Histogram> histograms,
				final SortedMap<String, Meter> meters,
				final SortedMap<String, Timer> timers) {

				final long timestamp = clock.getTime() / 1000;
				final SeriesSet seriesSet = new SeriesSet();

				try {
						if (!mqttSender.isConnected()) {
								mqttSender.connect();
						}

						for (Map.Entry<String, Gauge> entry : gauges.entrySet()) {
								reportGauge(entry.getKey(), entry.getValue(), timestamp);
						}

						for (Map.Entry<String, Counter> entry : counters.entrySet()) {
								reportCounter(entry.getKey(), entry.getValue(),
										timestamp);
						}

						for (Map.Entry<String, Histogram> entry : histograms.entrySet()) {
								reportHistogram(entry.getKey(), entry.getValue(),
										timestamp);
						}

						for (Map.Entry<String, Meter> entry : meters.entrySet()) {
								reportMetered(entry.getKey(), entry.getValue(),
										timestamp);
						}

						for (Map.Entry<String, Timer> entry : timers.entrySet()) {
								//reportTimer(entry.getKey(), entry.getValue(), timestamp);
						}
				}
				catch (IOException e) {
						LOGGER.warn("Unable to report via MQTT", mqttSender, e);
				}
				catch (MqttException e) {
						e.printStackTrace();
				}
				finally {
						try {
								this.mqttSender.disconnect();
						}
						catch (MqttException e) {
								e.printStackTrace();
						}
				}

		}

		private String format(final Object o) {
				if (o instanceof Float) {
						return format(((Float) o).doubleValue());
				}
				else if (o instanceof Double) {
						return format(((Double) o).doubleValue());
				}
				else if (o instanceof Byte) {
						return format(((Byte) o).longValue());
				}
				else if (o instanceof Short) {
						return format(((Short) o).longValue());
				}
				else if (o instanceof Integer) {
						return format(((Integer) o).longValue());
				}
				else if (o instanceof Long) {
						return format(((Long) o).longValue());
				}
				else if (o instanceof BigInteger) {
						return format(((BigInteger) o).doubleValue());
				}
				else if (o instanceof BigDecimal) {
						return format(((BigDecimal) o).doubleValue());
				}
				return null;
		}

		private String format(long n) {
				return Long.toString(n);
		}

		private String format(double v) {
				return String.format(Locale.US, "%2.2f", v);
		}

		private String prefix(String... components) {
				return MetricRegistry.name(prefix, components);
		}

		private void reportTimer(final String name, final Timer timer,
				final long timestamp, final SeriesSet seriesSet) {
				final Snapshot snapshot = timer.getSnapshot();
		/*		mqttSender.send(prefix(name, "max"),
						format(convertDuration(snapshot.getMax())), timestamp);
				mqttSender.send(prefix(name, "mean"),
						format(convertDuration(snapshot.getMean())), timestamp);
				mqttSender.send(prefix(name, "min"),
						format(convertDuration(snapshot.getMin())), timestamp);
				mqttSender.send(prefix(name, "stddev"),
						format(convertDuration(snapshot.getStdDev())), timestamp);
				mqttSender.send(prefix(name, "p50"),
						format(convertDuration(snapshot.getMedian())), timestamp);
				mqttSender.send(prefix(name, "p75"),
						format(convertDuration(snapshot.get75thPercentile())), timestamp);
				mqttSender.send(prefix(name, "p95"),
						format(convertDuration(snapshot.get95thPercentile())), timestamp);
				mqttSender.send(prefix(name, "p98"),
						format(convertDuration(snapshot.get98thPercentile())), timestamp);
				mqttSender.send(prefix(name, "p99"),
						format(convertDuration(snapshot.get99thPercentile())), timestamp);
				mqttSender.send(prefix(name, "p999"),
						format(convertDuration(snapshot.get999thPercentile())),
						timestamp); */

		}

		private void reportMetered(String name, Metered meter, long timestamp) {
		/*		mqttSender
						.send(prefix(name, "count"), format(meter.getCount()), timestamp);
				mqttSender.send(prefix(name, "m1_rate"),
						format(convertRate(meter.getOneMinuteRate())), timestamp);
				mqttSender.send(prefix(name, "m5_rate"),
						format(convertRate(meter.getFiveMinuteRate())), timestamp);
				mqttSender.send(prefix(name, "m15_rate"),
						format(convertRate(meter.getFifteenMinuteRate())), timestamp);
				mqttSender.send(prefix(name, "mean_rate"),
						format(convertRate(meter.getMeanRate())), timestamp); */
		}

		private void reportHistogram(String name, Histogram histogram, long timestamp) {
		/*		final Snapshot snapshot = histogram.getSnapshot();
				mqttSender.send(prefix(name, "count"), format(histogram.getCount()),
						timestamp);
				mqttSender
						.send(prefix(name, "max"), format(snapshot.getMax()), timestamp);
				mqttSender.send(prefix(name, "mean"), format(snapshot.getMean()),
						timestamp);
				mqttSender
						.send(prefix(name, "min"), format(snapshot.getMin()), timestamp);
				mqttSender.send(prefix(name, "stddev"), format(snapshot.getStdDev()),
						timestamp);
				mqttSender.send(prefix(name, "p50"), format(snapshot.getMedian()),
						timestamp);
				mqttSender.send(prefix(name, "p75"), format(snapshot.get75thPercentile()),
						timestamp);
				mqttSender.send(prefix(name, "p95"), format(snapshot.get95thPercentile()),
						timestamp);
				mqttSender.send(prefix(name, "p98"), format(snapshot.get98thPercentile()),
						timestamp);
				mqttSender.send(prefix(name, "p99"), format(snapshot.get99thPercentile()),
						timestamp);
				mqttSender
						.send(prefix(name, "p999"), format(snapshot.get999thPercentile()),
								timestamp); */
		}

		private void reportCounter(String name, Counter counter, long timestamp) {
		/*		mqttSender.send(prefix(name, "count"), format(counter.getCount()),
						timestamp); */
		}

		private void reportGauge(String name, Gauge gauge, long timestamp) {
		/*		final String value = format(gauge.getValue());
				if (value != null) {
						mqttSender.send(prefix(name), value, timestamp);
				} */
		}
}
