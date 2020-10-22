package fail.mercury.client.client.modules.movement;

import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.api.util.EntityUtil;
import fail.mercury.client.api.util.MotionUtil;
import fail.mercury.client.client.events.CollisionBoxEvent;
import fail.mercury.client.client.events.JumpEvent;
import fail.mercury.client.client.events.UpdateEvent;
import me.kix.lotus.property.annotations.Mode;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.b0at.api.event.types.EventTiming;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;

/**
 * @author auto on 2/18/2020
 */
@ModuleManifest(label = "Jesus", category = Category.MOVEMENT, aliases = {"WaterWalk", "Basilisk"})
public class Jesus extends Module {

    @Property("Mode")
    @Mode({"Solid"})
    public String mode = "Solid";

    @Property("Criticals")
    public boolean criticals = false;


    @EventHandler
    public void onBoundingBox(CollisionBoxEvent event) {
        if (mc.player == null)
            return;
        if (((event.getBlock() instanceof BlockLiquid)) && event.getEntity() == mc.player
                && !EntityUtil.isInLiquid() && (mc.player.fallDistance < 3.0F) && (!mc.player.isSneaking())) {
            event.setAABB(Block.FULL_BLOCK_AABB);
        }
    }

    @EventHandler
    public void onJump(JumpEvent event) {
        if (EntityUtil.isColliding(0, -0.8, 0) instanceof BlockLiquid && !EntityUtil.isInLiquid())
            MotionUtil.setSpeed(mc.player, MotionUtil.getBaseMoveSpeed() - 0.24);
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (mc.gameSettings.keyBindSneak.isKeyDown())
            return;
        if (EntityUtil.isInLiquid()) {
            if (event.getTiming().equals(EventTiming.PRE))
                mc.player.motionY = 0.1;
        }
        if (mode.equalsIgnoreCase("solid") && event.getTiming().equals(EventTiming.PRE)) {
            if (EntityUtil.isColliding(0, -0.1, 0) instanceof BlockLiquid && !EntityUtil.isInLiquid()) {
                event.getLocation().setY(mc.player.posY + (mc.player.ticksExisted % 2 == 0 ? 0.01 : -0.01));
                event.getLocation().setOnGround(mc.player.ticksExisted % 2 != 0);
            }
        }
    }
}
