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
        if ((context.getSource() != null &&
            context.getSource().getPlayer() != null &&
            context.getSource().getPlayer().hasPermissions(4)) ||
            (context.getSource() != null &&
            !context.getSource().isPlayer())) {
            
            ServerPlayer player = context.getSource().getPlayerOrException();
            EventServerEvents.executeEvent(eventName, player);
            return Command.SINGLE_SUCCESS;
        } else {
            if (context.getSource().getPlayer() != null) {
                context.getSource().getPlayer().sendSystemMessage(Component.literal("You don't have permission to use this command (OP required)"));
            }
            return 0;
        }
    }
}