package abused_master.refinedmachinery.items;

import abused_master.abusedlib.items.ItemBase;
import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.registry.ModItems;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.GlStateManager;
import nerdhub.cardinalenergy.api.IEnergyItemHandler;
import nerdhub.cardinalenergy.impl.ItemEnergyStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.Style;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TextFormat;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ItemBlockExchanger extends ItemBase implements IEnergyItemHandler {

    public ItemEnergyStorage storage = new ItemEnergyStorage(250000);
    public int usePerBlock = 250;
    public static int[] ranges = new int[] {1, 3, 5, 7, 9, 12};

    public ItemBlockExchanger() {
        super("block_exchanger", new Settings().stackSize(1).itemGroup(RefinedMachinery.modItemGroup).durability(250000));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        if(playerEntity.isSneaking()) {
            switchRange(playerEntity, playerEntity.getMainHandStack());
        }

        return super.use(world, playerEntity, hand);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity playerEntity = context.getPlayer();
        BlockPos pos = context.getBlockPos();
        ItemStack stack = context.getItemStack();
        BlockState state = world.getBlockState(pos);

        if(context.isPlayerSneaking()) {
            if (world.isAir(pos) || world.getBlockEntity(pos) != null) {
                return ActionResult.FAIL;
            }

            setBlock(playerEntity, stack, state.getBlock());
            return ActionResult.SUCCESS;
        }else {
            if(getSavedBlock(stack) == null || getSavedBlock(stack) == Blocks.AIR) {
                if(world.isClient) playerEntity.addChatMessage(new StringTextComponent("Cannot exchange with air!").setStyle(new Style().setColor(TextFormat.DARK_RED)), false);
                return ActionResult.FAIL;
            }

            if(getSavedBlock(stack) == state.getBlock()) {
                if(world.isClient) playerEntity.addChatMessage(new StringTextComponent("Cannot exchange the same blocks!").setStyle(new Style().setColor(TextFormat.DARK_RED)), false);
                return ActionResult.FAIL;
            }else {
                this.exchangeBlocks(stack, world, playerEntity, pos, context.getFacing());
                return ActionResult.SUCCESS;
            }
        }
    }

    public void exchangeBlocks(ItemStack stack, World world, PlayerEntity playerEntity, BlockPos pos, Direction direction) {
        int range = getRange(stack);
        Block exchangeBlock = getSavedBlock(stack);
        Block toExchangeBlock = world.getBlockState(pos).getBlock();
        Iterable<BlockPos> exchangeArea = getPosArea(direction, pos, range);

        if(exchangeArea != null) {
            for (Iterator<BlockPos> it = exchangeArea.iterator(); it.hasNext(); ) {
                BlockPos blockPos = it.next();
                BlockState state = world.getBlockState(blockPos);
                if(world.isAir(blockPos) || world.getBlockEntity(blockPos) != null || state.getBlock() instanceof FluidBlock || state.getBlock() == Blocks.BEDROCK || state.getBlock() != toExchangeBlock) {
                    continue;
                }

                if(!doesPlayerHaveBlock(playerEntity, exchangeBlock)) {
                    if(world.isClient) playerEntity.addChatMessage(new StringTextComponent("No valid blocks in your inventory!").setStyle(new Style().setColor(TextFormat.DARK_RED)), false);
                    break;
                }

                if(playerEntity.giveItemStack(new ItemStack(state.getBlock()))) {
                    if(storage.getEnergyStored(stack) >= usePerBlock) {
                        playerEntity.inventory.takeInvStack(getSlotForBlock(playerEntity, exchangeBlock), 1);
                        world.setBlockState(blockPos, exchangeBlock.getDefaultState());
                        storage.extractEnergy(stack, usePerBlock);
                    }else {
                        if(world.isClient) playerEntity.addChatMessage(new StringTextComponent("Not enough power!").setStyle(new Style().setColor(TextFormat.DARK_RED)), false);
                        break;
                    }
                }
            }
        }
    }

    public static Iterable<BlockPos> getPosArea(Direction direction, BlockPos pos, int range) {
        switch (direction) {
            case UP:
            case DOWN:
                return BlockPos.iterate(pos.add(range, 0, range), pos.add(-range, 0, -range));
            case NORTH:
            case SOUTH:
                return BlockPos.iterate(pos.add(range, range, 0), pos.add(-range, -range, 0));
            case EAST:
            case WEST:
                return BlockPos.iterate(pos.add(0, range, range), pos.add(0, -range, -range));
            default:
                return null;
        }
    }

    public boolean doesPlayerHaveBlock(PlayerEntity playerEntity, Block block) {
        return getSlotForBlock(playerEntity, block) != -1;
    }

    public int getSlotForBlock(PlayerEntity playerEntity, Block block) {
        for (int i = 0; i < playerEntity.inventory.getInvSize(); i++) {
            ItemStack stack = playerEntity.inventory.getInvStack(i);
            if (!stack.isEmpty() && stack.getItem() == Item.getItemFromBlock(block)) {
                return i;
            }
        }

        return -1;
    }

    public static Block getSavedBlock(ItemStack stack) {
        if(!stack.hasTag()) {
            getRange(stack);
        }

        return Registry.BLOCK.get(new Identifier(stack.getTag().getString("block")));
    }

    public static void setBlock(PlayerEntity playerEntity, ItemStack stack, Block block) {
        if(!stack.hasTag()) {
            getRange(stack);
        }

        Identifier blockID = Registry.BLOCK.getId(block);
        stack.getTag().putString("block", blockID.toString());
        if(playerEntity.world.isClient) playerEntity.addChatMessage(new StringTextComponent("Block set to " + I18n.translate(block.getTranslationKey())).setStyle(new Style().setColor(TextFormat.GOLD)), false);
    }

    public static void switchRange(PlayerEntity playerEntity, ItemStack stack) {
        int range = getRange(stack);
        if(range == 5) {
            range = 0;
        }else {
            range++;
        }

        stack.getTag().putInt("range", range);
        if(playerEntity.world.isClient) playerEntity.addChatMessage(new StringTextComponent("Switched mode to " + ranges[range] + "x" + ranges[range]).setStyle(new Style().setColor(TextFormat.GOLD)), false);
    }

    public static int getRange(ItemStack stack) {
        if(!stack.hasTag()) {
            stack.setTag(new CompoundTag());
            stack.getTag().putInt("range", 0);
            stack.getTag().putString("block", "minecraft:air");
        }else if(!stack.getTag().containsKey("range")) {
            stack.getTag().putInt("range", 0);
        }

        return stack.getTag().getInt("range");
    }

    @Override
    public Rarity getRarity(ItemStack itemStack_1) {
        return Rarity.EPIC;
    }

    @Override
    public ItemEnergyStorage getEnergyStorage() {
        return storage;
    }

    @Override
    public void buildTooltip(ItemStack stack, @Nullable World world, List<TextComponent> list, TooltipContext tooltipOptions) {
        list.add(new StringTextComponent("Mode: " + ranges[getRange(stack)] + "x" + ranges[getRange(stack)]).setStyle(new Style().setColor(TextFormat.DARK_PURPLE)));
        if(stack.hasTag() && stack.getTag().containsKey("block")) {
            list.add(new StringTextComponent("Block Set: " + I18n.translate(Registry.BLOCK.get(new Identifier(stack.getTag().getString("block"))).getTranslationKey())).setStyle(new Style().setColor(TextFormat.DARK_PURPLE)));
        }
        list.add(new StringTextComponent("Energy: " + storage.getEnergyStored(stack) + " / " + storage.getEnergyCapacity(stack) + " CE").setStyle(new Style().setColor(TextFormat.GOLD)));
    }

    @Environment(EnvType.CLIENT)
    public static void drawBlockOutline(PlayerEntity playerEntity, Camera camera_1, int int_1) {
        if(playerEntity.getMainHandStack().getItem() == ModItems.EXCHANGER) {
            HitResult result = playerEntity.rayTrace(5, 1, false);
            if (result.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHitResult = (BlockHitResult) result;
                BlockPos pos = blockHitResult.getBlockPos();
                BlockState state = playerEntity.world.getBlockState(pos);

                for (Iterator<BlockPos> it = getPosArea(blockHitResult.getSide(), pos, getRange(playerEntity.getMainHandStack())).iterator(); it.hasNext(); ) {
                    BlockPos blockPos = it.next();
                    BlockState blockState = playerEntity.world.getBlockState(blockPos);
                    if (playerEntity.world.isAir(blockPos) || playerEntity.world.getBlockEntity(blockPos) != null || blockState.getBlock() instanceof FluidBlock || blockState.getBlock() == Blocks.BEDROCK || blockState.getBlock() != state.getBlock()) {
                        continue;
                    }

                    GlStateManager.enableBlend();
                    GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    GlStateManager.lineWidth(Math.max(5.5F, MinecraftClient.getInstance().window.getFramebufferWidth() / 1920.0F * 2.5F));
                    GlStateManager.disableTexture();
                    GlStateManager.depthMask(false);
                    GlStateManager.matrixMode(5889);
                    GlStateManager.pushMatrix();
                    GlStateManager.scalef(1.0F, 1.0F, 0.999F);
                    double double_1 = camera_1.getPos().x;
                    double double_2 = camera_1.getPos().y;
                    double double_3 = camera_1.getPos().z;
                    WorldRenderer.drawShapeOutline(blockState.getOutlineShape(playerEntity.world, blockPos, VerticalEntityPosition.fromEntity(camera_1.getFocusedEntity())), (double)blockPos.getX() - double_1, (double)blockPos.getY() - double_2, (double)blockPos.getZ() - double_3, 0, 191 / 255f, 255 / 255f, 5);
                    GlStateManager.popMatrix();
                    GlStateManager.matrixMode(5888);
                    GlStateManager.depthMask(true);
                    GlStateManager.enableTexture();
                    GlStateManager.disableBlend();
                }
            }
        }
    }
}
