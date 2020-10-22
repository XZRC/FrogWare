package fail.mercury.client.client.modules.misc;

import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.client.events.PacketEvent;
import io.netty.buffer.Unpooled;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

/**
 * @author auto on 2/24/2020
 */
@ModuleManifest(label = "Handshake", aliases = {"CustomHandshake", "NoHandshake"}, category = Category.MISC, hidden = true)
public class Handshake extends Module {

    @Property("Brand")
    public String brand = "vanilla";

    @EventHandler
    public void onPacket(PacketEvent event) {
        if (event.getType().equals(PacketEvent.Type.OUTGOING)) {
            if (event.getPacket() instanceof FMLProxyPacket && !mc.isSingleplayer()) {
                event.setCancelled(true);
            }
            if (event.getPacket() instanceof CPacketCustomPayload) {
                final CPacketCustomPayload packet = (CPacketCustomPayload) event.getPacket();
                if (packet.getChannelName().equals("MC|Brand")) {
                    packet.data = new PacketBuffer(Unpooled.buffer()).writeString(brand);
                }
            }
            /*if (event.getPacket() instanceof FMLHandshakeMessage.ModList) {
                FMLHandshakeMessage.ModList packet = (FMLHandshakeMessage.ModList) event.getPacket();
            }*/
        }
    }
}
