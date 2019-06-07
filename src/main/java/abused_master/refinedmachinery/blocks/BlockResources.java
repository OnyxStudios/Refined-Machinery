package abused_master.refinedmachinery.blocks;

import abused_master.abusedlib.blocks.BlockBase;
import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.RefinedMachineryClient;
import abused_master.refinedmachinery.utils.OreGenConfig;
import abused_master.refinedmachinery.utils.OreGenEntry;
import net.minecraft.block.Material;

public class BlockResources extends BlockBase {

    public BlockResources(String name, float hardness) {
        super(name, Material.STONE, hardness, RefinedMachineryClient.modItemGroup);
    }

    public enum EnumResourceOres {
        COPPER_ORE(1.0F, OreGenConfig.INSTANCE.getEntry("copper_ore")),
        TIN_ORE(1.0F, OreGenConfig.INSTANCE.getEntry("tin_ore")),
        LEAD_ORE(2.0F, OreGenConfig.INSTANCE.getEntry("lead_ore")),
        SILVER_ORE(3.0F, OreGenConfig.INSTANCE.getEntry("silver_ore")),
        NICKEL_ORE(2.0F, OreGenConfig.INSTANCE.getEntry("nickel_ore"));

        private BlockResources blockOres;
        private int maxHeight;
        private int spawnRate;
        private boolean generateOre;
        private int veinSize;

        EnumResourceOres(float hardness, OreGenEntry entry) {
            this.blockOres = new BlockResources(getName(), hardness);
            this.maxHeight = entry.getMaxHeight();
            this.spawnRate = entry.getCount();
            this.generateOre = entry.doesGenerate();
            this.veinSize = entry.getSize();
        }

        public String getName() {
            return this.toString().toLowerCase();
        }

        public BlockResources getBlockOres() {
            return this.blockOres;
        }

        public int getMaxHeight() {
            return maxHeight;
        }

        public int getSpawnRate() {
            return spawnRate;
        }

        public int getVeinSize() {
            return veinSize;
        }

        public boolean generateOre() {
            return generateOre;
        }
    }

    public enum EnumResourceBlocks {
        COPPER_BLOCK,
        TIN_BLOCK,
        LEAD_BLOCK,
        SILVER_BLOCK,
        NICKEL_BLOCK;

        private BlockResources blockOres;

        EnumResourceBlocks() {
            this.blockOres = new BlockResources(getName(), 2.0f);
        }

        public String getName() {
            return this.toString().toLowerCase();
        }

        public BlockResources getBlockOres() {
            return this.blockOres;
        }
    }
}