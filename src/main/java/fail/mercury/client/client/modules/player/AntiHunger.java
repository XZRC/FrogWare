package fail.mercury.client.client.modules.player;

import fail.mercury.client.Mercury;
import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.client.events.PacketEvent;
import fail.mercury.client.client.events.UpdateEvent;
import fail.mercury.client.client.modules.movement.Flight;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.minecraft.network.play.client.CPacketEntityAction;

@ModuleManifest(label = "AntiHunger", aliases = {"NoHunger"}, fakelabel = "Anti Hunger", category = Category.PLAYER)
public class AntiHunger extends Module {

    @Property("CancelSprint")
    public boolean sprint = false;

    @EventHandler
    public void onPacket(PacketEvent event) {
        if (event.getType().equals(PacketEvent.Type.OUTGOING)) {
            if (sprint && event.getPacket() instanceof CPacketEntityAction) {
                final CPacketEntityAction packet = (CPacketEntityAction) event.getPacket();
                if (packet.getAction() == CPacketEntityAction.Action.START_SPRINTING || packet.getAction() == CPacketEntityAction.Action.STOP_SPRINTING) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (Mercury.INSTANCE.getModuleManager().find(Flight.class).isEnabled() && (Flight.mode.equalsIgnoreCase("packet") || Flight.mode.equalsIgnoreCase("packet2")))
            return;
        event.getLocation().setOnGround(false);
    }
}
