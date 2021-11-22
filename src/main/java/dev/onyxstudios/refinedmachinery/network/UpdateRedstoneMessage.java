package dev.onyxstudios.refinedmachinery.network;

import dev.onyxstudios.refinedmachinery.tileentity.TileEntityConfigurable;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateRedstoneMessage {

    private BlockPos machinePos;
    private boolean requiresRedstone;

    public UpdateRedstoneMessage(BlockPos machinePos, boolean requiresRedstone) {
        this.machinePos = machinePos;
        this.requiresRedstone = requiresRedstone;
    }

    public UpdateRedstoneMessage() {
        this.requiresRedstone = false;
        this.machinePos = new BlockPos(0, 0, 0);
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeBlockPos(machinePos);
        buffer.writeBoolean(requiresRedstone);
    }

    public static void handleMessage(UpdateRedstoneMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            World world = ctx.get().getSender().world;

            if (world.isBlockLoaded(message.machinePos) && world.getTileEntity(message.machinePos) instanceof TileEntityConfigurable) {
                TileEntityConfigurable tile = (TileEntityConfigurable) world.getTileEntity(message.machinePos);
                tile.setRequiresRedstone(message.requiresRedstone);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static UpdateRedstoneMessage decode(PacketBuffer buffer) {
        return new UpdateRedstoneMessage(buffer.readBlockPos(), buffer.readBoolean());
    }
}
