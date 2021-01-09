package dev.onyxstudios.refinedmachinery.blocks.generators;

import dev.onyxstudios.refinedmachinery.tileentity.TileEntityInventory;
import dev.onyxstudios.refinedmachinery.utils.InventoryUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.*;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public abstract class GeneratorBlock extends Block {

    public final int productionAmount;
    public final int maxExtract;

    public GeneratorBlock(Properties properties, int productionAmount, int maxExtract) {
        super(properties);
        this.productionAmount = productionAmount;
        this.maxExtract = maxExtract;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if(!world.isRemote() && world.getTileEntity(pos) instanceof INamedContainerProvider) {
            NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) world.getTileEntity(pos), pos);
        }

        return ActionResultType.func_233537_a_(world.isRemote);
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBlockHarvested(world, pos, state, player);
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.isIn(newState.getBlock())) {
            //If the generator has an inventory, drop stacks stored
            if (world.getTileEntity(pos) instanceof TileEntityInventory) {
                InventoryUtils.dropInventoryItems(world, pos, ((TileEntityInventory) world.getTileEntity(pos)).inventory);
                world.updateComparatorOutputLevel(pos, this);
            }

            super.onReplaced(state, world, pos, newState, isMoving);
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        IFormattableTextComponent generates = new StringTextComponent("Generates: ").setStyle(Style.EMPTY.applyFormatting(TextFormatting.GRAY));
        generates.append(new StringTextComponent(productionAmount + " FE/Tick").setStyle(Style.EMPTY.applyFormatting(TextFormatting.DARK_GRAY)));
        tooltip.add(generates);

        IFormattableTextComponent extracts = new StringTextComponent("Max Extract: ").setStyle(Style.EMPTY.applyFormatting(TextFormatting.GRAY));
        extracts.append(new StringTextComponent(maxExtract + " FE/Tick").setStyle(Style.EMPTY.applyFormatting(TextFormatting.DARK_GRAY)));
        tooltip.add(extracts);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);
}
