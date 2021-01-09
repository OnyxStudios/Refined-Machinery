package dev.onyxstudios.refinedmachinery.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public abstract class TileEntityInventory extends TileEntityBase {

    public final ItemStackHandler inventory;
    public final LazyOptional<?> capabilityInventory;

    public TileEntityInventory(TileEntityType<?> tileEntityTypeIn, int slots) {
        super(tileEntityTypeIn);
        this.inventory = new ItemStackHandler(slots) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                TileEntityInventory.this.markDirty();
            }
        };

        this.capabilityInventory = LazyOptional.of(() -> inventory);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.put("items", inventory.serializeNBT());
        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        if(nbt.contains("items")) {
            inventory.deserializeNBT(nbt.getCompound("items"));
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (LazyOptional<T>) capabilityInventory;
        }

        return LazyOptional.empty();
    }
}
