package com.solegendary.reignofnether.events.exceptions;

public class EventExecutionException extends Exception {
    private final String eventName;
    private final String eventType;

    public EventExecutionException(String eventName, String eventType, String message) {
        super(message);
        this.eventName = eventName;
        this.eventType = eventType;
    }

    public EventExecutionException(String eventName, String eventType, String message, Throwable cause) {
        super(message, cause);
        this.eventName = eventName;
        this.eventType = eventType;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventType() {
        return eventType;
    }

    public String getDetailedMessage() {
        return String.format("Event '%s' (type: %s): %s", eventName, eventType, getMessage());
    }
}