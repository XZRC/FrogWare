package fail.mercury.client.client.modules.movement;

import fail.mercury.client.Mercury;
import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.api.util.MotionUtil;
import fail.mercury.client.client.events.JumpEvent;
import fail.mercury.client.client.events.UpdateEvent;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;

@ModuleManifest(label = "Sprint", aliases = {"AutoSprint"}, category = Category.MOVEMENT)
public class Sprint extends Module {

    @Property("Omni")
    public boolean omni = false;

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (Mercury.INSTANCE.getModuleManager().find(Scaffold.class).isEnabled() && !Scaffold.sprint) {
            mc.player.setSprinting(false);
            return;
        }
        if (Mercury.INSTANCE.getModuleManager().find(Scaffold.class).isEnabled() && mc.gameSettings.keyBindSneak.isKeyDown() && Scaffold.down) {
            mc.player.setSprinting(false);
            return;
        }
        if (mc.player.getFoodStats().getFoodLevel() > 6 && omni ? (mc.player.moveForward != 0 || mc.player.moveStrafing != 0) : mc.player.moveForward > 0)
            mc.player.setSprinting(true);
    }

    @EventHandler
    public void onJump(JumpEvent event) {
        if (omni) {
            double[] dir = MotionUtil.forward(0.017453292F);
            event.getLocation().setX(dir[0] * 0.2f);
            event.getLocation().setZ(dir[1] * 0.2f);
        }
    }

}
