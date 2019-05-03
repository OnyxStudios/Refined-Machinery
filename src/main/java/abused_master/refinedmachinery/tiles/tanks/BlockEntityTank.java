package abused_master.refinedmachinery.tiles.tanks;

import abused_master.abusedlib.client.render.hud.IHudSupport;
import abused_master.abusedlib.fluid.FluidContainer;
import abused_master.abusedlib.fluid.FluidStack;
import abused_master.abusedlib.fluid.IFluidHandler;
import abused_master.abusedlib.tiles.BlockEntityBase;
import abused_master.refinedmachinery.blocks.tanks.EnumTankTypes;
import abused_master.refinedmachinery.registry.ModBlockEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Arrays;
import java.util.List;

public class BlockEntityTank extends BlockEntityBase implements IHudSupport, IFluidHandler {

    public EnumTankTypes type;
    public FluidContainer tank;

    public BlockEntityTank() {
        super(ModBlockEntities.TANK);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putInt("type", this.type.ordinal());

        if (this.tank != null && this.tank.getFluidStack() != null) {
            CompoundTag tankTag = new CompoundTag();
            this.tank.getFluidStack().toTag(tankTag);
            tag.put("FluidData", tankTag);
            this.tank.writeToNBT(tag);
        }

        return tag;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        this.type = EnumTankTypes.values()[tag.getInt("type")];

        this.tank = new FluidContainer(type.getStorageCapacity());
        this.tank.setBlockEntity(this);
        if (this.tank.getFluidStack() != null) {
            this.tank.readFromNBT(tag);
        }

        if (tag.containsKey("FluidData")) {
            this.tank.setFluidStack(FluidStack.fluidFromTag(tag.getCompound("FluidData")));
        }
    }

    public void setType(EnumTankTypes type) {
        this.type = type;
        if(tank != null && tank.getFluidAmount() > 0) {
            int stored = tank.getFluidAmount();
            this.tank = new FluidContainer(tank.getFluidStack(), type.getStorageCapacity(), this);
            tank.setFluidStack(new FluidStack(tank.getFluidStack().getFluid(), stored));
        }else {
            this.tank = new FluidContainer(type.getStorageCapacity());
        }
        this.markDirty();
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
    public List<String> getClientLog() {
        return Arrays.asList(tank.getFluidAmount() + " / " + tank.getFluidCapacity() + " MB");
    }

    @Override
    public BlockPos getBlockPos() {
        return getPos();
    }

    @Override
    public FluidContainer getFluidTank() {
        return tank;
    }
}
