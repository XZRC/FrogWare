package fail.mercury.client.client.modules.player;

import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.client.events.UpdateEvent;
import net.b0at.api.event.EventHandler;
import net.b0at.api.event.types.EventTiming;
import net.minecraft.network.play.client.CPacketPlayer;

@ModuleManifest(label = "Revive", category = Category.PLAYER)
public class Revive extends Module {


    //TODO: fix this, (I know its possible)
    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (event.getTiming().equals(EventTiming.PRE)) {
            if (mc.player.getHealth() <= 0) {
                mc.player.setHealth(20);
                mc.currentScreen = null;
                mc.player.isDead = false;
                double x, z;
                x = mc.player.posX;
                z = mc.player.posZ;
                for (int i = 0; i <= 100; i++) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(x, 0, z, mc.player.onGround));
                }
                mc.player.respawnPlayer();
            }
        }
    }
}
