package fail.mercury.client.client.modules.combat;

import fail.mercury.client.Mercury;
import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.client.events.PacketEvent;
import me.kix.lotus.property.annotations.Clamp;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.SPacketEntityStatus;

/**
 * @author Crystallinqq 2/10/2020
 * Updated 2/23/2020
 */

@ModuleManifest(label = "AntiChainPop", category = Category.COMBAT, fakelabel = "Anti Chain Pop", description = "Toggle surround after popping a totem to not get popped multiple times in a row")
public class AntiChainPop extends Module {
    private Entity entity;
    private int popCount = 0;

    @Property("Pops")
    @Clamp(minimum = "1")
    public int pops = 1;

    @EventHandler
    public void onDisable() {
        popCount = 0;
    }

    @EventHandler
    public void onPacket(PacketEvent event) {
        if (event.getType().equals(PacketEvent.Type.INCOMING)) {
            if (event.getPacket() instanceof SPacketEntityStatus) {
                SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
                if (packet.getOpCode() == 35) {
                    entity = packet.getEntity(mc.world);
                    if (entity == mc.player) {
                        popCount++;
                        if (popCount >= pops) {
                            if (!Mercury.INSTANCE.getModuleManager().find(Surround.class).isEnabled())
                                Mercury.INSTANCE.getModuleManager().find(Surround.class).setEnabled(true);
                            popCount = 0;
                        }
                    }
                }
            }
        }
    }
}
