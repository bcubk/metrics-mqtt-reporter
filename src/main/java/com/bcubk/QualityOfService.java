package com.bcubk;

/**
 * Created by baris on 25.09.16.
 */
public enum QualityOfService {
    AT_MOST_ONCE(0), AT_LEAST_ONCE(1), EXACTLY_ONCE(2);

    private final int qos;

    QualityOfService(int qos) {
        this.qos = qos;
    }

    public int getQos() {
        return qos;
    }
}
