package fail.mercury.client.client.modules.visual;

import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.client.events.PacketEvent;
import me.kix.lotus.property.annotations.Mode;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

/**
 * @author auto on 3/25/2020
 */
@ModuleManifest(label = "HitEffects" , aliases = {"KillEffects"}, fakelabel = "Hit Effects", category = Category.VISUAL)
public class HitEffects extends Module {

    @Property("Mode")
    @Mode({"Lightning"})
    public String mode = "Lightning";

    @Property("Sounds")
    public boolean sounds = false;

    @Property("Death")
    public boolean death = false;

    @EventHandler
    public void onPacket(PacketEvent event) {
        if (event.getType().equals(PacketEvent.Type.OUTGOING)) {
            if (event.getPacket() instanceof CPacketUseEntity) {
                final CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();
                if (packet.getAction() == CPacketUseEntity.Action.ATTACK) {
                    Entity entity = packet.getEntityFromWorld(mc.world);
                    switch (mode.toLowerCase()) {
                        case "lightning":
                            if (death && !entity.isDead)
                                return;

                            final EntityLightningBolt lightning = new EntityLightningBolt(mc.world, entity.posX, entity.posY, entity.posZ, true);
                            mc.world.spawnEntity(lightning);
                            if (sounds) {
                                final ResourceLocation thunderLocal = new ResourceLocation("minecraft", "entity.lightning.thunder");
                                final SoundEvent thunderSound = new SoundEvent(thunderLocal);
                                final ResourceLocation lightningImpactLocal = new ResourceLocation("minecraft", "entity.lightning.impact");
                                final SoundEvent lightningImpactSound = new SoundEvent(lightningImpactLocal);
                                mc.world.playSound(mc.player, new BlockPos(entity.posX, entity.posY, entity.posZ), thunderSound, SoundCategory.WEATHER, 1.0f, 1.0f);
                                mc.world.playSound(mc.player, new BlockPos(entity.posX, entity.posY, entity.posZ), lightningImpactSound, SoundCategory.WEATHER, 1.0f, 1.0f);
                            }
                            break;
                    }
                }
            }
        }
    }

}
