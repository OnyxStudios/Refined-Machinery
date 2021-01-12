package dev.onyxstudios.refinedmachinery.items;

import dev.onyxstudios.refinedmachinery.RefinedMachinery;
import dev.onyxstudios.refinedmachinery.blocks.generators.WindTurbineBlock;
import dev.onyxstudios.refinedmachinery.registry.ModBlocks;
import dev.onyxstudios.refinedmachinery.utils.WindTurbineType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WindTurbineItem extends BlockItem {

    public WindTurbineItem() {
        super(ModBlocks.windTurbineObject.get(), new Item.Properties().group(RefinedMachinery.TAB));
    }

    @Override
    protected boolean onBlockPlaced(BlockPos pos, World world, PlayerEntity player, ItemStack stack, BlockState state) {
        boolean success = super.onBlockPlaced(pos, world, player, stack, state);
        if(success) {
            world.setBlockState(pos.offset(Direction.UP), ModBlocks.windTurbineObject.get().getDefaultState().with(WindTurbineBlock.TURBINE_TYPE, WindTurbineType.MIDDLE));
            world.setBlockState(pos.offset(Direction.UP, 2), ModBlocks.windTurbineObject.get().getDefaultState().with(WindTurbineBlock.TURBINE_TYPE, WindTurbineType.TOP));
        }

        return success;
    }

    @Override
    protected boolean canPlace(BlockItemUseContext itemUseContext, BlockState state) {
        BlockPos pos = itemUseContext.getPos();
        World world = itemUseContext.getWorld();

        if(world.isAirBlock(pos.offset(Direction.UP)) && world.isAirBlock(pos.offset(Direction.UP, 2))) {
            return super.canPlace(itemUseContext, state);
        }

        return false;
    }
}
