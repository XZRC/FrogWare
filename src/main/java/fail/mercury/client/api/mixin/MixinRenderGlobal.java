package fail.mercury.client.api.mixin;

import fail.mercury.client.Mercury;
import fail.mercury.client.client.events.OutlineEvent;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author auto on 2/29/2020
 */
@Mixin(RenderGlobal.class)
public class MixinRenderGlobal {

    @Inject(method = "isOutlineActive(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;Lnet/minecraft/client/renderer/culling/ICamera;)Z", at = @At("HEAD"), cancellable = true)
    private void onOutlineActive(Entity entityIn, Entity viewer, ICamera camera, CallbackInfoReturnable<Boolean> cir) {
        OutlineEvent event = new OutlineEvent(entityIn);
        Mercury.INSTANCE.getEventManager().fireEvent(event);
        if (event.isCancelled()) {
            cir.setReturnValue(true);
        }
    }


}
