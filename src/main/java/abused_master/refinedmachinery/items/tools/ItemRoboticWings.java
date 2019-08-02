package abused_master.refinedmachinery.items.tools;

import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.utils.EnergyHelper;
import abused_master.refinedmachinery.utils.ItemHelper;
import nerdhub.cardinal.components.api.event.ItemComponentCallback;
import nerdhub.cardinalenergy.DefaultTypes;
import nerdhub.cardinalenergy.api.IEnergyItemHandler;
import nerdhub.cardinalenergy.api.IEnergyStorage;
import nerdhub.cardinalenergy.impl.EnergyStorage;
import nerdhub.cardinalenergy.impl.ItemEnergyStorage;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRoboticWings extends ArmorItem implements IEnergyItemHandler {

    public int usePerTick = 25;

    public ItemRoboticWings() {
        super(CustomArmorMaterials.ROBOTIC_WINGS_MATERIAL, EquipmentSlot.CHEST, new Settings().maxCount(1).group(RefinedMachinery.modItemGroup));
        ItemComponentCallback.event(this).register((stack, components) -> components.put(DefaultTypes.CARDINAL_ENERGY, new EnergyStorage(1000000)));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int int_1, boolean boolean_1) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (player.isCreative()) {
                if(!player.abilities.allowFlying)
                    player.abilities.allowFlying = true;

                return;
            }

            if (player.getEquippedStack(EquipmentSlot.CHEST) == stack) {
                IEnergyStorage storage = EnergyHelper.getEnergyStorage(stack);

                if (storage != null && storage.getEnergyStored() >= usePerTick) {
                    if (!player.abilities.allowFlying) {
                        player.abilities.allowFlying = true;
                    }

                    if (player.abilities.flying) {
                        storage.extractEnergy(usePerTick);
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
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> list, TooltipContext tooltipOptions) {
        IEnergyStorage storage = EnergyHelper.getEnergyStorage(stack);

        if (storage != null) {
            list.add(new LiteralText("Energy: " + storage.getEnergyStored() + " / " + storage.getCapacity() + " CE").setStyle(new Style().setColor(Formatting.GOLD)));
        }
    }
}
