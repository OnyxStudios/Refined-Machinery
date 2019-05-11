package abused_master.refinedmachinery.utils;

import abused_master.refinedmachinery.registry.ModPackets;
import io.netty.buffer.Unpooled;
import nerdhub.cardinalenergy.api.IEnergyHandler;
import nerdhub.cardinalenergy.impl.EnergyStorage;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class EnergyHelper {

    public static void sendEnergyToNeighbors(EnergyStorage storage, World world, BlockPos pos, int sendAmount) {
        if(!world.isClient && storage.getEnergyStored() >= sendAmount) {
            for (Direction direction : Direction.values()) {
                BlockPos offsetPos = pos.offset(direction);
                if(world.getBlockEntity(offsetPos) instanceof IEnergyHandler) {
                    int energySent = storage.sendEnergy(world, offsetPos, sendAmount);

                    if (energySent > 0) {
                        for (PlayerEntity playerEntity : world.getPlayers()) {
                            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                            buf.writeBlockPos(offsetPos);
                            buf.writeInt(((IEnergyHandler) world.getBlockEntity(offsetPos)).getEnergyStorage(null).getEnergyStored());
                            ((ServerPlayerEntity) playerEntity).networkHandler.sendPacket(new CustomPayloadS2CPacket(ModPackets.PACKET_UPDATE_CLIENT_ENERGY, buf));
                        }
                    }
                }
            }
        }
    }

    public static void sendEnergy(EnergyStorage storage, World world, BlockPos receiver, int sendAmount) {
        if(!world.isClient && storage.getEnergyStored() >= sendAmount) {
            if(world.getBlockEntity(receiver) instanceof IEnergyHandler) {
                int energySent = storage.sendEnergy(world, receiver, sendAmount);

                if(energySent > 0) {
                    for (PlayerEntity playerEntity : world.getPlayers()) {
                        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                        buf.writeBlockPos(receiver);
                        buf.writeInt(((IEnergyHandler) world.getBlockEntity(receiver)).getEnergyStorage(null).getEnergyStored());
                        ((ServerPlayerEntity) playerEntity).networkHandler.sendPacket(new CustomPayloadS2CPacket(ModPackets.PACKET_UPDATE_CLIENT_ENERGY, buf));
                    }
                }
            }
        }
    }
}
