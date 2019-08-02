package abused_master.refinedmachinery.items.tools;

import abused_master.refinedmachinery.items.EnumResourceItems;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public enum CustomToolMaterials implements ToolMaterial {
    ENERGIZED_SWORD(3, -1, 8.0f, 12.0f, 22, Ingredient.ofItems(EnumResourceItems.ENERGIZED_STEEL_INGOT.getItemIngot()));

    private int miningLevel;
    private int durability;
    private float blockBreakSpeed;
    private float attackDamage;
    private int enchantability;
    private Ingredient repairIngredient;

    CustomToolMaterials(int miningLevel, int durability, float blockBreakSpeed, float attackDamage, int enchantability, Ingredient repairIngredient) {
        this.miningLevel = miningLevel;
        this.durability = durability;
        this.blockBreakSpeed = blockBreakSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurability() {
        return this.durability;
    }

    @Override
    public float getMiningSpeed() {
        return this.blockBreakSpeed;
    }

    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public int getMiningLevel() {
        return this.miningLevel;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient;
    }
}
