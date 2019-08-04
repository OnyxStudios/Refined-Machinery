package abused_master.refinedmachinery.client;

import abused_master.refinedmachinery.registry.ModItems;
import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

import java.util.Iterator;

import static abused_master.refinedmachinery.items.ItemBlockExchanger.getPosArea;
import static abused_master.refinedmachinery.items.ItemBlockExchanger.getRange;

public class RenderHelper {

    @Environment(EnvType.CLIENT)
    public static void drawBlockOutline(PlayerEntity playerEntity, Camera camera_1, int int_1) {
        if(playerEntity.getMainHandStack().getItem() == ModItems.EXCHANGER) {
            HitResult result = playerEntity.rayTrace(5, 1, false);
            if (result.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHitResult = (BlockHitResult) result;
                BlockPos pos = blockHitResult.getBlockPos();
                BlockState state = playerEntity.world.getBlockState(pos);

                for (Iterator<BlockPos> it = getPosArea(blockHitResult.getSide(), pos, getRange(playerEntity.getMainHandStack())).iterator(); it.hasNext(); ) {
                    BlockPos blockPos = it.next();
                    BlockState blockState = playerEntity.world.getBlockState(blockPos);
                    if (playerEntity.world.isAir(blockPos) || playerEntity.world.getBlockEntity(blockPos) != null || blockState.getBlock() instanceof FluidBlock || blockState.getBlock() == Blocks.BEDROCK || blockState.getBlock() != state.getBlock()) {
                        continue;
                    }

                    GlStateManager.enableBlend();
                    GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    GlStateManager.lineWidth(Math.max(5.5F, MinecraftClient.getInstance().window.getFramebufferWidth() / 1920.0F * 2.5F));
                    GlStateManager.disableTexture();
                    GlStateManager.depthMask(false);
                    GlStateManager.matrixMode(5889);
                    GlStateManager.pushMatrix();
                    GlStateManager.scalef(1.0F, 1.0F, 0.999F);
                    double double_1 = camera_1.getPos().x;
                    double double_2 = camera_1.getPos().y;
                    double double_3 = camera_1.getPos().z;
                    WorldRenderer.drawShapeOutline(blockState.getOutlineShape(playerEntity.world, blockPos, EntityContext.of(camera_1.getFocusedEntity())), (double)blockPos.getX() - double_1, (double)blockPos.getY() - double_2, (double)blockPos.getZ() - double_3, 0, 191 / 255f, 255 / 255f, 5);
                    GlStateManager.popMatrix();
                    GlStateManager.matrixMode(5888);
                    GlStateManager.depthMask(true);
                    GlStateManager.enableTexture();
                    GlStateManager.disableBlend();
                }
            }
        }
    }
}
