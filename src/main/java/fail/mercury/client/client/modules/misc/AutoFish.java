package fail.mercury.client.client.modules.misc;

import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.client.events.PacketEvent;
import me.kix.lotus.property.annotations.Mode;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;

import java.util.Objects;

@ModuleManifest(label = "AutoFish", fakelabel = "Auto Fish", category = Category.MISC)
public class AutoFish extends Module {

    @Property("Mode")
    @Mode({"Normal", "Lava"})
    public String mode = "Normal";

    @Property("Cast")
    public boolean cast = false;

    @EventHandler
    public void onPacket(PacketEvent event) {
        if (event.getType().equals(PacketEvent.Type.INCOMING)) {
            if (mode.equalsIgnoreCase("Normal")) {
                if (event.getPacket() instanceof SPacketSoundEffect) {
                    SPacketSoundEffect packet = (SPacketSoundEffect)event.getPacket();
                    if (packet.getCategory() == SoundCategory.NEUTRAL && packet.getSound() == SoundEvents.ENTITY_BOBBER_SPLASH) {
                        if (mc.player.getHeldItemMainhand().getItem() instanceof ItemFishingRod) {
                            click();
                            if (cast)
                                click();
                        }
                    }
                }
            }
            //TODO: fix this
            if (mode.equalsIgnoreCase("Lava")) {
                    if (event.getPacket() instanceof SPacketSpawnMob) {
                    SPacketSpawnMob packet = (SPacketSpawnMob)event.getPacket();
                    Entity entity = mc.world.getEntityByID(packet.getEntityID());
                    if (mc.player.fishEntity != null && entity instanceof EntityItem && mc.player.fishEntity.getDistance(Objects.requireNonNull(entity)) < 3) {
                        if (mc.player.getHeldItemMainhand().getItem() instanceof ItemFishingRod) {
                            click();
                            if (cast)
                                click();
                        }
                    }
                }
            }
        }

    }

    public void click() {
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        mc.player.swingArm(EnumHand.MAIN_HAND);
    }

}
