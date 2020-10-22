package fail.mercury.client.client.modules.movement;

import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.client.events.UpdateEvent;
import net.b0at.api.event.EventHandler;
import net.b0at.api.event.types.EventTiming;
/**
 * @author Crystallinqq 2/25/2020
 */
//TODO: fix
@ModuleManifest(label = "AntiVoid", fakelabel = "Anti Void", category = Category.MOVEMENT, description = "Prevents you from falling in da void")
public class AntiVoid extends Module {
    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if(event.getTiming().equals(EventTiming.PRE)) {
            if (mc.player.fallDistance > 5) {
                if (mc.player.posY < 0) {
                    event.getLocation().setY(event.getLocation().getY() + 8);
                }
                }
            }
        }
    }