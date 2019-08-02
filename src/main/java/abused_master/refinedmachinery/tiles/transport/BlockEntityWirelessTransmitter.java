package abused_master.refinedmachinery.tiles.transport;

import abused_master.abusedlib.tiles.BlockEntityBase;
import abused_master.refinedmachinery.registry.ModBlockEntities;
import abused_master.refinedmachinery.utils.EnergyHelper;
import abused_master.refinedmachinery.utils.ItemHelper;
import abused_master.refinedmachinery.utils.linker.ILinkerHandler;
import nerdhub.cardinalenergy.api.IEnergyHandler;
import nerdhub.cardinalenergy.impl.EnergyStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.util.TagHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class BlockEntityWirelessTransmitter extends BlockEntityBase implements IEnergyHandler, ILinkerHandler {

    public EnergyStorage storage = new EnergyStorage(10000);
    private BlockPos crystalPos = null;
    private int sendPerTick = 500;

    public BlockEntityWirelessTransmitter() {
        super(ModBlockEntities.ENERGY_CRYSTAL_COLLECTOR);
    }

    @Override
    public void fromTag(CompoundTag nbt) {
        super.fromTag(nbt);
        this.storage.fromTag(nbt);
        if(nbt.containsKey("crystalPos")) {
            this.crystalPos = TagHelper.deserializeBlockPos(nbt.getCompound("crystalPos"));
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag nbt) {
        super.toTag(nbt);
        storage.toTag(nbt);
        if(crystalPos != null) {
            nbt.put("crystalPos", TagHelper.serializeBlockPos(crystalPos));
        }
        return nbt;
    }

    @Override
    public void tick() {
        if (crystalPos != null && world.getBlockEntity(crystalPos) != null && world.getBlockEntity(crystalPos) instanceof BlockEntityWirelessController && storage.getEnergyStored() >= sendPerTick) {
            EnergyHelper.sendEnergy(getPos(), storage, world, crystalPos, sendPerTick);
        } else if (crystalPos != null && world.getBlockEntity(crystalPos) == null) {
            crystalPos = null;
            world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        }
    }

    public void setCrystalPos(BlockPos crystalPos) {
        this.crystalPos = crystalPos;
    }

    public BlockPos getCrystalPos() {
        return crystalPos;
    }

    @Override
    public void link(PlayerEntity player, CompoundTag tag) {
        ItemHelper.linkCollectorPos(world, pos, player, tag);
    }
}
