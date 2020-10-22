package fail.mercury.client.client.modules.combat;

import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.client.events.PacketEvent;
import me.kix.lotus.property.annotations.Clamp;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

@ModuleManifest(label = "Velocity", aliases = {"antikb", "antivelocity", "kb"}, category = Category.COMBAT)
public class Velocity extends Module {

    @Property("Percent")
    @Clamp(maximum = "100")
    public int percent = 0;

    @EventHandler
    public void onPacket(PacketEvent event) {
        if (mc.world == null || mc.player == null)
            return;
            if (event.getPacket() instanceof SPacketEntityVelocity) {
                final SPacketEntityVelocity packet = (SPacketEntityVelocity) event.getPacket();
                if (mc.world.getEntityByID(packet.getEntityID()) == mc.player) {
                    if (this.percent > 0) {
                        final SPacketEntityVelocity s12PacketEntityVelocity = packet;
                        s12PacketEntityVelocity.motionX *= (this.percent / 100.0);
                        final SPacketEntityVelocity s12PacketEntityVelocit2 = packet;
                        s12PacketEntityVelocit2.motionY *= (this.percent / 100.0);
                        final SPacketEntityVelocity s12PacketEntityVelocity3 = packet;
                        s12PacketEntityVelocity3.motionZ *= (this.percent / 100.0);
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
            if (event.getPacket() instanceof SPacketExplosion) {
                final SPacketExplosion s27PacketExplosion;
                final SPacketExplosion packet2 = s27PacketExplosion = (SPacketExplosion) event.getPacket();
                if (percent > 0) {
                    s27PacketExplosion.motionX  *= (this.percent / 100.0);
                    final SPacketExplosion s27PacketExplosion2 = packet2;
                    s27PacketExplosion2.motionY *= (this.percent/ 100.0);
                    final SPacketExplosion s27PacketExplosion3 = packet2;
                    s27PacketExplosion3.motionZ *= (this.percent / 100.0);
                } else {
                    event.setCancelled(true);
                }
            }
    }
}

