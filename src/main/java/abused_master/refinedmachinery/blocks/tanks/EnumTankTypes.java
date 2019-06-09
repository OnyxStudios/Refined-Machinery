package abused_master.refinedmachinery.blocks.tanks;

import abused_master.refinedmachinery.RefinedMachinery;

import java.util.Locale;

public enum EnumTankTypes {
    COPPER_TANK(RefinedMachinery.config.getInt("copperTankStorage")),
    SILVER_TANK(RefinedMachinery.config.getInt("silverTankStorage")),
    STEEL_TANK(RefinedMachinery.config.getInt("steelTankStorage"));

    private int storageCapacity;
    private BlockTank tank;

    EnumTankTypes(int storageCapacity) {
        this.tank = new BlockTank(this);
        this.storageCapacity = storageCapacity;
    }

    public String getName() {
        return this.toString().toLowerCase(Locale.ROOT);
    }

    public BlockTank getTank() {
        return tank;
    }

    public int getStorageCapacity() {
        return storageCapacity;
    }
}
