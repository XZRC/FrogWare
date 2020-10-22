package fail.mercury.client.client.modules.movement;

import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.api.util.EntityUtil;
import fail.mercury.client.api.util.MathUtil;
import fail.mercury.client.api.util.MotionUtil;
import fail.mercury.client.api.util.TimerUtil;
import fail.mercury.client.client.events.CollisionBoxEvent;
import fail.mercury.client.client.events.MotionEvent;
import fail.mercury.client.client.events.PacketEvent;
import fail.mercury.client.client.events.UpdateEvent;
import fail.mercury.client.api.util.*;
import me.kix.lotus.property.annotations.Clamp;
import me.kix.lotus.property.annotations.Mode;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.b0at.api.event.types.EventTiming;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

@ModuleManifest(label = "Flight", aliases = {"Fly"}, category = Category.MOVEMENT)
public class Flight extends Module {

    @Property("Mode")
    @Mode({"Normal", "Packet", "SlowPacket", "DelayPacket", "Damage"})
    public static String mode = "Packet";

    @Property("Speed")
    @Clamp(maximum = "10")
    private double speed = 8.0;

    @Property("NoClip")
    private boolean noclip = false;

    @Property("AntiKick")
    private boolean antikick = false;

    private int teleportId;
    private List<CPacketPlayer> packets = new ArrayList<>();

    private double moveSpeed, lastDist;
    private int level;
    private TimerUtil delayTimer = new TimerUtil();

    @Override
    public void onDisable() {
        EntityUtil.resetTimer();
        mc.player.setVelocity(0, 0, 0);
        this.moveSpeed = MotionUtil.getBaseMoveSpeed();
        this.lastDist = 0;
        if (noclip)
            mc.player.noClip = false;
    }

