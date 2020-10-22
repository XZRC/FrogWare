package fail.mercury.client.client.modules.misc;

import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.api.util.MotionUtil;
import fail.mercury.client.api.util.TimerUtil;
import fail.mercury.client.client.events.CollisionBoxEvent;
import fail.mercury.client.client.events.InsideBlockRenderEvent;
import fail.mercury.client.client.events.PushEvent;
import fail.mercury.client.client.events.UpdateEvent;
import me.kix.lotus.property.annotations.Clamp;
import me.kix.lotus.property.annotations.Mode;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.minecraft.util.math.BlockPos;

@ModuleManifest(label = "Phase", category = Category.MISC)
public class Phase extends Module {

    @Property("Speed")
    @Clamp(minimum = "0.1")
    public double speed = 10;

    @Property("Mode")
    @Mode({"Down", "Destroy", "NCP"})
    public String mode = "Vertical";

    public TimerUtil timer = new TimerUtil();

    @EventHandler
    public void onRender(InsideBlockRenderEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPush(PushEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (!canPhase())
            return;
        if (mode.equalsIgnoreCase("down")) {
            double posX = mc.player.posX;
            double posY = mc.player.posY;
            double posZ = mc.player.posZ;
            boolean ground = mc.player.onGround;
            if (mc.player.isSneaking() && mc.currentScreen == null) {
                    MotionUtil.setSpeed(mc.player, 0);
                    mc.player.setPositionAndUpdate(mc.player.posX, mc.player.posY - 0.1, mc.player.posZ);
                    mc.player.motionY = (-speed * 2);
            }
        }

        if (mode.equalsIgnoreCase("destroy")) {
            double dir[] = MotionUtil.forward(1);
            if (mc.player.collidedHorizontally) {
                mc.world.destroyBlock(new BlockPos(mc.player.posX + dir[0], mc.player.posY, mc.player.posZ + dir[1]), false);
                mc.world.destroyBlock(new BlockPos(mc.player.posX + dir[0], mc.player.posY + 1, mc.player.posZ + dir[1]), false);
            }
            if (MotionUtil.isMoving(mc.player) && mc.player.onGround) {
                //mc.player.setPosition(mc.player.posX, mc.player.posY + 0.1, mc.player.posZ);
                MotionUtil.setSpeed(mc.player, 0.23);
            }
        }
    }

        @EventHandler
        public void onCollision(CollisionBoxEvent event) {
        }

    public boolean canPhase() {
        return !mc.player.isOnLadder() && !mc.player.isInWater() && !mc.player.isInLava();
    }

}
