package abused_master.refinedmachinery.registry;

import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.utils.ItemHelper;
import nerdhub.cardinalenergy.api.IEnergyHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ModPackets {

    public static final Identifier PACKET_UPDATE_CLIENT_ENERGY = new Identifier(RefinedMachinery.MODID, "packet_update_client_energy");
    public static final Identifier PACKET_LINK_ENERGY = new Identifier(RefinedMachinery.MODID, "packet_link_energy");

    public static void registerPackets() {
    }

    @Environment(EnvType.CLIENT)
    public static void registerClientPackets() {
        ClientSidePacketRegistry.INSTANCE.register(PACKET_UPDATE_CLIENT_ENERGY, ((context, buffer) -> {
            BlockPos pos = buffer.readBlockPos();
            int energy = buffer.readInt();

            if(context.getPlayer() != null && context.getPlayer().world != null) {
                PlayerEntity player = context.getPlayer();
                World world = player.world;
                context.getTaskQueue().execute(() -> {
                    if(world.getBlockEntity(pos) instanceof IEnergyHandler) {
                        IEnergyHandler energyHandler = (IEnergyHandler) world.getBlockEntity(pos);
                        energyHandler.getEnergyStorage(null).setEnergyStored(energy);
                        world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
                    }
                });
            }
        }));

        ClientSidePacketRegistry.INSTANCE.register(PACKET_LINK_ENERGY, (context, buffer) -> {
            BlockPos pos = buffer.readBlockPos();
            CompoundTag tag = buffer.readCompoundTag();

            if(context.getPlayer() != null && context.getPlayer().world != null) {
                PlayerEntity player = context.getPlayer();
                World world = player.world;

                context.getTaskQueue().execute(() -> ItemHelper.handleControllerLink(world, pos, player, tag));
            }
        });
    }
}
