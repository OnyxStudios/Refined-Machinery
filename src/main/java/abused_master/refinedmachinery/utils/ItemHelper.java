package abused_master.refinedmachinery.utils;

import abused_master.refinedmachinery.tiles.transport.BlockEntityWirelessController;
import abused_master.refinedmachinery.tiles.transport.BlockEntityWirelessTransmitter;
import nerdhub.cardinalenergy.DefaultTypes;
import nerdhub.cardinalenergy.api.IEnergyHandler;
import nerdhub.cardinalenergy.api.IEnergyItemStorage;
import nerdhub.cardinalenergy.api.IEnergyStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.util.TagHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemHelper {

    public static void updateItemDurability(ItemStack stack, IEnergyStorage storage) {
        stack.setDamage(storage.getCapacity() - storage.getEnergyStored());
    }

    public static void linkBlockPos(World world, BlockPos pos, PlayerEntity player, CompoundTag tag) {
        if (tag.containsKey("collectorPos")) {
            tag.remove("collectorPos");
        }

        tag.put("blockPos", TagHelper.serializeBlockPos(pos));

        if(world.isClient)
            player.addChatMessage(new LiteralText("Saved block position!"), true);
    }

    public static void linkCollectorPos(World world, BlockPos pos, PlayerEntity player, CompoundTag tag) {
        if (tag.containsKey("blockPos")) {
            tag.remove("blockPos");
        }

        tag.put("collectorPos", TagHelper.serializeBlockPos(pos));

        if (world.isClient)
            player.addChatMessage(new LiteralText("Saved collector position!"), true);
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
                player.addChatMessage(new LiteralText("Linked collector position!"), true);
            } else {
                player.addChatMessage(new LiteralText("Invalid collector position!"), true);
            }
        } else if (tag.containsKey("blockPos")) {
            BlockPos blockPos = TagHelper.deserializeBlockPos(tag.getCompound("blockPos"));
            if (world.getBlockEntity(blockPos) != null && world.getBlockEntity(blockPos) instanceof IEnergyHandler && ((IEnergyHandler) world.getBlockEntity(blockPos)).isEnergyReceiver(null, DefaultTypes.CARDINAL_ENERGY) && !controller.tilePositions.contains(blockPos)) {
                controller.tilePositions.add(blockPos);
                controller.markDirty();
                player.addChatMessage(new LiteralText("Linked BlockEntity position!"), true);
            }
        } else {
            player.addChatMessage(new LiteralText("No block position has been linked!"), true);
        }
    }
}
