package com.bcubk;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author Baris Cubukcuoglu
 */
public interface MqttSender {
    void connect() throws IllegalStateException, IOException, MqttException;

    void send(String name, String value, long timestamp);

    void disconnect() throws MqttException;

    boolean isConnected();

}
