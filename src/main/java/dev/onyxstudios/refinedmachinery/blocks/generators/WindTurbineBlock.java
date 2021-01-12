package dev.onyxstudios.refinedmachinery.blocks.generators;

import dev.onyxstudios.refinedmachinery.tileentity.generators.TileEntityWindTurbine;
import dev.onyxstudios.refinedmachinery.utils.WindTurbineType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class WindTurbineBlock extends GeneratorBlock {

    public static EnumProperty<WindTurbineType> TURBINE_TYPE = EnumProperty.create("turbine_type", WindTurbineType.class);
    private static VoxelShape BASE_SHAPE;
    private static VoxelShape TOP_SHAPE;
    private static VoxelShape MIDDLE_SHAPE = createShape(5, 0, 5, 11, 16, 11);
    {
        TOP_SHAPE = createShape(6, 0, 6, 10, 16, 10);
        TOP_SHAPE = VoxelShapes.or(TOP_SHAPE, createShape(7, 16, 7, 9, 17, 9));

        //Create all the little parts of the base
        BASE_SHAPE = createShape(4, 3, 4, 12, 16, 12);
        BASE_SHAPE = VoxelShapes.or(BASE_SHAPE, createShape(2, 0, 2, 14, 3, 14));
        BASE_SHAPE = VoxelShapes.or(BASE_SHAPE, createShape(1, 6, 6, 4, 10, 10));
        BASE_SHAPE = VoxelShapes.or(BASE_SHAPE, createShape(6, 6, 12, 10, 10, 15));
        BASE_SHAPE = VoxelShapes.or(BASE_SHAPE, createShape(6, 6, 1, 10, 10, 4));
        BASE_SHAPE = VoxelShapes.or(BASE_SHAPE, createShape(4, 4, 15, 12, 12, 16));
        BASE_SHAPE = VoxelShapes.or(BASE_SHAPE, createShape(4, 4, 0, 12, 12, 1));
        BASE_SHAPE = VoxelShapes.or(BASE_SHAPE, createShape(15, 4, 4, 16, 12, 12));
        BASE_SHAPE = VoxelShapes.or(BASE_SHAPE, createShape(0, 4, 4, 1, 12, 12));
        BASE_SHAPE = VoxelShapes.or(BASE_SHAPE, createShape(12, 6, 6, 15, 10, 10));
    }

    public WindTurbineBlock() {
        super(Properties.create(Material.ROCK).hardnessAndResistance(2f).notSolid(), 60, 480);
        this.setDefaultState(this.stateContainer.getBaseState().with(TURBINE_TYPE, WindTurbineType.BASE));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        WindTurbineType type = state.get(TURBINE_TYPE);
        BlockState baseState = state;
        BlockPos basePos = pos;

        switch (type) {
            case MIDDLE:
                basePos = pos.offset(Direction.DOWN);
                baseState = world.getBlockState(basePos);
                break;
            case TOP:
                basePos = pos.offset(Direction.DOWN, 2);
                baseState = world.getBlockState(basePos);
                break;
        }

        return super.onBlockActivated(baseState, world, basePos, player, hand, hit);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);

        if(!world.isRemote) {
            BlockPos middlePos = pos.offset(Direction.UP);
            BlockPos topPos = pos.offset(Direction.UP, 2);

            world.setBlockState(middlePos, this.getDefaultState().with(TURBINE_TYPE, WindTurbineType.MIDDLE), 3);
            world.setBlockState(topPos, this.getDefaultState().with(TURBINE_TYPE, WindTurbineType.TOP), 3);

            if(world.getTileEntity(pos) != null)
                ((TileEntityWindTurbine) world.getTileEntity(pos)).checkNearbyTurbines();
        }
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if(!world.isRemote && world.getTileEntity(pos) != null) {
            ((TileEntityWindTurbine) world.getTileEntity(pos)).updateNearbyTurbines();
        }

        super.onReplaced(state, world, pos, newState, isMoving);
        if(!world.isRemote) {
            BlockPos accessor1 = null;
            BlockPos accessor2 = null;

            switch (state.get(TURBINE_TYPE)) {
                case BASE:
                    accessor1 = pos.offset(Direction.UP);
                    accessor2 = pos.offset(Direction.UP, 2);
                    break;
                case MIDDLE:
                    accessor1 = pos.offset(Direction.UP);
                    accessor2 = pos.offset(Direction.DOWN);
                    break;
                case TOP:
                    accessor1 = pos.offset(Direction.DOWN);
                    accessor2 = pos.offset(Direction.DOWN, 2);
                    break;
            }

            if(accessor1 != null)
                world.destroyBlock(accessor1, false);

            if(accessor2 != null)
                world.destroyBlock(accessor2, false);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (state.get(TURBINE_TYPE)) {
            case MIDDLE:
                return MIDDLE_SHAPE;
            case TOP:
                return TOP_SHAPE;
            case BASE:
            default:
                return BASE_SHAPE;
        }
    }

    @Override
    public void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(TURBINE_TYPE);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        if(state.get(TURBINE_TYPE) == WindTurbineType.BASE) {
            return super.hasTileEntity(state);
        }

        return false;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if(state.get(TURBINE_TYPE) == WindTurbineType.BASE) {
            return new TileEntityWindTurbine();
        }

        return null;
    }

    private static VoxelShape createShape(int x, int y, int z, int x1, int y1, int z1) {
        return VoxelShapes.create(x / 16.0f, y / 16.0f, z / 16.0f, x1 / 16.0f, y1 / 16.0f, z1 / 16.0f);
    }
}
