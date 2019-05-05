package abused_master.refinedmachinery.client.render;

import abused_master.refinedmachinery.RefinedMachinery;
import abused_master.refinedmachinery.client.model.RoboWingsModel;
import abused_master.refinedmachinery.registry.ModItems;
import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class WingsFeatureRenderer <T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {

    public Identifier WINGS_TEXTURE = new Identifier(RefinedMachinery.MODID, "textures/models/wings.png");

    public WingsFeatureRenderer(FeatureRendererContext<T, M> featureRendererContext_1) {
        super(featureRendererContext_1);
    }

    @Override
    public void render(T entity, float float_1, float float_2, float float_3, float float_4, float float_5, float float_6, float float_7) {
        ItemStack stack = entity.getEquippedStack(EquipmentSlot.CHEST);
        if(stack.getItem() == ModItems.ROBOTIC_WINGS) {
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            this.bindTexture(WINGS_TEXTURE);

            GlStateManager.pushMatrix();
            GlStateManager.translatef(0.0F, 0.0F, 0.125F);
            RoboWingsModel.INSTANCE.render(entity, float_1, float_2, float_4, float_5, float_6, float_7);

            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean hasHurtOverlay() {
        return false;
    }
}
