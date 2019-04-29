package abused_master.refinedmachinery.blocks.machines;

import abused_master.abusedlib.blocks.BlockBase;
import abused_master.refinedmachinery.RefinedMachinery;
import net.minecraft.block.Material;

public class BlockMachineFrame extends BlockBase {

    public BlockMachineFrame() {
        super("machine_frame", Material.STONE, 1.0f, RefinedMachinery.modItemGroup);
    }
}
