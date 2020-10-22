package fail.mercury.client.client.modules.misc;

import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.api.util.MotionUtil;
import fail.mercury.client.client.events.*;
import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.annotations.Replace;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.api.util.MotionUtil;
import fail.mercury.client.client.events.*;
import me.kix.lotus.property.annotations.Clamp;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.util.math.Vec3d;

@ModuleManifest(label = "FreeCam", fakelabel = "Free Cam", category = Category.MISC)
public class Freecam extends Module {

    public static EntityOtherPlayerMP entity;
    private Entity riding;
    private Vec3d position;
    private float yaw;
    private float pitch;

    @Property("Speed")
    @Clamp(minimum = "0.1", maximum = "10")
    public float speed = 2;

    @Override
    public void onEnable() {
        if (mc.world != null) {
            if (mc.player.getRidingEntity() != null) {
                this.riding = mc.player.getRidingEntity();
                mc.player.dismountRidingEntity();
            }
            this.entity = new EntityOtherPlayerMP(mc.world, mc.session.getProfile());
            this.entity.copyLocationAndAnglesFrom(mc.player);
            this.entity.rotationYaw = mc.player.rotationYaw;
            this.entity.rotationYawHead = mc.player.rotationYawHead;
            this.entity.inventory.copyInventory(mc.player.inventory);
            mc.world.addEntityToWorld(1337, this.entity);
            this.position = mc.player.getPositionVector();
            this.yaw = mc.player.rotationYaw;
            this.pitch = mc.player.rotationPitch;
        }
    }

        @Override
        public void onDisable() {
            if (mc.world != null) {
                if (this.riding != null) {
                    mc.player.startRiding(this.riding, true);
                }
                if (this.entity != null) {
                    mc.world.removeEntity(this.entity);
                }
                if (this.position != null) {
                    mc.player.setPosition(this.position.x, this.position.y, this.position.z);
                }
                mc.player.rotationYaw = this.yaw;
                mc.player.rotationPitch = this.pitch;
                mc.player.noClip = false;
                mc.player.setVelocity(0,0,0);
                MotionUtil.setSpeed(mc.player, 0);
            }
        }

        @EventHandler
        public void onUpdate(UpdateEvent event) {
            mc.player.setVelocity(0, 0, 0);
            mc.player.noClip = true;
            mc.player.renderArmPitch = 5000;
            mc.player.jumpMovementFactor = speed;
            if (MotionUtil.isMoving(mc.player)) {
                MotionUtil.setSpeed(mc.player, speed);
            } else {
                MotionUtil.setSpeed(mc.player, 0);
            }
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.player.motionY += speed;
            }
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.motionY -= speed;
            }
        }

    @EventHandler
    public void onPacket(PacketEvent event) {
        if (mc.world == null)
            return;
        if (event.getType() == PacketEvent.Type.OUTGOING) {
            if (mc.world != null) {
                    /*if (!(event.getPacket() instanceof CPacketUseEntity) && !(event.getPacket() instanceof CPacketPlayerTryUseItem) && !(event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) && !(event.getPacket() instanceof CPacketPlayer) && !(event.getPacket() instanceof CPacketVehicleMove) && !(event.getPacket() instanceof CPacketChatMessage) && !(event.getPacket() instanceof CPacketKeepAlive)) {
                        event.setCancelled(true);
                    }*/
                    if (event.getPacket() instanceof CPacketPlayer)
                        event.setCancelled(true);
                }
            }
        if (event.getType() == PacketEvent.Type.INCOMING) {
            if (event.getPacket() instanceof SPacketSetPassengers) {
                final SPacketSetPassengers packet = (SPacketSetPassengers) event.getPacket();
                final Entity riding = mc.world.getEntityByID(packet.getEntityId());
                if (riding != null && riding == this.riding) {
                    this.riding = null;
                }
            }
        }
    }

    @EventHandler
    public void onPush(PushEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onRender(InsideBlockRenderEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onCollision(CollisionBoxEvent event) {
        if (event.getEntity() == mc.player)
        event.setAABB(Block.NULL_AABB);
    }

}



