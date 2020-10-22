package fail.mercury.client.client.modules.combat;

import fail.mercury.client.Mercury;
import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.api.util.AngleUtil;
import fail.mercury.client.client.events.PacketEvent;
import fail.mercury.client.client.events.UpdateEvent;
import me.kix.lotus.property.annotations.Clamp;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.b0at.api.event.types.EventTiming;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

@ModuleManifest(label = "KillAura", aliases = {"Aura"}, fakelabel = "Kill Aura", category = Category.COMBAT, description = "Automatically attacks players.")
public class KillAura extends Module {

    @Property("Range")
    @Clamp(minimum = "3.0", maximum = "6.0")
    public static double reach = 4.2;

    @Property("FOV")
    @Clamp(minimum = "1", maximum = "180")
    public static int fov = 180;

    @Property("MaxTargets")
    @Clamp(minimum = "1")
    public int maxTargets = 6;

    @Property("SwitchSwings")
    @Clamp(minimum = "1")
    public int switchSwings = 2;

    @Property("Silent")
    public boolean silent = true;

    @Property("Invisibles")
    public static boolean invisibles = true;

    @Property("Walls")
    public static boolean walls = false;

    @Property("Players")
    public static boolean players = true;

    @Property("Mobs")
    public static boolean mobs = true;

    @Property("Sync")
    public boolean sync = true;

    @Property("Criticals")
    public boolean crits = false;

    public static List<EntityLivingBase> targets = new ArrayList();
    public static EntityLivingBase target;
    private int index;
    private int hits;
    public static float yaw, pitch;

    @Override
    public void onDisable() {
        targets.clear();
        target = null;
    }

    @Override
    public void onEnable() {

    }

    @EventHandler
    public void onPacket(PacketEvent event) {
        if (event.getType().equals(PacketEvent.Type.OUTGOING)) {
            if (event.getPacket() instanceof CPacketPlayer.Rotation) {
                CPacketPlayer.Rotation packet = (CPacketPlayer.Rotation) event.getPacket();
                yaw = packet.yaw;
                pitch = packet.pitch;
            }
        }
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (mc.player == null) {
            target = null;
        }
        if (event.getTiming().equals(EventTiming.PRE)) {
            targets = getTargets();
            if (targets.size() > maxTargets) {
                if (index >= maxTargets) {
                    index = 0;
                }
            } else if (index >= targets.size()) {
                index = 0;
            }
            if (!targets.isEmpty()) {
                target = targets.get(index);

                Vec3d vec = AngleUtil.resolveBestHitVec(target, 3, walls);
                if (vec != null) {
                    float[] rotation = AngleUtil.getRotationFromPosition(vec.x, vec.z, vec.y - mc.player.getEyeHeight());
                    event.getRotation().setYaw(!silent ? mc.player.rotationYaw = rotation[0] : rotation[0]);
                    event.getRotation().setPitch(!silent ? mc.player.rotationPitch = mc.player.getDistance(target) <= 0.5 ? 90 : rotation[1] : mc.player.getDistance(target) <= 0.5 ? 90 : rotation[1]);
                    final float ticks = 20.0f - Mercury.INSTANCE.getTickRateManager().getTickRate();
                    final boolean canAttack = mc.player.getCooledAttackStrength(sync ? -ticks : 0.0f) >= 1 && AngleUtil.rayTrace(event.getRotation().getYaw(), event.getRotation().getPitch(), reach) != null;
                    if (canAttack) {
                        if (Mercury.INSTANCE.getModuleManager().find(Criticals.class).isEnabled() && target != null) {
                            if (Criticals.mode.equalsIgnoreCase("edit") && Criticals.canCrit(target)) {
                                if (mc.player.fallDistance != 0) {
                                    Criticals.waitDelay = 2;
                                }
                                //if (Block.isFullBlock(mc.world.getb) && !BlockUtil.getBlock(mc.player.posX, mc.player.posY + 1, mc.player.posZ).isFullBlock()) {
                                if (Criticals.waitDelay <= 0) {
                                    Criticals.waitDelay = 0;
                                    if (Criticals.canCrit(target)) {
                                        event.getLocation().setOnGround(false);
                                        Criticals.groundTicks += 1;
                                        if (Criticals.groundTicks == 1) {
                                            event.getLocation().setOnGround(false);
                                            event.getLocation().setY(event.getLocation().getY() + 0.0625101D);
                                        } else if (Criticals.groundTicks == 2) {
                                            event.getLocation().setOnGround(false);
                                            event.getLocation().setY(event.getLocation().getY() + 0.062666);
                                        } else if (Criticals.groundTicks == 3) {
                                            event.getLocation().setOnGround(false);
                                            event.getLocation().setY(event.getLocation().getY() + 0.0001);
                                        } else if (Criticals.groundTicks >= 4) {
                                            event.getLocation().setOnGround(false);
                                            event.getLocation().setY(event.getLocation().getY() + 0.0001);
                                            Criticals.groundTicks = 0;
                                        }
                                    } else {
                                        Criticals.waitDelay = 2;
                                    }
                                } else {
                                    Criticals.waitDelay -= 1;
                                }
                        /*} else if (Criticals.groundTicks != 0) {
                            Criticals.waitDelay = 4;
                            Criticals.groundTicks = 0;
                        }*/
                            }
                        }
                        if (crits && !mc.player.onGround && mc.player.fallDistance < 0.1)
                            return;
                        if (AngleUtil.rayTrace(event.getRotation().getYaw(), event.getRotation().getPitch(), reach) != null)
                        attack(target);
                        hits++;
                    }
                }
            } else if (target != null)
                target = null;
        }
        if (target != null) {
            if (targets.size() > 0 && hits > switchSwings) {
                ++index;
                hits = 0;
            }
        }
    }

    public void attack(Entity entity) {
        mc.player.connection.sendPacket(new CPacketUseEntity(entity));
        mc.player.swingArm(EnumHand.MAIN_HAND);
        if (Mercury.INSTANCE.getModuleManager().find(Criticals.class).isEnabled() && Criticals.canCrit(entity) && Criticals.mode.equalsIgnoreCase("edit") && Criticals.groundTicks != 0) {
            mc.world.playSound(null, mc.player.posX, mc.player.posY, mc.player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, mc.player.getSoundCategory(), 1.0F, 1.0F);
            mc.player.onCriticalHit(entity);
        }
        mc.player.resetCooldown();
    }

    public List<EntityLivingBase> getTargets() {
        List<EntityLivingBase> targets = new ArrayList<>();
        for (Object o : mc.world.getLoadedEntityList()) {
            if (o instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) o;
                if (doesQualify(entity)) {
                    targets.add(entity);
                }
            }
        }
        targets.sort((o1, o2) -> {
            float[] rot1 = AngleUtil.getRotations(o1);
            float[] rot2 = AngleUtil.getRotations(o2);
            return Float.compare(rot2[0], rot1[0]);
        });
        return targets;
    }

    private static boolean doesQualify(EntityLivingBase entity) {
        final double d = reach;
        return entity != null && entity.isEntityAlive() && AngleUtil.isEntityInFov(entity, fov)
                && entity.isEntityAlive() && entity.getHealth() > 0.0f && entity != mc.player
                && ((entity instanceof EntityPlayer && players) || ((entity instanceof EntityAnimal
                || entity instanceof EntityMob || entity instanceof EntitySlime || entity instanceof EntityVillager) && mobs))
                && entity.getDistanceSq(mc.player) <= d * d
                && (!entity.isInvisible() || invisibles) && !Mercury.INSTANCE.getFriendManager().isFriend(entity.getName());
    }



}
