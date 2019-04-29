package abused_master.refinedmachinery.registry;

import abused_master.abusedlib.registry.RegistryHelper;
import abused_master.refinedmachinery.blocks.BlockResources;

public class WorldGenRegistry {

    public static void generateOres() {
        for (BlockResources.EnumResourceOres ore : BlockResources.EnumResourceOres.values()) {
            if (ore.generateOre()) {
                RegistryHelper.generateOreInStone(ore.getBlockOres(), ore.getVeinSize(), ore.getSpawnRate(), ore.getMaxHeight());
            }
        }
    }
}
