package com.solegendary.reignofnether.events;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class EventServerEvents {

    public static void executeEvent(String eventName, ServerPlayer player) {
        String message = "Event '" + eventName + "' executed successfully!";
        player.sendSystemMessage(Component.literal(message));
    }
}