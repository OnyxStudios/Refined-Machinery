package abused_master.refinedmachinery.tiles.machine;

import abused_master.abusedlib.tiles.BlockEntityBase;
import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.registry.ModBlockEntities;
import abused_master.refinedmachinery.utils.linker.ILinkerHandler;
import nerdhub.cardinalenergy.api.IEnergyHandler;
import nerdhub.cardinalenergy.impl.EnergyStorage;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.InfoEnchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.StringTextComponent;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.TagHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;
import java.util.Iterator;

public class BlockEntityDisenchanter extends BlockEntityBase implements IEnergyHandler, SidedInventory, ILinkerHandler {

    public EnergyStorage storage = new EnergyStorage(100000);
    public DefaultedList<ItemStack> inventory = DefaultedList.create(7, ItemStack.EMPTY);

    public int costPerEnchant = RefinedMachinery.config.getInt("disenchanter_cost");
    public int requiredTime = 250;
    public int workTime = 0;

    public BlockEntityDisenchanter() {
        super(ModBlockEntities.DISENCHANTER);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        this.storage.readEnergyFromTag(tag);
        inventory = DefaultedList.create(7, ItemStack.EMPTY);
        Inventories.fromTag(tag, this.inventory);
        workTime = tag.getInt("workTime");
        requiredTime = tag.getInt("requiredTime");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        this.storage.writeEnergyToTag(tag);
        Inventories.toTag(tag, this.inventory);
        tag.putInt("workTime", this.workTime);
        tag.putInt("requiredTime", this.requiredTime);
        return tag;
    }

    @Override
    public void tick() {
        super.tick();
        if(canRun()) {
            workTime++;

            if(workTime >= requiredTime) {
                this.run();
                workTime = 0;
            }
        }else if(!canRun() && inventory.get(0).isEmpty()) {
            this.workTime = 0;
        }
    }

    public boolean canRun() {
        if(inventory.get(0).isEmpty() || EnchantmentHelper.getEnchantments(inventory.get(0)).size() <= 0 || storage.getEnergyStored() < costPerEnchant || inventory.get(0).getItem() == Items.ENCHANTED_BOOK) {
            return false;
        }

        int empty = 0;
        for (int i = 1; i < inventory.size(); i++) {
            if(inventory.get(i).isEmpty()) {
                empty++;
            }
        }

        return empty > 0;
    }

    public void run() {
        ItemStack input = inventory.get(0);
        ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);

        ListTag listTag = getEnchantments(input);

        if(listTag.size() > 0) {
            CompoundTag tag = (CompoundTag) listTag.get(0);

            Registry.ENCHANTMENT.getOrEmpty(Identifier.create(tag.getString("id"))).ifPresent((enchantment) -> {
                EnchantedBookItem.addEnchantment(output, new InfoEnchantment(enchantment, tag.getInt("lvl")));
                listTag.remove(tag);
                input.getTag().put("Enchantments", listTag);
            });

            for (int i = 1; i < inventory.size(); i++) {
                if (inventory.get(i).isEmpty()) {
                    inventory.set(i, output);
                    break;
                }
            }

            this.storage.extractEnergy(costPerEnchant);
        }

        this.updateEntity();
    }

    public ListTag getEnchantments(ItemStack stack) {
        if (stack.getTag() != null && stack.getTag().containsKey("Enchantments", 9)) {
            return stack.getTag().getList("Enchantments", 10);
        } else {
            return new ListTag();
        }
    }

    @Override
    public int[] getInvAvailableSlots(Direction direction) {
        return new int[] {0, 1, 2, 3, 4, 5, 6};
    }

    @Override
    public boolean canInsertInvStack(int i, ItemStack itemStack, @Nullable Direction direction) {
        return i == 0;
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

    @Override
    public void link(PlayerEntity player, CompoundTag tag) {
        if (tag.containsKey("collectorPos")) {
            tag.remove("collectorPos");
        }

        tag.put("blockPos", TagHelper.serializeBlockPos(pos));
        player.addChatMessage(new StringTextComponent("Saved block position!"), true);
    }
}
