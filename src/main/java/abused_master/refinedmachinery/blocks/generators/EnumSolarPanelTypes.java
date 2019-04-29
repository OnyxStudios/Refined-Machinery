package abused_master.refinedmachinery.blocks.generators;

public enum EnumSolarPanelTypes {
    SOLAR_PANEL_MK1(16, 10000),
    SOLAR_PANEL_MK2(256, 100000),
    SOLAR_PANEL_MK3(1024, 1000000);

    private BlockSolarPanel solarPanel;
    private int generationPerTick, energyStorage;

    EnumSolarPanelTypes(int energyPerTick, int energyStorage) {
        this.solarPanel = new BlockSolarPanel(this, getName());
        this.generationPerTick = energyPerTick;
        this.energyStorage = energyStorage;
    }

    public String getName() {
        return this.toString().toLowerCase();
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
