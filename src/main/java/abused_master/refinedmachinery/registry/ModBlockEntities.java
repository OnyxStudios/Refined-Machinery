package abused_master.refinedmachinery.registry;

import abused_master.abusedlib.registry.RegistryHelper;
import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.blocks.generators.EnumSolarPanelTypes;
import abused_master.refinedmachinery.blocks.tanks.EnumTankTypes;
import abused_master.refinedmachinery.client.gui.container.*;
import abused_master.refinedmachinery.client.render.*;
import abused_master.refinedmachinery.tiles.generator.BlockEntityCoalGen;
import abused_master.refinedmachinery.tiles.tanks.BlockEntityTank;
import abused_master.refinedmachinery.tiles.transport.BlockEntityEnergyCable;
import abused_master.refinedmachinery.tiles.transport.BlockEntityWirelessController;
import abused_master.refinedmachinery.tiles.transport.BlockEntityWirelessTransmitter;
import abused_master.refinedmachinery.tiles.generator.BlockEntityLavaGenerator;
import abused_master.refinedmachinery.tiles.generator.BlockEntitySolarPanel;
import abused_master.refinedmachinery.tiles.machine.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;

public class ModBlockEntities {

    public static BlockEntityType<BlockEntityEnergyFurnace> ENERGY_FURNACE;
    public static BlockEntityType<BlockEntityQuarry> QUARRY;
    public static BlockEntityType<BlockEntitySolarPanel> SOLAR_PANEL;
    public static BlockEntityType<BlockEntityWirelessController> ENERGY_CRYSTAL;
    public static BlockEntityType<BlockEntityWirelessTransmitter> ENERGY_CRYSTAL_COLLECTOR;
    public static BlockEntityType<BlockEntityPulverizer> PULVERIZER;
    public static BlockEntityType<BlockEntityLavaGenerator> LAVA_GENERATOR;
    public static BlockEntityType<BlockEntityEnergyCharger> ENERGY_CHARGER;
    public static BlockEntityType<BlockEntityFluidPump> FLUID_PUMP;
    public static BlockEntityType<BlockEntityFarmer> FARMER;
    public static BlockEntityType<BlockEntityMobGrinder> MOB_GRINDER;
    public static BlockEntityType<BlockEntityVacuum> VACUUM;
    public static BlockEntityType<BlockEntityPhaseCell> PHASE_CELL;
    public static BlockEntityType<BlockEntityEnergyCable> ENERGY_CABLE;
    public static BlockEntityType<BlockEntityTank> TANK;
    public static BlockEntityType<BlockEntityCoalGen> COALGEN;
    public static BlockEntityType<BlockEntityDisenchanter> DISENCHANTER;

    //Container Identifiers
    public static final Identifier ENERGY_FURNACE_CONTAINER = new Identifier(RefinedMachinery.MODID, "energy_furnace_container");
    public static final Identifier PULVERIZER_CONTAINER = new Identifier(RefinedMachinery.MODID, "pulverizer_container");
    public static final Identifier ENERGY_CHARGER_CONTAINER = new Identifier(RefinedMachinery.MODID, "energy_charger_container");
    public static final Identifier FARMER_CONTAINER = new Identifier(RefinedMachinery.MODID, "farmer_container");
    public static final Identifier VACUUM_CONTAINER = new Identifier(RefinedMachinery.MODID, "vacuum_container");
    public static final Identifier COALGEN_CONTAINER = new Identifier(RefinedMachinery.MODID, "coalgen");
    public static final Identifier DISENCHANTER_CONTAINER = new Identifier(RefinedMachinery.MODID, "disenchanter");
    public static final Identifier QUARRY_CONTAINER = new Identifier(RefinedMachinery.MODID, "quarry");

