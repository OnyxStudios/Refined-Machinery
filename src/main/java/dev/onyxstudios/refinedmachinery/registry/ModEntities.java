package dev.onyxstudios.refinedmachinery.registry;

import dev.onyxstudios.refinedmachinery.RefinedMachinery;
import dev.onyxstudios.refinedmachinery.client.container.CoalGenContainer;
import dev.onyxstudios.refinedmachinery.client.container.WindTurbineContainer;
import dev.onyxstudios.refinedmachinery.tileentity.generators.TileEntityCoalGen;
import dev.onyxstudios.refinedmachinery.tileentity.generators.TileEntityWindTurbine;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntities {

    public static final DeferredRegister<TileEntityType<?>> tileRegistry = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, RefinedMachinery.MODID);
    public static final DeferredRegister<ContainerType<?>> containersRegistry = DeferredRegister.create(ForgeRegistries.CONTAINERS, RefinedMachinery.MODID);

    public static RegistryObject<TileEntityType<TileEntityCoalGen>> coalGenTileType = tileRegistry.register("coal_gen_tile", () -> TileEntityType.Builder.create(TileEntityCoalGen::new, ModBlocks.coalGenObject.get()).build(null));
    public static RegistryObject<ContainerType<CoalGenContainer>> coalGenContainerType = containersRegistry.register("coal_gen_container", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        TileEntityCoalGen tileEntityCoalGen = (TileEntityCoalGen) inv.player.world.getTileEntity(pos);
        return new CoalGenContainer(windowId, inv, tileEntityCoalGen);
    }));

    public static RegistryObject<TileEntityType<TileEntityWindTurbine>> windTurbineTileType = tileRegistry.register("wind_turbine_tile", () -> TileEntityType.Builder.create(TileEntityWindTurbine::new, ModBlocks.windTurbineObject.get()).build(null));
    public static RegistryObject<ContainerType<WindTurbineContainer>> turbineContainerType = containersRegistry.register("wind_turbine_container", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        TileEntityWindTurbine tileEntityWindTurbine = (TileEntityWindTurbine) inv.player.world.getTileEntity(pos);
        return new WindTurbineContainer(windowId, inv, tileEntityWindTurbine);
    }));
}
