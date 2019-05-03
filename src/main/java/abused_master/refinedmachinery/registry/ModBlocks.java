package abused_master.refinedmachinery.registry;

import abused_master.abusedlib.registry.RegistryHelper;
import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.blocks.BlockResources;
import abused_master.refinedmachinery.blocks.tanks.EnumTankTypes;
import abused_master.refinedmachinery.blocks.transport.BlockEnergyCable;
import abused_master.refinedmachinery.blocks.transport.BlockWirelessTransmitter;
import abused_master.refinedmachinery.blocks.transport.BlockWirelessController;
import abused_master.refinedmachinery.blocks.decoration.BlockGlassBase;
import abused_master.refinedmachinery.blocks.generators.BlockLavaGenerator;
import abused_master.refinedmachinery.blocks.generators.EnumSolarPanelTypes;
import abused_master.refinedmachinery.blocks.machines.*;

public class ModBlocks {

    public static BlockEnergyFurnace ENERGY_FURNACE = new BlockEnergyFurnace();
    public static BlockPulverizer PULVERIZER = new BlockPulverizer();
    public static BlockQuarry QUARRY = new BlockQuarry();
    public static BlockWirelessController WIRELESS_CONTROLLER = new BlockWirelessController();
    public static BlockWirelessTransmitter WIRELESS_TRANSMITTER = new BlockWirelessTransmitter();
    public static BlockLavaGenerator LAVA_GENERATOR = new BlockLavaGenerator();
    public static BlockEnergyCharger ENERGY_CHARGER = new BlockEnergyCharger();
    public static BlockFluidPump FLUID_PUMP = new BlockFluidPump();
    public static BlockFarmer FARMER = new BlockFarmer();
    public static BlockMobGrinder MOB_GRINDER = new BlockMobGrinder();
    public static BlockVacuum VACUUM = new BlockVacuum();
    public static BlockConveyorBelt CONVEYOR_BELT = new BlockConveyorBelt();
    public static BlockEnergyCell PHASE_CELL = new BlockEnergyCell();
    public static BlockMachineFrame MACHINE_FRAME = new BlockMachineFrame();
    public static BlockEnergyCable ENERGY_CABLE = new BlockEnergyCable();

    //Decoration
    public static BlockGlassBase GLASS_BLOCK = new BlockGlassBase("glass_block");
    public static BlockGlassBase BLACK_GLASS_BLOCK = new BlockGlassBase("black_glass_block");

    public static void registerBlocks() {
        RegistryHelper.registerBlock(RefinedMachinery.MODID, ENERGY_FURNACE);
        RegistryHelper.registerBlock(RefinedMachinery.MODID, PULVERIZER);
        RegistryHelper.registerBlock(RefinedMachinery.MODID, QUARRY);
        RegistryHelper.registerBlock(RefinedMachinery.MODID, WIRELESS_CONTROLLER);
        RegistryHelper.registerBlock(RefinedMachinery.MODID, WIRELESS_TRANSMITTER);
        RegistryHelper.registerBlock(RefinedMachinery.MODID, LAVA_GENERATOR);
        RegistryHelper.registerBlock(RefinedMachinery.MODID, ENERGY_CHARGER);
        RegistryHelper.registerBlock(RefinedMachinery.MODID, FLUID_PUMP);
        RegistryHelper.registerBlock(RefinedMachinery.MODID, FARMER);
        RegistryHelper.registerBlock(RefinedMachinery.MODID, MOB_GRINDER);
        RegistryHelper.registerBlock(RefinedMachinery.MODID, VACUUM);
        RegistryHelper.registerBlock(RefinedMachinery.MODID, CONVEYOR_BELT);
        RegistryHelper.registerBlock(RefinedMachinery.MODID, PHASE_CELL);
        RegistryHelper.registerBlock(RefinedMachinery.MODID, MACHINE_FRAME);
        RegistryHelper.registerBlock(RefinedMachinery.MODID, ENERGY_CABLE);

        RegistryHelper.registerBlock(RefinedMachinery.MODID, GLASS_BLOCK);
        RegistryHelper.registerBlock(RefinedMachinery.MODID, BLACK_GLASS_BLOCK);

        for (EnumSolarPanelTypes panel : EnumSolarPanelTypes.values()) {
            RegistryHelper.registerBlock(RefinedMachinery.MODID, panel.getBlockSolar());
        }

        for (EnumTankTypes tank : EnumTankTypes.values()) {
            RegistryHelper.registerBlock(RefinedMachinery.MODID, tank.getTank());
        }

        for (BlockResources.EnumResourceOres ore : BlockResources.EnumResourceOres.values()) {
            RegistryHelper.registerBlock(RefinedMachinery.MODID, ore.getBlockOres());
        }

        for (BlockResources.EnumResourceBlocks ore : BlockResources.EnumResourceBlocks.values()) {
            RegistryHelper.registerBlock(RefinedMachinery.MODID, ore.getBlockOres());
        }
    }
}
