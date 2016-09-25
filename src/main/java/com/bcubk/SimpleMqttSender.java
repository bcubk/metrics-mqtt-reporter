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
    private InetSocketAddress address;
    private final String clientId;

    private MqttClient mqttClient;

    public SimpleMqttSender(String hostname, int port, String topic) {
        this(hostname, port, QualityOfService.EXACTLY_ONCE, topic, null, MqttClient.generateClientId());
    }

    public SimpleMqttSender(String hostname, int port, String topic, String clientId) {
        this(hostname, port, QualityOfService.EXACTLY_ONCE, topic, null, clientId);
    }

    public SimpleMqttSender(InetSocketAddress address, String topic, QualityOfService qos, String clientId) {
        this(null, -1, qos, topic, address, clientId);
    }

    public SimpleMqttSender(String topic, InetSocketAddress address) {
        this(null, -1, QualityOfService.EXACTLY_ONCE, topic, address, MqttClient.generateClientId());
    }

    public SimpleMqttSender(String topic, InetSocketAddress address, String clientId) {
        this(null, -1, QualityOfService.EXACTLY_ONCE, topic, address, clientId);
    }

    public SimpleMqttSender(String hostname, int port, QualityOfService qos, String topic, InetSocketAddress address, String clientId) {
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

        this.mqttClient = new MqttClient(this.address.getHostName() + ":" + this.address.getPort(), clientId, new MemoryPersistence());
        this.mqttClient.connect();

    }

    public void send(String name, String value, long timestamp) {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(this.qos.getQos());
        try {
            mqttClient.publish(this.topic, mqttMessage);
        } catch (MqttException e) {
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
