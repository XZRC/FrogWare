package fail.mercury.client.client.modules.combat;

import fail.mercury.client.Mercury;
import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.client.events.JumpEvent;
import fail.mercury.client.client.events.PacketEvent;
import fail.mercury.client.client.modules.movement.Flight;
import fail.mercury.client.client.modules.movement.Speed;
import me.kix.lotus.property.annotations.Mode;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;

@ModuleManifest(label = "Criticals", aliases = {"crits"}, category = Category.COMBAT)
public class Criticals extends Module {

    @Property("Mode")
    @Mode({"Packet", "Edit"})
    public static String mode = "Packet";

    public static int waitDelay, groundTicks;

    @EventHandler
    public void onPacket(PacketEvent event) {
        if (event.getType().equals(PacketEvent.Type.OUTGOING)) {
            if (event.getPacket() instanceof CPacketUseEntity) {
                final CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();
                if (packet.getAction() == CPacketUseEntity.Action.ATTACK) {
                    if (canCrit(packet.getEntityFromWorld(mc.world))) {
                        if (mode.equalsIgnoreCase("Packet")) {
                            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.0625101D, mc.player.posZ, false));
                            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onJump(JumpEvent event) {
        if (KillAura.target != null && (mode.equalsIgnoreCase("edit")) && groundTicks != 0) {
            event.setCancelled(true);
            mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(
                    mc.player.posX, mc.player.posY, mc.player.posZ, KillAura.yaw, KillAura.pitch, true));
            mc.player.motionY = .42f;
            groundTicks = 0;
        } else {
            event.setCancelled(false);
        }
    }

    public static boolean canCrit(Entity entity) {
        return !Mercury.INSTANCE.getModuleManager().find(Speed.class).isEnabled() && !Mercury.INSTANCE.getModuleManager().find(Flight.class).isEnabled()
                && mc.player.onGround && !mc.gameSettings.keyBindJump.isKeyDown() && !(entity instanceof EntityEnderCrystal);
    }
}
