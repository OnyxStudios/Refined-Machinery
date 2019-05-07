package abused_master.refinedmachinery.registry;

import abused_master.abusedlib.registry.RegistryHelper;
import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.blocks.BlockResources;
import net.fabricmc.loader.api.FabricLoader;

public class WorldGenRegistry {

    public static void generateOres() {
        if(RefinedMachinery.config.getBoolean("useCottonResources") && FabricLoader.getInstance().isModLoaded("cotton-resources")) {
            return;
        }

        for (BlockResources.EnumResourceOres ore : BlockResources.EnumResourceOres.values()) {
            if (ore.generateOre()) {
                RegistryHelper.generateOreInStone(ore.getBlockOres(), ore.getVeinSize(), ore.getSpawnRate(), ore.getMaxHeight());
            }
        }
    }
}
