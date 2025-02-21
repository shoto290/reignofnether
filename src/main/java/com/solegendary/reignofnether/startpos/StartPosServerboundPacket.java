package com.solegendary.reignofnether.startpos;

import com.solegendary.reignofnether.registrars.PacketHandler;
import com.solegendary.reignofnether.survival.SurvivalClientEvents;
import com.solegendary.reignofnether.survival.SurvivalServerEvents;
import com.solegendary.reignofnether.survival.WaveDifficulty;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class StartPosServerboundPacket {

    StartPosAction action;
    BlockPos blockPos;

    public static void reservePos(BlockPos pos) {
        PacketHandler.INSTANCE.sendToServer(new StartPosServerboundPacket(StartPosAction.RESERVE, pos));
    }

    public static void unreservePos(BlockPos pos) {
        PacketHandler.INSTANCE.sendToServer(new StartPosServerboundPacket(StartPosAction.UNRESERVE, pos));
    }

    public StartPosServerboundPacket(StartPosAction action, BlockPos pos) {
        this.action = action;
        this.blockPos = pos;
    }

    public StartPosServerboundPacket(FriendlyByteBuf buffer) {
        this.action = buffer.readEnum(StartPosAction.class);
        this.blockPos = buffer.readBlockPos();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeEnum(this.action);
        buffer.writeBlockPos(this.blockPos);
    }

    // server-side packet-consuming functions
    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
            switch (action) {
                case RESERVE -> {
                    for (StartPos startPos : StartPosServerEvents.startPoses) {
                        if (startPos.pos.equals(blockPos)) {
                            startPos.reserved = true;
                            StartPosClientboundPacket.reservePos(blockPos);
                            break;
                        }
                    }
                }
                case UNRESERVE -> {
                    for (StartPos startPos : StartPosServerEvents.startPoses) {
                        if (startPos.pos.equals(blockPos)) {
                            startPos.reserved = false;
                            StartPosClientboundPacket.unreservePos(blockPos);
                            break;
                        }
                    }
                }
            }
            success.set(true);
        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }
}