package dev.onyxstudios.refinedmachinery.tileentity.generators;

import dev.onyxstudios.refinedmachinery.blocks.generators.CoalGeneratorBlock;
import dev.onyxstudios.refinedmachinery.client.container.CoalGenContainer;
import dev.onyxstudios.refinedmachinery.registry.ModBlocks;
import dev.onyxstudios.refinedmachinery.registry.ModEntities;
import dev.onyxstudios.refinedmachinery.tileentity.TileEntityConfigurable;
import dev.onyxstudios.refinedmachinery.utils.EnergyUtils;
import dev.onyxstudios.refinedmachinery.utils.RMEnergyStorage;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class TileEntityCoalGen extends TileEntityConfigurable implements ITickableTileEntity, INamedContainerProvider {

    public RMEnergyStorage storage;

    public boolean isBurning = false;
    public int maxBurnTime = 0;
    public int burnTime = 0;

    public TileEntityCoalGen() {
        super(ModEntities.coalGenTileType.get(), 1, true);
        storage = new RMEnergyStorage(100000, ModBlocks.coalGenObject.get().maxExtract);
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        storage.read(compound);
        this.isBurning = compound.getBoolean("isBurning");
        this.maxBurnTime = compound.getInt("maxBurnTime");
        this.burnTime = compound.getInt("burnTime");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        storage.write(compound);
        compound.putBoolean("isBurning", this.isBurning);
        compound.putInt("maxBurnTime", this.maxBurnTime);
        compound.putInt("burnTime", this.burnTime);
        return compound;
    }

    @Override
    public void tick() {
        if(world.isRemote || (isRedstoneRequired() && !world.isBlockPowered(getPos())))
            return;

        if (isBurning) {
            burnTime--;

            if (burnTime <= 0) {
                burnTime = 0;
                maxBurnTime = 0;
                isBurning = false;

                world.setBlockState(pos, world.getBlockState(pos).with(CoalGeneratorBlock.IS_BURNING, false));
            }

            storage.receiveEnergy(ModBlocks.coalGenObject.get().productionAmount, false);
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        } else if (canBurn()) {
            this.maxBurnTime = ForgeHooks.getBurnTime(inventory.getStackInSlot(0)) / 2;
            this.burnTime = maxBurnTime;
            inventory.extractItem(0, 1, false);
            this.isBurning = true;

            world.setBlockState(pos, world.getBlockState(pos).with(CoalGeneratorBlock.IS_BURNING, true));
        }

        EnergyUtils.transferEnergy(world, pos, storage);
    }

    public boolean canBurn() {
        ItemStack stack = inventory.getStackInSlot(0);
        return !stack.isEmpty() && ForgeHooks.getBurnTime(stack) > 0 && storage.getEnergyStored() < storage.getMaxEnergyStored();
    }

    @Override
    public LazyOptional<?> getEnergyCapability() {
        return LazyOptional.of(() -> storage);
    }

    @Override
    public ITextComponent getDisplayName() {
        return this.getBlockState().getBlock().getTranslatedName();
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new CoalGenContainer(id, playerInventory, this);
    }
}
