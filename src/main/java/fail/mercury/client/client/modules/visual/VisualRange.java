package fail.mercury.client.client.modules.visual;

import com.mojang.realmsclient.gui.ChatFormatting;
import fail.mercury.client.Mercury;
import fail.mercury.client.api.audio.AudioPlayer;
import fail.mercury.client.api.friend.Friend;
import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.api.util.ChatUtil;
import fail.mercury.client.client.events.PlayerEvent;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

@ModuleManifest(label = "VisualRange", category = Category.MISC, fakelabel = "Visual Range")
public class VisualRange extends Module {


    @Property("SendEnterMessage")
    public boolean sendEnterMessage = false;

    @Property("EnterMessage")
    public String enterMessage = "/msg %s hi";

    @Property("SendExitMessage")
    public boolean sendExitMessage = false;

    @Property("ExitMessage")
    public String exitMessage = "/msg %s bye";

    @Property("Sound")
    public boolean sound = false;

    private int prevPlayer = -1;

    //TODO: fix audio playing
    @EventHandler
    public void onPlayer(PlayerEvent event) {
        switch (event.getType()) {
            case ENTERING:
                if (mc.world != null && mc.player != null && !mc.player.isDead && event.getEntity() instanceof EntityPlayer && !event.getEntity().getName().equalsIgnoreCase(mc.player.getName())) {
                    final Friend friend = Mercury.INSTANCE.getFriendManager().getFriend(event.getEntity().getName());
                    final String msg = (friend != null ? ChatFormatting.DARK_PURPLE : ChatFormatting.RED) + (friend != null ? friend.getAlias() : event.getEntity().getName()) + ChatFormatting.WHITE + " has entered your visual range.";
                        ChatUtil.print(msg);
                    if (sendEnterMessage) {
                        if (enterMessage.contains("%s")) {
                            mc.player.sendChatMessage(String.format(enterMessage, event.getEntity().getName()));
                        } else {
                            mc.player.sendChatMessage(enterMessage);
                        }
                    }
                    if (sound) {
                        AudioPlayer player = new AudioPlayer(new ResourceLocation("assets\\mercury", "enter.wav"));
                        player.play();
                        player.stop();
                        player.close();
                    }
                    if (event.getEntity().getEntityId() == this.prevPlayer) {
                        this.prevPlayer = -1;
                    }
                }
                break;
            case EXITING:
                if (mc.world != null && mc.player != null && !mc.player.isDead && event.getEntity() instanceof EntityPlayer && !event.getEntity().getName().equalsIgnoreCase(mc.player.getName())) {
                    if (this.prevPlayer != event.getEntity().getEntityId()) {
                        this.prevPlayer = event.getEntity().getEntityId();
                        final Friend friend = Mercury.INSTANCE.getFriendManager().getFriend(event.getEntity().getName());
                        final String msg = (friend != null ? ChatFormatting.DARK_PURPLE : ChatFormatting.RED) + (friend != null ? friend.getAlias() : event.getEntity().getName()) + ChatFormatting.WHITE + " has left your visual range.";
                        ChatUtil.print(msg);
                        if (sendExitMessage) {
                            if (exitMessage.contains("%s")) {
                                mc.player.sendChatMessage(String.format(exitMessage, event.getEntity().getName()));
                            } else {
                                mc.player.sendChatMessage(exitMessage);
                            }
                        }
                        if (sound) {
                            AudioPlayer player = new AudioPlayer(new ResourceLocation("assets\\trident", "exit.wav"));
                            player.play();
                            player.stop();
                            player.close();
                        }
                    }
                    break;
                }
        }
    }
}
