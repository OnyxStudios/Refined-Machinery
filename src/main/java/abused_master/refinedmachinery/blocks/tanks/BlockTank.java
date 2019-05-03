package abused_master.refinedmachinery.blocks.tanks;

import abused_master.abusedlib.blocks.BlockWithEntityBase;
import abused_master.abusedlib.fluid.FluidHelper;
import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.tiles.tanks.BlockEntityTank;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockTank extends BlockWithEntityBase {

    public EnumTankTypes type;

    public BlockTank(EnumTankTypes type) {
        super(type.getName(), Material.STONE, 1.5f, RefinedMachinery.modItemGroup);
        this.type = type;
    }

    @Override
    public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        BlockEntityTank tank = (BlockEntityTank) world.getBlockEntity(pos);
        FluidHelper.fillFluidHandler(player.getMainHandStack(), tank, player);

        return true;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isSimpleFullBlock(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1) {
        return false;
    }

    @Override
    public boolean skipRenderingSide(BlockState blockState_1, BlockState blockState_2, Direction direction_1) {
        return blockState_1.getBlock() == this ? true : super.skipRenderingSide(blockState_1, blockState_2, direction_1);
    }

    @Override
    public boolean isTranslucent(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView var1) {
        BlockEntityTank tank = new BlockEntityTank();
        tank.setType(type);
        return tank;
    }
}
