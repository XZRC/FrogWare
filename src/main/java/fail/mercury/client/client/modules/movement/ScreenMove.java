package fail.mercury.client.client.modules.movement;

import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.client.events.UpdateEvent;
import fail.mercury.client.client.gui.click.Menu;
import net.b0at.api.event.EventHandler;
import net.b0at.api.event.types.EventTiming;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * @author auto on 4/6/2020
 */
@ModuleManifest(label = "ScreenMove", fakelabel = "Screen Move", aliases = {"GuiMove", "InvWalk", "ScreenWalk", "GuiWalk"}, category = Category.MOVEMENT)
public class ScreenMove extends Module {

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (event.getTiming().equals(EventTiming.PRE)) {
            if (mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof Menu || mc.currentScreen instanceof GuiEditSign || mc.currentScreen == null) {
                return;
            }
            KeyBinding[] moveKeys = new KeyBinding[]{
                    mc.gameSettings.keyBindForward,
                    mc.gameSettings.keyBindBack,
                    mc.gameSettings.keyBindLeft,
                    mc.gameSettings.keyBindRight,
                    mc.gameSettings.keyBindJump
            };
            for (KeyBinding bind : moveKeys) {
                KeyBinding.setKeyBindState(bind.getKeyCode(), Keyboard.isKeyDown(bind.getKeyCode()));
            }

            if (Mouse.isButtonDown(2)) {
                Mouse.setGrabbed(true);
                mc.inGameHasFocus = true;
            } else {
                Mouse.setGrabbed(false);
                mc.inGameHasFocus = false;
            }
        }
    }
}
