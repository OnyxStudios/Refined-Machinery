package abused_master.refinedmachinery.blocks.machines;

import abused_master.abusedlib.blocks.BlockWithEntityBase;
import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.registry.ModBlockEntities;
import abused_master.refinedmachinery.tiles.machine.BlockEntityVacuum;
import abused_master.refinedmachinery.utils.wrench.IWrenchable;
import abused_master.refinedmachinery.utils.wrench.WrenchHelper;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockVacuum extends BlockWithEntityBase implements IWrenchable {

    public BlockVacuum() {
        super("vacuum", Material.STONE, 1.0f, RefinedMachinery.modItemGroup);
    }

    @Override
    public boolean activate(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {
        if (!world.isClient) {
            ContainerProviderRegistry.INSTANCE.openContainer(ModBlockEntities.VACUUM_CONTAINER, player, buf -> buf.writeBlockPos(blockPos));
        }

        return true;
    }

    @Override
    public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState state2, boolean boolean_1) {
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if(state.getBlock() != state2.getBlock() && blockEntity instanceof BlockEntityVacuum) {
            ItemScatterer.spawn(world, pos, (BlockEntityVacuum) blockEntity);
            world.updateHorizontalAdjacent(pos, this);
        }

        super.onBlockRemoved(state, world, pos, state2, boolean_1);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new BlockEntityVacuum();
    }

    @Override
    public boolean onWrenched(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        return player.isSneaking() ? WrenchHelper.dropBlock(world, pos) : false;
    }
}
