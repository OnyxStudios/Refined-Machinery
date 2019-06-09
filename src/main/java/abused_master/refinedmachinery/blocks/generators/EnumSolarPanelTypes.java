package abused_master.refinedmachinery.blocks.generators;

import abused_master.refinedmachinery.RefinedMachinery;

import java.util.Locale;

public enum EnumSolarPanelTypes {
    SOLAR_PANEL_MK1(RefinedMachinery.config.getInt("solarPanelMK1_Generation"), 10000),
    SOLAR_PANEL_MK2(RefinedMachinery.config.getInt("solarPanelMK2_Generation"), 100000),
    SOLAR_PANEL_MK3(RefinedMachinery.config.getInt("solarPanelMK3_Generation"), 1000000);

    private BlockSolarPanel solarPanel;
    private int generationPerTick, energyStorage;

    EnumSolarPanelTypes(int energyPerTick, int energyStorage) {
        this.solarPanel = new BlockSolarPanel(this, getName());
        this.generationPerTick = energyPerTick;
        this.energyStorage = energyStorage;
    }

    public String getName() {
        return this.toString().toLowerCase(Locale.ROOT);
    }

    public BlockSolarPanel getBlockSolar() {
        return solarPanel;
    }

    public int getGenerationPerTick() {
        return generationPerTick;
    }

    public int getEnergyStorage() {
        return energyStorage;
    }
}
