package abused_master.refinedmachinery.tiles.generator;

import abused_master.abusedlib.tiles.BlockEntityBase;
import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.registry.ModBlockEntities;
import abused_master.refinedmachinery.utils.EnergyHelper;
import nerdhub.cardinalenergy.api.IEnergyHandler;
import nerdhub.cardinalenergy.impl.EnergyStorage;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.math.Direction;

import javax.annotation.Nullable;
import java.util.Iterator;

public class BlockEntityCoalGen extends BlockEntityBase implements SidedInventory, IEnergyHandler {

    public EnergyStorage storage = new EnergyStorage(100000);
    public int sendPerTick = 500;
    public int generatePerTick = RefinedMachinery.config.getInt("coalGen_Generation");
    public int burnTime;
    public int burnTotalTime;

    public DefaultedList<ItemStack> inventory = DefaultedList.create(1, ItemStack.EMPTY);

    public BlockEntityCoalGen() {
        super(ModBlockEntities.COALGEN);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        this.storage.readEnergyFromTag(tag);
        inventory = DefaultedList.create(1, ItemStack.EMPTY);
        Inventories.fromTag(tag, this.inventory);
        this.burnTime = tag.getInt("burnTime");
        this.burnTotalTime = tag.getInt("burnTotalTime");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        this.storage.writeEnergyToTag(tag);
        Inventories.toTag(tag, this.inventory);
        tag.putInt("burnTime", this.burnTime);
        tag.putInt("burnTotalTime", this.burnTotalTime);
        return tag;
    }

    @Override
    public void tick() {
        super.tick();
        if((storage.getEnergyStored() + generatePerTick) <= storage.getEnergyCapacity()) {
            if (burnTime > 0) {
                burnTime--;
                if(burnTime == 0) {
                    burnTotalTime = 0;
                }
                this.storage.receiveEnergy(generatePerTick);
                this.markDirty();
            } else if (FurnaceBlockEntity.canUseAsFuel(inventory.get(0))) {
                burnTotalTime = FurnaceBlockEntity.createFuelTimeMap().getOrDefault(inventory.get(0).getItem(), 0);
                burnTime = burnTotalTime;
                inventory.get(0).subtractAmount(1);
                this.markDirty();
            }
        }

        EnergyHelper.sendEnergy(storage, world, pos, sendPerTick);
        this.markDirty();
    }

    @Override
    public int[] getInvAvailableSlots(Direction direction) {
        return new int[] {0};
    }

    @Override
    public boolean canInsertInvStack(int i, ItemStack itemStack, @Nullable Direction direction) {
        return FurnaceBlockEntity.canUseAsFuel(itemStack);
    }

    @Override
    public boolean canExtractInvStack(int i, ItemStack itemStack, Direction direction) {
        return true;
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
        this.markDirty();
    }

    @Override
    public ItemStack takeInvStack(int i, int i1) {
        return Inventories.splitStack(this.inventory, i, i1);
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
    public void clear() {
        inventory.clear();
    }

    @Override
    public EnergyStorage getEnergyStorage(Direction direction) {
        return storage;
    }
}
