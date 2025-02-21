package com.solegendary.reignofnether.startpos;

import com.solegendary.reignofnether.registrars.PacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class StartPosClientboundPacket {

    StartPosAction action;
    boolean reserved;
    BlockPos blockPos;
    int colorId;

    public static void addPos(StartPos startPos) {
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
                new StartPosClientboundPacket(StartPosAction.ADD, startPos.pos, startPos.reserved, startPos.colorId));
    }

    public static void removePos(BlockPos pos) {
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
                new StartPosClientboundPacket(StartPosAction.REMOVE, pos, false,0));
    }

    public static void reservePos(BlockPos pos) {
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
                new StartPosClientboundPacket(StartPosAction.RESERVE, pos,false,0));
    }

    public static void unreservePos(BlockPos pos) {
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
                new StartPosClientboundPacket(StartPosAction.UNRESERVE, pos,false,0));
    }

    public StartPosClientboundPacket(StartPosAction action, BlockPos blockPos, boolean reserved, int colorId) {
        this.action = action;
        this.blockPos = blockPos;
        this.reserved = reserved;
        this.colorId = colorId;
    }

    public StartPosClientboundPacket(FriendlyByteBuf buffer) {
        this.action = buffer.readEnum(StartPosAction.class);
        this.blockPos = buffer.readBlockPos();
        this.reserved = buffer.readBoolean();
        this.colorId = buffer.readInt();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeEnum(this.action);
        buffer.writeBlockPos(this.blockPos);
        buffer.writeBoolean(this.reserved);
        buffer.writeInt(this.colorId);
    }

    // server-side packet-consuming functions
    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);

        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                    () -> () -> {
                        switch (action) {
                            case ADD -> {
                                StartPosClientEvents.startPoses.removeIf(sp -> sp.pos.equals(blockPos));
                                StartPosClientEvents.startPoses.add(new StartPos(blockPos, reserved, colorId));
                            }
                            case REMOVE -> {
                                StartPosClientEvents.startPoses.removeIf(sp -> sp.pos.equals(blockPos));
                            }
                            case RESERVE -> {
                                for (StartPos startPos : StartPosClientEvents.startPoses) {
                                    if (startPos.pos.equals(blockPos)) {
                                        startPos.reserved = true;
                                        break;
                                    }
                                }
                            }
                            case UNRESERVE -> {
                                for (StartPos startPos : StartPosClientEvents.startPoses) {
                                    if (startPos.pos.equals(blockPos)) {
                                        startPos.reserved = false;
                                        break;
                                    }
                                }
                            }
                        }
                        success.set(true);
                    });
        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }
}
