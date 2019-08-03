package abused_master.refinedmachinery.tiles.machine;

import abused_master.abusedlib.tiles.BlockEntityBase;
import abused_master.refinedmachinery.items.EnumResourceItems;
import abused_master.refinedmachinery.registry.ModBlockEntities;
import abused_master.refinedmachinery.registry.ModItems;
import abused_master.refinedmachinery.utils.EnergyHelper;
import abused_master.refinedmachinery.utils.ItemHelper;
import abused_master.refinedmachinery.utils.linker.ILinkerHandler;
import nerdhub.cardinalenergy.api.IEnergyHandler;
import nerdhub.cardinalenergy.api.IEnergyStorage;
import nerdhub.cardinalenergy.impl.EnergyStorage;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.math.Direction;

import javax.annotation.Nullable;
import java.util.Iterator;

public class BlockEntityEnergyCharger extends BlockEntityBase implements IEnergyHandler, SidedInventory, ILinkerHandler {

    public EnergyStorage storage = new EnergyStorage(50000);
    public DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
    public int chargePerTick = 50;
    public PropertyDelegate property = new PropertyDelegate() {
        @Override
        public int get(int i) {
            return i == 0 && EnergyHelper.getEnergyStorage(inventory.get(0)) != null ? EnergyHelper.getEnergyStorage(inventory.get(0)).getEnergyStored() : 0;
        }

        @Override
        public void set(int i, int i1) {
            if(i == 0 && EnergyHelper.getEnergyStorage(inventory.get(0)) != null) {
                EnergyHelper.getEnergyStorage(inventory.get(0)).setEnergyStored(i1);
            }
        }

        @Override
        public int size() {
            return 1;
        }
    };

    public BlockEntityEnergyCharger() {
        super(ModBlockEntities.ENERGY_CHARGER);
    }

    @Override
    public void fromTag(CompoundTag nbt) {
        super.fromTag(nbt);
        this.storage.fromTag(nbt);

        inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
        Inventories.fromTag(nbt, this.inventory);
    }

    @Override
    public CompoundTag toTag(CompoundTag nbt) {
        super.toTag(nbt);
        this.storage.toTag(nbt);
        Inventories.toTag(nbt, this.inventory);
        return nbt;
    }

    @Override
    public void tick() {
        if(!world.isClient) {
            ItemStack stack = inventory.get(0);
            IEnergyStorage itemStorage = EnergyHelper.getEnergyStorage(stack);

            if (!stack.isEmpty() && itemStorage != null && storage.canExtract(chargePerTick)) {

                if (itemStorage.getEnergyStored() < itemStorage.getCapacity()) {
                    storage.extractEnergy(itemStorage.receiveEnergy(chargePerTick));
                    property.set(0, itemStorage.getEnergyStored());
                    if (stack.isDamaged()) ItemHelper.updateItemDurability(stack, itemStorage);

                } else if (inventory.get(1).isEmpty()) {
                    inventory.set(0, ItemStack.EMPTY);
                    if (stack.getItem() == ModItems.STEEL_INGOT) {
                        inventory.set(1, new ItemStack(EnumResourceItems.ENERGIZED_STEEL_INGOT.getItemIngot(), stack.getCount()));
                    } else {
                        inventory.set(1, stack);
                    }
                }
            }
        }
    }

    @Override
    public int[] getInvAvailableSlots(Direction direction) {
        return new int[] {0, 1};
    }

    @Override
    public boolean canInsertInvStack(int i, ItemStack itemStack, @Nullable Direction direction) {
        return i != 1;
    }

    @Override
    public boolean canExtractInvStack(int i, ItemStack itemStack, Direction direction) {
        return i != 0;
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
        return true;
    }

    @Override
    public void clear() {
        inventory.clear();
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
    public void link(PlayerEntity player, CompoundTag tag) {
        ItemHelper.linkBlockPos(world, pos, player, tag);
    }
}
