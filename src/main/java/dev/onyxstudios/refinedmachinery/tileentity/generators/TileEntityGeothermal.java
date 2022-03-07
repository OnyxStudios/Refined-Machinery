package dev.onyxstudios.refinedmachinery.tileentity.generators;

import dev.onyxstudios.refinedmachinery.registry.ModBlocks;
import dev.onyxstudios.refinedmachinery.registry.ModEntities;
import dev.onyxstudios.refinedmachinery.tileentity.TileEntityBase;
import dev.onyxstudios.refinedmachinery.utils.EnergyUtils;
import dev.onyxstudios.refinedmachinery.utils.RMEnergyStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;

public class TileEntityGeothermal extends TileEntityBase implements ITickableTileEntity {

    public RMEnergyStorage storage;
    public int cooldown = 0;

    public TileEntityGeothermal() {
        super(ModEntities.geothermalTileType.get());
        storage = new RMEnergyStorage(100000, ModBlocks.geothermalGenObject.get().maxExtract);
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        storage.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        storage.write(compound);
        return compound;
    }

    @Override
    public void tick() {
        if (world.isRemote())
            return;

        cooldown++;
        if (cooldown > 4) {
            int genAmount = getGenAmount();
            storage.receiveEnergy(genAmount, false);
            cooldown = 0;
        }

        EnergyUtils.transferEnergy(world, pos, storage);
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
    }

    public int getGenAmount() {
        int totalGen = 0;
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.offset(direction);

            if(world.getBlockState(neighborPos).isIn(Blocks.LAVA)) {
                totalGen += ModBlocks.geothermalGenObject.get().productionAmount;
            }
        }

        return totalGen;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return LazyOptional.of(() -> storage).cast();
        }

        return super.getCapability(cap, side);
    }
}
