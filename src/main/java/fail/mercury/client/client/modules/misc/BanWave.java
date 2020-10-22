package fail.mercury.client.client.modules.misc;

import fail.mercury.client.Mercury;
import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.api.util.ChatUtil;
import fail.mercury.client.api.util.TimerUtil;
import fail.mercury.client.client.events.UpdateEvent;
import me.kix.lotus.property.annotations.Clamp;
import me.kix.lotus.property.annotations.Mode;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * @author auto on 3/18/2020
 */
@ModuleManifest(label = "BanWave", category = Category.MISC, fakelabel = "Ban Wave", description = "Automatically bans players.")
public class BanWave extends Module {

    private TimerUtil timer = new TimerUtil();
    public ArrayList<Entity> banned = new ArrayList<Entity>();
    public static Path path = new File(Mercury.INSTANCE.getDirectory(), "banwave-names.txt").toPath();

    @Property("Mode")
    @Mode({"Normal", "Read"})
    private String mode = "Normal";

    @Property("SendMessage")
    public boolean message = true;

    @Property("Message")
    private String banMessage = "bannd bi mercury benwav ez ee";

    @Property("Delay")
    @Clamp(minimum = "1")
    private int banDelay = 10;

    @Override
    public void onEnable() {
        if (!Files.exists(BanWave.path)) {
            try {
                Files.createFile(BanWave.path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        banned.clear();
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (mode.equalsIgnoreCase("normal")) {
            for (final Object o : mc.world.getLoadedEntityList()) {
                if (o instanceof EntityOtherPlayerMP) {
                    final EntityOtherPlayerMP e = (EntityOtherPlayerMP) o;
                    if (!timer.hasReached(banDelay * 100) || Mercury.INSTANCE.getFriendManager().isFriend(e.getName()) || e.getName() == mc.player.getName()) {
                        continue;
                    }
                    if (banned.contains(e)) {
                        continue;
                    }
                    if (message) {
                        mc.player.sendChatMessage("/ban " + e.getName() + " " + banMessage);
                        System.out.println("banned " + e.getName() + " " + banMessage);
                    } else {
                        mc.player.sendChatMessage("/ban " + e.getName());
                        System.out.println("banned " + e.getName());
                    }
                    banned.add(e);
                    timer.reset();
                }
            }
        }
        if (mode.equalsIgnoreCase("read")) {
            try {
                if (Files.lines(path).count() <= 0) {
                    ChatUtil.print("No names found in file " + path.getFileName());
                    return;
                }
                Files.lines(path).forEach(n -> {
                    for (final Object o : mc.world.getLoadedEntityList()) {
                        if (o instanceof EntityOtherPlayerMP) {
                            final EntityOtherPlayerMP e = (EntityOtherPlayerMP) o;
                            if (e.getName().equalsIgnoreCase(n)) {
                                if (!timer.hasReached(banDelay * 100) || Mercury.INSTANCE.getFriendManager().isFriend(e.getName()) || e.getName() == mc.player.getName()) {
                                    continue;
                                }
                                if (banned.contains(e)) {
                                    continue;
                                }
                                if (message) {
                                    mc.player.sendChatMessage("/ban " + e.getName() + " " + banMessage);
                                    System.out.println("banned " + e.getName() + " " + banMessage);
                                } else {
                                    mc.player.sendChatMessage("/ban " + e.getName());
                                    System.out.println("banned " + e.getName());
                                }
                                banned.add(e);
                                timer.reset();
                            }
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
