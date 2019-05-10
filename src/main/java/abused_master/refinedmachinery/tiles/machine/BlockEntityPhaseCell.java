package abused_master.refinedmachinery.tiles.machine;

import abused_master.abusedlib.client.render.hud.IHudSupport;
import abused_master.abusedlib.tiles.BlockEntityBase;
import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.registry.ModBlockEntities;
import abused_master.refinedmachinery.utils.linker.ILinkerHandler;
import nerdhub.cardinalenergy.api.IEnergyHandler;
import nerdhub.cardinalenergy.impl.EnergyStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.StringTextComponent;
import net.minecraft.util.TagHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Arrays;
import java.util.List;

public class BlockEntityPhaseCell extends BlockEntityBase implements IEnergyHandler, IHudSupport, ILinkerHandler {

    public EnergyStorage storage = new EnergyStorage(RefinedMachinery.config.getInt("phaseCellStorage"));

    public BlockEntityPhaseCell() {
        super(ModBlockEntities.PHASE_CELL);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        storage.readEnergyFromTag(tag);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        storage.writeEnergyToTag(tag);
        return tag;
    }

    @Override
    public void tick() {
        sendEnergy();
    }

    public void sendEnergy() {
        for (Direction direction : Direction.values()) {
            BlockPos offsetPos = pos.offset(direction);
            storage.sendEnergy(world, offsetPos, 1000);
            this.markDirty();
        }
    }

    @Override
    public EnergyStorage getEnergyStorage(Direction direction) {
        return storage;
    }

    @Override
    public Direction getBlockOrientation() {
        return null;
    }

    @Override
    public boolean isBlockAboveAir() {
        return world.isAir(pos.up());
    }

    @Override
    public List<String> getClientLog() {
        return Arrays.asList(new String[]{"Energy: " + storage.getEnergyStored() + " / " + storage.getEnergyCapacity() + " CE"});
    }

    @Override
    public BlockPos getBlockPos() {
        return pos;
    }

    @Override
    public void link(PlayerEntity player, CompoundTag tag) {
        if (tag.containsKey("collectorPos")) {
            tag.remove("collectorPos");
        }
        tag.put("blockPos", TagHelper.serializeBlockPos(pos));
        player.addChatMessage(new StringTextComponent("Saved block position!"), true);
    }
}
