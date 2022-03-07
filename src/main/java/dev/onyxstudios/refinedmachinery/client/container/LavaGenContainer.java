package dev.onyxstudios.refinedmachinery.client.container;

import dev.onyxstudios.refinedmachinery.registry.ModEntities;
import dev.onyxstudios.refinedmachinery.tileentity.generators.TileEntityLavaGen;
import dev.onyxstudios.refinedmachinery.utils.InventoryUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class LavaGenContainer extends BaseMachineContainer {

    public LavaGenContainer(int id, PlayerInventory playerInventory, TileEntityLavaGen lavaGen) {
        super(ModEntities.lavaGenContainerType.get(), id, playerInventory, lavaGen);
        this.addSlot(new LavaSlot(lavaGen.inventory, 0, 80, 35));
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        return InventoryUtils.handleShiftClick(this, playerIn, index);
    }

    private static class LavaSlot extends SlotItemHandler {

        public LavaSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return stack.isItemEqual(Items.LAVA_BUCKET.getDefaultInstance());
        }
    }
}
