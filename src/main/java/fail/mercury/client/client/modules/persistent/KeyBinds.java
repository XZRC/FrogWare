package fail.mercury.client.client.modules.persistent;

import fail.mercury.client.Mercury;
import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.client.events.KeypressEvent;
import fail.mercury.client.client.gui.click.Menu;
import fail.mercury.client.client.gui.hudeditor.GuiHud;
import net.b0at.api.event.EventHandler;
import org.lwjgl.input.Keyboard;

/**
 * @author auto on 2/11/2020
 */
@ModuleManifest(label = "KeyBinds", category = Category.MISC, description = "KeyBinds for the client.", hidden = true, persistent = true)
public class KeyBinds extends Module {

    private Menu guiCllck;
    private GuiHud guiHud;

    @EventHandler
    public void onKeyPress(KeypressEvent event) {
        Mercury.INSTANCE.getModuleManager().getRegistry().values().forEach(m -> {
            if (m.getBind() == event.getKey()) {
                m.toggle();
            }
        });
        if (event.getKey() == Keyboard.KEY_RSHIFT) {
            if (guiCllck == null) {
                guiCllck = new Menu();
                guiCllck.init();
            }
            mc.displayGuiScreen(guiCllck);
        }
        if (event.getKey() == Keyboard.KEY_GRAVE) {
            if (guiHud == null) {
                guiHud = new GuiHud();
            }
            mc.displayGuiScreen(guiHud);
        }
    }

}
