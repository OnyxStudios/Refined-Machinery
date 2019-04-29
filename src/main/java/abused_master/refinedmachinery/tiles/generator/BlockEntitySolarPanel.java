package abused_master.refinedmachinery.tiles.generator;

import abused_master.abusedlib.tiles.BlockEntityBase;
import abused_master.refinedmachinery.blocks.generators.EnumSolarPanelTypes;
import abused_master.refinedmachinery.registry.ModBlockEntities;
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
        if(world.isDaylight()) {
            if((storage.getEnergyStored() + generationPerTick) < storage.getEnergyCapacity()) {
                storage.receiveEnergy(generationPerTick);
            }
        }

        sendEnergy();
    }

    public void setType(EnumSolarPanelTypes type) {
        this.type = type;
        if(storage != null && storage.getEnergyStored() > 0) {
            this.storage = new EnergyStorage(type.getEnergyStorage(), storage.getEnergyStored());
        }else {
            this.storage = new EnergyStorage(type.getEnergyStorage());
        }
        this.generationPerTick = type.getGenerationPerTick();
        markDirty();
    }

    public void sendEnergy() {
        for (Direction direction : Direction.values()) {
            BlockPos offsetPos = pos.offset(direction);
            storage.sendEnergy(world, offsetPos, generationPerTick);
            this.markDirty();
        }
    }

    @Override
    public EnergyStorage getEnergyStorage(Direction direction) {
        return storage;
    }

    @Override
    public boolean isEnergyProvider(Direction direction) {
        return true;
    }
}
