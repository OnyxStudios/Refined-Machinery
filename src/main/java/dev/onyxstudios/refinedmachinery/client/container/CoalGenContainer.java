package dev.onyxstudios.refinedmachinery.client.container;

import dev.onyxstudios.refinedmachinery.registry.ModEntities;
import dev.onyxstudios.refinedmachinery.tileentity.generators.TileEntityCoalGen;
import dev.onyxstudios.refinedmachinery.utils.InventoryUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class CoalGenContainer extends BaseMachineContainer {

    public CoalGenContainer(int id, PlayerInventory playerInv, TileEntityCoalGen coalGen) {
        super(ModEntities.coalGenContainerType.get(), id, playerInv, coalGen);
        this.addSlot(new CoalSlot(coalGen.inventory, 0, 80, 35));
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        return InventoryUtils.handleShiftClick(this, playerIn, index);
    }

    private static class CoalSlot extends SlotItemHandler {

        public CoalSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return ForgeHooks.getBurnTime(stack) > 0;
        }
    }
}
