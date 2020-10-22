package fail.mercury.client.client.modules.player;

import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.client.events.PushEvent;
import net.b0at.api.event.EventHandler;

@ModuleManifest(label = "NoPush", category = Category.PLAYER, fakelabel = "No Push")
public class NoPush extends Module {

    @EventHandler
    public void onPush(PushEvent event) {
        event.setCancelled(true);
    }

}
