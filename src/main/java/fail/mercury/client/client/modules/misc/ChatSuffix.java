package fail.mercury.client.client.modules.misc;

import fail.mercury.client.Mercury;
import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.client.events.PacketEvent;
import fail.mercury.client.client.modules.persistent.Commands;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.minecraft.network.play.client.CPacketChatMessage;

@ModuleManifest(label = "ChatSuffix", category = Category.MISC, fakelabel = "Chat Suffix", hidden = true)
public class ChatSuffix extends Module {

    @Property("Suffix")
    private String suffix = "| Mercury";

    @EventHandler
    public void onPacket(PacketEvent event) {
        if (event.getType().equals(PacketEvent.Type.OUTGOING) && event.getPacket() instanceof CPacketChatMessage) {
            CPacketChatMessage packet = (CPacketChatMessage) event.getPacket();
            if (packet.getMessage().startsWith("/") || packet.getMessage().startsWith(Commands.prefix))
                return;
            packet.message = packet.getMessage() + " " + Mercury.INSTANCE.getSmallTextManager().convert(this.suffix);
        }
    }
}

