package abused_master.refinedmachinery.items;

import abused_master.abusedlib.items.ItemBase;
import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.RefinedMachineryClient;
import abused_master.refinedmachinery.utils.wrench.IWrenchable;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemWrench extends ItemBase {

    public ItemWrench() {
        super("wrench", new Settings().group(RefinedMachineryClient.modItemGroup).maxCount(1));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState block = world.getBlockState(pos);

        if(block.getBlock() instanceof IWrenchable) {
            return ((IWrenchable) block.getBlock()).onWrenched(world, pos, block, context.getPlayer()) ? ActionResult.SUCCESS : ActionResult.PASS;
        }

        return super.useOnBlock(context);
    }
}
