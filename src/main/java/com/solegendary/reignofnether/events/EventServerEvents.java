package com.solegendary.reignofnether.events;

import com.solegendary.reignofnether.config.EventEntry;
import com.solegendary.reignofnether.config.JsonEventConfig;
import com.solegendary.reignofnether.config.JsonEventConfigManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class EventServerEvents {

    public static void executeEvent(String eventName, ServerPlayer player) {
        JsonEventConfig config = JsonEventConfigManager.getConfig();
        if (config == null) {
            player.sendSystemMessage(Component.literal("Event system not initialized!"));
            return;
        }
        
        EventEntry eventEntry = config.getEntry(eventName);
        if (eventEntry == null) {
            player.sendSystemMessage(Component.literal("Event '" + eventName + "' not found!"));
            return;
        }
        
        switch (eventEntry.type) {
            case "COMMAND":
                executeCommandEvent(eventEntry, player);
                break;
            default:
                player.sendSystemMessage(Component.literal("Unknown event type: " + eventEntry.type));
        }
    }
    
    private static void executeCommandEvent(EventEntry eventEntry, ServerPlayer player) {
        Object commandObj = eventEntry.parameters.get("command");
        if (commandObj instanceof String) {
            String command = (String) commandObj;
            player.getServer().getCommands().performPrefixedCommand(
                player.getServer().createCommandSourceStack(), command);
            player.sendSystemMessage(Component.literal("Executed command: " + command));
        } else {
            player.sendSystemMessage(Component.literal("Invalid command parameter for event"));
        }
    }
}