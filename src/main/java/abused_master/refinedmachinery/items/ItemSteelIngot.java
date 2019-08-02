package abused_master.refinedmachinery.items;

import abused_master.abusedlib.items.ItemBase;
import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.registry.ModItems;
import abused_master.refinedmachinery.utils.EnergyHelper;
import nerdhub.cardinal.components.api.event.ItemComponentCallback;
import nerdhub.cardinalenergy.DefaultTypes;
import nerdhub.cardinalenergy.api.IEnergyItemHandler;
import nerdhub.cardinalenergy.api.IEnergyStorage;
import nerdhub.cardinalenergy.impl.EnergyStorage;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSteelIngot extends ItemBase implements IEnergyItemHandler {

    public ItemSteelIngot() {
        super("steel_ingot", new Settings().group(RefinedMachinery.modItemGroup));
        ItemComponentCallback.event(this).register((stack, components) -> components.put(DefaultTypes.CARDINAL_ENERGY, new EnergyStorage(500000)));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack stack = playerEntity.getMainHandStack();

        IEnergyStorage storage = EnergyHelper.getEnergyStorage(stack);

        if(storage != null) {
            if (playerEntity.isSneaking() && stack.getItem() == ModItems.STEEL_INGOT && storage.getEnergyStored() == storage.getCapacity()) {
                playerEntity.setStackInHand(hand, new ItemStack(EnumResourceItems.ENERGIZED_STEEL_INGOT.getItemIngot()));
            }
        }

        return new TypedActionResult<>(ActionResult.SUCCESS, playerEntity.getMainHandStack());
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int int_1, boolean boolean_1) {
        IEnergyStorage storage = EnergyHelper.getEnergyStorage(stack);

        if (stack.getCount() > 1 && storage.getEnergyStored() > 0 && storage.getCapacity() < (stack.getCount() * 500000)) {
            storage.setCapacity(stack.getCount() * 500000);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> list, TooltipContext context) {
        IEnergyStorage storage = EnergyHelper.getEnergyStorage(stack);

        if(storage != null) {
            list.add(new LiteralText("Energy Stored: " + storage.getEnergyStored() + " / " + storage.getCapacity() + " CE").setStyle(new Style().setColor(Formatting.GOLD)));
        }
    }
}
