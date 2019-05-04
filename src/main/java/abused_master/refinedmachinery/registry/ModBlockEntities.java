package abused_master.refinedmachinery.registry;

import abused_master.abusedlib.registry.RegistryHelper;
import abused_master.refinedmachinery.RefinedMachinery;
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

    //Container Identifiers
    public static final Identifier ENERGY_FURNACE_CONTAINER = new Identifier(RefinedMachinery.MODID, "energy_furnace_container");
    public static final Identifier PULVERIZER_CONTAINER = new Identifier(RefinedMachinery.MODID, "pulverizer_container");
    public static final Identifier ENERGY_CHARGER_CONTAINER = new Identifier(RefinedMachinery.MODID, "energy_charger_container");
    public static final Identifier FARMER_CONTAINER = new Identifier(RefinedMachinery.MODID, "farmer_container");
    public static final Identifier VACUUM_CONTAINER = new Identifier(RefinedMachinery.MODID, "vacuum_container");
    public static final Identifier COALGEN_CONTAINER = new Identifier(RefinedMachinery.MODID, "coalgen");

    public static void registerBlockEntities() {
        ENERGY_FURNACE = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "energy_furnace"), BlockEntityEnergyFurnace.class);
        QUARRY = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "quarry"), BlockEntityQuarry.class);
        SOLAR_PANEL = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "solar_panel"), BlockEntitySolarPanel.class);
        ENERGY_CRYSTAL = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "energy_crystal"), BlockEntityWirelessController.class);
        ENERGY_CRYSTAL_COLLECTOR = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "energy_crystal_collector"), BlockEntityWirelessTransmitter.class);
        PULVERIZER = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "pulverizer"), BlockEntityPulverizer.class);
        LAVA_GENERATOR = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "lava_generator"), BlockEntityLavaGenerator.class);
        ENERGY_CHARGER = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "energy_charger"), BlockEntityEnergyCharger.class);
        FLUID_PUMP = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "fluid_pump"), BlockEntityFluidPump.class);
        FARMER = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "farmer"), BlockEntityFarmer.class);
        MOB_GRINDER = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "mob_grinder"), BlockEntityMobGrinder.class);
        VACUUM = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "vacuum"), BlockEntityVacuum.class);
        PHASE_CELL = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "phase_cell"), BlockEntityPhaseCell.class);
        ENERGY_CABLE = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "energy_cable"), BlockEntityEnergyCable.class);
        TANK = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "tank"), BlockEntityTank.class);
        COALGEN = RegistryHelper.registerTile(new Identifier(RefinedMachinery.MODID, "coalgen"), BlockEntityCoalGen.class);
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
    }
}
