package abused_master.refinedmachinery.items.tools;

import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.utils.ItemHelper;
import nerdhub.cardinalenergy.api.IEnergyItemHandler;
import nerdhub.cardinalenergy.impl.ItemEnergyStorage;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.Style;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TextFormat;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRoboticWings extends ArmorItem implements IEnergyItemHandler {

    public ItemEnergyStorage storage = new ItemEnergyStorage(1000000);
    public int usePerTick = 25;

    public ItemRoboticWings() {
        super(CustomArmorMaterials.ROBOTIC_WINGS_MATERIAL, EquipmentSlot.CHEST, new Settings().stackSize(1).itemGroup(RefinedMachinery.modItemGroup));
    }

    @Override
    public void onEntityTick(ItemStack stack, World world, Entity entity, int int_1, boolean boolean_1) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (player.isCreative()) {
                player.abilities.allowFlying = true;
                return;
            }
            if (player.getEquippedStack(EquipmentSlot.CHEST).getItem() == this) {

                if (storage.getEnergyStored(stack) >= usePerTick) {
                    if (!player.abilities.allowFlying) {
                        player.abilities.allowFlying = true;
                    }

                    if (player.abilities.flying) {
                        storage.extractEnergy(stack, usePerTick);
                        ItemHelper.updateItemDurability(stack, storage);
                    }
                } else {
                    if (player.abilities.flying) {
                        player.abilities.allowFlying = false;
                        player.abilities.flying = false;
                    }
                }
            }else {
                player.abilities.allowFlying = false;
                player.abilities.flying = false;
            }
        }
    }

    @Override
    public ItemEnergyStorage getEnergyStorage() {
        return this.storage;
    }

    @Override
    public void buildTooltip(ItemStack stack, @Nullable World world, List<TextComponent> list, TooltipContext tooltipOptions) {
        list.add(new StringTextComponent("Energy: " + storage.getEnergyStored(stack) + " / " + storage.getEnergyCapacity(stack) + " CE").setStyle(new Style().setColor(TextFormat.GOLD)));
    }
}
