package fail.mercury.client.client.modules.visual;

import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.client.events.RotationEvent;
import fail.mercury.client.client.events.UpdateEvent;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;

/**
 * @author auto on 4/6/2020
 */
@ModuleManifest(label = "Location", aliases = {"Rotations"}, description = "Shows rotations and y position in F5.", category = Category.VISUAL, hidden = true)
public class Location extends Module {

    @Property("Rotations")
    public boolean rotations = true;

    @Property("Active")
    public boolean active = true;

    public float yaw, pitch, prevYaw, prevPitch;
    public boolean isActive;

    @EventHandler
    public void onRotate(RotationEvent event) {
        if (rotations) {
            if (active && !isActive)
                return;
            event.getRotation().setYaw(yaw);
            event.getPrevRotation().setYaw(prevYaw);

            event.getRotation().setPitch(pitch);
            event.getPrevRotation().setPitch(prevPitch);

            event.setRenderYawOffset(yaw);
            event.setPrevRenderYawOffset(prevYaw);
        }
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        prevYaw = yaw;
        prevPitch = pitch;

        yaw = event.getRotation().getYaw();
        pitch = event.getRotation().getPitch();

        if (event.getRotation().getYaw() != mc.player.rotationYaw
                || event.getRotation().getPitch() != mc.player.rotationPitch) isActive = true;
        else isActive = false;
    }

}
