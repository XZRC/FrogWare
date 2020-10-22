package fail.mercury.client.api.mixin;

import fail.mercury.client.Mercury;
import fail.mercury.client.client.events.InsideBlockRenderEvent;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class MixinEntity {

    @Inject(method = "isEntityInsideOpaqueBlock",at = @At("HEAD"),cancellable = true)
    private void onIsEntityInsideOpaqueBlock(CallbackInfoReturnable<Boolean> cir) {
        InsideBlockRenderEvent event = new InsideBlockRenderEvent();
        Mercury.INSTANCE.getEventManager().fireEvent(event);
        if(event.isCancelled()) {
            cir.setReturnValue(false);
        }
    }

}
