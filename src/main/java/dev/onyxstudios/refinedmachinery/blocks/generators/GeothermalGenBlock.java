package dev.onyxstudios.refinedmachinery.blocks.generators;

import dev.onyxstudios.refinedmachinery.tileentity.generators.TileEntityGeothermal;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

public class GeothermalGenBlock extends GeneratorBlock {

    public GeothermalGenBlock() {
        super(Properties.create(Material.ROCK).hardnessAndResistance(1.8f), 5, 300);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if(world.isRemote && world.getTileEntity(pos) != null) {
            world.getTileEntity(pos).getCapability(CapabilityEnergy.ENERGY).ifPresent(storage -> {
                player.sendStatusMessage(new StringTextComponent(storage.getEnergyStored() + " / " + storage.getMaxEnergyStored() + " FE"), true);
            });
        }

        return ActionResultType.func_233537_a_(world.isRemote);
    }
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityGeothermal();
    }
}
