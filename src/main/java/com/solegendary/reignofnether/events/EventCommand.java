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
        ServerPlayer player = context.getSource().getPlayerOrException();
        EventServerEvents.executeEvent(eventName, player);
        return Command.SINGLE_SUCCESS;
    }
}