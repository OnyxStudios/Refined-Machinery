package abused_master.refinedmachinery.items.tools;

import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.utils.ItemHelper;
import nerdhub.cardinal.components.api.ItemComponentProvider;
import nerdhub.cardinal.components.api.accessor.StackComponentAccessor;
import nerdhub.cardinalenergy.DefaultTypes;
import nerdhub.cardinalenergy.api.IEnergyItemHandler;
import nerdhub.cardinalenergy.api.IEnergyStorage;
import nerdhub.cardinalenergy.impl.ItemEnergyStorage;
import net.minecraft.ChatFormat;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRoboticWings extends ArmorItem implements IEnergyItemHandler, ItemComponentProvider {

    public int usePerTick = 25;

    public ItemRoboticWings() {
        super(CustomArmorMaterials.ROBOTIC_WINGS_MATERIAL, EquipmentSlot.CHEST, new Settings().stackSize(1).itemGroup(RefinedMachinery.modItemGroup));
    }

    @Override
    public void onEntityTick(ItemStack stack, World world, Entity entity, int int_1, boolean boolean_1) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (player.isCreative()) {
                if(!player.abilities.allowFlying)
                    player.abilities.allowFlying = true;

                return;
            }

            if (player.getEquippedStack(EquipmentSlot.CHEST) == stack) {
                IEnergyStorage storage = ((StackComponentAccessor) (Object) stack).getComponent(DefaultTypes.CARDINAL_ENERGY);
                if (storage.getEnergyStored() >= usePerTick) {
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
    public void buildTooltip(ItemStack stack, @Nullable World world, List<Component> list, TooltipContext tooltipOptions) {
        IEnergyStorage storage = ((StackComponentAccessor) (Object) stack).getComponent(DefaultTypes.CARDINAL_ENERGY);

        if (storage != null) {
            list.add(new TextComponent("Energy: " + storage.getEnergyStored() + " / " + storage.getCapacity() + " CE").setStyle(new Style().setColor(ChatFormat.GOLD)));
        }
    }

    @Override
    public void createComponents(ItemStack stack) {
        addComponent(stack, DefaultTypes.CARDINAL_ENERGY, new ItemEnergyStorage(1000000));
    }
}
