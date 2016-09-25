package com.bcubk;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by baris on 25.09.16.
 */
public class BasicMqttSender implements MqttSender {
    private final String hostname;
    private final int port;
    private final QualityOfService qos;
    private final String topic;
    private InetSocketAddress address;
    private final String clientId;

    public BasicMqttSender(String hostname, int port, String topic, String clientId) {
        this(hostname, port, QualityOfService.EXACTLY_ONCE, topic, null, clientId);
    }

    public BasicMqttSender(InetSocketAddress address, String topic, QualityOfService qos, String clientId) {
        this(null, -1, qos, topic, address, clientId);
    }

    public BasicMqttSender(String topic, InetSocketAddress address, String clientId) {
        this(null, -1, QualityOfService.EXACTLY_ONCE, topic, address, clientId);
    }

    public BasicMqttSender(String hostname, int port, QualityOfService qos, String topic, InetSocketAddress address, String clientId) {
        this.hostname = hostname;
        this.port = port;
        this.qos = qos;
        this.topic = topic;
        this.address = address;
        this.clientId = clientId;
    }

    public void connect() {
        if(isConnected()) {
            throw new IllegalStateException("Already connected");
        }


    }

    public void send(String name, String value, long timestamp) {

    }

    public void disconnect() {

    }

    public boolean isConnected() {
        return false;
    }

    public void close() throws IOException {

    }
}
