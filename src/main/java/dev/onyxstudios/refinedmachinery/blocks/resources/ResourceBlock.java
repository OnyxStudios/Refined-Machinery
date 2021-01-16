package dev.onyxstudios.refinedmachinery.blocks.resources;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class ResourceBlock extends Block {

    public ResourceBlock(float hardness, int harvestLevel) {
        super(AbstractBlock.Properties.create(Material.ROCK)
                .hardnessAndResistance(hardness)
                .setRequiresTool()
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(harvestLevel)
                .notSolid()
        );
    }
}
