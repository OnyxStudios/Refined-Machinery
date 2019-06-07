package abused_master.refinedmachinery.items;

import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.registry.ModItems;
import abused_master.refinedmachinery.utils.ItemBase;
import nerdhub.cardinal.components.api.ItemComponentProvider;
import nerdhub.cardinal.components.api.accessor.StackComponentAccessor;
import nerdhub.cardinalenergy.DefaultTypes;
import nerdhub.cardinalenergy.api.IEnergyItemHandler;
import nerdhub.cardinalenergy.api.IEnergyStorage;
import nerdhub.cardinalenergy.impl.ItemEnergyStorage;
import net.minecraft.ChatFormat;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSteelIngot extends ItemBase implements IEnergyItemHandler, ItemComponentProvider {

    public ItemSteelIngot() {
        super("steel_ingot", new Settings().group(RefinedMachinery.modItemGroup));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack stack = playerEntity.getMainHandStack();
        IEnergyStorage storage = ((StackComponentAccessor) (Object) stack).getComponent(DefaultTypes.CARDINAL_ENERGY);

        if(storage != null) {
            if (playerEntity.isSneaking() && stack.getItem() == ModItems.STEEL_INGOT && storage.getEnergyStored() == storage.getCapacity()) {
                playerEntity.setStackInHand(hand, new ItemStack(EnumResourceItems.ENERGIZED_STEEL_INGOT.getItemIngot()));
            }
        }

        return new TypedActionResult<>(ActionResult.SUCCESS, playerEntity.getMainHandStack());
    }

    public void usageTick(World world, LivingEntity entity, ItemStack stack, int int_1) {
        IEnergyStorage storage = ((StackComponentAccessor) (Object) stack).getComponent(DefaultTypes.CARDINAL_ENERGY);

        if(storage != null) {
            if (stack.getCount() > 1 && storage.getEnergyStored() > 0 && storage.getCapacity() < (stack.getCount() * 500000)) {
                storage.setCapacity(stack.getCount() * 500000);
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Component> list, TooltipContext context) {
        IEnergyStorage storage = ((StackComponentAccessor) (Object) stack).getComponent(DefaultTypes.CARDINAL_ENERGY);

        if(storage != null) {
            list.add(new TextComponent("Energy Stored: " + storage.getEnergyStored() + " / " + storage.getCapacity() + " CE").setStyle(new Style().setColor(ChatFormat.GOLD)));
        }
    }

    @Override
    public void createComponents(ItemStack stack) {
        addComponent(stack, DefaultTypes.CARDINAL_ENERGY, new ItemEnergyStorage(500000));
    }
}
