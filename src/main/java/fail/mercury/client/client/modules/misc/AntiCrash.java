package fail.mercury.client.client.modules.misc;

import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.client.events.PacketEvent;
import fail.mercury.client.client.events.UpdateEvent;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;

import java.util.Objects;

/**
 * @author auto on 2/2/2020
 */
@ModuleManifest(label = "AntiCrash", aliases = {"ACrash"}, fakelabel = "Anti Crash", category = Category.MISC)
public class AntiCrash extends Module {

    @Property("Slime")
    public boolean slime = true;

    @Property("Offhand")
    public boolean offhand = true;

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (Objects.nonNull(mc.world) && slime) {
            mc.world.loadedEntityList.forEach(e -> {
                if (e instanceof EntitySlime) {
                    EntitySlime slime = (EntitySlime) e;
                    if (slime.getSlimeSize() > 4) {
                        mc.world.removeEntity(e);
                    }
                }
            });
        }
    }

    @EventHandler
    public void onPacket(PacketEvent event) {
        if (offhand && event.getType().equals(PacketEvent.Type.INCOMING)) {
            if (event.getPacket() instanceof SPacketSoundEffect) {
                if (((SPacketSoundEffect) event.getPacket()).getSound() == SoundEvents.ITEM_ARMOR_EQUIP_GENERIC) {
                    event.setCancelled(true);
                }
            }
        }
    }
}