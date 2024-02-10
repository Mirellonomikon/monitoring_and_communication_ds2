package com.morello.monitoring_and_communication.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MaxEnergyExceededEvent extends ApplicationEvent {
    private final String message;
    private final Integer deviceId;

    public MaxEnergyExceededEvent(Object source, Integer deviceId, String message) {
        super(source);
        this.deviceId = deviceId;
        this.message = message;
    }

}
