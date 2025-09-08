package com.solegendary.reignofnether.events.handlers;

import com.solegendary.reignofnether.config.EventEntry;
import com.solegendary.reignofnether.events.exceptions.EventExecutionException;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

public interface EventHandler {
    void execute(EventEntry entry, ServerPlayer player, BlockPos position) throws EventExecutionException;
    boolean canExecute(EventEntry entry, ServerPlayer player);
    String getSupportedType();
}