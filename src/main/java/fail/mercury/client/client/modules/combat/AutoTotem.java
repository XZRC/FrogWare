package fail.mercury.client.client.modules.combat;

import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.api.util.ChatUtil;
import fail.mercury.client.api.util.InventoryUtil;
import fail.mercury.client.client.events.PacketEvent;
import fail.mercury.client.client.events.UpdateEvent;
import me.kix.lotus.property.annotations.Clamp;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.b0at.api.event.types.EventTiming;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.play.server.SPacketUpdateHealth;

/**
 * @author auto on 2/3/2020
 */
@ModuleManifest(label = "AutoTotem", category = Category.COMBAT, fakelabel = "Auto Totem", description = "Automatically replaces offhand with totems.")
public class AutoTotem extends Module {

    @Property("Health")
    @Clamp(minimum = "0.5", maximum = "10")
    public double health = 10;

    @Property("Smart")
    public boolean smart = false;

    private boolean predicted = false;

    @EventHandler
    public void onPacket(PacketEvent event) {
        if (!smart)
            return;
        if (event.getType().equals(PacketEvent.Type.OUTGOING) && event.getPacket() instanceof SPacketUpdateHealth) {
            SPacketUpdateHealth packet = (SPacketUpdateHealth) event.getPacket();
            if (mc.player.getHealth() - packet.getHealth() <= 0) predicted = true;
        }
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (event.getTiming().equals(EventTiming.PRE)) {
            if (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory) {
                if (mc.player.getHealth() <= health * 2 || (smart && predicted)) {
                    if (predicted)
                        ChatUtil.print("predicted");
                    Item offhand = mc.player.getHeldItemOffhand().getItem();
                    if (InventoryUtil.getItemCount(mc.player.inventoryContainer, Items.TOTEM_OF_UNDYING) > 0 && !offhand.equals(Items.TOTEM_OF_UNDYING)) {
                        InventoryUtil.swap(InventoryUtil.getItemSlot(mc.player.inventoryContainer, Items.TOTEM_OF_UNDYING), 45);
                    }
                    if (predicted) predicted = false;
                }
            }
        }
    }
}
