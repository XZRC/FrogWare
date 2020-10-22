package fail.mercury.client.client.modules.combat;

import fail.mercury.client.Mercury;
import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.api.util.AngleUtil;
import fail.mercury.client.api.util.InventoryUtil;
import fail.mercury.client.api.util.TimerUtil;
import fail.mercury.client.client.events.UpdateEvent;
import fail.mercury.client.client.modules.misc.Freecam;
import me.kix.lotus.property.annotations.Clamp;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.b0at.api.event.types.EventTiming;
import net.minecraft.block.BlockWeb;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import java.util.ArrayList;
import java.util.List;

/**
 * @author auto on 2/4/2020
 */
@ModuleManifest(label = "AutoWeb", category = Category.COMBAT, aliases = {"webaura"}, fakelabel = "Auto Web", description = "Automatically places cobwebs on nearby entities.")
public class AutoWeb extends Module {

    @Property("Range")
    @Clamp(minimum = "3.0", maximum = "6.0")
    public static double reach = 4.2;

    @Property("Speed")
    @Clamp(minimum = "1")
    public int speed = 8;

    @Property("FOV")
    @Clamp(minimum = "1", maximum = "180")
    public static int fov = 180;

    @Property("Invisibles")
    public static boolean invisibles = true;

    @Property("Walls")
    public static boolean walls = false;

    @Property("Players")
    public static boolean players = true;

    @Property("Mobs")
    public static boolean mobs = true;

    @Property("Hold")
    public boolean hold = true;

    @Property("Swing")
    public boolean swing = false;

    @Property("Silent")
    public boolean silent = false;

    @Property("Replenish")
    public boolean replenish = false;

    public static List<EntityLivingBase> targets = new ArrayList();
    public static EntityLivingBase target;
    public TimerUtil placeTimer = new TimerUtil();


    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (mc.player == null) {
            target = null;
        }
        if (event.getTiming().equals(EventTiming.PRE)) {
            targets = getTargets();
            if (!targets.isEmpty()) {
                target = targets.get(0);
                BlockPos pos = new BlockPos(target.posX, target.posY, target.posZ);
                    //Vec3d vec = AngleUtil.resolveBestHitVec(target, 3, walls);
                    //BlockPos aimPos = new BlockPos(target.posX, target.posY - 1, target.posZ);
                    if (canPlace()) {
                        float[] rotation = AngleUtil.getRotationFromPosition(pos.getX() + 0.5, pos.getZ() + 0.5, pos.add(0, 0, 0).getY() - mc.player.getEyeHeight());
                        event.getRotation().setYaw(!silent ? mc.player.rotationYaw = (rotation[0]) : rotation[0]);
                        event.getRotation().setPitch(!silent ? mc.player.rotationPitch = rotation[1] : rotation[1]);
                        int lastSlot;
                        int slot;
                        if (InventoryUtil.getItemSlot(mc.player.inventoryContainer, Item.getItemById(30)) < 36 && replenish) {
                            InventoryUtil.swap(InventoryUtil.getItemSlot(mc.player.inventoryContainer, Item.getItemById(30)), 44);
                        }
                        slot = InventoryUtil.getItemSlotInHotbar(Item.getItemById(30));
                        lastSlot = mc.player.inventory.currentItem;
                        mc.player.inventory.currentItem = slot;
                        mc.playerController.updateController();
                        if (placeTimer.hasReached(1000 / speed)) {
                            for (final EnumFacing side : EnumFacing.values()) {
                                final BlockPos neighbor = pos.offset(side);
                                if (canBeClicked(neighbor)) {
                                    place(neighbor, side.getOpposite());
                                }
                            }
                            placeTimer.reset();
                        }
                        mc.player.inventory.currentItem = lastSlot;
                        mc.playerController.updateController();
                    }
            } else if (target != null)
                target = null;
        }
    }

    public boolean canBeClicked(final BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock().canCollideCheck(mc.world.getBlockState(pos), false);
    }

    public void place(BlockPos pos, EnumFacing direction) {
        if (swing) {
            mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
        }
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, direction, EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
    }

        public boolean canPlace() {
        return InventoryUtil.getItemCount(mc.player.inventoryContainer, Item.getItemById(30)) != 0;
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
        BlockPos pos = new BlockPos(entity.posX, entity.posY, entity.posZ);
        final boolean freecam = Mercury.INSTANCE.getModuleManager().find(Freecam.class).isEnabled() && entity != Freecam.entity;
        return entity != null && !(mc.world.getBlockState(pos).getBlock() instanceof BlockWeb) && entity.onGround && entity.isEntityAlive() && AngleUtil.isEntityInFov(entity, fov)
                && entity.isEntityAlive() && entity.getHealth() > 0.0f && entity != mc.player
                && ((entity instanceof EntityPlayer && players) || ((entity instanceof EntityAnimal
                || entity instanceof EntityMob || entity instanceof EntitySlime || entity instanceof EntityVillager) && mobs))
                && entity.getDistanceSq(mc.player) <= d * d
                && (!entity.isInvisible() || invisibles) && !Mercury.INSTANCE.getFriendManager().isFriend(entity.getName())
                && (!entity.isInvisible() || invisibles);
    }

}