    public static void registerBlockEntities() {
        ENERGY_FURNACE = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "energy_furnace"), BlockEntityEnergyFurnace.class, ModBlocks.ENERGY_FURNACE);
        QUARRY = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "quarry"), BlockEntityQuarry.class, ModBlocks.QUARRY);
        SOLAR_PANEL = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "solar_panel"), BlockEntitySolarPanel.class, EnumSolarPanelTypes.SOLAR_PANEL_MK1.getBlockSolar(), EnumSolarPanelTypes.SOLAR_PANEL_MK2.getBlockSolar(), EnumSolarPanelTypes.SOLAR_PANEL_MK3.getBlockSolar());
        ENERGY_CRYSTAL = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "energy_crystal"), BlockEntityWirelessController.class, ModBlocks.WIRELESS_CONTROLLER);
        ENERGY_CRYSTAL_COLLECTOR = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "energy_crystal_collector"), BlockEntityWirelessTransmitter.class, ModBlocks.WIRELESS_TRANSMITTER);
        PULVERIZER = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "pulverizer"), BlockEntityPulverizer.class, ModBlocks.PULVERIZER);
        LAVA_GENERATOR = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "lava_generator"), BlockEntityLavaGenerator.class, ModBlocks.LAVA_GENERATOR);
        ENERGY_CHARGER = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "energy_charger"), BlockEntityEnergyCharger.class, ModBlocks.ENERGY_CHARGER);
        FLUID_PUMP = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "fluid_pump"), BlockEntityFluidPump.class, ModBlocks.FLUID_PUMP);
        FARMER = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "farmer"), BlockEntityFarmer.class, ModBlocks.FARMER);
        MOB_GRINDER = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "mob_grinder"), BlockEntityMobGrinder.class, ModBlocks.MOB_GRINDER);
        VACUUM = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "vacuum"), BlockEntityVacuum.class, ModBlocks.VACUUM);
        PHASE_CELL = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "phase_cell"), BlockEntityPhaseCell.class, ModBlocks.PHASE_CELL);
        ENERGY_CABLE = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "energy_cable"), BlockEntityEnergyCable.class, ModBlocks.ENERGY_CABLE);
        TANK = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "tank"), BlockEntityTank.class, EnumTankTypes.COPPER_TANK.getTank(), EnumTankTypes.SILVER_TANK.getTank(), EnumTankTypes.STEEL_TANK.getTank());
        COALGEN = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "coalgen"), BlockEntityCoalGen.class, ModBlocks.COAL_GENERATOR);
        DISENCHANTER = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "disenchanter"), BlockEntityDisenchanter.class, ModBlocks.DISENCHANTER);
    }

    @Environment(EnvType.CLIENT)
    public static void registerEntityRenders() {
        BlockEntityRendererRegistry.INSTANCE.register(BlockEntityQuarry.class, new QuarryRenderer());
        BlockEntityRendererRegistry.INSTANCE.register(BlockEntityWirelessController.class, new WirelessControllerRenderer());
        BlockEntityRendererRegistry.INSTANCE.register(BlockEntityWirelessTransmitter.class, new WirelessTransmitterRenderer());
        BlockEntityRendererRegistry.INSTANCE.register(BlockEntityLavaGenerator.class, new LavaGeneratorRenderer());
        BlockEntityRendererRegistry.INSTANCE.register(BlockEntityFluidPump.class, new FluidPumpRenderer());
        BlockEntityRendererRegistry.INSTANCE.register(BlockEntityMobGrinder.class, new MobGrinderRenderer());
        BlockEntityRendererRegistry.INSTANCE.register(BlockEntityPhaseCell.class, new PhaseCellRenderer());
        BlockEntityRendererRegistry.INSTANCE.register(BlockEntityTank.class, new TankRenderer());
    }

    public static void registerServerGUIs() {
        ContainerProviderRegistry.INSTANCE.registerFactory(ENERGY_FURNACE_CONTAINER, (syncid, identifier, player, buf) -> new ContainerEnergyFurnace(syncid, player.inventory, (BlockEntityEnergyFurnace) player.world.getBlockEntity(buf.readBlockPos())));
        ContainerProviderRegistry.INSTANCE.registerFactory(PULVERIZER_CONTAINER, (syncid, identifier, player, buf) -> new ContainerPulverizer(syncid, player.inventory, (BlockEntityPulverizer) player.world.getBlockEntity(buf.readBlockPos())));
        ContainerProviderRegistry.INSTANCE.registerFactory(ENERGY_CHARGER_CONTAINER, (syncid, identifier, player, buf) -> new ContainerEnergyCharger(syncid, player.inventory, (BlockEntityEnergyCharger) player.world.getBlockEntity(buf.readBlockPos())));
        ContainerProviderRegistry.INSTANCE.registerFactory(FARMER_CONTAINER, (syncid, identifier, player, buf) -> new ContainerFarmer(syncid, player.inventory, (BlockEntityFarmer) player.world.getBlockEntity(buf.readBlockPos())));
        ContainerProviderRegistry.INSTANCE.registerFactory(VACUUM_CONTAINER, (syncid, identifier, player, buf) -> new ContainerVacuum(syncid, player.inventory, (BlockEntityVacuum) player.world.getBlockEntity(buf.readBlockPos())));
        ContainerProviderRegistry.INSTANCE.registerFactory(COALGEN_CONTAINER, (syncid, identifier, player, buf) -> new ContainerCoalGen(syncid, player.inventory, (BlockEntityCoalGen) player.world.getBlockEntity(buf.readBlockPos())));
        ContainerProviderRegistry.INSTANCE.registerFactory(DISENCHANTER_CONTAINER, (syncid, identifier, player, buf) -> new ContainerDisenchanter(syncid, player.inventory, (BlockEntityDisenchanter) player.world.getBlockEntity(buf.readBlockPos())));
        ContainerProviderRegistry.INSTANCE.registerFactory(QUARRY_CONTAINER, (syncid, identifier, player, buf) -> new ContainerQuarry(syncid, player.inventory, (BlockEntityQuarry) player.world.getBlockEntity(buf.readBlockPos())));
    }
}
