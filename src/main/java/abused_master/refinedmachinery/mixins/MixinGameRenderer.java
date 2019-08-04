package abused_master.refinedmachinery.mixins;

import abused_master.refinedmachinery.client.RenderHelper;
import abused_master.refinedmachinery.items.ItemBlockExchanger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    @Final
    private Camera camera;

    @Inject(at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = "ldc=entities", shift = At.Shift.BEFORE), method = "renderCenter")
    private void renderCenter(float deltaTime, long long_1, CallbackInfo ci) {
        this.client.getProfiler().push("refinedmachinery:renderExchanger");
        RenderHelper.drawBlockOutline(client.player, camera, 0);
        this.client.getProfiler().pop();
    }
}
