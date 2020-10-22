package fail.mercury.client.api.mixin;

import fail.mercury.client.Mercury;
import fail.mercury.client.api.util.Rotation;
import fail.mercury.client.client.events.RotationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author auto on 4/2/2020
 */
@Mixin(RenderLivingBase.class)
public class MixinRenderLivingBase {

    private float yaw, pitch, yawOffset, prevYaw, prevPitch, prevYawOffset;

    @Inject(method = {"doRender"}, at = {@At("HEAD")})
    private void preHeadRotation(final EntityLivingBase entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks, final CallbackInfo info) {
        if (entity == Minecraft.getMinecraft().player) {
            this.yaw = entity.rotationYawHead;
            this.pitch = entity.rotationPitch;
            this.prevYaw = entity.prevRotationYawHead;
            this.prevPitch = entity.prevRotationPitch;
            this.yawOffset = entity.renderYawOffset;
            this.prevYawOffset = entity.prevRenderYawOffset;

            RotationEvent event = new RotationEvent(new Rotation(yaw, pitch), new Rotation(prevYaw, prevPitch), yawOffset, prevYawOffset);
            Mercury.INSTANCE.getEventManager().fireEvent(event);

            entity.rotationYawHead = event.getRotation().getYaw();
            entity.rotationPitch = event.getRotation().getPitch();
            entity.prevRotationYawHead = event.getPrevRotation().getYaw();
            entity.prevRotationPitch = event.getPrevRotation().getPitch();
            entity.renderYawOffset = event.getRenderYawOffset();
            entity.prevRenderYawOffset = event.getPrevRenderYawOffset();

        }
    }

    @Inject(method = {"doRender"}, at = {@At("TAIL")})
    private void postHeadRotation(final EntityLivingBase entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks, final CallbackInfo info) {
        if (entity == Minecraft.getMinecraft().player) {
            entity.rotationYawHead = yaw;
            entity.rotationPitch = pitch;
            entity.prevRotationYawHead = prevYaw;
            entity.prevRotationPitch = prevPitch;
            entity.renderYawOffset = yawOffset;
            entity.prevRenderYawOffset = prevYawOffset;
        }
    }
}
