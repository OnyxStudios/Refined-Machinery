package dev.onyxstudios.refinedmachinery.tileentity.generators;

import dev.onyxstudios.refinedmachinery.client.container.WindTurbineContainer;
import dev.onyxstudios.refinedmachinery.registry.ModBlocks;
import dev.onyxstudios.refinedmachinery.registry.ModEntities;
import dev.onyxstudios.refinedmachinery.tileentity.TileEntityBase;
import dev.onyxstudios.refinedmachinery.utils.EnergyUtils;
import dev.onyxstudios.refinedmachinery.utils.RMEnergyStorage;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;

public class TileEntityWindTurbine extends TileEntityBase implements ITickableTileEntity, INamedContainerProvider {

    //2 from the center which would be a 5x5
    public static int DEGRADE_RADIUS = 2;
    //10% Degrade per turbine
    public static float DEGRADE_AMOUNT = 0.1f;

    //2% Energy Increase Per Block after level 64
    public static float HEIGHT_UPGRADE = 0.02f;
    public static int Y_LEVEL = 64;

    public RMEnergyStorage storage;

    private int nearbyTurbines = 0;

    public TileEntityWindTurbine() {
        super(ModEntities.windTurbineTileType.get());
        storage = new RMEnergyStorage(100000, ModBlocks.windTurbineObject.get().maxExtract);
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        storage.read(compound);
        nearbyTurbines = compound.getInt("nearbyTurbines");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        storage.write(compound);
        compound.putInt("nearbyTurbines", nearbyTurbines);

        return compound;
    }

    @Override
    public void tick() {
        if (world.isRemote)
            return;

        int genAmount = getGenAmount();
        storage.receiveEnergy(genAmount, false);
        EnergyUtils.transferEnergy(world, pos, storage);
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
    }

    public int getGenAmount() {
        float genAmount = ModBlocks.windTurbineObject.get().productionAmount;
        if (getPos().getY() > Y_LEVEL)
            genAmount += (genAmount * HEIGHT_UPGRADE) * (getPos().getY() - Y_LEVEL);

        genAmount -= (genAmount * DEGRADE_AMOUNT) * nearbyTurbines;

        return Math.max((int) genAmount, 10);
    }

    public void checkNearbyTurbines() {
        nearbyTurbines = 0;
        Iterable<BlockPos> blocks = BlockPos.getAllInBoxMutable(this.getPos().add(-DEGRADE_RADIUS, 0, -DEGRADE_RADIUS), this.getPos().add(DEGRADE_RADIUS, 3, DEGRADE_RADIUS));

        for (BlockPos radiusPos : blocks) {
            if (!radiusPos.equals(pos) && world.getTileEntity(radiusPos) instanceof TileEntityWindTurbine) {
                addTurbine();
                ((TileEntityWindTurbine) world.getTileEntity(radiusPos)).addTurbine();
            }
        }

        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
    }

    public void updateNearbyTurbines() {
        Iterable<BlockPos> blocks = BlockPos.getAllInBoxMutable(this.getPos().add(-DEGRADE_RADIUS, 0, -DEGRADE_RADIUS), this.getPos().add(DEGRADE_RADIUS, 3, DEGRADE_RADIUS));

        for (BlockPos radiusPos : blocks) {
            if (!radiusPos.equals(pos) && world.getTileEntity(radiusPos) instanceof TileEntityWindTurbine) {
                removeTurbine();
                ((TileEntityWindTurbine) world.getTileEntity(radiusPos)).removeTurbine();
            }
        }

        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
    }

    public void addTurbine() {
        nearbyTurbines++;
    }

    public void removeTurbine() {
        if (nearbyTurbines > 0)
            nearbyTurbines--;
    }

    public int getNearbyTurbines() {
        return nearbyTurbines;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return LazyOptional.of(() -> storage).cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public ITextComponent getDisplayName() {
        return this.getBlockState().getBlock().getTranslatedName();
    }

    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new WindTurbineContainer(id, playerInventory, this);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos, pos.add(1, 3, 1));
    }
}
