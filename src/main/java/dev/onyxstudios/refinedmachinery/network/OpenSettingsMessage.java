package dev.onyxstudios.refinedmachinery.network;

import dev.onyxstudios.refinedmachinery.client.container.BaseMachineContainer;
import dev.onyxstudios.refinedmachinery.tileentity.TileEntityConfigurable;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenSettingsMessage {

    private final BlockPos machinePos;
    private final boolean openSettings;

    public OpenSettingsMessage(BlockPos machinePos, boolean openSettings) {
        this.machinePos = machinePos;
        this.openSettings = openSettings;
    }

    public OpenSettingsMessage() {
        this.openSettings = false;
        this.machinePos = new BlockPos(0, 0, 0);
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeBlockPos(machinePos);
        buffer.writeBoolean(openSettings);
    }

    public static void handleMessage(OpenSettingsMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            World world = ctx.get().getSender().world;

            if (world.isBlockLoaded(message.machinePos) && world.getTileEntity(message.machinePos) instanceof TileEntityConfigurable) {
                TileEntityConfigurable tile = (TileEntityConfigurable) world.getTileEntity(message.machinePos);
                tile.setSettingsOpen(message.openSettings);

                Container container = ctx.get().getSender().openContainer;
                if (container instanceof BaseMachineContainer) {
                    ((BaseMachineContainer) container).isSettingsOpen = message.openSettings;
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static OpenSettingsMessage decode(PacketBuffer buffer) {
        return new OpenSettingsMessage(buffer.readBlockPos(), buffer.readBoolean());
    }
}
