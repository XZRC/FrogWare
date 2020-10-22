package fail.mercury.client.api.mixin;

import com.mojang.authlib.GameProfile;
import fail.mercury.client.Mercury;
import fail.mercury.client.api.util.Location;
import fail.mercury.client.api.util.Rotation;
import fail.mercury.client.client.events.MotionEvent;
import fail.mercury.client.client.events.PushEvent;
import fail.mercury.client.client.events.UpdateEvent;
import fail.mercury.client.client.events.*;
import net.b0at.api.event.types.EventTiming;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.MoverType;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityPlayerSP.class, priority=1001)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {

    @Shadow
    @Final
    public NetHandlerPlayClient connection;

    @Shadow
    @Final
    private double lastReportedPosX;
    @Shadow
    private double lastReportedPosY;
    @Shadow
    private double lastReportedPosZ;
    @Shadow
    private float lastReportedYaw;
    @Shadow
    private float lastReportedPitch;
    @Shadow
    private boolean serverSprintState;
    @Shadow
    private boolean serverSneakState;
    @Shadow
    private boolean prevOnGround;
    @Shadow
    private int positionUpdateTicks;
    @Shadow
    protected Minecraft mc;
    @Shadow
    private boolean autoJumpEnabled;

    @Shadow
    public MovementInput movementInput;

    public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }
    @Shadow
    public abstract boolean isSneaking();

    @Shadow
    protected abstract boolean isCurrentViewEntity();

    private Location location;
    private Rotation rotation;


    @Override
    public void move(MoverType type, double x, double y, double z) {
        MotionEvent event = new MotionEvent(x, y, z);
        Mercury.INSTANCE.getEventManager().fireEvent(event);
        super.move(type, event.getX(), event.getY(), event.getZ());
    }

    @Inject(method = "pushOutOfBlocks",at = @At("HEAD"),cancellable = true)
    private void onPushOutOfBlocks(double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        PushEvent event = new PushEvent(PushEvent.Type.BLOCK);
        Mercury.INSTANCE.getEventManager().fireEvent(event);
        if (event.isCancelled()) {
            cir.setReturnValue(false);
        }
    }
    /*
    @Author 4ChanUser20 on 2/5/20
     */
    @Inject(method = "onUpdateWalkingPlayer", at =@At("HEAD"), cancellable = true)
    public void onStartUpdateWalkingPlayer(CallbackInfo ci){
        final UpdateEvent event = new UpdateEvent(EventTiming.PRE, getLocation(), getRotation());
        Mercury.INSTANCE.getEventManager().fireEvent(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
    @Inject(method = "onUpdateWalkingPlayer", at =@At("RETURN"))
    public void onEndUpdateWalkingPlayer(CallbackInfo ci){
        Mercury.INSTANCE.getEventManager().fireEvent(new UpdateEvent(EventTiming.POST, getLocation(), getRotation()));
    }

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;posX:D"))
    private double onUpdateWalkingPlayerPosX(EntityPlayerSP player) {
        return location.getX();
    }

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/util/math/AxisAlignedBB;minY:D"))
    private double onUpdateWalkingPlayerMinY(AxisAlignedBB boundingBox) {
        return location.getY();
    }

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;posZ:D"))
    private double onUpdateWalkingPlayerPosZ(EntityPlayerSP player) {
        return location.getZ();
    }

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;onGround:Z"))
    private boolean onUpdateWalkingPlayerOnGround(EntityPlayerSP player) {
        return location.isOnGround();
    }

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;rotationYaw:F"))
    private float onUpdateWalkingPlayerRotationYaw(EntityPlayerSP player) {
        return rotation.getYaw();
    }

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;rotationPitch:F"))
    private float onUpdateWalkingPlayerRotationPitch(EntityPlayerSP player) {
        return rotation.getPitch();
    }

    public Location getLocation() {
        if (location == null)
            location = new Location(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.onGround);
        location.setX(mc.player.posX);
        location.setY(mc.player.posY);
        location.setZ(mc.player.posZ);
        location.setOnGround(mc.player.onGround);
        return location;
    }

    public Rotation getRotation() {
        if (rotation == null)
            rotation = new Rotation(mc.player.rotationYaw, mc.player.rotationPitch);
        rotation.setYaw(mc.player.rotationYaw);
        rotation.setPitch(mc.player.rotationPitch);
        return rotation;
    }

}
