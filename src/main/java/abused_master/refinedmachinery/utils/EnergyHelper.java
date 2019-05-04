package abused_master.refinedmachinery.utils;

import nerdhub.cardinalenergy.impl.EnergyStorage;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class EnergyHelper {

    public static void sendEnergy(EnergyStorage storage, World world, BlockPos pos, int sendAmount) {
        for (Direction direction : Direction.values()) {
            BlockPos offsetPos = pos.offset(direction);
            storage.sendEnergy(world, offsetPos, sendAmount);
        }
    }
}
