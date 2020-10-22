package fail.mercury.client.client.modules.misc;

import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.client.event.GuiScreenEvent;

/**
 * @author Crystallinqq on 2/21/20
 */
@ModuleManifest(label = "DeathMessage", category = Category.MISC, fakelabel = "DeathMessage", description = "Send message when you die.")
public class DeathMessage extends Module {

    @Property("AutoRespawn")
    public boolean autorespawn = false;

    @Property("Death Message")
    private String suffix = "lag killed me not you skid";

    @EventHandler
    public void onUpdate(GuiScreenEvent event) {
        if (event.getGui() instanceof GuiGameOver) {
            if (autorespawn) {
                mc.player.respawnPlayer();
                mc.displayGuiScreen(null);
                mc.player.sendChatMessage(suffix);
            }
        }
    }
}