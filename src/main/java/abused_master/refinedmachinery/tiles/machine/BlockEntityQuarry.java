package abused_master.refinedmachinery.tiles.machine;

import abused_master.abusedlib.tiles.BlockEntityBase;
import abused_master.abusedlib.utils.InventoryHelper;
import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.registry.ModBlockEntities;
import abused_master.refinedmachinery.registry.ModItems;
import abused_master.refinedmachinery.utils.ItemHelper;
import abused_master.refinedmachinery.utils.linker.ILinkerHandler;
import nerdhub.cardinalenergy.api.IEnergyHandler;
import nerdhub.cardinalenergy.impl.EnergyStorage;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.TagHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import javax.annotation.Nullable;
import java.util.*;

public class BlockEntityQuarry extends BlockEntityBase implements IEnergyHandler, ILinkerHandler, SidedInventory {

    public EnergyStorage storage = new EnergyStorage(100000);
    public DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
    public List<BlockPos> cachedAreaPos = new ArrayList<>();
    public BlockPos miningPos = null, firstCorner = null, secondCorner = null;
    public int energyUsagePerBlock = RefinedMachinery.config.getInt("quarryUsagePerBlock"), miningSpeed = 0;
    public BlockState miningBlock = null;
    public boolean miningError = false, running = false;

    public boolean silkTouch = false;
    public int fortuneLevel = 0, speedMultiplier = 1;

