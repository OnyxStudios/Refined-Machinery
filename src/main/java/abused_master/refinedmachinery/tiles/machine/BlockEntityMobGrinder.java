package abused_master.refinedmachinery.tiles.machine;

import abused_master.abusedlib.client.render.hud.IHudSupport;
import abused_master.abusedlib.tiles.BlockEntityBase;
import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.blocks.machines.BlockMobGrinder;
import abused_master.refinedmachinery.registry.ModBlockEntities;
import abused_master.refinedmachinery.utils.ItemHelper;
import abused_master.refinedmachinery.utils.linker.ILinkerHandler;
import nerdhub.cardinalenergy.api.IEnergyHandler;
import nerdhub.cardinalenergy.impl.EnergyStorage;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

import java.util.Collections;
import java.util.List;

public class BlockEntityMobGrinder extends BlockEntityBase implements IEnergyHandler, IHudSupport, ILinkerHandler {

    public EnergyStorage storage = new EnergyStorage(100000);
    public Box mobKillBox = null;
    public int killTimer = 0;
    public int costPerHeart = RefinedMachinery.config.getInt("grinderCostPerHeart");

    public BlockEntityMobGrinder() {
        super(ModBlockEntities.MOB_GRINDER);
    }

    @Override
    public void fromTag(CompoundTag nbt) {
        super.fromTag(nbt);
        this.storage.fromTag(nbt);
        killTimer = nbt.getInt("killTimer");
    }

    @Override
    public CompoundTag toTag(CompoundTag nbt) {
        super.toTag(nbt);
        this.storage.toTag(nbt);
        nbt.putInt("killTimer", this.killTimer);
        return nbt;
    }

    @Override
    public void tick() {
        if(mobKillBox == null) {
            updateOrientation();
        }

        if(storage.getEnergyStored() >= costPerHeart) {
            killTimer++;
            if (killTimer >= 20) {
                killMobs();
            }
        }
    }

    public void killMobs() {
        LivingEntity target = getTarget();

        if(target == null) {
            return;
        }

        int totalCost = (int) (target.getHealth() * costPerHeart);
        if(storage.getEnergyStored() >= totalCost) {
            target.damage(DamageSource.MAGIC, target.getHealth());
            storage.extractEnergy(totalCost);
        }else {
            target.damage(DamageSource.GENERIC, 1);
            storage.extractEnergy(costPerHeart);
        }
        this.markDirty();
        this.world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        killTimer = 0;
    }

    public LivingEntity getTarget() {
        List<LivingEntity> entitiesInRange = world.getEntities(LivingEntity.class, mobKillBox);

        for (LivingEntity entity : entitiesInRange) {
            if(entity != null && !(entity instanceof PlayerEntity) && !entity.isInvulnerable()) {
                return entity;
            }
        }

        return null;
    }

    public void updateOrientation() {
        Direction direction = world.getBlockState(pos).get(BlockMobGrinder.FACING);
        BlockPos pos1 = pos.add(-3, -3, -3).add(direction.getOffsetX() * 4, 0, direction.getOffsetZ() * 4);
        BlockPos pos2 = pos.add(4, 4, 4).add(direction.getOffsetX() * 4, 0, direction.getOffsetZ() * 4);
        mobKillBox = new Box(pos1, pos2);
    }

    @Override
    public Direction getBlockOrientation() {
        return null;
    }

    @Override
    public boolean isBlockAboveAir() {
        return getWorld().isAir(pos.up());
    }

    @Override
    public List<String> getClientLog() {
        return Collections.singletonList("Energy: " + storage.getEnergyStored() + " / " + storage.getCapacity() + " CE");
    }

    @Override
    public BlockPos getBlockPos() {
        return getPos();
    }

    @Override
    public void link(PlayerEntity player, CompoundTag tag) {
        ItemHelper.linkBlockPos(world, pos, player, tag);
    }
}
