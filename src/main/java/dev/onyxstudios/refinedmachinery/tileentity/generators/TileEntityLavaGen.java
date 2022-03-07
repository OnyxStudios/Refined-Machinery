package dev.onyxstudios.refinedmachinery.tileentity.generators;

import dev.onyxstudios.refinedmachinery.client.container.LavaGenContainer;
import dev.onyxstudios.refinedmachinery.registry.ModBlocks;
import dev.onyxstudios.refinedmachinery.registry.ModEntities;
import dev.onyxstudios.refinedmachinery.tileentity.TileEntityConfigurable;
import dev.onyxstudios.refinedmachinery.utils.RMEnergyStorage;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nullable;

public class TileEntityLavaGen extends TileEntityConfigurable implements ITickableTileEntity, INamedContainerProvider {

    public RMEnergyStorage storage;
    public FluidTank tank;

    public TileEntityLavaGen() {
        super(ModEntities.lavaGenTileType.get(), 1, true);
        storage = new RMEnergyStorage(100000, ModBlocks.lavaGenObject.get().maxExtract);
        tank = new FluidTank(10000);
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        storage.read(compound);
        tank.readFromNBT(compound.getCompound("tank"));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        storage.write(compound);
        compound.put("tank", tank.writeToNBT(new CompoundNBT()));

        return compound;
    }

    @Override
    public void tick() {
        if(world.isRemote || (isRedstoneRequired() && !world.isBlockPowered(getPos())))
            return;
    }

    @Override
    public LazyOptional<?> getEnergyCapability() {
        return LazyOptional.of(() -> storage);
    }

    @Override
    public LazyOptional<?> getFluidCapability() {
        return LazyOptional.of(() -> tank);
    }

    @Override
    public ITextComponent getDisplayName() {
        return this.getBlockState().getBlock().getTranslatedName();
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new LavaGenContainer(id, playerInventory, this);
    }
}
