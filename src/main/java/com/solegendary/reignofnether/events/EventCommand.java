package com.solegendary.reignofnether.events;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.solegendary.reignofnether.config.JsonEventConfig;
import com.solegendary.reignofnether.config.JsonEventConfigManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class EventCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> eventCommand = Commands.literal("rts-event");
        
        JsonEventConfig config = JsonEventConfigManager.getConfig();
        if (config != null && config.events != null) {
            for (String eventName : config.events.keySet()) {
                eventCommand.then(Commands.literal(eventName)
                    .executes(context -> executeEvent(context, eventName)));
            }
        }
        
        dispatcher.register(eventCommand);
    }

    private static int executeEvent(CommandContext<CommandSourceStack> context, String eventName) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        
        if (!hasPermission(source)) {
            if (source.getPlayer() != null) {
                source.getPlayer().sendSystemMessage(Component.literal(EventConstants.Messages.NO_PERMISSION));
            }
            return 0;
        }
        
        try {
            ServerPlayer player = source.getPlayerOrException();
            EventServerEvents.executeEvent(eventName, player);
            return Command.SINGLE_SUCCESS;
        } catch (CommandSyntaxException e) {
            if (source.getPlayer() != null) {
                source.getPlayer().sendSystemMessage(Component.literal("Failed to execute event: " + e.getMessage()));
            }
            throw e;
        } catch (Exception e) {
            if (source.getPlayer() != null) {
                source.getPlayer().sendSystemMessage(Component.literal("Unexpected error executing event: " + e.getMessage()));
            }
            return 0;
        }
    }
    
    private static boolean hasPermission(CommandSourceStack source) {
        return (source != null && source.getPlayer() != null && source.getPlayer().hasPermissions(4)) ||
               (source != null && !source.isPlayer());
    }
}