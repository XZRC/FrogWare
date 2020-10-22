package fail.mercury.client.client.modules.persistent;

import fail.mercury.client.Mercury;
import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.client.events.PacketEvent;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.minecraft.network.play.client.CPacketChatMessage;

/**
 * @author auto on 2/11/2020
 */
@ModuleManifest(label = "Commands", aliases = {"command", "cmd"},category = Category.MISC, description = "Commands for the client.", hidden = true, persistent = true)
public class Commands extends Module {

    @Property("Prefix")
    public static String prefix = ".";

    /*@EventHandler
    public void onChat(ChatEvent event) {
        if (event.getMessage().startsWith(prefix + prefix) && !event.isCancelled()) {
            event.setMessage(event.getMessage().substring(1).toLowerCase());
        } else if (event.getMessage().startsWith(prefix)) {
            event.setCancelled(true);
            Trident.INSTANCE.getCommandManager().dispatch(event.getMessage().substring(1));
        }
    }*/

    @EventHandler
    public void onPacket(PacketEvent event) {
        if (event.getType().equals(PacketEvent.Type.OUTGOING) && event.getPacket() instanceof CPacketChatMessage) {
            CPacketChatMessage packet = (CPacketChatMessage) event.getPacket();
            if (packet.getMessage().startsWith(prefix + prefix) && !event.isCancelled()) {
                packet.message = packet.getMessage().substring(1).toLowerCase();
            } else if (packet.getMessage().startsWith(prefix)) {
                event.setCancelled(true);
                Mercury.INSTANCE.getCommandManager().dispatch(packet.getMessage().substring(1));
            }
        }
    }

}
