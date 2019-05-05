package abused_master.refinedmachinery.mixins;

import abused_master.refinedmachinery.client.render.WingsFeatureRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer {

    @Shadow protected abstract boolean addFeature(FeatureRenderer<LivingEntity, EntityModel<LivingEntity>> featureRenderer);

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(EntityRenderDispatcher renderDispatcher, EntityModel model, float float_1, CallbackInfo ci) {
        this.addFeature(new WingsFeatureRenderer((LivingEntityRenderer) (Object) this));
    }
}
