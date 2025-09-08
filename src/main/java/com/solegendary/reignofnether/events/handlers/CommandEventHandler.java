package com.solegendary.reignofnether.events.handlers;

import com.solegendary.reignofnether.config.EventEntry;
import com.solegendary.reignofnether.events.EventConstants;
import com.solegendary.reignofnether.events.exceptions.EventExecutionException;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class CommandEventHandler implements EventHandler {

    @Override
    public void execute(EventEntry entry, ServerPlayer player, BlockPos position) throws EventExecutionException {
        Object commandObj = entry.parameters.get("command");
        if (!(commandObj instanceof String)) {
            throw new EventExecutionException(entry.description, getSupportedType(), 
                EventConstants.Messages.INVALID_COMMAND_PARAMETER);
        }

        String command = (String) commandObj;
        try {
            player.getServer().getCommands().performPrefixedCommand(
                player.getServer().createCommandSourceStack(), command);
            
            player.sendSystemMessage(Component.literal(
                String.format(EventConstants.Messages.EXECUTED_COMMAND, command)));
        } catch (Exception e) {
            throw new EventExecutionException(entry.description, getSupportedType(), 
                "Failed to execute command: " + command, e);
        }
    }

    @Override
    public boolean canExecute(EventEntry entry, ServerPlayer player) {
        Object commandObj = entry.parameters.get("command");
        return commandObj instanceof String && !((String) commandObj).trim().isEmpty();
    }

    @Override
    public String getSupportedType() {
        return "COMMAND";
    }
}