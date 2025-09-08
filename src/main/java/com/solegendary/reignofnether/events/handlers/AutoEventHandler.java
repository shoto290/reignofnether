package com.solegendary.reignofnether.events.handlers;

import com.solegendary.reignofnether.config.EventEntry;
import com.solegendary.reignofnether.events.EventConstants;
import com.solegendary.reignofnether.events.exceptions.EventExecutionException;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class AutoEventHandler implements EventHandler {

    private final EventHandlerFactory handlerFactory;

    public AutoEventHandler(EventHandlerFactory handlerFactory) {
        this.handlerFactory = handlerFactory;
    }

    @Override
    public void execute(EventEntry entry, ServerPlayer player, BlockPos position) throws EventExecutionException {
        Object subTypeObj = entry.parameters.get("subType");
        if (!(subTypeObj instanceof String)) {
            throw new EventExecutionException(entry.description, getSupportedType(), 
                EventConstants.Messages.AUTO_EVENT_MISSING_SUBTYPE);
        }

        String subType = (String) subTypeObj;
        EventHandler delegateHandler = handlerFactory.getHandler(subType);
        
        if (delegateHandler == null) {
            throw new EventExecutionException(entry.description, getSupportedType(), 
                String.format(EventConstants.Messages.UNKNOWN_AUTO_EVENT_SUBTYPE, subType));
        }

        delegateHandler.execute(entry, player, position);
    }

    @Override
    public boolean canExecute(EventEntry entry, ServerPlayer player) {
        Object subTypeObj = entry.parameters.get("subType");
        if (!(subTypeObj instanceof String)) {
            return false;
        }

        String subType = (String) subTypeObj;
        EventHandler delegateHandler = handlerFactory.getHandler(subType);
        
        return delegateHandler != null && delegateHandler.canExecute(entry, player);
    }

    @Override
    public String getSupportedType() {
        return "AUTO";
    }
}