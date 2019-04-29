package abused_master.refinedmachinery.items;

import abused_master.abusedlib.items.ItemBase;
import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.registry.ModItems;
import nerdhub.cardinalenergy.api.IEnergyItemHandler;
import nerdhub.cardinalenergy.impl.ItemEnergyStorage;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.TextComponent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSteelIngot extends ItemBase implements IEnergyItemHandler {

    public ItemEnergyStorage storage = new ItemEnergyStorage(500000);

    public ItemSteelIngot() {
        super("steel_ingot", new Settings().itemGroup(RefinedMachinery.modItemGroup).durability(500000));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack stack = playerEntity.getMainHandStack();
        if(playerEntity.isSneaking() && stack.getItem() == ModItems.STEEL_INGOT && storage.getEnergyStored(stack) == storage.getEnergyCapacity(stack)) {
            playerEntity.setStackInHand(hand, new ItemStack(EnumResourceItems.ENERGIZED_STEEL_INGOT.getItemIngot()));
        }

        return new TypedActionResult<>(ActionResult.SUCCESS, playerEntity.getMainHandStack());
    }

    @Override
    public void buildTooltip(ItemStack stack, @Nullable World world, List<TextComponent> list, TooltipContext context) {
        if(stack.hasTag() && stack.getTag().containsKey(ItemEnergyStorage.ENERGY_TAG)) {
            list.add(new StringTextComponent("Energy Stored: " + storage.getEnergyStored(stack) + " / " + storage.getEnergyCapacity(stack) + " CE"));
        }
    }

    @Override
    public ItemEnergyStorage getEnergyStorage() {
        return this.storage;
    }
}
