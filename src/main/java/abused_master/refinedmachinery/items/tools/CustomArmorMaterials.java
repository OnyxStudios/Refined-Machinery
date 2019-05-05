package abused_master.refinedmachinery.items.tools;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public enum CustomArmorMaterials implements ArmorMaterial {
    ROBOTIC_WINGS_MATERIAL("robotic_wings_material", new int[]{3, 6, 8, 3}, 1000000,0, SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA, 0, null);

    private String name;
    private int[] protectionAmounts;
    private int durability;
    private int enchantability;
    private SoundEvent equipSound;
    private float toughness;
    private Ingredient repairIngredient;

    CustomArmorMaterials(String name, int[] protectionAmount, int durability, int enchantability, SoundEvent equipSound, float toughness, Ingredient repairIngredient) {
        this.name = name;
        this.protectionAmounts = protectionAmount;
        this.durability = durability;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurability(EquipmentSlot var1) {
        return durability;
    }

    @Override
    public int getProtectionAmount(EquipmentSlot var1) {
        return protectionAmounts[var1.getEntitySlotId()];
    }

    @Override
    public int getEnchantability() {
        return enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public float getToughness() {
        return toughness;
    }
}
