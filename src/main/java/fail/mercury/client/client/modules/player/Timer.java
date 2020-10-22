package fail.mercury.client.client.modules.player;

import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.api.util.EntityUtil;
import fail.mercury.client.client.events.UpdateEvent;
import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.api.util.EntityUtil;
import fail.mercury.client.client.events.UpdateEvent;
import me.kix.lotus.property.annotations.Clamp;
import me.kix.lotus.property.annotations.Mode;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;

@ModuleManifest(label = "Timer", aliases = {"GameSpeed"}, category = Category.PLAYER)
public class Timer extends Module {

    @Property("Speed")
    @Clamp(minimum = "0.1", maximum = "10")
    private float speed = 4.0f;

    @Property("Mode")
    @Mode({"Normal", "Test"})
    private String mode = "Normal";

    @Override
    public void onDisable() {
        EntityUtil.resetTimer();
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        switch (mode.toLowerCase()) {
            case "normal":
                EntityUtil.setTimer(speed);
                break;
            case "test":
                    EntityUtil.setTimer(10f);
                if (mc.player.ticksExisted % 10 == 0) {
                    EntityUtil.setTimer(1f);
                }
                break;
        }
    }

}
