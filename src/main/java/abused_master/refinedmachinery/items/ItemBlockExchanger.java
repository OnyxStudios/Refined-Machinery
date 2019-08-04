package abused_master.refinedmachinery.items;

import abused_master.abusedlib.items.ItemBase;
import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.utils.EnergyHelper;
import nerdhub.cardinal.components.api.event.ItemComponentCallback;
import nerdhub.cardinalenergy.DefaultTypes;
import nerdhub.cardinalenergy.api.IEnergyItemHandler;
import nerdhub.cardinalenergy.api.IEnergyStorage;
import nerdhub.cardinalenergy.impl.EnergyStorage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

public class ItemBlockExchanger extends ItemBase implements IEnergyItemHandler {

    public int usePerBlock = 250;
    public static int[] ranges = new int[] {1, 3, 5, 7, 9, 12};

    public ItemBlockExchanger() {
        super("block_exchanger", new Settings().maxCount(1).group(RefinedMachinery.modItemGroup));
        ItemComponentCallback.event(this).register((stack, components) -> components.put(DefaultTypes.CARDINAL_ENERGY, new EnergyStorage(25000)));
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
        ItemStack stack = context.getStack();
        BlockState state = world.getBlockState(pos);

        if(context.isPlayerSneaking()) {
            if (world.isAir(pos) || world.getBlockEntity(pos) != null) {
                return ActionResult.FAIL;
            }

            setBlock(playerEntity, stack, state.getBlock());
            return ActionResult.SUCCESS;
        }else {
            if(getSavedBlock(stack) == null || getSavedBlock(stack) == Blocks.AIR) {
                if(world.isClient) playerEntity.addChatMessage(new LiteralText("Cannot exchange with air!").setStyle(new Style().setColor(Formatting.DARK_RED)), false);
                return ActionResult.FAIL;
            }

            if(getSavedBlock(stack) == state.getBlock()) {
                if(world.isClient) playerEntity.addChatMessage(new LiteralText("Cannot exchange the same blocks!").setStyle(new Style().setColor(Formatting.DARK_RED)), false);
                return ActionResult.FAIL;
            }else {
                this.exchangeBlocks(stack, world, playerEntity, pos, context.getSide());
                return ActionResult.SUCCESS;
            }
        }
    }

    public void exchangeBlocks(ItemStack stack, World world, PlayerEntity playerEntity, BlockPos pos, Direction direction) {
        IEnergyStorage storage = EnergyHelper.getEnergyStorage(stack);
        if(storage != null) {

            int range = getRange(stack);
            Block exchangeBlock = getSavedBlock(stack);
            Block toExchangeBlock = world.getBlockState(pos).getBlock();
            Iterable<BlockPos> exchangeArea = getPosArea(direction, pos, range);

            if (exchangeArea != null) {
                for (Iterator<BlockPos> it = exchangeArea.iterator(); it.hasNext(); ) {
                    BlockPos blockPos = it.next();
                    BlockState state = world.getBlockState(blockPos);
                    if (world.isAir(blockPos) || world.getBlockEntity(blockPos) != null || state.getBlock() instanceof FluidBlock || state.getBlock() == Blocks.BEDROCK || state.getBlock() != toExchangeBlock) {
                        continue;
                    }

                    if (!doesPlayerHaveBlock(playerEntity, exchangeBlock)) {
                        if (world.isClient)
                            playerEntity.addChatMessage(new LiteralText("No valid blocks in your inventory!").setStyle(new Style().setColor(Formatting.DARK_RED)), false);
                        break;
                    }

                    if (playerEntity.giveItemStack(new ItemStack(state.getBlock()))) {
                        if (storage.getEnergyStored() >= usePerBlock) {
                            playerEntity.inventory.takeInvStack(getSlotForBlock(playerEntity, exchangeBlock), 1);
                            world.setBlockState(blockPos, exchangeBlock.getDefaultState());
                            storage.extractEnergy(usePerBlock);
                        } else {
                            if (world.isClient)
                                playerEntity.addChatMessage(new LiteralText("Not enough power!").setStyle(new Style().setColor(Formatting.DARK_RED)), false);
                            break;
                        }
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
            if (!stack.isEmpty() && stack.getItem() == Item.fromBlock(block)) {
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
        if(playerEntity.world.isClient) playerEntity.addChatMessage(new LiteralText("Block set to " + I18n.translate(block.getTranslationKey())).setStyle(new Style().setColor(Formatting.GOLD)), false);
    }

    public static void switchRange(PlayerEntity playerEntity, ItemStack stack) {
        int range = getRange(stack);
        if(range == 5) {
            range = 0;
        }else {
            range++;
        }

        stack.getTag().putInt("range", range);
        if(playerEntity.world.isClient) playerEntity.addChatMessage(new LiteralText("Switched mode to " + ranges[range] + "x" + ranges[range]).setStyle(new Style().setColor(Formatting.GOLD)), false);
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
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> list, TooltipContext tooltipOptions) {
        IEnergyStorage storage = EnergyHelper.getEnergyStorage(stack);

        list.add(new LiteralText("Mode: " + ranges[getRange(stack)] + "x" + ranges[getRange(stack)]).setStyle(new Style().setColor(Formatting.DARK_PURPLE)));
        if(stack.hasTag() && stack.getTag().containsKey("block")) {
            list.add(new LiteralText("Block Set: " + I18n.translate(Registry.BLOCK.get(new Identifier(stack.getTag().getString("block"))).getTranslationKey())).setStyle(new Style().setColor(Formatting.DARK_PURPLE)));
        }

        if(storage != null)
            list.add(new LiteralText("Energy: " + storage.getEnergyStored() + " / " + storage.getCapacity() + " CE").setStyle(new Style().setColor(Formatting.GOLD)));
    }
}
