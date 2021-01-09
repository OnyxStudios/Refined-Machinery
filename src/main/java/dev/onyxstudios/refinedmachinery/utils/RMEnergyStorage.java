package dev.onyxstudios.refinedmachinery.utils;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.energy.EnergyStorage;

public class RMEnergyStorage extends EnergyStorage {

    public RMEnergyStorage(int capacity) {
        super(capacity, capacity, capacity, 0);
    }

    public RMEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer, maxTransfer, 0);
    }

    public RMEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract, 0);
    }

    public RMEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public void setEnergyStored(int energy) {
        this.energy = Math.min(Math.max(energy, 0), capacity);
    }

    public int getMaxExtract() {
        return maxExtract;
    }

    public int getMaxReceive() {
        return maxReceive;
    }

    public RMEnergyStorage read(CompoundNBT tag) {
        CompoundNBT energyTag = tag.getCompound("energyStorage");
        this.capacity = energyTag.getInt("capacity");
        this.energy = energyTag.getInt("energy");
        this.maxExtract = energyTag.getInt("maxExtract");
        this.maxReceive = energyTag.getInt("maxReceive");

        return this;
    }

    public void write(CompoundNBT tag) {
        CompoundNBT energyTag = new CompoundNBT();
        energyTag.putInt("capacity", this.capacity);
        energyTag.putInt("energy", this.energy);
        energyTag.putInt("maxExtract", this.maxExtract);
        energyTag.putInt("maxReceive", this.maxReceive);
        tag.put("energyStorage", energyTag);
    }
}
