package com.solegendary.reignofnether.events;

import com.solegendary.reignofnether.config.EventEntry;
import com.solegendary.reignofnether.config.JsonEventConfig;
import com.solegendary.reignofnether.config.JsonEventConfigManager;
import com.solegendary.reignofnether.events.handlers.EventHandlerFactory;
import com.solegendary.reignofnether.events.handlers.EventHandler;
import com.solegendary.reignofnether.events.exceptions.EventExecutionException;
import com.solegendary.reignofnether.events.validation.EventParameterValidator;
import com.solegendary.reignofnether.events.validation.ValidationResult;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class EventServerEvents {
    
    private static final EventHandlerFactory handlerFactory = new EventHandlerFactory();

    public static void executeEvent(String eventName, ServerPlayer player) {
        JsonEventConfig config = JsonEventConfigManager.getConfig();
        if (config == null) {
            player.sendSystemMessage(Component.literal(EventConstants.Messages.EVENT_SYSTEM_NOT_INITIALIZED));
            return;
        }
        
        EventEntry eventEntry = config.getEntry(eventName);
        if (eventEntry == null) {
            player.sendSystemMessage(Component.literal(
                String.format(EventConstants.Messages.EVENT_NOT_FOUND, eventName)));
            return;
        }
        
        ValidationResult validationResult = EventParameterValidator.validate(eventEntry);
        if (!validationResult.isValid()) {
            player.sendSystemMessage(Component.literal("Event validation failed: " + validationResult.getErrorMessage()));
            return;
        }
        
        EventHandler handler = handlerFactory.getHandler(eventEntry.type);
        if (handler == null) {
            player.sendSystemMessage(Component.literal(
                String.format(EventConstants.Messages.UNKNOWN_EVENT_TYPE, eventEntry.type)));
            return;
        }
        
        if (!handler.canExecute(eventEntry, player)) {
            player.sendSystemMessage(Component.literal("Event cannot be executed at this time"));
            return;
        }
        
        try {
            handler.execute(eventEntry, player, player.blockPosition());
        } catch (EventExecutionException e) {
            player.sendSystemMessage(Component.literal("Event execution failed: " + e.getMessage()));
        }
    }
}