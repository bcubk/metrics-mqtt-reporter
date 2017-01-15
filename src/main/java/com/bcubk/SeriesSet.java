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

import java.util.LinkedList;
import java.util.List;

/**
 * Created by baris on 26.12.16.
 */
public class SeriesSet {
		private final List<DiscreteTimeValue> discreteTimeValues = new LinkedList<>();

		public boolean isEmpty() {
				return this.discreteTimeValues.isEmpty();
		}

		public int size() {
				return discreteTimeValues.size();
		}

		public void addDiscreteTimeValue(final DiscreteTimeValue discreteTimeValue) {
				this.discreteTimeValues.add(discreteTimeValue);
		}

		public void resetSeries() {
				this.discreteTimeValues.clear();
		}

		public List<DiscreteTimeValue> getDiscreteTimeValues() {
				return discreteTimeValues;
		}
}
