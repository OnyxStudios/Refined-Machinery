package abused_master.refinedmachinery.utils;

import abused_master.refinedmachinery.registry.ModPackets;
import io.netty.buffer.Unpooled;
import nerdhub.cardinal.components.api.component.BlockComponentProvider;
import nerdhub.cardinalenergy.DefaultTypes;
import nerdhub.cardinalenergy.api.IEnergyHandler;
import nerdhub.cardinalenergy.api.IEnergyStorage;
import nerdhub.cardinalenergy.impl.EnergyStorage;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Optional;

public class EnergyHelper {

    public static void sendEnergyToNeighbors(EnergyStorage storage, World world, BlockPos pos, int sendAmount) {
        if(!world.isClient && storage.getEnergyStored() >= sendAmount) {
            for (Direction direction : Direction.values()) {
                BlockPos offsetPos = pos.offset(direction);
                sendEnergy(pos, storage, world, offsetPos, sendAmount);
            }
        }
    }

    public static void sendEnergy(BlockPos sendingPos, EnergyStorage storage, World world, BlockPos receiver, int sendAmount) {
        if(!world.isClient && storage.getEnergyStored() >= sendAmount) {
            BlockComponentProvider componentProvider = (BlockComponentProvider) world.getBlockState(receiver).getBlock();
            if(world.getBlockEntity(receiver) instanceof IEnergyHandler && ((IEnergyHandler) world.getBlockEntity(receiver)).isEnergyReceiver(null, DefaultTypes.CARDINAL_ENERGY) && componentProvider.hasComponent(world, receiver, DefaultTypes.CARDINAL_ENERGY, null)) {
                int energySent = storage.sendEnergy(world, receiver, sendAmount);

                if(energySent > 0) {
                    sendUpdatePacket(world, sendingPos, receiver, energySent);
                }
                world.updateListeners(sendingPos, world.getBlockState(sendingPos), world.getBlockState(sendingPos), 3);
                world.updateListeners(receiver, world.getBlockState(receiver), world.getBlockState(receiver), 3);
            }
        }
    }

    public static void sendUpdatePacket(World world, BlockPos sendingPos, BlockPos receiver, int energySent) {
        for (PlayerEntity playerEntity : world.getPlayers()) {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeBlockPos(sendingPos);
            buf.writeBlockPos(receiver);
            buf.writeInt(energySent);
            ((ServerPlayerEntity) playerEntity).networkHandler.sendPacket(new CustomPayloadS2CPacket(ModPackets.PACKET_UPDATE_CLIENT_ENERGY, buf));
        }
    }

    public static IEnergyStorage getEnergyStorage(ItemStack stack) {
        Optional<IEnergyStorage> optional = DefaultTypes.CARDINAL_ENERGY.maybeGet(stack);

        if(optional.isPresent()) {
            return optional.get();
        }

        return null;
    }
}
