package abused_master.refinedmachinery.blocks.transport;

import abused_master.abusedlib.blocks.BlockWithEntityBase;
import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.tiles.transport.BlockEntityEnergyCable;
import abused_master.refinedmachinery.utils.wrench.IWrenchable;
import abused_master.refinedmachinery.utils.wrench.WrenchHelper;
import nerdhub.cardinalenergy.api.IEnergyHandler;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockEnergyCable extends BlockWithEntityBase implements IWrenchable {

    public static final BooleanProperty[] PROPS = new BooleanProperty[] {
            BooleanProperty.create("down"),
            BooleanProperty.create("up"),
            BooleanProperty.create("north"),
            BooleanProperty.create("south"),
            BooleanProperty.create("west"),
            BooleanProperty.create("east"),
            BooleanProperty.create("none")
    };

    public BlockEnergyCable() {
        super("energy_cable", Material.STONE, 1.0f, RefinedMachinery.modItemGroup);
        this.setDefaultState(this.getStateFactory().getDefaultState().with(PROPS[6], true));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return getStateWithProps(this.getStateFactory().getDefaultState(), context.getWorld(), context.getBlockPos());
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState state2, IWorld world, BlockPos blockPos, BlockPos blockPos_2) {
        return getStateWithProps(state, world, blockPos);
    }

    public BlockState getStateWithProps(BlockState state, IWorld world, BlockPos pos) {
        for (int i = 0; i < PROPS.length - 1; i++) {
            Direction facing = Direction.values()[i];
            BlockEntity blockEntity = world.getBlockEntity(pos.offset(facing));

            state = state.with(PROPS[i], blockEntity instanceof BlockEntityEnergyCable || (blockEntity instanceof IEnergyHandler && ((IEnergyHandler) blockEntity).canConnectEnergy(facing.getOpposite())));
        }

        return state;
    }

    @Override
    public void appendProperties(StateFactory.Builder<Block, BlockState> stateBuilder) {
        super.appendProperties(stateBuilder.with(PROPS));
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
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new BlockEntityEnergyCable();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, VerticalEntityPosition verticalEntityPosition) {
        return Block.createCuboidShape(5, 5, 5, 11, 11, 11);
    }

    @Override
    public boolean onWrenched(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        return player.isSneaking() ? WrenchHelper.dropBlock(world, pos) : false;
    }
}
