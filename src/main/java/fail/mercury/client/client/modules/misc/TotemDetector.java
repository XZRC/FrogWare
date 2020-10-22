package fail.mercury.client.client.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.api.util.ChatUtil;
import fail.mercury.client.client.events.PacketEvent;
import fail.mercury.client.client.events.UpdateEvent;
import me.kix.lotus.property.annotations.Clamp;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.SPacketEntityStatus;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author auto on 2/6/2020
 */
@ModuleManifest(label = "TotemDetector", fakelabel = "Totem Detector", category = Category.MISC)
public class TotemDetector extends Module {

    private HashMap<String, Integer> popList = new HashMap<>();
    private Entity entity;

    @Property("Range")
    @Clamp(minimum = "1", maximum = "1000")
    public double range = 50;


    //TODO: fix this
    // no
    @EventHandler
    public void onPacket(PacketEvent event) {
        if (event.getType().equals(PacketEvent.Type.INCOMING)) {
            if (event.getPacket() instanceof SPacketEntityStatus) {
                SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
                //ChatUtil.print(packet.getOpCode() + "");
                if (packet.getOpCode() == 35) {
                    int popCounter;
                    int newPopCounter;
                    entity = packet.getEntity(mc.world);
                    if (entity != mc.player && mc.player.getDistance(entity) <= range) {
                        if (!popList.containsKey(entity.getName())) {
                            ChatUtil.print(ChatFormatting.RED + entity.getName() + ChatFormatting.WHITE + " has popped 1 totem!");
                            popList.put(entity.getName(), 1);
                        } else {
                            popCounter = this.popList.get(entity.getName());
                            newPopCounter = ++popCounter;
                            popList.put(entity.getName(), newPopCounter);
                            ChatUtil.print(ChatFormatting.RED + entity.getName() + ChatFormatting.WHITE + " has popped " + newPopCounter + " totems!");
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (Objects.isNull(mc.world) || Objects.isNull(mc.player) || Objects.isNull(mc.world.playerEntities))
            return;
        mc.world.playerEntities.forEach(e -> {
            if (Objects.isNull(entity))
                return;
            if (e.getName().equals(this.entity.getName()) && (entity.isDead)) {
                popList.remove(this.entity.getName());
            }
        });
    }
}

