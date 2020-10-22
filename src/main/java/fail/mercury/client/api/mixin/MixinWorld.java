package fail.mercury.client.api.mixin;

import fail.mercury.client.Mercury;
import fail.mercury.client.client.events.PlayerEvent;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = World.class, priority = 1001)
public class MixinWorld {

    @Inject(method = "onEntityAdded", at = @At("HEAD"))
    private void onEntityAdded(Entity entityIn, CallbackInfo callbackInfo) {
        PlayerEvent event = new PlayerEvent(PlayerEvent.Type.ENTERING, entityIn);
        Mercury.INSTANCE.getEventManager().fireEvent(event);
    }

    @Inject(method = "onEntityRemoved", at = @At("HEAD"))
    private void onEntityRemoved(Entity entityIn, CallbackInfo callbackInfo) {
        PlayerEvent event = new PlayerEvent(PlayerEvent.Type.EXITING, entityIn);
        Mercury.INSTANCE.getEventManager().fireEvent(event);
    }

}
