package abused_master.refinedmachinery.items;

import abused_master.abusedlib.items.ItemBase;
import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.RefinedMachineryClient;
import net.minecraft.item.ItemStack;

public enum EnumResourceItems {
    COPPER_INGOT,
    TIN_INGOT,
    LEAD_INGOT,
    SILVER_INGOT,
    NICKEL_INGOT,
    COAL_DUST,
    DIAMOND_DUST,
    IRON_DUST,
    GOLD_DUST,
    COPPER_DUST,
    TIN_DUST,
    LEAD_DUST,
    SILVER_DUST,
    NICKEL_DUST,
    ENERGIZED_STEEL_INGOT(true);

    private ItemBase itemResource;

    EnumResourceItems() {
        this.itemResource = new ItemBase(getName(), RefinedMachineryClient.modItemGroup);
    }

    EnumResourceItems(boolean enchant) {
        this.itemResource = new ItemBase(getName(), RefinedMachineryClient.modItemGroup) {
            @Override
            public boolean hasEnchantmentGlint(ItemStack stack) {
                return enchant;
            }
        };
    }

    public String getName() {
        return this.toString().toLowerCase();
    }

    public ItemBase getItemIngot() {
        return itemResource;
    }
}
