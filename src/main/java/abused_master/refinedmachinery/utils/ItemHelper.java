package abused_master.refinedmachinery.utils;

import abused_master.refinedmachinery.tiles.transport.BlockEntityWirelessController;
import abused_master.refinedmachinery.tiles.transport.BlockEntityWirelessTransmitter;
import nerdhub.cardinalenergy.api.IEnergyHandler;
import nerdhub.cardinalenergy.impl.ItemEnergyStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.StringTextComponent;
import net.minecraft.util.TagHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemHelper {

    public static void updateItemDurability(ItemStack stack, ItemEnergyStorage storage) {
        stack.setDamage(storage.getEnergyCapacity(stack) - storage.getEnergyStored(stack));
    }

    public static void linkBlockPos(World world, BlockPos pos, PlayerEntity player, CompoundTag tag) {
        if (tag.containsKey("collectorPos")) {
            tag.remove("collectorPos");
        }

        tag.put("blockPos", TagHelper.serializeBlockPos(pos));

        if(world.isClient)
            player.addChatMessage(new StringTextComponent("Saved block position!"), true);
    }

    public static void linkCollectorPos(World world, BlockPos pos, PlayerEntity player, CompoundTag tag) {
        if (tag.containsKey("blockPos")) {
            tag.remove("blockPos");
        }

        tag.put("collectorPos", TagHelper.serializeBlockPos(pos));

        if (world.isClient)
            player.addChatMessage(new StringTextComponent("Saved collector position!"), true);
    }

    public static void handleControllerLink(World world, BlockPos pos, PlayerEntity player, CompoundTag tag) {
        BlockEntityWirelessController controller = (BlockEntityWirelessController) world.getBlockEntity(pos);

        if (tag.containsKey("collectorPos")) {
            BlockPos collectorPos = TagHelper.deserializeBlockPos(tag.getCompound("collectorPos"));
            BlockEntityWirelessTransmitter energyCollector = (BlockEntityWirelessTransmitter) world.getBlockEntity(collectorPos);
            if (energyCollector != null) {
                energyCollector.setCrystalPos(pos);
                energyCollector.markDirty();
                world.updateListeners(collectorPos, world.getBlockState(collectorPos), world.getBlockState(collectorPos), 3);
                player.addChatMessage(new StringTextComponent("Linked collector position!"), true);
            } else {
                player.addChatMessage(new StringTextComponent("Invalid collector position!"), true);
            }
        } else if (tag.containsKey("blockPos")) {
            BlockPos blockPos = TagHelper.deserializeBlockPos(tag.getCompound("blockPos"));
            if (world.getBlockEntity(blockPos) != null && world.getBlockEntity(blockPos) instanceof IEnergyHandler && ((IEnergyHandler) world.getBlockEntity(blockPos)).isEnergyReceiver(null) && !controller.tilePositions.contains(blockPos)) {
                controller.tilePositions.add(blockPos);
                controller.markDirty();
                player.addChatMessage(new StringTextComponent("Linked BlockEntity position!"), true);
            }
        } else {
            player.addChatMessage(new StringTextComponent("No block position has been linked!"), true);
        }
    }
}
