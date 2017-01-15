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

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.IOException;
import java.net.InetSocketAddress;

public class SimpleMqttSender implements MqttSender {
		private final QualityOfService qos;
		private final String topic;
		private final String clientId;
		private InetSocketAddress address;
		private MqttClient mqttClient;

		public SimpleMqttSender(String hostname, int port, String topic) {
				this(hostname, port, QualityOfService.EXACTLY_ONCE, topic, null,
						MqttClient.generateClientId());
		}

		public SimpleMqttSender(String hostname, int port, String topic,
				String clientId) {
				this(hostname, port, QualityOfService.EXACTLY_ONCE, topic, null,
						clientId);
		}

		public SimpleMqttSender(InetSocketAddress address, String topic,
				QualityOfService qos, String clientId) {
				this(null, -1, qos, topic, address, clientId);
		}

		public SimpleMqttSender(String topic, InetSocketAddress address) {
				this(null, -1, QualityOfService.EXACTLY_ONCE, topic, address,
						MqttClient.generateClientId());
		}

		public SimpleMqttSender(String topic, InetSocketAddress address,
				String clientId) {
				this(null, -1, QualityOfService.EXACTLY_ONCE, topic, address, clientId);
		}

		public SimpleMqttSender(String hostname, int port, QualityOfService qos,
				String topic, InetSocketAddress address, String clientId) {
				if (address == null) {
						this.address = new InetSocketAddress(hostname, port);
				}
				this.qos = qos;
				this.topic = topic;
				this.clientId = clientId;
		}

		public void connect() throws IllegalStateException, IOException, MqttException {
				if (isConnected()) {
						throw new IllegalStateException("Already connected");
				}

				this.mqttClient = new MqttClient(
						this.address.getHostName() + ":" + this.address.getPort(),
						clientId, new MemoryPersistence());
				this.mqttClient.connect();

		}

		public void send(final String jsonPayload) {
				MqttMessage mqttMessage = new MqttMessage();
				mqttMessage.setQos(this.qos.getQos());
				mqttMessage.setPayload(jsonPayload.getBytes());
				try {
						mqttClient.publish(this.topic, mqttMessage);
				}
				catch (MqttException e) {
						e.printStackTrace();
				}
		}

		public void disconnect() throws MqttException {
				this.mqttClient.close();
		}

		public boolean isConnected() {
				return this.mqttClient != null && this.mqttClient.isConnected();
		}

}
