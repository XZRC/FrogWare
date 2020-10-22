package fail.mercury.client.client.modules.combat;

import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.api.util.TimerUtil;
import fail.mercury.client.client.events.PacketEvent;
import net.b0at.api.event.EventHandler;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;

/**
 * @author auto on 3/19/2020
 */
@ModuleManifest(label = "ArmorBreaker", fakelabel = "Armor Breaker", category = Category.COMBAT, aliases = {"Dura", "Durability"}, description = "Lowers armor durability faster.")
public class ArmorBreaker extends Module {

    public TimerUtil timer = new TimerUtil();

    @EventHandler
    public void onPacket(PacketEvent event) {
        if (event.getType().equals(PacketEvent.Type.OUTGOING)) {
            if (event.getPacket() instanceof CPacketUseEntity) {
                CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();
                if (packet.getAction() == CPacketUseEntity.Action.ATTACK) {
                    if (timer.hasReached(200)) {
                        event.setCancelled(true);
                        swap(9, mc.player.inventory.currentItem);
                        mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                        mc.player.connection.sendPacket(new CPacketUseEntity(packet.getEntityFromWorld(mc.world)));
                        swap(9, mc.player.inventory.currentItem);
                        timer.reset();
                    }
                }
            }
        }
    }

    private void swap(final int slot, int hotbarNum) {
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, hotbarNum, ClickType.SWAP, mc.player);
    }

}
