package abused_master.refinedmachinery.blocks.generators;

import abused_master.abusedlib.blocks.BlockWithEntityBase;
import abused_master.abusedlib.fluid.FluidHelper;
import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.tiles.generator.BlockEntityLavaGenerator;
import abused_master.refinedmachinery.utils.wrench.IWrenchable;
import abused_master.refinedmachinery.utils.wrench.WrenchHelper;
import nerdhub.cardinal.components.api.BlockComponentProvider;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import nerdhub.cardinalenergy.DefaultTypes;
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
import java.util.Collections;
import java.util.Set;

public class BlockLavaGenerator extends BlockWithEntityBase implements IWrenchable, BlockComponentProvider {

    public BlockLavaGenerator() {
        super("lava_generator", Material.STONE, 1.0f, RefinedMachinery.modItemGroup);
    }

    @Override
    public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        BlockEntityLavaGenerator lavaGenerator = (BlockEntityLavaGenerator) world.getBlockEntity(pos);
        FluidHelper.fillFluidHandler(player.getMainHandStack(), lavaGenerator, player);
        return true;
    }

    @Override
    public int getWeakRedstonePower(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, Direction direction_1) {
        return super.getWeakRedstonePower(blockState_1, blockView_1, blockPos_1, direction_1);
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
    public boolean isSideInvisible(BlockState blockState_1, BlockState blockState_2, Direction direction_1) {
        return blockState_1.getBlock() == this ? true : super.isSideInvisible(blockState_1, blockState_2, direction_1);
    }

    @Override
    public boolean isTranslucent(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new BlockEntityLavaGenerator();
    }

    @Override
    public boolean onWrenched(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        return player.isSneaking() ? WrenchHelper.dropBlock(world, pos) : false;
    }

    @Override
    public <T extends Component> boolean hasComponent(BlockView blockView, BlockPos pos, ComponentType<T> type, @Nullable Direction side) {
        return type == DefaultTypes.CARDINAL_ENERGY;
    }

    @Override
    public <T extends Component> T getComponent(BlockView blockView, BlockPos pos, ComponentType<T> type, @Nullable Direction side) {
        return type == DefaultTypes.CARDINAL_ENERGY ? (T) ((BlockEntityLavaGenerator) blockView.getBlockEntity(pos)).storage : null;
    }

    @Override
    public Set<ComponentType<?>> getComponentTypes(BlockView blockView, BlockPos pos, @Nullable Direction side) {
        return Collections.singleton(DefaultTypes.CARDINAL_ENERGY);
    }
}
