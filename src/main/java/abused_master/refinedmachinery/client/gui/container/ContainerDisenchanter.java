package abused_master.refinedmachinery.client.gui.container;

import abused_master.abusedlib.client.gui.OutputSlot;
import abused_master.abusedlib.utils.InventoryHelper;
import net.minecraft.container.Container;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerDisenchanter extends Container {

    public final Inventory inventory;
    public final PlayerInventory playerInventory;
    public final World world;

    public ContainerDisenchanter(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(null, syncId);
        this.inventory = inventory;
        this.playerInventory = playerInventory;
        this.world = playerInventory.player.world;

        //Input Slot
        this.addSlot(new Slot(inventory, 0, 80, 24));
        this.addSlot(new Slot(inventory, 1, 134, 55));

        //Output Slots
        for (int slot = 2; slot < 7; slot++) {
            this.addSlot(new OutputSlot(inventory, slot, slot * 18, 55));
        }

        //Vanilla Slots
        int i;
        for(i = 0; i < 3; ++i) {
            for(int var4 = 0; var4 < 9; ++var4) {
                this.addSlot(new Slot(playerInventory, var4 + i * 9 + 9, 8 + var4 * 18, 84 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canUse(PlayerEntity playerEntity) {
        return this.inventory.canPlayerUseInv(playerEntity);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int int_1) {
        return InventoryHelper.handleShiftClick(this, player, int_1);
    }
}
