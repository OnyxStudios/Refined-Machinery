package abused_master.refinedmachinery.blocks.tanks;

import abused_master.abusedlib.blocks.BlockWithEntityBase;
import abused_master.abusedlib.fluid.FluidHelper;
import abused_master.abusedlib.fluid.FluidStack;
import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.tiles.tanks.BlockEntityTank;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stat.Stats;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.Style;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TextFormat;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.loot.context.LootContext;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class BlockTank extends BlockWithEntityBase {

    public EnumTankTypes type;

    public BlockTank(EnumTankTypes type) {
        super(type.getName(), Material.STONE, 1.5f, RefinedMachinery.modItemGroup);
        this.type = type;
    }

    @Override
    public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        BlockEntityTank tank = (BlockEntityTank) world.getBlockEntity(pos);
        return FluidHelper.fillFluidHandler(player.getMainHandStack(), tank, player);
    }

    @Override
    public void afterBreak(World world, PlayerEntity playerEntity, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        if(blockEntity instanceof BlockEntityTank) {
            playerEntity.incrementStat(Stats.MINED.getOrCreateStat(this));
            playerEntity.addExhaustion(0.005F);
            ItemStack dataStack = saveFluidToStack((BlockEntityTank) blockEntity);
            dropStack(world, pos, dataStack);
            return;
        }

        super.afterBreak(world, playerEntity, pos, state, blockEntity, stack);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        if(world.getBlockEntity(pos) instanceof BlockEntityTank) {
            BlockEntityTank tank = (BlockEntityTank) world.getBlockEntity(pos);
            if(tank.tank != null && stack.hasTag() && stack.getTag().containsKey("TileData")) {
                tank.fromTag(stack.getTag().getCompound("TileData"));
                tank.setPos(pos);
                world.updateListeners(pos, state, state, 3);
            }
        }
    }

    @Override
    public ItemStack getPickStack(BlockView view, BlockPos pos, BlockState state) {
        return saveFluidToStack((BlockEntityTank) view.getBlockEntity(pos));
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        return Collections.singletonList(ItemStack.EMPTY);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isSimpleFullBlock(BlockState state, BlockView view, BlockPos pos) {
        return false;
    }

    @Override
    public boolean skipRenderingSide(BlockState state, BlockState state2, Direction direction) {
        return state.getBlock() == this ? true : super.skipRenderingSide(state, state2, direction);
    }

    @Override
    public boolean isTranslucent(BlockState state, BlockView view, BlockPos pos) {
        return true;
    }

    @Override
    public void buildTooltip(ItemStack stack, @Nullable BlockView view, List<TextComponent> list, TooltipContext context) {
        if(stack.hasTag() && stack.getTag().containsKey("TileData")) {
            FluidStack fluidStack = FluidStack.fluidFromTag(stack.getTag().getCompound("TileData").getCompound("FluidData"));
            if(fluidStack != null && fluidStack.getFluid() != null) {
                list.add(new StringTextComponent("Holding: " + fluidStack.getAmount() + " MB of " + Registry.FLUID.getId(fluidStack.getFluid()).getPath()).setStyle(new Style().setColor(TextFormat.GOLD)));
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        BlockEntityTank tank = new BlockEntityTank();
        tank.setType(type);
        return tank;
    }

    public ItemStack saveFluidToStack(BlockEntityTank tank) {
        ItemStack stack = new ItemStack(this);

        if (tank != null && tank.tank.getFluidStack() != null && tank.tank.getFluidAmount() > 0) {
            CompoundTag tag = new CompoundTag();
            tag.put("TileData", tank.toTag(new CompoundTag()));
            stack.setTag(tag);
        }

        return stack;
    }
}
