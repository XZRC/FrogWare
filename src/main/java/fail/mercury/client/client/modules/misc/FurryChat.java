package fail.mercury.client.client.modules.misc;

import fail.mercury.client.Mercury;
import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.api.util.MathUtil;
import fail.mercury.client.client.events.PacketEvent;
import fail.mercury.client.client.modules.persistent.Commands;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.minecraft.network.play.client.CPacketChatMessage;

/**
 * @author auto on 2/2/2020
 */
@ModuleManifest(label = "FurryChat", fakelabel = "Furry Chat", aliases = {"FChat", "OwOify", "WeebTalk"}, category = Category.MISC)
public class FurryChat extends Module {

    @Property("Suffix")
    public boolean suffix = true;

    @Property("Prefix")
    public boolean prefix = false;

    /*@EventHandler
    public void onChat(ChatEvent event) {
        if (packet.getMessage().startsWith("/") || packet.getMessage().startsWith(Mercury.INSTANCE.getCommandManager().getPrefix()))
            return;
        if (prefix && !packet.getMessage().startsWith("owo:"))
            return;
        String[] cancer = {"owo", "OwO", "uwu", "UwU", ">w<", "^w^", "7w7", "^o^", ":3", "@w@"};
        event.setMessage(yep((prefix && packet.getMessage().startsWith("owo:")) ? packet.getMessage().replace("owo:", "") : event.getMessage()) + " " + (suffix ? cancer[MathUtil.getRandom(0, cancer.length - 1)] : ""));
    }*/

    @EventHandler
    public void onPacket(PacketEvent event) {
        if (event.getType().equals(PacketEvent.Type.OUTGOING) && event.getPacket() instanceof CPacketChatMessage) {
            CPacketChatMessage packet = (CPacketChatMessage) event.getPacket();
            if (packet.getMessage().startsWith("/") || packet.getMessage().startsWith(Commands.prefix))
                return;
            if (Mercury.INSTANCE.getModuleManager().find(Translator.class).isEnabled())
                return;
            if (prefix && !packet.getMessage().startsWith("owo:"))
                return;
            String[] cancer = {"owo", "OwO", "uwu", "UwU", ">w<", "^w^", "7w7", "^o^", ":3", "@w@"};
            packet.message = yep((prefix && packet.getMessage().startsWith("owo:")) ? packet.getMessage().replace("owo:", "") : packet.getMessage()) + " " + (suffix ? cancer[MathUtil.getRandom(0, cancer.length - 1)] : "");
        }
    }

    public String yep(String chat) {
        return chat.replace("r", "w")
                .replace("R", "W")
                .replace("l", "w")
                .replace("L", "W")
                .replace(" n", " ny")
                .replace(" N", " Ny")
                .replace("ove", "uv")
                .replace("OVE", "UV")
                .replace("this", "dis");
    }

}
