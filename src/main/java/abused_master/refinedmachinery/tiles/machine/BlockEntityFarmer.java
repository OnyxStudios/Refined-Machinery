package abused_master.refinedmachinery.tiles.machine;

import abused_master.abusedlib.tiles.BlockEntityBase;
import abused_master.abusedlib.utils.InventoryHelper;
import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.registry.ModBlockEntities;
import abused_master.refinedmachinery.utils.ItemHelper;
import abused_master.refinedmachinery.utils.linker.ILinkerHandler;
import nerdhub.cardinalenergy.api.IEnergyHandler;
import nerdhub.cardinalenergy.impl.EnergyStorage;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BlockEntityFarmer extends BlockEntityBase implements IEnergyHandler, SidedInventory, ILinkerHandler {

    public EnergyStorage storage = new EnergyStorage(50000);
    public DefaultedList<ItemStack> inventory = DefaultedList.create(12, ItemStack.EMPTY);
    public int[] outputSlots = new int[] {5, 6, 7, 8, 9, 10, 11};
    public int[] seedsSlot = new int[] {1, 2, 3, 4};
    public int farmerRange = 3;
    public int timer = 0;
    public int costPerBlock = RefinedMachinery.config.getInt("farmerCostPerBlock");
    public int timerCounter = 20;

    public BlockEntityFarmer() {
        super(ModBlockEntities.FARMER);
    }

    @Override
    public void fromTag(CompoundTag nbt) {
        super.fromTag(nbt);
        this.storage.readEnergyFromTag(nbt);

        inventory = DefaultedList.create(12, ItemStack.EMPTY);
        Inventories.fromTag(nbt, this.inventory);
    }

    @Override
    public CompoundTag toTag(CompoundTag nbt) {
        super.toTag(nbt);
        this.storage.writeEnergyToTag(nbt);
        Inventories.toTag(nbt, this.inventory);
        return nbt;
    }

    @Override
    public void tick() {
        if (canRun()) {
            timer++;
            if (timer >= timerCounter) {
                run();
            }
        }

        extractItems();
        autoPlant();
    }

    public void run() {
        Iterable<BlockPos> farmerTreeArea = BlockPos.iterate(new BlockPos(pos.getX() - farmerRange, pos.getY(), pos.getZ() - farmerRange), new BlockPos(pos.getX() + farmerRange, pos.getY() + farmerRange, pos.getZ() + farmerRange));
        for (BlockPos pos : farmerTreeArea) {
            if(world.isAir(pos) || world.getBlockEntity(pos) != null || !(world.getBlockState(pos).getBlock() instanceof CropBlock || world.getBlockState(pos).getBlock() instanceof LeavesBlock || world.getBlockState(pos).getBlock() instanceof LogBlock)) {
                continue;
            }

            if(timer >= timerCounter) {
                if (!world.isClient) {
                    List<ItemStack> drops = new ArrayList<>();
                    if (world.getBlockState(pos).getBlock() instanceof CropBlock) {
                        CropBlock block = (CropBlock) world.getBlockState(pos).getBlock();
                        if (block.isMature(world.getBlockState(pos))) {
                            drops = CropBlock.getDroppedStacks(world.getBlockState(pos), (ServerWorld) world, pos, null);
                            world.setBlockState(pos, Blocks.AIR.getDefaultState());
                        }else {
                            continue;
                        }
                    } else if (world.getBlockState(pos).getBlock() instanceof LeavesBlock) {
                        drops = PlantBlock.getDroppedStacks(world.getBlockState(pos), (ServerWorld) world, pos, null);
                        world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    } else if (world.getBlockState(pos).getBlock() instanceof LogBlock) {
                        drops.add(new ItemStack(world.getBlockState(pos).getBlock()));
                        world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    }

                    for (ItemStack stack : drops) {
                        insertItem(stack, (stack.getItem() instanceof AliasedBlockItem && ((AliasedBlockItem) stack.getItem()).getBlock() instanceof CropBlock) || ItemTags.SAPLINGS.contains(stack.getItem()));
                    }

                    timer = 0;
                }
            }
        }
    }

    public void autoPlant() {
        Iterable<BlockPos> farmerPlantArea = BlockPos.iterate(new BlockPos(pos.getX() - farmerRange, pos.getY(), pos.getZ() - farmerRange), new BlockPos(pos.getX() + farmerRange, pos.getY(), pos.getZ() + farmerRange));

        for (int i : seedsSlot) {
            for (BlockPos blockPos : farmerPlantArea) {
                if (!world.isAir(blockPos) || inventory.get(i).isEmpty()) {
                    continue;
                }

                if(plant(blockPos, inventory.get(i))) {
                    inventory.get(i).subtractAmount(1);
                }
            }
        }
    }

    public boolean plant(BlockPos plantingPos, ItemStack stack) {
        if (ItemTags.SAPLINGS.contains(stack.getItem())) {
            for (Item saplingBlock : ItemTags.SAPLINGS.values()) {
                if (saplingBlock == stack.getItem() && !world.isAir(plantingPos.down())) {
                    world.setBlockState(plantingPos, ((BlockItem) saplingBlock).getBlock().getDefaultState());
                    return true;
                }
            }
        } else if (world.getBlockState(plantingPos.down()).getBlock() == Blocks.FARMLAND && (stack.getItem() instanceof AliasedBlockItem && ((AliasedBlockItem) stack.getItem()).getBlock() instanceof CropBlock)) {
            useOnBlock(plantingPos, stack.getItem());
            return true;
        }

        return false;
    }

    public void useOnBlock(BlockPos plantingPos, Item seedsItem) {
        if(seedsItem instanceof AliasedBlockItem && ((AliasedBlockItem) seedsItem).getBlock() instanceof CropBlock) {
            world.setBlockState(plantingPos, ((AliasedBlockItem) seedsItem).getBlock().getDefaultState(), 11);
        }
    }

    public void extractItems() {
        for (Direction direction : Direction.values()) {
            BlockPos offsetPosition = new BlockPos(pos).offset(direction);
            BlockEntity entity = world.getBlockEntity(offsetPosition);
            if (entity != null && entity instanceof Inventory) {
                Inventory nearbyInv = (Inventory) entity;
                for (int slot : outputSlots) {
                    if(!inventory.get(slot).isEmpty()) {
                        InventoryHelper.insertItemIfPossible(nearbyInv, inventory.get(slot), false);
                    }
                }
            }
        }
    }

    public boolean canRun() {
        if(storage.getEnergyStored() < costPerBlock || !canInsert() || world.isReceivingRedstonePower(pos)) {
           return false;
        }

        return true;
    }

    public boolean canInsert() {
        for (int i : outputSlots) {
            return inventory.get(i).isEmpty() || inventory.get(i).getAmount() < 64;
        }

        return false;
    }

    public void insertItem(ItemStack stack, boolean areSeeds) {
        if(areSeeds) {
            for (int slot : seedsSlot) {
                ItemStack slotStack = inventory.get(slot);
                if (slotStack.isEmpty()) {
                    inventory.set(slot, stack);
                    markDirty();
                    return;
                }else if(slotStack.getItem() == stack.getItem() && stack.getAmount() < 64) {
                    inventory.get(slot).addAmount(1);
                    markDirty();
                    return;
                }
            }
        }else {
            for (int slot : outputSlots) {
                ItemStack slotStack = inventory.get(slot);
                if(slotStack.isEmpty()) {
                    inventory.set(slot, stack);
                    markDirty();
                    return;
                }else if(slotStack.getItem() == stack.getItem() && slotStack.getAmount() < 64) {
                    inventory.get(slot).addAmount(1);
                    markDirty();
                    return;
                }
            }
        }
    }

    @Override
    public int[] getInvAvailableSlots(Direction direction) {
        return new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    }

    @Override
    public boolean canInsertInvStack(int i, ItemStack itemStack, @Nullable Direction direction) {
        return i == 1 || i == 2 || i == 3 || i == 4;
    }

    @Override
    public boolean canExtractInvStack(int i, ItemStack itemStack, Direction direction) {
        return i == 5 || i == 6 || i == 7 || i == 8 || i == 9 || i == 10 || i == 11;
    }

    @Override
    public int getInvSize() {
        return inventory.size();
    }

    @Override
    public ItemStack getInvStack(int i) {
        return inventory.get(i);
    }

    @Override
    public ItemStack removeInvStack(int i) {
        return Inventories.removeStack(this.inventory, i);
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
        this.markDirty();
    }

    @Override
    public ItemStack takeInvStack(int i, int i1) {
        return Inventories.splitStack(this.inventory, i, i1);
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public void link(PlayerEntity player, CompoundTag tag) {
        ItemHelper.linkBlockPos(world, pos, player, tag);
    }
}
