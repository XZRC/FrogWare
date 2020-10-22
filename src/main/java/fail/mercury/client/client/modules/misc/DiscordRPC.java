package fail.mercury.client.client.modules.misc;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRichPresence;
import fail.mercury.client.Mercury;
import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.api.util.MathUtil;
import fail.mercury.client.api.util.MotionUtil;
import fail.mercury.client.api.util.TimerUtil;
import fail.mercury.client.client.events.UpdateEvent;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.ServerData;

import java.util.Objects;

@ModuleManifest(label = "DiscordRPC", fakelabel = "Discord RPC", aliases = {"RPC"}, category = Category.MISC, hidden = true)
public class DiscordRPC extends Module {

    private static club.minnced.discord.rpc.DiscordRPC LIB;
    private DiscordRichPresence presence;
    private ServerData serverData;
    private long lastTime;
    private boolean afk;
    private TimerUtil timer = new TimerUtil();
    //private String[] devs = {"ad3cb52d-524b-41b4-b9d6-b91ec440811d", "a078c542-832f-4619-93dc-666be31fff9b", "e9a1c395-9536-4325-8758-90ee74b78f23"};

    @Property("Server")
    public boolean server = true;

    @Property("Name")
    public boolean name = true;

    @Override
    public void onEnable() {
        DiscordRPC.LIB = club.minnced.discord.rpc.DiscordRPC.INSTANCE;
        this.lastTime = System.currentTimeMillis() / 1000L;
        final String applicationId = "670676274338988042";
        final DiscordEventHandlers handlers = new DiscordEventHandlers();
        DiscordRPC.LIB.Discord_Initialize(applicationId, handlers, true, "");
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                this.presence = new DiscordRichPresence();
                this.presence.startTimestamp = lastTime;
                this.presence.largeImageText = String.format("%s %s | 1.12.2", Mercury.INSTANCE.getName(), Mercury.INSTANCE.getVersion());
                this.presence.largeImageKey = String.format("name_%s_", MathUtil.getRandom(1, 50));
                this.presence.smallImageKey = "discord";
                this.presence.smallImageText = "https://discord.io/mercurymod";
           //     for (String devs : devs) {
           //         if (mc.session.getProfile().getId().toString().equals(devs)) {
            //            this.presence.smallImageKey = "dev";
            //            this.presence.smallImageText = Trident.INSTANCE.getProfileManager().getName(UUID.fromString(devs));
            //        }
           //     }
                    this.presence.details = (afk || mc.currentScreen instanceof GuiMainMenu || mc.currentScreen instanceof GuiMultiplayer) ? "Currently AFK" : "Currently Exploring";
                serverData = mc.getCurrentServerData();
                if (serverData != null) {
                    StringBuilder sb = new StringBuilder("Multiplayer");
                    if (this.server)
                        sb.append(": " + serverData.serverIP);
                    if (name)
                        sb.append(String.format(" (%s)", mc.getSession().getUsername()));
                    this.presence.state = sb.toString();
                } else if (mc.isSingleplayer()) {
                    this.presence.state = "Singleplayer";
                } else if (mc.currentScreen != null) {
                    if (mc.currentScreen instanceof GuiMainMenu) {
                        this.presence.state = "Main Menu";
                    }
                    if (mc.currentScreen instanceof GuiMultiplayer) {
                        this.presence.state = "Multiplayer";
                    }
                }
                DiscordRPC.LIB.Discord_UpdatePresence(this.presence);
                DiscordRPC.LIB.Discord_RunCallbacks();
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException ignored) {
                    ignored.printStackTrace();
                }
            }
        }, "RPC-Callback-Handler").start();
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (MotionUtil.getSpeed(Objects.requireNonNull(mc.player)) == 0.0) {
            if (timer.hasReached(10000))
            afk = true;
        } else {
            timer.reset();
            afk = false;
        }
    }

}
