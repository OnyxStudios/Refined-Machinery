package dev.onyxstudios.refinedmachinery.blocks.generators;

import dev.onyxstudios.refinedmachinery.tileentity.generators.TileEntityLavaGen;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class LavaGeneratorBlock extends GeneratorBlock {

    public LavaGeneratorBlock() {
        super(Properties.create(Material.ROCK).hardnessAndResistance(1.5f), 80, 640);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityLavaGen();
    }
}
