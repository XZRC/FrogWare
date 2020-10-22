package fail.mercury.client.client.modules.player;

import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.client.events.PacketEvent;
import net.b0at.api.event.EventHandler;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

import java.util.Objects;

@ModuleManifest(label = "NoRotate", aliases = {"NoForceRotate"}, fakelabel = "No Rotate", category = Category.PLAYER)
public class NoRotate extends Module {

    @EventHandler
    public void onPacket(PacketEvent event) {
            if (Objects.isNull(mc.player) || Objects.isNull(mc.world))
                return;
            if (event.getType().equals(PacketEvent.Type.INCOMING) && event.getPacket() instanceof SPacketPlayerPosLook) {
                SPacketPlayerPosLook poslook = (SPacketPlayerPosLook) event.getPacket();
                if (mc.player.rotationYaw != -180 && mc.player.rotationPitch != 0) {
                    poslook.yaw = mc.player.rotationYaw;
                    poslook.pitch = mc.player.rotationPitch;
                }
            }
        }

}
