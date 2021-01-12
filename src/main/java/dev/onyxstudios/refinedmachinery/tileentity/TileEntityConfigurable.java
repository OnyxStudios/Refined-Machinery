package dev.onyxstudios.refinedmachinery.tileentity;

import dev.onyxstudios.refinedmachinery.utils.MachineConfigType;
import dev.onyxstudios.refinedmachinery.utils.ModelDataTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import java.util.HashMap;
import java.util.Map;

public class TileEntityConfigurable extends TileEntityInventory {

    protected Map<Direction, MachineConfigType> sideConfig = new HashMap<>();
    protected boolean isSettingsOpen = false;
    protected boolean requiresRedstone = false;
    protected boolean isInputOnly;

    public TileEntityConfigurable(TileEntityType<?> tileEntityTypeIn, int slots, boolean isInputOnly) {
        super(tileEntityTypeIn, slots);
        this.isInputOnly = isInputOnly;
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        CompoundNBT settings = compound.getCompound("extractionSettings");
        for (Direction direction : Direction.values()) {
            int index = settings.getInt(direction.getName2() + "_config");
            sideConfig.put(direction, MachineConfigType.values()[index]);
        }

        requiresRedstone = compound.getBoolean("requiresRedstone");
        isInputOnly = compound.getBoolean("isInputOnly");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        CompoundNBT settings = new CompoundNBT();
        for (Direction direction : Direction.values()) {
            settings.putInt(direction.getName2() + "_config", sideConfig.getOrDefault(direction, MachineConfigType.NONE).ordinal());
        }
        compound.put("extractionSettings", settings);
        compound.putBoolean("requiresRedstone", requiresRedstone);
        compound.putBoolean("isInputOnly", isInputOnly);

        return compound;
    }

    public boolean isSideInput(Direction direction) {
        MachineConfigType sideType = sideConfig.getOrDefault(direction, MachineConfigType.NONE);
        return sideType == MachineConfigType.INPUT || sideType == MachineConfigType.INPUT_EXTRACT;
    }

    public boolean isSideOutput(Direction direction) {
        MachineConfigType sideType = sideConfig.getOrDefault(direction, MachineConfigType.NONE);
        return sideType == MachineConfigType.EXTRACT || sideType == MachineConfigType.INPUT_EXTRACT;
    }

    public MachineConfigType getSideConfig(Direction direction) {
        return sideConfig.getOrDefault(direction, MachineConfigType.NONE);
    }

    public void updateConfiguration(Direction direction) {
        MachineConfigType sideType = sideConfig.getOrDefault(direction, MachineConfigType.NONE);

        switch (sideType) {
            case NONE:
                sideConfig.put(direction, MachineConfigType.INPUT);
                break;
            case INPUT:
                //Cases like coal generator, can only input coal
                if(isInputOnly) {
                    sideConfig.put(direction, MachineConfigType.NONE);
                    break;
                }

                sideConfig.put(direction, MachineConfigType.EXTRACT);
                break;
            case EXTRACT:
                sideConfig.put(direction, MachineConfigType.INPUT_EXTRACT);
                break;
            case INPUT_EXTRACT:
                sideConfig.put(direction, MachineConfigType.NONE);
                break;
            default:
                break;
        }

        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        //Update neighbors so they update connections (ex: Pipes to reconnect to tile)
        world.notifyNeighborsOfStateChange(getPos(), getBlockState().getBlock());
        requestModelDataUpdate();
    }

    public boolean isSettingsOpen() {
        return isSettingsOpen;
    }

    public void setSettingsOpen(boolean settingsOpen) {
        isSettingsOpen = settingsOpen;
    }

    public boolean isRedstoneRequired() {
        return requiresRedstone;
    }

    public void setRequiresRedstone(boolean requiresRedstone) {
        this.requiresRedstone = requiresRedstone;
    }

    //Only return the capability if the side trying to interact with is active
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if(isSideInput(side) || isSideOutput(side)) {
            return super.getCapability(cap, side);
        }

        return LazyOptional.empty();
    }

    @Override
    public IModelData getModelData() {
        MachineConfigType[] types = new MachineConfigType[Direction.values().length];

        for (Direction direction : Direction.values()) {
            types[direction.ordinal()] = getSideConfig(direction);
        }

        return new ModelDataMap.Builder().withInitial(ModelDataTypes.CONFIG_PROPERTY, types).build();
    }
}
