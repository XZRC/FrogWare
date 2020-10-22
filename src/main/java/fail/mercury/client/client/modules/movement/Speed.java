package fail.mercury.client.client.modules.movement;

import fail.mercury.client.Mercury;
import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.api.util.EntityUtil;
import fail.mercury.client.api.util.MotionUtil;
import fail.mercury.client.api.util.TimerUtil;
import fail.mercury.client.client.events.MotionEvent;
import fail.mercury.client.client.events.UpdateEvent;
import me.kix.lotus.property.annotations.Clamp;
import me.kix.lotus.property.annotations.Mode;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.b0at.api.event.types.EventTiming;
import net.minecraft.block.*;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;

@ModuleManifest(label = "Speed", category = Category.MOVEMENT)
public class Speed extends Module {

    @Property("Mode")
    @Mode({"BHop", "Ground", "YPort", "Packet", "Packet2"})
    public static String mode = "BHop";

    @Property("Speed")
    @Clamp(maximum = "10")
    private double speed = 8.0;

    @Property("Ice")
    public boolean ice = false;

    private double moveSpeed;
    public static boolean doSlow;
    public TimerUtil waitTimer = new TimerUtil();
    @Override
    public void onDisable() {
        if (!mode.equalsIgnoreCase("bhop"))
            mc.player.setVelocity(0, 0, 0);
        EntityUtil.resetTimer();
    }

    @Override
    public void onEnable() {
        moveSpeed = MotionUtil.getBaseMoveSpeed();
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        boolean icee = this.ice && (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 1, mc.player.posZ)).getBlock() instanceof BlockIce || mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 1, mc.player.posZ)).getBlock() instanceof BlockPackedIce);
        if (icee) {
            MotionUtil.setSpeed(mc.player, MotionUtil.getBaseMoveSpeed() + (mc.player.isPotionActive(MobEffects.SPEED) ? (mc.player.ticksExisted % 2 == 0 ? 0.7 : 0.1) : 0.4));
        }
        if (!icee) {
            if (mode.equalsIgnoreCase("packet") || mode.equalsIgnoreCase("packet2")) {
                if (MotionUtil.isMoving(mc.player) && mc.player.onGround) {
                    boolean step = Mercury.INSTANCE.getModuleManager().find(Step.class).isEnabled();
                    double posX = mc.player.posX;
                    double posY = mc.player.posY;
                    double posZ = mc.player.posZ;
                    boolean ground = mc.player.onGround;
                    double[] dir1 = MotionUtil.forward(0.5);
                    BlockPos pos = new BlockPos(posX + dir1[0], posY, posZ + dir1[1]);
                    Block block = mc.world.getBlockState(pos).getBlock();
                    if (step && !(block instanceof BlockAir)) {
                        MotionUtil.setSpeed(mc.player, 0);
                        return;
                    }
                    if (mc.world.getBlockState(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ())).getBlock() instanceof BlockAir)
                        return;
                    for (double x = 0.0625; x < speed; x += 0.262) {
                        double[] dir = MotionUtil.forward(x);
                        // if (mc.world.getBlockState(new BlockPos(posX + dir1[0], posY - 1, posZ + dir1[1])).getBlock() instanceof BlockAir)
                        //     return;
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(posX + dir[0], posY, posZ + dir[1], ground));
                    }
                    if (mode.equalsIgnoreCase("packet2"))
                        MotionUtil.setSpeed(mc.player, 2);

                    mc.player.connection.sendPacket(new CPacketPlayer.Position(posX + mc.player.motionX, mc.player.posY <= 10 ? 255 : 1, posZ + mc.player.motionZ, ground));
                }
            }
            if (mode.equalsIgnoreCase("yport") ) {
                if (!MotionUtil.isMoving(mc.player) || mc.player.isInWater() && mc.player.isInLava() || mc.player.collidedHorizontally) {
                    return;
                }
                if (mc.player.onGround) {
                    EntityUtil.setTimer(1.15f);
                    mc.player.jump();
                    boolean ice = mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 1, mc.player.posZ)).getBlock() instanceof BlockIce || mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 1, mc.player.posZ)).getBlock() instanceof BlockPackedIce;
                    MotionUtil.setSpeed(mc.player, MotionUtil.getBaseMoveSpeed() + (ice ? 0.3 : 0.06));
                } else {
                    mc.player.motionY = -1;
                    EntityUtil.resetTimer();
                }
            }
            if (mode.equalsIgnoreCase("ground") && event.getTiming().equals(EventTiming.PRE)) {
                if (!MotionUtil.isMoving(mc.player) || mc.player.isInWater() && mc.player.isInLava() || mc.player.collidedHorizontally) {
                    return;
                }
                if (mc.player.onGround) {
                    if (mc.player.ticksExisted % 2 == 0) {
                        boolean notUnder = mc.world.getBlockState(event.getLocation().toBlockPos().add(0, 2, 0)).getBlock() instanceof BlockAir;
                        event.getLocation().setY(mc.player.posY + (notUnder ? 0.4 : 0.2));
                        MotionUtil.setSpeed(mc.player, MotionUtil.getBaseMoveSpeed() - 0.15);
                    } else {
                        MotionUtil.setSpeed(mc.player, MotionUtil.getBaseMoveSpeed() + 0.065);
                    }
                } else {
                    mc.player.motionY = -1;
                }
            }
        }
    }
    @EventHandler
    public void onMotion(MotionEvent event) {
        boolean icee = this.ice && (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 1, mc.player.posZ)).getBlock() instanceof BlockIce || mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 1, mc.player.posZ)).getBlock() instanceof BlockPackedIce);
        if (icee)
            return;
        if (mode.equalsIgnoreCase("bhop")) {
            boolean jesus = Mercury.INSTANCE.getModuleManager().find(Jesus.class).isEnabled();
            double motionY = 0.42f;
            if (mc.player.onGround && MotionUtil.isMoving(mc.player) && waitTimer.hasReached(300)) {
                if (mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                    motionY += (mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1f;
                }
                event.setY(mc.player.motionY = motionY);
                moveSpeed = MotionUtil.getBaseMoveSpeed() * (jesus && EntityUtil.isColliding(0, -0.5, 0) instanceof BlockLiquid && !EntityUtil.isInLiquid() ? 0.9 : 1.901);
                doSlow = true;
                waitTimer.reset();
            } else {
                if (doSlow || mc.player.collidedHorizontally) {
                    moveSpeed -= (jesus && EntityUtil.isColliding(0, -0.8, 0) instanceof BlockLiquid && !EntityUtil.isInLiquid()) ? 0.4 : 0.7 * (moveSpeed = MotionUtil.getBaseMoveSpeed());
                    doSlow = false;
                } else {
                    moveSpeed -= moveSpeed / 159.0;
                }
            }
            moveSpeed = Math.max(moveSpeed, MotionUtil.getBaseMoveSpeed());
            double[] dir = MotionUtil.forward(moveSpeed);
            event.setX(dir[0]);
            event.setZ(dir[1]);
        }
    }


}
