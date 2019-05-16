package abused_master.refinedmachinery.tiles.generator;

import abused_master.abusedlib.tiles.BlockEntityBase;
import abused_master.refinedmachinery.blocks.generators.EnumSolarPanelTypes;
import abused_master.refinedmachinery.registry.ModBlockEntities;
import abused_master.refinedmachinery.utils.EnergyHelper;
import nerdhub.cardinalenergy.api.EnergyType;
import nerdhub.cardinalenergy.api.IEnergyHandler;
import nerdhub.cardinalenergy.impl.EnergyStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class BlockEntitySolarPanel extends BlockEntityBase implements IEnergyHandler {

    public EnergyStorage storage;
    public EnumSolarPanelTypes type;
    public int generationPerTick;

    public BlockEntitySolarPanel() {
        super(ModBlockEntities.SOLAR_PANEL);
    }

    @Override
    public void fromTag(CompoundTag nbt) {
        super.fromTag(nbt);
        this.setType(EnumSolarPanelTypes.values()[nbt.getInt("type")]);
        this.storage.readEnergyFromTag(nbt);
    }

    @Override
    public CompoundTag toTag(CompoundTag nbt) {
        super.toTag(nbt);
        nbt.putInt("type", this.type.ordinal());
        this.storage.writeEnergyToTag(nbt);
        return nbt;
    }

    @Override
    public void tick() {
        if (world.isDaylight() && canSeeSky()) {
            if ((storage.getEnergyStored() + generationPerTick) < storage.getEnergyCapacity()) {
                storage.receiveEnergy(generationPerTick);
                this.markDirty();
            }
        }

        EnergyHelper.sendEnergyToNeighbors(storage, world, pos, generationPerTick);
        this.markDirty();
    }

    public boolean canSeeSky() {
        int height = 256 - pos.getY();

        for (int i = 1; i < height; i++) {
            BlockPos blockPos = new BlockPos(pos.getX(), pos.getY() + i, pos.getZ());
            if(world.isHeightInvalid(blockPos) || !world.isAir(blockPos)) {
                return false;
            }

            continue;
        }

        return true;
    }

    public void setType(EnumSolarPanelTypes type) {
        this.type = type;
        this.storage = new EnergyStorage(type.getEnergyStorage());
        this.generationPerTick = type.getGenerationPerTick();
        markDirty();
    }

    @Override
    public EnergyStorage getEnergyStorage(Direction direction) {
        return storage;
    }

    @Override
    public boolean isEnergyProvider(Direction direction, EnergyType type) {
        return true;
    }

    @Override
    public boolean isEnergyReceiver(Direction direction, EnergyType type) {
        return false;
    }
}
