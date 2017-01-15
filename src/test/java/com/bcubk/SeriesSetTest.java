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

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Baris Cubukcuoglu
 */
public class SeriesSetTest {

		private SeriesSet seriesSet;
		private DiscreteTimeValue discreteTimeValue = new DiscreteTimeValueBuilder()
				.setTimeStamp(System.currentTimeMillis()).setName("test")
				.setValue("value").build();

		@Before
		public void setUp() throws Exception {
				this.seriesSet = new SeriesSet();
				this.seriesSet.addDiscreteTimeValue(discreteTimeValue);
		}

		@Test
		public void isEmpty() throws Exception {
				assertThat(this.seriesSet.isEmpty()).isFalse();
		}

		@Test
		public void addDiscreteTimeValue() throws Exception {
				this.seriesSet.addDiscreteTimeValue(new DiscreteTimeValueBuilder()
						.setTimeStamp(System.currentTimeMillis()).setName("test2")
						.setValue("value").build());
				assertThat(this.seriesSet.size()).isEqualTo(2);
		}

		@Test
		public void resetSeries() throws Exception {
			this.seriesSet.resetSeries();
			assertThat(this.seriesSet.size()).isEqualTo(0);
		}

		@Test
		public void getDiscreteTimeValues() throws Exception {
			assertThat(this.seriesSet.getDiscreteTimeValues()).isNotNull();
			for(DiscreteTimeValue discreteTimeValue : this.seriesSet.getDiscreteTimeValues()) {
					assertThat(discreteTimeValue.getName()).isEqualTo(discreteTimeValue.getName());
					assertThat(discreteTimeValue.getTimeStamp()).isEqualTo(discreteTimeValue.getTimeStamp());
					assertThat(discreteTimeValue.getValue()).isEqualTo(discreteTimeValue.getValue());
			}
		}
}