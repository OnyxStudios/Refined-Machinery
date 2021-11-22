package dev.onyxstudios.refinedmachinery.network;

import dev.onyxstudios.refinedmachinery.tileentity.TileEntityConfigurable;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ConfigureMachineMessage {

    private final BlockPos machinePos;
    private final Direction direction;

    public ConfigureMachineMessage(BlockPos pos, Direction direction) {
        this.machinePos = pos;
        this.direction = direction;
    }

    public ConfigureMachineMessage() {
        this.machinePos = new BlockPos(0, 0, 0);
        this.direction = Direction.NORTH;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeBlockPos(machinePos);
        buffer.writeInt(direction.getIndex());
    }

    public static ConfigureMachineMessage decode(PacketBuffer buffer) {
        return new ConfigureMachineMessage(buffer.readBlockPos(), Direction.values()[buffer.readInt()]);
    }

    public static void handleMessage(ConfigureMachineMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            World world = ctx.get().getSender().world;

            if(world.isBlockLoaded(message.machinePos) && world.getTileEntity(message.machinePos) instanceof TileEntityConfigurable) {
                TileEntityConfigurable tile = (TileEntityConfigurable) world.getTileEntity(message.machinePos);
                tile.updateConfiguration(message.direction);
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
