package abused_master.refinedmachinery.registry;

import abused_master.abusedlib.fluid.FluidStack;
import abused_master.abusedlib.fluid.IFluidHandler;
import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.tiles.machine.BlockEntityQuarry;
import abused_master.refinedmachinery.utils.ItemHelper;
import nerdhub.cardinal.components.api.BlockComponentProvider;
import nerdhub.cardinal.components.api.accessor.StackComponentAccessor;
import nerdhub.cardinalenergy.DefaultTypes;
import nerdhub.cardinalenergy.api.IEnergyStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ModPackets {

    public static final Identifier PACKET_UPDATE_CLIENT_ENERGY = new Identifier(RefinedMachinery.MODID, "packet_update_client_energy");
    public static final Identifier PACKET_LINK_ENERGY = new Identifier(RefinedMachinery.MODID, "packet_link_energy");
    public static final Identifier PACKET_UPDATE_CLIENT_FLUID = new Identifier(RefinedMachinery.MODID, "packet_update_client_fluid");
    public static final Identifier PACKET_CHARGE_ITEM = new Identifier(RefinedMachinery.MODID, "packet_charge_item");
    public static final Identifier PACKET_HANDLE_QUARRY = new Identifier(RefinedMachinery.MODID, "packet_handle_quarry");

    public static void registerPackets() {
        ServerSidePacketRegistry.INSTANCE.register(PACKET_HANDLE_QUARRY, (context, buffer) -> {
            BlockPos pos = buffer.readBlockPos();
            int action = buffer.readInt();

            if(context.getPlayer() != null && context.getPlayer().world != null) {
                PlayerEntity player = context.getPlayer();
                World world = player.world;

                context.getTaskQueue().execute(() -> {
                    if(!(world.getBlockEntity(pos) instanceof BlockEntityQuarry)) return;

                    BlockEntityQuarry quarry = (BlockEntityQuarry) world.getBlockEntity(pos);

                    switch (action) {
                        case 0:
                            if(!quarry.isRunning() && quarry.blockPositionsActive()) {
                                quarry.setRunning(true);
                                world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
                            }
                            break;
                        case 1:
                            if(quarry.isRunning()) {
                                quarry.setRunning(false);
                                world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
                            }
                            break;
                        default:
                            break;
                    }
                });
            }
        });
    }

    @Environment(EnvType.CLIENT)
    public static void registerClientPackets() {
        ClientSidePacketRegistry.INSTANCE.register(PACKET_UPDATE_CLIENT_ENERGY, (context, buffer) -> {
            BlockPos sendingPos = buffer.readBlockPos();
            BlockPos offsetPos = buffer.readBlockPos();
            int energy = buffer.readInt();

            if(context.getPlayer() != null && context.getPlayer().world != null) {
                PlayerEntity player = context.getPlayer();
                World world = player.world;
                context.getTaskQueue().execute(() -> {
                    BlockComponentProvider sendingComponentProvider = (BlockComponentProvider) world.getBlockState(sendingPos).getBlock();
                    BlockComponentProvider componentProvider = (BlockComponentProvider) world.getBlockState(offsetPos).getBlock();

                    if(componentProvider.hasComponent(world, offsetPos, DefaultTypes.CARDINAL_ENERGY, null)) {
                        sendingComponentProvider.getComponent(world, sendingPos, DefaultTypes.CARDINAL_ENERGY, null).sendEnergy(world, offsetPos, energy);
                        world.updateListeners(offsetPos, world.getBlockState(offsetPos), world.getBlockState(offsetPos), 3);
                    }
                });
            }
        });

        ClientSidePacketRegistry.INSTANCE.register(PACKET_LINK_ENERGY, (context, buffer) -> {
            BlockPos pos = buffer.readBlockPos();
            CompoundTag tag = buffer.readCompoundTag();

            if(context.getPlayer() != null && context.getPlayer().world != null) {
                PlayerEntity player = context.getPlayer();
                World world = player.world;

                context.getTaskQueue().execute(() -> ItemHelper.handleControllerLink(world, pos, player, tag));
            }
        });

        ClientSidePacketRegistry.INSTANCE.register(PACKET_UPDATE_CLIENT_FLUID, ((context, buffer) -> {
            BlockPos pos = buffer.readBlockPos();
            FluidStack stack = FluidStack.fluidFromTag(buffer.readCompoundTag());

            if(context.getPlayer() != null && context.getPlayer().world != null) {
                PlayerEntity player = context.getPlayer();
                World world = player.world;
                context.getTaskQueue().execute(() -> {
                    if(world.getBlockEntity(pos) instanceof IFluidHandler) {
                        IFluidHandler fluidHandler = (IFluidHandler) world.getBlockEntity(pos);
                        fluidHandler.getFluidTank().fillFluid(stack);
                        world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
                    }
                });
            }
        }));

        ClientSidePacketRegistry.INSTANCE.register(PACKET_CHARGE_ITEM, (context, buf) -> {
            BlockPos pos = buf.readBlockPos();
            ItemStack stack = buf.readItemStack();

            if(context.getPlayer() != null && context.getPlayer().world != null) {
                PlayerEntity player = context.getPlayer();
                World world = player.world;
                context.getTaskQueue().execute(() -> {
                    Block block = world.getBlockState(pos).getBlock();
                    IEnergyStorage storage = ((BlockComponentProvider) block).getComponent(world, pos, DefaultTypes.CARDINAL_ENERGY, null);
                    IEnergyStorage energyItemStorage = ((StackComponentAccessor) (Object) stack).getComponent(DefaultTypes.CARDINAL_ENERGY);

                    storage.extractEnergy(energyItemStorage.receiveEnergy(50));

                    if (stack.isDamageable())
                        ItemHelper.updateItemDurability(stack, energyItemStorage);
                });
            }
        });
    }
}
