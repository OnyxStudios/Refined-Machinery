package abused_master.refinedmachinery.tiles.transport;

import abused_master.abusedlib.tiles.BlockEntityBase;
import abused_master.refinedmachinery.registry.ModBlockEntities;
import abused_master.refinedmachinery.utils.linker.ILinkerHandler;
import nerdhub.cardinalenergy.api.IEnergyHandler;
import nerdhub.cardinalenergy.impl.EnergyStorage;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.text.StringTextComponent;
import net.minecraft.util.TagHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BlockEntityWirelessController extends BlockEntityBase implements IEnergyHandler, ILinkerHandler {

    public EnergyStorage storage = new EnergyStorage(100000);
    public final Set<BlockPos> tilePositions = new HashSet<>();
    public int sendPerTick = 250;

    public BlockEntityWirelessController() {
        super(ModBlockEntities.ENERGY_CRYSTAL);
    }

    @Override
    public void fromTag(CompoundTag nbt) {
        super.fromTag(nbt);
        storage.readEnergyFromTag(nbt);

        if(nbt.containsKey("tilePositions")) {
            tilePositions.clear();
            ListTag tags = nbt.getList("tilePositions", NbtType.COMPOUND);
            for (Tag tag : tags) {
                tilePositions.add(TagHelper.deserializeBlockPos((CompoundTag) tag));
            }
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag nbt) {
        super.toTag(nbt);
        storage.writeEnergyToTag(nbt);

        if(tilePositions.size() > 0) {
            ListTag tags = new ListTag();
            for (BlockPos pos1 : tilePositions) {
                tags.add(TagHelper.serializeBlockPos(pos1));
            }

            nbt.put("tilePositions", tags);
        }

        return nbt;
    }

    @Override
    public void tick() {
        if(storage.getEnergyStored() >= sendPerTick && tilePositions.size() > 0) {
            sendEnergy();
        }
    }

    public void sendEnergy() {
        for (Iterator<BlockPos> it = tilePositions.iterator(); it.hasNext();) {
            BlockPos blockPos = it.next();
            if(blockPos == null || !(world.getBlockEntity(blockPos) instanceof IEnergyHandler) || !((IEnergyHandler) world.getBlockEntity(blockPos)).isEnergyReceiver(null)) {
                it.remove();
                this.updateEntity();
                continue;
            }

            storage.sendEnergy(world, blockPos, sendPerTick);
            this.updateEntity();
        }
    }

    @Override
    public EnergyStorage getEnergyStorage(Direction direction) {
        return storage;
    }

    @Override
    public void link(PlayerEntity player, CompoundTag tag) {
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
            if (world.getBlockEntity(blockPos) != null && world.getBlockEntity(blockPos) instanceof IEnergyHandler && ((IEnergyHandler) world.getBlockEntity(blockPos)).isEnergyReceiver(null) && !tilePositions.contains(blockPos)) {
                tilePositions.add(blockPos);
                this.updateEntity();
                player.addChatMessage(new StringTextComponent("Linked BlockEntity position!"), true);
            }
        } else {
            player.addChatMessage(new StringTextComponent("No block position has been linked!"), true);
        }
    }
}
