package com.solegendary.reignofnether.events.handlers;

import java.util.HashMap;
import java.util.Map;

public class EventHandlerFactory {
    private final Map<String, EventHandler> handlers;

    public EventHandlerFactory() {
        this.handlers = new HashMap<>();
        initializeHandlers();
    }

    private void initializeHandlers() {
        CommandEventHandler commandHandler = new CommandEventHandler();
        PatrolEventHandler patrolHandler = new PatrolEventHandler();
        AutoEventHandler autoHandler = new AutoEventHandler(this);

        handlers.put(commandHandler.getSupportedType(), commandHandler);
        handlers.put(patrolHandler.getSupportedType(), patrolHandler);
        handlers.put(autoHandler.getSupportedType(), autoHandler);
    }

    public EventHandler getHandler(String eventType) {
        return handlers.get(eventType != null ? eventType.toUpperCase() : null);
    }

    public boolean isSupported(String eventType) {
        return handlers.containsKey(eventType != null ? eventType.toUpperCase() : null);
    }
}