    public BlockEntityQuarry() {
        super(ModBlockEntities.QUARRY);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        this.storage.fromTag(tag);
        inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
        Inventories.fromTag(tag, this.inventory);

        if(tag.containsKey("cachedAreaPos")) {
            this.cachedAreaPos.clear();
            ListTag listTag = tag.getList("cachedAreaPos", NbtType.COMPOUND);

            for (Iterator<Tag> it = listTag.iterator(); it.hasNext(); ) {
                CompoundTag compoundTag = (CompoundTag) it.next();
                cachedAreaPos.add(TagHelper.deserializeBlockPos(compoundTag));
            }
        }

        this.running = tag.getBoolean("running");
        this.silkTouch = tag.getBoolean("silkTouch");
        this.fortuneLevel = tag.getInt("fortuneLevel");
        this.speedMultiplier = tag.getInt("speedMultiplier");
        if (tag.containsKey("firstCorner")) {
            this.firstCorner = TagHelper.deserializeBlockPos(tag.getCompound("firstCorner"));
        }

        if (tag.containsKey("secondCorner")) {
            this.secondCorner = TagHelper.deserializeBlockPos(tag.getCompound("secondCorner"));
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        this.storage.toTag(tag);
        Inventories.toTag(tag, this.inventory);

        if(!this.cachedAreaPos.isEmpty()) {
            ListTag listTag = new ListTag();

            for (BlockPos areaPos : cachedAreaPos) {
                listTag.add(TagHelper.serializeBlockPos(areaPos));
            }

            tag.put("cachedAreaPos", listTag);
        }

        tag.putBoolean("running", this.running);
        tag.putBoolean("silkTouch", this.silkTouch);
        tag.putInt("fortuneLevel", this.fortuneLevel);
        tag.putInt("speedMultiplier", this.speedMultiplier);
        if(firstCorner != null) {
            tag.put("firstCorner", TagHelper.serializeBlockPos(firstCorner));
        }

        if(secondCorner != null) {
            tag.put("secondCorner", TagHelper.serializeBlockPos(secondCorner));
        }

        return tag;
    }

    @Override
    public void tick() {
        if(!world.isReceivingRedstonePower(pos)) {
            if (!miningError) {
                if (canRun() && running && storage.getEnergyStored() >= energyUsagePerBlock) {
                    miningSpeed++;
                    if (miningSpeed >= (20 / speedMultiplier)) {
                        Inventory inventory = InventoryHelper.getNearbyInventory(world, pos);
                        this.mineBlocks(inventory);
                    }
                } else {
                    this.setMiningError(true);
                }
            } else {
                this.checkMiningError();
            }
        }
    }

    public void checkMiningError() {
        if(miningPos == null) {
            miningPos = cachedAreaPos.isEmpty() ? null : cachedAreaPos.get(0);
        }

        if(miningPos == null || world.getBlockState(miningPos) == null || !InventoryHelper.insertItemIfPossible(InventoryHelper.getNearbyInventory(world, pos), new ItemStack(world.getBlockState(miningPos).getBlock()), true) || storage.getEnergyStored() < energyUsagePerBlock) {
            return;
        }

        this.setMiningError(false);
    }

    public void mineBlocks(Inventory inventory) {
        if (!cachedAreaPos.isEmpty()) {
            BlockPos currentMiningPos = null;

            for (Iterator<BlockPos> it = cachedAreaPos.iterator(); it.hasNext(); ) {
                BlockPos pos = it.next();
                if (world.isAir(pos) || world.getBlockState(pos).getBlock() == Blocks.BEDROCK || world.getBlockState(pos).getBlock() instanceof FluidBlock || world.getBlockEntity(pos) != null || world.isHeightInvalid(pos)) {
                    it.remove();
                    continue;
                }

                currentMiningPos = pos;
                this.miningPos = currentMiningPos;
                break;
            }

            if (currentMiningPos != null) {
                miningSpeed = 0;
                BlockState state = world.getBlockState(currentMiningPos);
                miningBlock = state;

                List<ItemStack> drops = world.isClient ? new ArrayList<>() : Block.getDroppedStacks(state, (ServerWorld) world, currentMiningPos, world.getBlockEntity(currentMiningPos));
                world.setBlockState(currentMiningPos, Blocks.AIR.getDefaultState());

                if (silkTouch) {
                    if (!InventoryHelper.insertItemIfPossible(inventory, new ItemStack(state.getBlock()), false)) {
                        setMiningError(true);
                    }
                } else {
                    for (ItemStack itemStack : drops) {
                        Random random = new Random();
                        ItemStack stackWithFortune = new ItemStack(itemStack.getItem(), fortuneLevel == 0 ? 1 : random.nextInt(fortuneLevel * 2));

                        if (!InventoryHelper.insertItemIfPossible(inventory, stackWithFortune, false)) {
                            setMiningError(true);
                        }
                    }
                }

                storage.extractEnergy(energyUsagePerBlock);
                cachedAreaPos.remove(currentMiningPos);
            } else if (currentMiningPos == null && cachedAreaPos.isEmpty()) {
                this.setRunning(false);
            }
        }
    }

    public void cacheMiningArea() {
        if(secondCorner != null && firstCorner != null) {
            Iterable<BlockPos> blocksInQuarry = BlockPos.iterate(secondCorner, firstCorner);
            cachedAreaPos = listBlocksInIterable(blocksInQuarry);
        }

        this.updateEntity();
    }

    public static List<BlockPos> listBlocksInIterable(Iterable<BlockPos> iterable) {
        List<BlockPos> list = new ArrayList<>();

        for (Iterator<BlockPos> it = iterable.iterator(); it.hasNext(); ) {
            BlockPos pos = it.next();
            list.add(pos.toImmutable());
        }

        Collections.sort(list, Collections.reverseOrder());

        return list;
    }

    public BlockPos[] listFourCorners() {
        if(!blockPositionsActive()) {
            return null;
        }

        BlockPos corner1 = this.firstCorner.offset(Direction.UP, 1);
        BlockPos corner2 = new BlockPos(firstCorner.getX(), corner1.getY(), secondCorner.getZ());
        BlockPos corner3 = new BlockPos(secondCorner.getX(), corner1.getY(), firstCorner.getZ());
        BlockPos corner4 = new BlockPos(secondCorner.getX(), corner1.getY(), secondCorner.getZ());

        return new BlockPos[] {corner1, corner2, corner3, corner4};
    }

    public void setCorners(BlockPos firstCorner, BlockPos secondCorner) {
        this.firstCorner = firstCorner;
        this.secondCorner = secondCorner;
    }

    public boolean blockPositionsActive() {
        if(firstCorner == null || secondCorner == null) {
            return false;
        }

        return true;
    }

    public boolean canRun() {
        if (!blockPositionsActive() || cachedAreaPos.isEmpty()) {
            return false;
        }

        return true;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setMiningError(boolean miningError) {
        this.miningError = miningError;
    }

    public boolean isRunning() {
        return running;
    }

    public void stopAndUpdate() {
        setRunning(false);
        setCorners(null, null);
        world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
    }

    @Override
    public int[] getInvAvailableSlots(Direction direction) {
        return new int[] {0, 1};
    }

    @Override
    public boolean canInsertInvStack(int i, ItemStack itemStack, @Nullable Direction direction) {
        return true;
    }

    @Override
    public boolean canExtractInvStack(int i, ItemStack itemStack, Direction direction) {
        return false;
    }

    @Override
    public int getInvSize() {
        return inventory.size();
    }

    @Override
    public boolean isInvEmpty() {
        Iterator var1 = this.inventory.iterator();

        ItemStack itemStack_1;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            itemStack_1 = (ItemStack)var1.next();
        } while(itemStack_1.isEmpty());

        return false;
    }

    @Override
    public void setInvStack(int i, ItemStack itemStack) {
        inventory.set(i, itemStack);
        if(i == 0 && itemStack.getItem() == ModItems.RECORDER) {
            if(itemStack.isEmpty()) {
                stopAndUpdate();
            }else if(itemStack.getItem() == ModItems.RECORDER) {
                CompoundTag tag = itemStack.getTag();

                if (tag != null && tag.containsKey("coordinates1") && tag.containsKey("coordinates2")) {
                    setCorners(TagHelper.deserializeBlockPos(tag.getCompound("coordinates1")), TagHelper.deserializeBlockPos(tag.getCompound("coordinates2")));
                    cacheMiningArea();
                }
            }
        }

        this.markDirty();
        world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
    }

    @Override
    public ItemStack takeInvStack(int i, int i1) {
        ItemStack stack = Inventories.splitStack(this.inventory, i, i1);
        if(i == 0 && inventory.get(0).getItem() != ModItems.RECORDER) {
            stopAndUpdate();
        }

        return stack;
    }

    @Override
    public ItemStack getInvStack(int i) {
        return inventory.get(i);
    }

    @Override
    public ItemStack removeInvStack(int i) {
        ItemStack stack = Inventories.removeStack(this.inventory, i);

        if(i == 0 && inventory.get(0).getItem() != ModItems.RECORDER) {
            stopAndUpdate();
        }

        return stack;
    }

    @Override
    public boolean canPlayerUseInv(PlayerEntity playerEntity) {
        if (this.world.getBlockEntity(this.pos) != this) {
            return false;
        } else {
            return playerEntity.squaredDistanceTo((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clear() {
        setRunning(false);
        setCorners(null, null);
        world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        inventory.clear();
    }

    @Override
    public void link(PlayerEntity player, CompoundTag tag) {
        ItemHelper.linkBlockPos(world, pos, player, tag);
    }
}
