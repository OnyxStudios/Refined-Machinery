package dev.onyxstudios.refinedmachinery.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class InventoryUtils {

    public static void dropInventoryItems(World world, BlockPos pos, ItemStackHandler inventory) {
        for (int i = 0; i < inventory.getSlots(); i++) {
            InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(i));
        }
    }

    public static ItemStack handleShiftClick(Container container, PlayerEntity player, int slotIndex) {
        List<Slot> slots = container.inventorySlots;
        Slot sourceSlot = slots.get(slotIndex);
        ItemStack inputStack = sourceSlot.getStack();
        if (inputStack == ItemStack.EMPTY) {
            return ItemStack.EMPTY;
        }

        boolean sourceIsPlayer = sourceSlot.inventory == player.inventory;

        ItemStack copy = inputStack.copy();

        if (sourceIsPlayer) {
            if (!mergeStack(player.inventory, false, sourceSlot, slots, false)) {
                return ItemStack.EMPTY;
            } else {
                return copy;
            }
        } else {
            boolean isMachineOutput = !sourceSlot.canTakeStack(player);
            if (!mergeStack(player.inventory, true, sourceSlot, slots, !isMachineOutput)) {
                return ItemStack.EMPTY;
            } else {
                return copy;
            }
        }
    }

    private static boolean mergeStack(PlayerInventory playerInv, boolean mergeIntoPlayer, Slot sourceSlot, List<Slot> slots, boolean reverse) {
        ItemStack sourceStack = sourceSlot.getStack();

        int originalSize = sourceStack.getCount();

        int len = slots.size();
        int idx;

        if (sourceStack.isStackable()) {
            idx = reverse ? len - 1 : 0;

            while (sourceStack.getCount() > 0 && (reverse ? idx >= 0 : idx < len)) {
                Slot targetSlot = slots.get(idx);
                if ((targetSlot.inventory == playerInv) == mergeIntoPlayer) {
                    ItemStack target = targetSlot.getStack();
                    if (ItemStack.areItemsEqual(sourceStack, target)) {
                        int targetMax = Math.min(targetSlot.getSlotStackLimit(), target.getMaxStackSize());
                        int toTransfer = Math.min(sourceStack.getCount(), targetMax - target.getCount());
                        if (toTransfer > 0) {
                            target.grow(toTransfer);
                            sourceSlot.decrStackSize(toTransfer);
                            targetSlot.onSlotChanged();
                        }
                    }
                }

                if (reverse) {
                    idx--;
                } else {
                    idx++;
                }
            }
            if (sourceStack.getCount() == 0) {
                sourceSlot.putStack(ItemStack.EMPTY);
                return true;
            }
        }

        idx = reverse ? len - 1 : 0;
        while (reverse ? idx >= 0 : idx < len) {
            Slot targetSlot = slots.get(idx);
            if ((targetSlot.inventory == playerInv) == mergeIntoPlayer
                    && !targetSlot.getHasStack() && targetSlot.isItemValid(sourceStack)) {
                targetSlot.putStack(sourceStack);
                sourceSlot.putStack(ItemStack.EMPTY);
                return true;
            }

            if (reverse) {
                idx--;
            } else {
                idx++;
            }
        }

        if (sourceStack.getCount() != originalSize) {
            sourceSlot.onSlotChanged();
            return true;
        }
        return false;
    }
}
