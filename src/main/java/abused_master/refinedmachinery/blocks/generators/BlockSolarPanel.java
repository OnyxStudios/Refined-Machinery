package abused_master.refinedmachinery.blocks.generators;

import abused_master.abusedlib.blocks.BlockWithEntityBase;
import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.tiles.generator.BlockEntitySolarPanel;
import abused_master.refinedmachinery.utils.wrench.IWrenchable;
import abused_master.refinedmachinery.utils.wrench.WrenchHelper;
import nerdhub.cardinal.components.api.BlockComponentProvider;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import nerdhub.cardinalenergy.DefaultTypes;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

public class BlockSolarPanel extends BlockWithEntityBase implements IWrenchable, BlockComponentProvider {

    public EnumSolarPanelTypes solarPanelType;

    public BlockSolarPanel(EnumSolarPanelTypes type, String name) {
        super(name, Material.STONE, 1.0f, RefinedMachinery.modItemGroup);
        this.solarPanelType = type;
    }

    @Override
    public boolean activate(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockHitResult blockHitResult) {
        BlockEntitySolarPanel panel = (BlockEntitySolarPanel) world.getBlockEntity(blockPos);

        if(!world.isClient) {
            playerEntity.addChatMessage(new TextComponent("Energy: " + panel.storage.getEnergyStored() + " / " + panel.storage.getCapacity() + " CE"), true);
        }

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
    public boolean isSideInvisible(BlockState blockState_1, BlockState blockState_2, Direction direction_1) {
        return blockState_1.getBlock() == this ? true : super.isSideInvisible(blockState_1, blockState_2, direction_1);
    }

    @Override
    public boolean isTranslucent(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView var1) {
        BlockEntitySolarPanel solarPanel = new BlockEntitySolarPanel();
        solarPanel.setType(solarPanelType);
        return solarPanel;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext EntityContext) {
        VoxelShape topShape = Block.createCuboidShape(0.0D, 14.0D, 0.0D, 16.0D, 15.0D, 16.0D);
        VoxelShape baseShape = Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 14.0D, 12.0D);

        return VoxelShapes.union(topShape, baseShape);
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
        return type == DefaultTypes.CARDINAL_ENERGY ? (T) ((BlockEntitySolarPanel) blockView.getBlockEntity(pos)).storage : null;
    }

    @Override
    public Set<ComponentType<?>> getComponentTypes(BlockView blockView, BlockPos pos, @Nullable Direction side) {
        return Collections.singleton(DefaultTypes.CARDINAL_ENERGY);
    }
}
