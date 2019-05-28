package abused_master.refinedmachinery;

import abused_master.refinedmachinery.client.gui.container.*;
import abused_master.refinedmachinery.client.gui.gui.*;
import abused_master.refinedmachinery.registry.ModBlockEntities;
import abused_master.refinedmachinery.registry.ModPackets;
import abused_master.refinedmachinery.tiles.generator.BlockEntityCoalGen;
import abused_master.refinedmachinery.tiles.machine.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.util.math.BlockPos;

import static abused_master.refinedmachinery.registry.ModBlockEntities.*;

public class RefinedMachineryClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModBlockEntities.registerEntityRenders();
        this.registerClientGUIs();
        ModPackets.registerClientPackets();
    }

    public void registerClientGUIs() {
        ScreenProviderRegistry.INSTANCE.registerFactory(ENERGY_FURNACE_CONTAINER, ((syncid, identifier, player, buf) -> {
            BlockPos pos = buf.readBlockPos();
            BlockEntityEnergyFurnace furnace = (BlockEntityEnergyFurnace) player.world.getBlockEntity(pos);
            return new GuiEnergyFurnace(furnace, new ContainerEnergyFurnace(syncid, player.inventory, furnace));
        }));

        ScreenProviderRegistry.INSTANCE.registerFactory(PULVERIZER_CONTAINER, ((syncid, identifier, player, buf) -> {
            BlockPos pos = buf.readBlockPos();
            BlockEntityPulverizer pulverizer = (BlockEntityPulverizer) player.world.getBlockEntity(pos);
            return new GuiPulverizer(pulverizer, new ContainerPulverizer(syncid, player.inventory, pulverizer));
        }));

        ScreenProviderRegistry.INSTANCE.registerFactory(ENERGY_CHARGER_CONTAINER, ((syncid, identifier, player, buf) -> {
            BlockPos pos = buf.readBlockPos();
            BlockEntityEnergyCharger energyCharger = (BlockEntityEnergyCharger) player.world.getBlockEntity(pos);
            return new GuiEnergyCharger(energyCharger, new ContainerEnergyCharger(syncid, player.inventory, energyCharger));
        }));

        ScreenProviderRegistry.INSTANCE.registerFactory(FARMER_CONTAINER, ((syncid, identifier, player, buf) -> {
            BlockPos pos = buf.readBlockPos();
            BlockEntityFarmer farmer = (BlockEntityFarmer) player.world.getBlockEntity(pos);
            return new GuiFarmer(farmer, new ContainerFarmer(syncid, player.inventory, farmer));
        }));

        ScreenProviderRegistry.INSTANCE.registerFactory(VACUUM_CONTAINER, ((syncid, identifier, player, buf) -> {
            BlockPos pos = buf.readBlockPos();
            BlockEntityVacuum vacuum = (BlockEntityVacuum) player.world.getBlockEntity(pos);
            return new GuiVacuum(vacuum, new ContainerVacuum(syncid, player.inventory, vacuum));
        }));

        ScreenProviderRegistry.INSTANCE.registerFactory(COALGEN_CONTAINER, ((syncid, identifier, player, buf) -> {
            BlockPos pos = buf.readBlockPos();
            BlockEntityCoalGen coalGen = (BlockEntityCoalGen) player.world.getBlockEntity(pos);
            return new GuiCoalGen(coalGen, new ContainerCoalGen(syncid, player.inventory, coalGen));
        }));

        ScreenProviderRegistry.INSTANCE.registerFactory(DISENCHANTER_CONTAINER, ((syncid, identifier, player, buf) -> {
            BlockPos pos = buf.readBlockPos();
            BlockEntityDisenchanter disenchanter = (BlockEntityDisenchanter) player.world.getBlockEntity(pos);
            return new GuiDisenchanter(disenchanter, new ContainerDisenchanter(syncid, player.inventory, disenchanter));
        }));

        ScreenProviderRegistry.INSTANCE.registerFactory(QUARRY_CONTAINER, ((syncid, identifier, player, buf) -> {
            BlockPos pos = buf.readBlockPos();
            BlockEntityQuarry quarry = (BlockEntityQuarry) player.world.getBlockEntity(pos);
            return new GuiQuarry(quarry, new ContainerQuarry(syncid, player.inventory, quarry));
        }));
    }
}
