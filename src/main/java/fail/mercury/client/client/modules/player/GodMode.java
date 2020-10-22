package fail.mercury.client.client.modules.player;

import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.client.events.PacketEvent;
import fail.mercury.client.client.events.UpdateEvent;
import me.kix.lotus.property.annotations.Mode;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.*;
import net.minecraft.util.EnumHand;

/**
 * @author Seth on 2/8/2020
 */
@ModuleManifest(label = "GodMode", category = Category.PLAYER, fakelabel = "God Mode", aliases = {"God"})
public class GodMode extends Module {

    private Entity riding;

    @Property("Mode")
    @Mode({"Portal", "Entity"})
    public String mode = "Portal";

    @Override
    public void onEnable() {
        if (mode.equalsIgnoreCase("entity")) {
            if (mc.player.getRidingEntity() != null) {
                this.riding = mc.player.getRidingEntity();
                mc.player.dismountRidingEntity();
                mc.world.removeEntity(this.riding);
                mc.player.setPosition(mc.player.getPosition().getX(), mc.player.getPosition().getY() - 1, mc.player.getPosition().getZ());
            }
        }
    }

    @Override
    public void onDisable() {
        if (mode.equalsIgnoreCase("entity")) {
            if (this.riding != null) {
                mc.player.connection.sendPacket(new CPacketUseEntity(this.riding, EnumHand.MAIN_HAND));
            }
        }
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (mode.equalsIgnoreCase("entity")) {
            if (this.riding != null) {
                this.riding.posX = mc.player.posX;
                this.riding.posY = mc.player.posY;
                this.riding.posZ = mc.player.posZ;
                this.riding.rotationYaw = mc.player.rotationYaw;
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, mc.player.rotationPitch, true));
                mc.player.connection.sendPacket(new CPacketInput(mc.player.movementInput.moveForward, mc.player.movementInput.moveStrafe, false, false));
                mc.player.connection.sendPacket(new CPacketVehicleMove(this.riding));
            }
        }
    }

    @EventHandler
    public void onPacket(PacketEvent event) {
        if (event.getType().equals(PacketEvent.Type.OUTGOING)) {
            if (this.mode.equalsIgnoreCase("portal")) {
                if (event.getPacket() instanceof CPacketConfirmTeleport) {
                    event.setCancelled(true);
                }
            }
            if (this.mode.equalsIgnoreCase("entity")) {
                if (event.getPacket() instanceof CPacketUseEntity) {
                    final CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();
                    if (this.riding != null) {
                        final Entity entity = packet.getEntityFromWorld(mc.world);
                        if (entity != null) {
                            this.riding.posX = entity.posX;
                            this.riding.posY = entity.posY;
                            this.riding.posZ = entity.posZ;
                            this.riding.rotationYaw = mc.player.rotationYaw;
                            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, mc.player.rotationPitch, true));
                            mc.player.connection.sendPacket(new CPacketInput(mc.player.movementInput.moveForward, mc.player.movementInput.moveStrafe, false, false));
                            mc.player.connection.sendPacket(new CPacketVehicleMove(this.riding));
                        }
                    }
                }

                if (event.getPacket() instanceof CPacketPlayer.Position || event.getPacket() instanceof CPacketPlayer.PositionRotation) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
