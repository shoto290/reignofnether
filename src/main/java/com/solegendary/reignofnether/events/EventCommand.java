package com.solegendary.reignofnether.events;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class EventCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("rts-event")
                .then(Commands.literal("debug")
                        .executes(EventCommand::executeDebugEvent))
                .then(Commands.literal("test")
                        .executes(EventCommand::executeTestEvent))
                .then(Commands.literal("spawn")
                        .executes(EventCommand::executeSpawnEvent)));
    }

    private static int executeDebugEvent(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        EventServerEvents.executeEvent("debug", player);
        return Command.SINGLE_SUCCESS;
    }

    private static int executeTestEvent(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        EventServerEvents.executeEvent("test", player);
        return Command.SINGLE_SUCCESS;
    }

    private static int executeSpawnEvent(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        EventServerEvents.executeEvent("spawn", player);
        return Command.SINGLE_SUCCESS;
    }
}