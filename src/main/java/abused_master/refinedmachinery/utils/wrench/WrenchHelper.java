package abused_master.refinedmachinery.utils.wrench;

import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WrenchHelper {

    public static boolean dropBlock(World world, BlockPos pos) {
        world.breakBlock(pos, true);
        return true;
    }

    public static boolean rotateBlock(World world, BlockPos pos, BlockState state) {
        if(!world.isClient) {
            world.setBlockState(pos, state.with(HorizontalFacingBlock.FACING, state.get(HorizontalFacingBlock.FACING).rotateYClockwise()), 3);
            return true;
        }

        return false;
    }
}
