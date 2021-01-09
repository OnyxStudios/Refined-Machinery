package dev.onyxstudios.refinedmachinery.utils;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyUtils {

    public static void transferEnergy(World world, BlockPos genPos, RMEnergyStorage energyStorage) {
        for (Direction direction : Direction.values()) {
            BlockPos pos = genPos.offset(direction);

            if(world.getTileEntity(pos) != null) {
                LazyOptional<IEnergyStorage> energyOptional = world.getTileEntity(pos).getCapability(CapabilityEnergy.ENERGY, direction.getOpposite());

                energyOptional.ifPresent(storage -> {
                    int simulated = energyStorage.extractEnergy(energyStorage.getMaxExtract(), true);
                    int received = storage.receiveEnergy(simulated, false);
                    energyStorage.extractEnergy(received, false);
                });
            }
        }
    }
}
