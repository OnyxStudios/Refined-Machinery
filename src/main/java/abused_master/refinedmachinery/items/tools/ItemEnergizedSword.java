package abused_master.refinedmachinery.items.tools;

import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.utils.EnergyHelper;
import abused_master.refinedmachinery.utils.ItemHelper;
import nerdhub.cardinal.components.api.event.ItemComponentCallback;
import nerdhub.cardinalenergy.DefaultTypes;
import nerdhub.cardinalenergy.api.IEnergyItemHandler;
import nerdhub.cardinalenergy.api.IEnergyStorage;
import nerdhub.cardinalenergy.impl.EnergyStorage;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemEnergizedSword extends SwordItem implements IEnergyItemHandler {

    public int attackHeartCost = 50;

    public ItemEnergizedSword() {
        super(CustomToolMaterials.ENERGIZED_SWORD, 12, 0, new Settings().maxCount(1).group(RefinedMachinery.modItemGroup).maxDamage(25000));
        ItemComponentCallback.event(this).register((stack, components) -> components.put(DefaultTypes.CARDINAL_ENERGY, new EnergyStorage(25000)));
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity livingEntity, LivingEntity damagedEntity) {
        int damageAmount = (int) (damagedEntity.getHealthMaximum() - damagedEntity.getHealth());
        int energyUsage = (damageAmount == 0 ? (int) damagedEntity.getHealthMaximum() : damageAmount) * attackHeartCost;
        IEnergyStorage storage = EnergyHelper.getEnergyStorage(stack);

        if (storage != null && storage.getEnergyStored() >= energyUsage) {
            storage.extractEnergy(energyUsage);
            ItemHelper.updateItemDurability(stack, storage);
            return true;
        }

        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> list, TooltipContext tooltipOptions) {
        IEnergyStorage storage = EnergyHelper.getEnergyStorage(stack);

        if(storage != null) {
            list.add(new LiteralText("Energy: " + storage.getEnergyStored() + " / " + storage.getCapacity() + " CE").setStyle(new Style().setColor(Formatting.GOLD)));
        }
    }
}
