package com.solegendary.reignofnether.events;

import net.minecraft.server.level.ServerPlayer;

public class Event {
    private final String name;
    private final String description;

    public Event(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void execute(ServerPlayer player) {
        EventServerEvents.executeEvent(name, player);
    }
}