    @Override
    public void onEnable() {
        level = 0;
        if (this.mode.equalsIgnoreCase("packet")) {
            if (mc.world != null) {
                this.teleportId = 0;
                this.packets.clear();
                final CPacketPlayer bounds = new CPacketPlayer.Position(mc.player.posX, mc.player.posY <= 10 ? 255 : 1, mc.player.posZ, mc.player.onGround);
                this.packets.add(bounds);
                mc.player.connection.sendPacket(bounds);
            }
        }
    }


    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (mode.equalsIgnoreCase("damage")) {
            if (event.getTiming().equals(EventTiming.PRE)) {
                mc.player.motionY = 0;
                double motionY = 0.42f;
                if (mc.player.onGround) {
                    if (mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                        motionY += (mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1f;
                    }
                    event.getLocation().setY(mc.player.motionY = motionY);
                    this.moveSpeed *= 2.149;
                }
            }
            if (mc.player.ticksExisted % 2 == 0) {
                mc.player.setPosition(mc.player.posX, mc.player.posY + MathUtil.getRandom(1.2354235325235235E-14, 1.2354235325235233E-13), mc.player.posZ);
            }
            if (mc.gameSettings.keyBindJump.isKeyDown())
                mc.player.motionY += speed / 2;
            if (mc.gameSettings.keyBindSneak.isKeyDown())
                mc.player.motionY -= speed / 2;
        }
        if (mode.equalsIgnoreCase("Normal")) {
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.player.motionY = speed;
            } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.motionY = -speed;
            } else {
                mc.player.motionY = 0;
            }
            if (antikick) {
                if (mc.player.ticksExisted % 5 == 0) {
                    event.getLocation().setY(event.getLocation().getY() - 0.03125D);
                    event.getLocation().setOnGround(true);
                }
            }
            double[] dir = MotionUtil.forward(speed);
            mc.player.motionX = dir[0];
            mc.player.motionZ = dir[1];
        }
        if (mode.equalsIgnoreCase("packet")) {
            if (this.teleportId <= 0) {
                final CPacketPlayer bounds = new CPacketPlayer.Position(mc.player.posX, mc.player.posY <= 10 ? 255 : 1, mc.player.posZ, mc.player.onGround);
                this.packets.add(bounds);
                mc.player.connection.sendPacket(bounds);
                return;
            }
            mc.player.setVelocity(0,0,0);
            double posY = -0.00000001;
            if (!mc.gameSettings.keyBindJump.isKeyDown() && !mc.gameSettings.keyBindSneak.isKeyDown()){
                if (MotionUtil.isMoving(mc.player)) {
                    for (double x = 0.0625; x < this.speed; x += 0.262) {
                        final double[] dir = MotionUtil.forward(x);
                        mc.player.setVelocity(dir[0], posY, dir[1]);
                        move(dir[0], posY, dir[1]);
                    }
                 //   if (mc.player.ticksExisted % 5 == 0)
                  //      mc.player.motionY -= 0.03125D;
                }
            } else {
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    for (int i = 0; i <= 3; i++) {
                        //mc.player.getEntityBoundingBox().offset(0, 0.10000545, 0);
                        mc.player.setVelocity(0, mc.player.ticksExisted % 20 == 0 ? -0.04f : 0.062f * i, 0);
                        move(0, mc.player.ticksExisted % 20 == 0 ? -0.04f : 0.062f * i, 0);

                    }
                } else if (mc.gameSettings.keyBindSneak.isKeyDown()){
                    for (int i = 0; i <= 3; i++) {
                        mc.player.setVelocity(0, posY - 0.0625 * i, 0);
                        move(0, posY - 0.0625 * i, 0);
                    }
                }
            }
            //mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + mc.player.motionX, 0.0, mc.player.posZ + mc.player.motionZ, true));
        }
        if (mode.equalsIgnoreCase("slowpacket")) {
            double posX = mc.player.posX;
            double posY = mc.player.posY;
            double posZ = mc.player.posZ;
            boolean ground = mc.player.onGround;
            mc.player.setVelocity(0,0,0);
                if (!mc.gameSettings.keyBindJump.isKeyDown() && !mc.gameSettings.keyBindSneak.isKeyDown()) {
                    double[] dir = MotionUtil.forward(0.0625);
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(posX + dir[0], posY, posZ + dir[1], ground));
                    //mc.player.setVelocity(dir[0],0,dir[1]);
                    mc.player.setPositionAndUpdate(posX + dir[0], posY, posZ + dir[1]);

                } else {
                    if (mc.gameSettings.keyBindJump.isKeyDown()) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(posX, posY + 0.0625, posZ, ground));
                        //mc.player.setVelocity(0,0.0625,0);
                        mc.player.setPositionAndUpdate(posX, posY + 0.0625, posZ);

                    } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(posX, posY - 0.0625, posZ, ground));
                        //mc.player.setVelocity(0,-0.0625,0);
                        mc.player.setPositionAndUpdate(posX, posY - 0.0625, posZ);
                    }
                }
                mc.player.connection.sendPacket(new CPacketPlayer.Position(posX + mc.player.motionX, mc.player.posY <= 10 ? 255 : 1, posZ + mc.player.motionZ, ground));
            /*if (this.teleportId <= 0) {
                final CPacketPlayer bounds = new CPacketPlayer.Position(mc.player.posX, mc.player.posY <= 10 ? 255 : 1, mc.player.posZ, mc.player.onGround);
                this.packets.add(bounds);
                mc.player.connection.sendPacket(bounds);
                return;
            }
            mc.player.setVelocity(0,0,0);
            double posY = -0.00000001;
            if (!mc.gameSettings.keyBindJump.isKeyDown() && !mc.gameSettings.keyBindSneak.isKeyDown()){
                if (MotionUtil.isMoving(mc.player)) {
                    final double[] dir = MotionUtil.forward(0.0625);
                    mc.player.setVelocity(dir[0], posY, dir[1]);
                    move(dir[0], posY, dir[1]);
                    //   if (mc.player.ticksExisted % 5 == 0)
                    //      mc.player.motionY -= 0.03125D;
                }
            } else {
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    //mc.player.getEntityBoundingBox().offset(0, 0.10000545, 0);
                    mc.player.setVelocity(0, 0.062f, 0);
                    move(0, 0.062f, 0);
                } else if (mc.gameSettings.keyBindSneak.isKeyDown()){
                    mc.player.setVelocity(0, 0.0625, 0);
                    move(0, 0.0625, 0);
                }
            }*/
        }

        if (mode.equalsIgnoreCase("delaypacket")) {
            if (delayTimer.hasReached(1000))
                delayTimer.reset();
            if (delayTimer.hasReached(600)) {
                mc.player.setVelocity(0,0,0);
                return;
            }
            if (this.teleportId <= 0) {
                final CPacketPlayer bounds = new CPacketPlayer.Position(mc.player.posX, mc.player.posY <= 10 ? 255 : 1, mc.player.posZ, mc.player.onGround);
                this.packets.add(bounds);
                mc.player.connection.sendPacket(bounds);
                return;
            }
            mc.player.setVelocity(0,0,0);
            double posY = -0.00000001;
            if (!mc.gameSettings.keyBindJump.isKeyDown() && !mc.gameSettings.keyBindSneak.isKeyDown()){
                if (MotionUtil.isMoving(mc.player)) {
                    final double[] dir = MotionUtil.forward(0.2);
                    mc.player.setVelocity(dir[0], posY, dir[1]);
                    move(dir[0], posY, dir[1]);
                    //   if (mc.player.ticksExisted % 5 == 0)
                    //      mc.player.motionY -= 0.03125D;
                }
            } else {
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    //mc.player.getEntityBoundingBox().offset(0, 0.10000545, 0);
                    mc.player.setVelocity(0, 0.062f, 0);
                    move(0, 0.062f, 0);
                } else if (mc.gameSettings.keyBindSneak.isKeyDown()){
                    mc.player.setVelocity(0, 0.0625, 0);
                    move(0, 0.0625, 0);
                }
            }
        }


        if (noclip)
            mc.player.noClip = true;
    }

    @EventHandler
    public void onMove(MotionEvent event) {
        if (mode.equalsIgnoreCase("damage")) {
                        double forward = mc.player.movementInput.moveForward;
                        double strafe = mc.player.movementInput.moveStrafe;
                        final float yaw = mc.player.rotationYaw;
                        if (forward == 0.0 && strafe == 0.0) {
                            event.setX(0.0);
                            event.setZ(0.0);
                        }
                        if (forward != 0.0 && strafe != 0.0) {
                            forward *= Math.sin(0.7853981633974483);
                            strafe *= Math.cos(0.7853981633974483);
                        }
                        if (this.level != 1 || (mc.player.moveForward == 0.0f && mc.player.moveStrafing == 0.0f)) {
                            if (this.level == 2) {
                                ++this.level;
                            }
                            else if (this.level == 3) {
                                ++this.level;
                                final double difference = (double)((((mc.player.ticksExisted % 2 == 0) ? -0.05 : 0.1)) * (this.lastDist - MotionUtil.getBaseMoveSpeed()));
                                this.moveSpeed = this.lastDist - difference;
                            }
                            else {
                                if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, mc.player.motionY, 0.0)).size() > 0 || mc.player.collidedVertically) {
                                    this.level = 1;
                                }
                                this.moveSpeed = this.lastDist - this.lastDist / 159.0;
                            }
                        }
                        else {
                            this.level = 2;
                            final double boost = mc.player.isPotionActive(MobEffects.SPEED) ? 1.86 : 2.05;
                            this.moveSpeed = boost * MotionUtil.getBaseMoveSpeed() - 0.01;
                        }
                        this.moveSpeed = Math.max(this.moveSpeed, MotionUtil.getBaseMoveSpeed());
                        final double mx = -Math.sin(Math.toRadians(yaw));
                        final double mz = Math.cos(Math.toRadians(yaw));
                        event.setX(forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz);
                        event.setZ(forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx);
                }
    }

    @EventHandler
    public void onPacket(PacketEvent event) {
        switch (event.getType()) {
            case INCOMING:
                if (this.mode.equalsIgnoreCase("packet") /*|| mode.equalsIgnoreCase("slowpacket")*/ || mode.equalsIgnoreCase("delaypacket")) {
                    if (event.getPacket() instanceof SPacketPlayerPosLook) {
                        final SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
                        if (Minecraft.getMinecraft().player.isEntityAlive() && Minecraft.getMinecraft().world.isBlockLoaded(new BlockPos(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY, Minecraft.getMinecraft().player.posZ)) && !(Minecraft.getMinecraft().currentScreen instanceof GuiDownloadTerrain)) {
                            if (this.teleportId <= 0) {
                                this.teleportId = packet.getTeleportId();
                            } else {
                                event.setCancelled(true);
                            }
                        }
                    }
                }
                break;
            case OUTGOING:
                if (this.mode.equalsIgnoreCase("packet") /*|| mode.equalsIgnoreCase("slowpacket")*/ || mode.equalsIgnoreCase("delaypacket")) {
                    if (event.getPacket() instanceof CPacketPlayer && !(event.getPacket() instanceof CPacketPlayer.Position)) {
                        event.setCancelled(true);
                    }
                    if (event.getPacket() instanceof CPacketPlayer) {
                        final CPacketPlayer packet = (CPacketPlayer) event.getPacket();
                        if (packets.contains(packet)) {
                            packets.remove(packet);
                            return;
                        }
                        event.setCancelled(true);
                    }
                }
                break;
        }
    }

    @EventHandler
    public void onCollision(CollisionBoxEvent event) {
        if (noclip && event.getEntity() == mc.player)
            event.setAABB(Block.NULL_AABB);
    }


    private void move(double x, double y, double z) {
        final Minecraft mc = Minecraft.getMinecraft();
        final CPacketPlayer pos = new CPacketPlayer.Position(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z, mc.player.onGround);
        this.packets.add(pos);
        mc.player.connection.sendPacket(pos);

        final CPacketPlayer bounds = new CPacketPlayer.Position(mc.player.posX + x, mc.player.posY <= 10 ? 255 : 1, mc.player.posZ + z, mc.player.onGround);
        this.packets.add(bounds);
        mc.player.connection.sendPacket(bounds);

        this.teleportId++;
        mc.player.connection.sendPacket(new CPacketConfirmTeleport(this.teleportId - 1));
        mc.player.connection.sendPacket(new CPacketConfirmTeleport(this.teleportId));
        mc.player.connection.sendPacket(new CPacketConfirmTeleport(this.teleportId + 1));
    }
}
