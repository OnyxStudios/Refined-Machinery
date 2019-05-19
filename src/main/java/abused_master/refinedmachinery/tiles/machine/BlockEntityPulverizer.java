package abused_master.refinedmachinery.tiles.machine;

import abused_master.abusedlib.tiles.BlockEntityBase;
import abused_master.refinedmachinery.registry.ModBlockEntities;
import abused_master.refinedmachinery.registry.PulverizerRecipes;
import abused_master.refinedmachinery.utils.ItemHelper;
import abused_master.refinedmachinery.utils.linker.ILinkerHandler;
import nerdhub.cardinalenergy.api.IEnergyHandler;
import nerdhub.cardinalenergy.impl.EnergyStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.math.Direction;

import javax.annotation.Nullable;
import java.util.Iterator;

public class BlockEntityPulverizer extends BlockEntityBase implements IEnergyHandler, SidedInventory, ILinkerHandler {

    public EnergyStorage storage = new EnergyStorage(100000);
    public DefaultedList<ItemStack> inventory = DefaultedList.create(3, ItemStack.EMPTY);
    private int upgradeTier = 1;
    private int pulverizeTime = 0;
    private int baseEnergyUsage = 400;

    public BlockEntityPulverizer() {
        super(ModBlockEntities.PULVERIZER);
    }

    @Override
    public void fromTag(CompoundTag nbt) {
        super.fromTag(nbt);
        this.upgradeTier = nbt.getInt("upgradeTier");
        this.pulverizeTime = nbt.getInt("pulverizeTime");
        this.storage.readEnergyFromTag(nbt);

        inventory = DefaultedList.create(3, ItemStack.EMPTY);
        Inventories.fromTag(nbt, this.inventory);
    }

    @Override
    public CompoundTag toTag(CompoundTag nbt) {
        super.toTag(nbt);
        nbt.putInt("upgradeTier", upgradeTier);
        nbt.putInt("pulverizeTime", this.pulverizeTime);
        this.storage.writeEnergyToTag(nbt);
        Inventories.toTag(nbt, this.inventory);
        return nbt;
    }

    @Override
    public void tick() {
        if(canRun()) {
            pulverizeTime++;
            if(pulverizeTime >= getTotalPulverizeTime()) {
                pulverizeTime = 0;
                pulverizeItem();
                world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
            }
        }else if(!canRun() && pulverizeTime > 0) {
            pulverizeTime = 0;
        }
    }

    public boolean canRun() {
        PulverizerRecipes.PulverizerRecipe recipe = PulverizerRecipes.INSTANCE.getOutputRecipe(inventory.get(0));

        if(inventory.get(0).isEmpty() || recipe == null || recipe.getOutput().isEmpty() || storage.getEnergyStored() < getEnergyUsage()) {
            return false;
        }else if(!inventory.get(1).isEmpty()) {
            if(recipe.getOutput().getItem() != inventory.get(1).getItem() || (inventory.get(1).getAmount() + recipe.getOutputAmount()) > 64) {
                return false;
            }
        }else if(!inventory.get(2).isEmpty() && !recipe.getOutput().isEmpty()) {
            if(recipe.getRandomDrop().getItem() != inventory.get(2).getItem() || (inventory.get(2).getAmount() + recipe.getRandomDropAmoumt()) > 64) {
                return false;
            }
        }

        return true;
    }

    public void pulverizeItem() {
        if(!world.isClient) {
            PulverizerRecipes.PulverizerRecipe recipe = PulverizerRecipes.INSTANCE.getOutputRecipe(inventory.get(0));
            if (inventory.get(1).isEmpty()) {
                inventory.set(1, new ItemStack(recipe.getOutput().getItem(), recipe.getOutputAmount()));
            } else {
                inventory.get(1).addAmount(recipe.getOutputAmount());
            }

            if (!recipe.getRandomDrop().isEmpty()) {
                float chance = world.getRandom().nextFloat() * 100;
                if (chance <= recipe.getPercentageDrop()) {
                    if (inventory.get(2).isEmpty()) {
                        inventory.set(2, new ItemStack(recipe.getRandomDrop().getItem(), recipe.getRandomDropAmoumt()));
                    } else {
                        inventory.get(2).addAmount(recipe.getRandomDropAmoumt());
                    }
                }
            }

            inventory.get(0).subtractAmount(1);
        }

        storage.extractEnergy(getEnergyUsage());
    }

    public int getEnergyUsage() {
        return baseEnergyUsage * upgradeTier;
    }

    public int getPulverizeTime() {
        return pulverizeTime;
    }

    public int getTotalPulverizeTime() {
        return 120 / this.upgradeTier;
    }

    @Override
    public int[] getInvAvailableSlots(Direction direction) {
        return new int[] {0, 1, 2};
    }

    @Override
    public boolean canInsertInvStack(int i, ItemStack itemStack, @Nullable Direction direction) {
        return i != 1 && i != 2;
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
