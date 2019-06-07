package abused_master.refinedmachinery.items.tools;

import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.RefinedMachineryClient;
import abused_master.refinedmachinery.utils.ItemHelper;
import nerdhub.cardinal.components.api.ItemComponentProvider;
import nerdhub.cardinal.components.api.accessor.StackComponentAccessor;
import nerdhub.cardinalenergy.DefaultTypes;
import nerdhub.cardinalenergy.api.IEnergyItemHandler;
import nerdhub.cardinalenergy.api.IEnergyStorage;
import nerdhub.cardinalenergy.impl.ItemEnergyStorage;
import net.minecraft.ChatFormat;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemEnergizedSword extends SwordItem implements IEnergyItemHandler, ItemComponentProvider {

    public int attackHeartCost = 50;

    public ItemEnergizedSword() {
        super(CustomToolMaterials.ENERGIZED_SWORD, 12, 0, new Settings().maxCount(1).group(RefinedMachineryClient.modItemGroup).maxDamage(25000));
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity livingEntity, LivingEntity damagedEntity) {
        int damageAmount = (int) (damagedEntity.getHealthMaximum() - damagedEntity.getHealth());
        int energyUsage = (damageAmount == 0 ? (int) damagedEntity.getHealthMaximum() : damageAmount) * attackHeartCost;
        IEnergyStorage storage = ((StackComponentAccessor) (Object) stack).getComponent(DefaultTypes.CARDINAL_ENERGY);

        if (storage != null && storage.getEnergyStored() >= energyUsage) {
            storage.extractEnergy(energyUsage);
            ItemHelper.updateItemDurability(stack, storage);
            return true;
        }

        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Component> list, TooltipContext tooltipOptions) {
        IEnergyStorage storage = ((StackComponentAccessor) (Object) stack).getComponent(DefaultTypes.CARDINAL_ENERGY);
        if(storage != null) {
            list.add(new TextComponent("Energy: " + storage.getEnergyStored() + " / " + storage.getCapacity() + " CE").setStyle(new Style().setColor(ChatFormat.GOLD)));
        }
    }

    @Override
    public void createComponents(ItemStack stack) {
        addComponent(stack, DefaultTypes.CARDINAL_ENERGY, new ItemEnergyStorage(25000));
    }
}
