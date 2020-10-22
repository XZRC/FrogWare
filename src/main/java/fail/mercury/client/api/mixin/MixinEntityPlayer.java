package fail.mercury.client.api.mixin;

import fail.mercury.client.Mercury;
import fail.mercury.client.client.events.PushEvent;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public class MixinEntityPlayer {

    @Inject(method = "isPushedByWater",at = @At("HEAD"),cancellable = true)
    private void onPushedByWater(CallbackInfoReturnable<Boolean> cir) {
        PushEvent event = new PushEvent(PushEvent.Type.LIQUID);
        Mercury.INSTANCE.getEventManager().fireEvent(event);
        if(event.isCancelled()) {
            cir.setReturnValue(false);
        }
    }

}
