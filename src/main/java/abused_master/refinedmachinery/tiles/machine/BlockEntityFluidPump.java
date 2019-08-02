package abused_master.refinedmachinery.tiles.machine;

import abused_master.abusedlib.client.render.hud.IHudSupport;
import abused_master.abusedlib.fluid.FluidStack;
import abused_master.abusedlib.fluid.FluidContainer;
import abused_master.abusedlib.fluid.IFluidHandler;
import abused_master.abusedlib.tiles.BlockEntityBase;
import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.registry.ModBlockEntities;
import abused_master.refinedmachinery.registry.ModPackets;
import abused_master.refinedmachinery.utils.ItemHelper;
import abused_master.refinedmachinery.utils.linker.ILinkerHandler;
import io.netty.buffer.Unpooled;
import nerdhub.cardinalenergy.api.IEnergyHandler;
import nerdhub.cardinalenergy.impl.EnergyStorage;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.TagHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BlockEntityFluidPump extends BlockEntityBase implements IEnergyHandler, IFluidHandler, IHudSupport, ILinkerHandler {

    public EnergyStorage storage = new EnergyStorage(50000);
    public FluidContainer tank = new FluidContainer(32000);
    public List<BlockPos> cachedDrainingPos = new ArrayList<>();
    public int pumpRage = RefinedMachinery.config.getInt("pumpRange");
    public BlockPos drainingPos = null;
    public int drainPerBlock = RefinedMachinery.config.getInt("pumpCostPerBlock");
    public int drainingSpeed = 0;

    public BlockEntityFluidPump() {
        super(ModBlockEntities.FLUID_PUMP);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        storage.fromTag(tag);

        if(this.tank != null) {
            this.tank.setBlockEntity(this);
            if(this.tank.getFluidStack() != null) {
                this.tank.readFromNBT(tag);
            }

            if (tag.containsKey("FluidData")) {
                this.tank.setFluidStack(FluidStack.fluidFromTag(tag.getCompound("FluidData")));
            }
        }

        if(tag.containsKey("cachedDrainingPos")) {
            this.cachedDrainingPos.clear();
            ListTag listTag = tag.getList("cachedDrainingPos", NbtType.COMPOUND);

            for (Iterator<Tag> it = listTag.iterator(); it.hasNext(); ) {
                CompoundTag compoundTag = (CompoundTag) it.next();
                cachedDrainingPos.add(TagHelper.deserializeBlockPos(compoundTag));
            }
        }

        if(tag.containsKey("drainingPos")) {
            this.drainingPos = TagHelper.deserializeBlockPos(tag.getCompound("drainingPos"));
        }

        this.drainingSpeed = tag.getInt("drainingSpeed");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        storage.toTag(tag);

        if (this.tank != null && this.tank.getFluidStack() != null) {
            CompoundTag tankTag = new CompoundTag();
            this.tank.getFluidStack().toTag(tankTag);
            tag.put("FluidData", tankTag);
            this.tank.writeToNBT(tag);
        }

        if(!this.cachedDrainingPos.isEmpty()) {
            ListTag listTag = new ListTag();

            for (BlockPos areaPos : cachedDrainingPos) {
                listTag.add(TagHelper.serializeBlockPos(areaPos));
            }

            tag.put("cachedDrainingPos", listTag);
        }


        if(drainingPos != null) {
            tag.put("drainingPos", TagHelper.serializeBlockPos(drainingPos));
        }

        tag.putInt("drainingSpeed", this.drainingSpeed);

        return tag;
    }

    @Override
    public void tick() {
        if (!world.isReceivingRedstonePower(pos) && canRun()) {
            drainingSpeed++;
            if (drainingSpeed >= 20) {
                run();
            }
        }

        sendFluid();
    }

    public boolean canRun() {
        if(storage.getEnergyStored() < drainPerBlock || (tank.getFluidCapacity() - tank.getFluidAmount()) < 1000) {
            return false;
        }

        return true;
    }

    public void run() {
        if (!cachedDrainingPos.isEmpty()) {
            BlockPos pos = null;

            for (Iterator<BlockPos> it = cachedDrainingPos.iterator(); it.hasNext(); ) {
                BlockPos pos2 = it.next();
                if (world.isAir(pos2) || world.getFluidState(pos2) == null || !(world.getBlockState(pos2).getBlock() instanceof FluidBlock) || world.isHeightInvalid(pos2)) {
                    it.remove();
                    continue;
                }

                pos = pos2;
                drainingPos = pos2;
                break;
            }

            if(pos != null) {
                if(!world.isClient) {
                    boolean filled = tank.fillFluid(new FluidStack(world.getFluidState(pos).getFluid(), 1000));
                    if (filled) {
                        world.setBlockState(pos, Blocks.STONE.getDefaultState());
                        storage.extractEnergy(drainingSpeed);
                        drainingSpeed = 0;

                        for (PlayerEntity playerEntity : world.getPlayers()) {
                            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) playerEntity;
                            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                            buf.writeBlockPos(getPos());
                            buf.writeCompoundTag(tank.getFluidStack().toTag(new CompoundTag()));

                            PacketByteBuf energyBuf = new PacketByteBuf(Unpooled.buffer());
                            energyBuf.writeBlockPos(getPos());
                            energyBuf.writeInt(storage.getEnergyStored());

                            serverPlayerEntity.networkHandler.sendPacket(new CustomPayloadS2CPacket(ModPackets.PACKET_UPDATE_CLIENT_FLUID, buf));
                            serverPlayerEntity.networkHandler.sendPacket(new CustomPayloadS2CPacket(ModPackets.PACKET_UPDATE_CLIENT_ENERGY, energyBuf));
                        }
                    }
                }

                cachedDrainingPos.remove(pos);
            }

            this.updateEntity();
        }
    }

    public void sendFluid() {
        if(tank.getFluidAmount() >= 250) {
            for (Direction direction : Direction.values()) {
                BlockPos offsetPos = pos.offset(direction);
                if (world.getBlockEntity(offsetPos) instanceof IFluidHandler) {
                    IFluidHandler fluidHandler = (IFluidHandler) world.getBlockEntity(offsetPos);
                    if((fluidHandler.getFluidTank().getFluidAmount() + 250) <= fluidHandler.getFluidTank().getFluidCapacity()) {
                        boolean filled = fluidHandler.getFluidTank().fillFluid(new FluidStack(tank.getFluidStack().getFluid(), 250));
                        if(filled) {
                            fluidHandler.getFluidTank().fillFluid(new FluidStack(tank.getFluidStack().getFluid(), 250));
                            tank.extractFluid(250);
                        }

                        world.updateNeighbors(getPos(), this.getCachedState().getBlock());
                        this.updateEntity();
                    }
                }
            }
        }
    }

    public void cacheDrainingArea() {
        Iterable<BlockPos> drainArea = BlockPos.iterate(new BlockPos(pos.getX() - pumpRage, pos.getY() - pumpRage, pos.getZ() - pumpRage), new BlockPos(pos.getX() + pumpRage, pos.getY() + pumpRage, pos.getZ() + pumpRage));
        for (BlockPos pos : BlockEntityQuarry.listBlocksInIterable(drainArea)) {
            if (world.isAir(pos) || world.getFluidState(pos) == null || !(world.getBlockState(pos).getBlock() instanceof FluidBlock) || world.isHeightInvalid(pos)) {
                continue;
            }

            this.cachedDrainingPos.add(pos.toImmutable());
        }

        this.updateEntity();
    }

    @Override
    public FluidContainer getFluidTank() {
        return tank;
    }

    @Override
    public Direction getBlockOrientation() {
        return null;
    }

    @Override
    public boolean isBlockAboveAir() {
        return getWorld().isAir(pos.up());
    }

    @Override
    public BlockPos getBlockPos() {
        return getPos();
    }

    @Override
    public List<String> getClientLog() {
        List<String> toDisplay = new ArrayList<>();
        toDisplay.add((tank.getFluidStack() != null ? I18n.translate(tank.getFluidStack().getFluid().getDefaultState().getBlockState().getBlock().getTranslationKey()) : "Empty") + ": " + tank.getFluidAmount() + " / " + tank.getFluidCapacity() + " MB");
        toDisplay.add("Energy: " + storage.getEnergyStored() + " / " + storage.getCapacity() + " CE");
        return toDisplay;
    }

    @Override
    public void link(PlayerEntity player, CompoundTag tag) {
        ItemHelper.linkBlockPos(world, pos, player, tag);
    }
}
