package abused_master.refinedmachinery.items;

import abused_master.abusedlib.items.ItemBase;
import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.utils.linker.ILinkerHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TagHelper;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class ItemLinker extends ItemBase {

    public ItemLinker() {
        super("linker", new Settings().itemGroup(RefinedMachinery.modItemGroup).stackSize(1));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext usageContext) {
        World world = usageContext.getWorld();
        PlayerEntity player = usageContext.getPlayer();
        ItemStack stack = usageContext.getItemStack();

        CompoundTag tag = stack.getTag();
        if (player.isSneaking()) {
            if (tag == null) {
                tag = new CompoundTag();
            }

            BlockEntity blockEntity = world.getBlockEntity(usageContext.getBlockPos());
            if (blockEntity != null && blockEntity instanceof ILinkerHandler) {
                ((ILinkerHandler) blockEntity).link(player, tag);
            } else {
                player.addChatMessage(new TextComponent("The selected block is invalid for linking!"), true);
            }

            stack.setTag(tag);
        }


        return ActionResult.SUCCESS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if(player.isSneaking()) {
            clearTag(player.getStackInHand(hand));
            if(!world.isClient) {
                player.addChatMessage(new TextComponent("Cleared linker settings"), true);
            }
        }

        return super.use(world, player, hand);
    }

    public void clearTag(ItemStack stack) {
        stack.setTag(null);
    }

    @Override
    public void buildTooltip(ItemStack itemStack, World world, List<Component> list, TooltipContext tooltipOptions) {
        CompoundTag tag = itemStack.getTag();
        if(tag != null) {
            if(tag.containsKey("collectorPos")) {
                BlockPos pos = TagHelper.deserializeBlockPos(tag.getCompound("collectorPos"));
                list.add(new TextComponent("Collector Pos, x: " + pos.getX() + " y: " + pos.getY() + " z: " + pos.getZ()));
            }else if(tag.containsKey("blockPos")) {
                BlockPos pos = TagHelper.deserializeBlockPos(tag.getCompound("blockPos"));
                list.add(new TextComponent("BlockEntity Pos, x: " + pos.getX() + " y: " + pos.getY() + " z: " + pos.getZ()));
            }else if(tag.containsKey("itemPos")) {
                BlockPos pos = TagHelper.deserializeBlockPos(tag.getCompound("itemPos"));
                list.add(new TextComponent("Transfer Crystal, x: " + pos.getX() + " y: " + pos.getY() + " z: " + pos.getZ()));
            }
        }else {
            list.add(new TextComponent("No block positions have been saved."));
        }
    }
}
