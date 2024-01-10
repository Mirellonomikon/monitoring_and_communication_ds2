package com.morello.monitoring_and_communication.events;

import org.springframework.context.ApplicationEvent;

public class MaxEnergyExceededEvent extends ApplicationEvent {
    private final String message;
    private final Integer deviceId;

    public MaxEnergyExceededEvent(Object source, Integer deviceId, String message) {
        super(source);
        this.deviceId = deviceId;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Integer getDeviceId() {
        return deviceId;
    }
}
